package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.HttpStatus;
import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.model.Finance;
import cn.shadowkylin.ham.service.AccountService;
import cn.shadowkylin.ham.service.AssetService;
import cn.shadowkylin.ham.service.FinanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */

@RestController
@RequestMapping("/finance")
public class FinanceController {
    @Resource
    private FinanceService financeService;
    @Resource
    private AssetService assetService;
    @Resource
    private AccountService accountService;

    /**
     * 获取财务列表
     */
    @GetMapping("/getFinanceList")
    public ResultUtil<Object> getFinanceList(
            @RequestParam("userId") int userId,
            @RequestParam("homeSerialNumber") String homeSerialNumber,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "searchType", defaultValue = "") String searchType,
            @RequestParam(value = "searchValue", defaultValue = "") String searchValue,
            @RequestParam(value = "type") String type) {
        //使用PageHelper分页
        PageHelper.startPage(pageNum, pageSize);
        System.out.println(searchValue);
        //获取财务列表
        List<Finance> financeList = financeService.getFinanceList(userId, homeSerialNumber, searchType, searchValue, type);
        //根据用户ID获取用户名称
        for (Finance finance : financeList) {
            finance.setUserName(accountService.getAccountDetail(finance.getUserId()).getUsername());
        }
        //使用PageInfo包装查询结果
        PageInfo<Finance> pageInfo = new PageInfo<>(financeList);
        return ResultUtil.success("获取财务列表成功", pageInfo);
    }

    /**
     * 获取财务详情
     */
    @GetMapping("/getFinanceDetail/{financeId}")
    public ResultUtil<Object> getFinanceDetail(@PathVariable("financeId") int financeId) {
        return ResultUtil.success("获取财务详情成功", financeService.getFinanceDetail(financeId));
    }

    /**
     * 添加财务
     */
    @PostMapping("/addFinance")
    public ResultUtil<Object> addFinance(@RequestBody Finance finance) {
        //检查资产有效性
        if (assetService.checkAsset(finance.getUserId(), finance.getHomeSerialNumber(),
                finance.getAssetSerialNumber()))
            return ResultUtil.error("关联资产序列号无效", null, HttpStatus.ASSET_SERIAL_NUMBER_NOT_EXIST);
        financeService.addFinance(finance);
        return ResultUtil.success("添加财务成功", null);
    }

    /**
     * 批量添加财务
     */
    @PostMapping("/addFinanceList")
    public ResultUtil<Object> addFinanceList(@RequestBody List<Finance> financeList) {
        //检查资产有效性
        for (Finance finance : financeList) {
            if (assetService.checkAsset(finance.getUserId(), finance.getHomeSerialNumber(),
                    finance.getAssetSerialNumber()))
                return ResultUtil.error("关联资产序列号无效", null, HttpStatus.ASSET_SERIAL_NUMBER_NOT_EXIST);
        }
        financeService.addFinanceList(financeList);
        return ResultUtil.success("批量添加财务成功", null);
    }

    /**
     * 修改财务
     */
    @PostMapping("/updateFinance")
    public ResultUtil<Object> updateFinance(@RequestBody Finance finance) {
        //检查资产有效性
        if (assetService.checkAsset(finance.getUserId(), finance.getHomeSerialNumber(),
                finance.getAssetSerialNumber()))
            return ResultUtil.error("关联资产序列号无效", null, HttpStatus.ASSET_SERIAL_NUMBER_NOT_EXIST);
        financeService.updateFinance(finance);
        return ResultUtil.success("修改财务成功", null);
    }

    /**
     * 删除财务
     */
    @PostMapping("/deleteFinance/{financeId}")
    public ResultUtil<Object> deleteFinance(@PathVariable("financeId") int financeId) {
        financeService.deleteFinance(financeId);
        return ResultUtil.success("删除财务成功", null);
    }

    /**
     * 批量删除财务
     */
    @PostMapping("/deleteFinanceList")
    public ResultUtil<Object> deleteFinanceList(@RequestBody int[] financeIdList) {
        financeService.deleteFinanceList(financeIdList);
        return ResultUtil.success("批量删除财务成功", null);
    }

    /**
     * 获取指定年份的财务收入列表
     */
    @GetMapping("/getIncomeByYear/{userId}/{year}")
    public ResultUtil<Object> getFinanceListByYear(
            @PathVariable("userId") int userId,
            @PathVariable("year") int year) {
        //获取用户家庭序列号
        String homeSerialNumber = accountService.getAccountDetail(userId).getHomeSerialNumber();
        //获取该年每个月的财务收入数组
        double[] incomeList = financeService.getIncomeByYear(userId,homeSerialNumber, year);
        return ResultUtil.success("获取财务收入列表成功", incomeList);
    }

    /**
     * 获取指定年份的财务支出列表
     */
    @GetMapping("/getExpenseByYear/{userId}/{year}")
    public ResultUtil<Object> getExpenseByYear(
            @PathVariable("userId") int userId,
            @PathVariable("year") int year) {
        //获取用户家庭序列号
        String homeSerialNumber = accountService.getAccountDetail(userId).getHomeSerialNumber();
        //获取该年每个月的财务支出数组
        double[] expenseList = financeService.getExpenditureByYear(userId,homeSerialNumber, year);
        return ResultUtil.success("获取财务支出列表成功", expenseList);
    }

    //getFinancialDataByTimeRange
    @GetMapping("/getFinancialDataByTimeRange")
    public ResultUtil<Object> getFinancialDataByTimeRange(
            @RequestParam(value="userId") int userId,
            @RequestParam(value="startDate") Date startDate,
            @RequestParam(value="endDate") Date endDate) {
        //获取用户家庭序列号
        String homeSerialNumber = accountService.getAccountDetail(userId).getHomeSerialNumber();
        //获取该年每个月的财务支出数组
        List<Finance> financeList = financeService.getFinancialDataByTimeRange(userId,homeSerialNumber,
                startDate, endDate);
        //将同一天的财务数据合并
        Map<Date, DailyFinance> dailyFinanceMap = new HashMap<>();
        for (Finance finance : financeList) {
            Date date = finance.getCreatedDate();
            if (dailyFinanceMap.containsKey(date)) {
                DailyFinance dailyFinance = dailyFinanceMap.get(date);
                if (finance.getType()==1)
                    dailyFinance.setIncome(dailyFinance.getIncome() + finance.getAmount());
                else
                    dailyFinance.setExpenditure(dailyFinance.getExpenditure() + finance.getAmount());
            } else {
                DailyFinance dailyFinance = new DailyFinance();
                dailyFinance.setDate(date);
                if (finance.getType()==1)
                    dailyFinance.setIncome(finance.getAmount());
                else
                    dailyFinance.setExpenditure(finance.getAmount());
                dailyFinanceMap.put(date, dailyFinance);
            }
        }
        List<DailyFinance> dailyFinanceList = new ArrayList<>();
        for (Date date : dailyFinanceMap.keySet()) {
            dailyFinanceList.add(dailyFinanceMap.get(date));
        }
        return ResultUtil.success("获取财务数据成功", dailyFinanceList);
    }
}

class DailyFinance{
    private Date date;
    private double income;
    private double expenditure;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(double expenditure) {
        this.expenditure = expenditure;
    }
}
