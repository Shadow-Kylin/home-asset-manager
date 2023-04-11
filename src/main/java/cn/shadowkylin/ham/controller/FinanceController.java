package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.HttpStatus;
import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.model.Finance;
import cn.shadowkylin.ham.service.AssetService;
import cn.shadowkylin.ham.service.FinanceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    /**
     * 获取财务列表
     */
    @GetMapping("/getFinanceList/{userId}")
    public ResultUtil<Object> getFinanceList(
            @PathVariable("userId") int userId,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "searchType", defaultValue = "") String searchType,
            @RequestParam(value = "searchValue", defaultValue = "") String searchValue,
            @RequestParam(value = "type") String type) {
        //使用PageHelper分页
        PageHelper.startPage(pageNum, pageSize);
        //获取财务列表
        List<Finance> financeList = financeService.getFinanceList(userId, searchType, searchValue,type);
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
        //查询关联资产序列号assetSerialNumber是否存在
        if(assetService.getAssetByASN(finance.getUserId(),finance.getAssetSerialNumber()) == null)
            return ResultUtil.error("关联资产序列号不存在", null, HttpStatus.ASSET_SERIAL_NUMBER_NOT_EXIST);
        financeService.addFinance(finance);
        return ResultUtil.success("添加财务成功", null);
    }

    /**
     * 批量添加财务
     */
    @PostMapping("/addFinanceList")
    public ResultUtil<Object> addFinanceList(@RequestBody List<Finance> financeList) {
        financeService.addFinanceList(financeList);
        return ResultUtil.success("批量添加财务成功", null);
    }

    /**
     * 修改财务
     */
    @PostMapping("/updateFinance")
    public ResultUtil<Object> updateFinance(@RequestBody Finance finance) {
        //查询关联资产序列号assetSerialNumber是否存在
        if(assetService.getAssetByASN(finance.getUserId(),finance.getAssetSerialNumber()) == null)
            return ResultUtil.error("关联资产序列号不存在", null, HttpStatus.ASSET_SERIAL_NUMBER_NOT_EXIST);

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
        //获取该年每个月的财务收入数组
        double[] incomeList = financeService.getIncomeByYear(userId, year);
        return ResultUtil.success("获取财务收入列表成功", incomeList);
    }
    /**
     * 获取指定年份的财务支出列表
     */
    @GetMapping("/getExpenseByYear/{userId}/{year}")
    public ResultUtil<Object> getExpenseByYear(
            @PathVariable("userId") int userId,
            @PathVariable("year") int year) {
        //获取该年每个月的财务支出数组
        double[] expenseList = financeService.getExpenditureByYear(userId, year);
        return ResultUtil.success("获取财务支出列表成功", expenseList);
    }
}
