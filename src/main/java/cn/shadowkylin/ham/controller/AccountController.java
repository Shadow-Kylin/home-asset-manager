package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.model.Result;
import cn.shadowkylin.ham.model.User;
import cn.shadowkylin.ham.service.AccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */

@RestController
@RequestMapping("/account")
public class AccountController {
    @Resource
    private AccountService accountService;
    /**
     * 获取账户列表
     */
    @GetMapping("/getAccountList")
    public Result<Object> getAccountList() {
        //调用service层的方法，获取账户列表
        List<User> accountList = accountService.getAccountList();
        return ResultUtil.success("获取账户列表成功", accountList);
    }

    /**
     * 获取账户详情
     */
    @GetMapping("/getAccountDetail/{id}")
    public Result<Object> getAccountDetail(@PathVariable("id") int id) {
        //调用service层的方法，获取账户详情
        User accountDetail = accountService.getAccountDetail(id);
        return ResultUtil.success("获取账户详情成功", accountDetail);
    }

    /**
     * 添加账户
     */
    @PostMapping("/addAccount")
    public Result<Object> addAccount(User user) {
        accountService.addAccount(user);
        return ResultUtil.success("添加成功！", null);
    }

    /**
     * 修改账户
     */
    @PostMapping("/updateAccount")
    public Result<Object> updateAccount(User user) {
        accountService.updateAccount(user);
        return ResultUtil.success("修改成功！", null);
    }

    /**
     * 删除账户
     */
    @PostMapping("/deleteAccount")
    public Result<Object> deleteAccount(int id) {
        accountService.deleteAccount(id);
        return ResultUtil.success("删除成功！", null);
    }

    /**
     * 批量删除账户
     */
    @PostMapping("/deleteAccountList")
    public Result<Object> deleteAccountList(@RequestBody List<Integer> ids) {
        accountService.deleteAccountList(ids);
        return ResultUtil.success("删除成功！", null);
    }
}
