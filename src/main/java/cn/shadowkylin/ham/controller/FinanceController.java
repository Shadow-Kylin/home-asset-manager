package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.model.Finance;
import cn.shadowkylin.ham.service.FinanceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    /**
     * 获取财务列表
     */
    @GetMapping("/getFinanceList/{userId}")
    public ResultUtil<Object> getFinanceList(@PathVariable("userId") int userId) {
        return ResultUtil.success("获取财务列表成功", financeService.getFinanceList(userId));
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
        financeService.addFinance(finance);
        return ResultUtil.success("添加财务成功", null);
    }

    /**
     * 修改财务
     */
    @PostMapping("/updateFinance")
    public ResultUtil<Object> updateFinance(@RequestBody Finance finance) {
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
}
