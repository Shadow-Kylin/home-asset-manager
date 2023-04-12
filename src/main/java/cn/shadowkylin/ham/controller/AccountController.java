package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.model.User;
import cn.shadowkylin.ham.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

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
    @Resource
    private HomeService homeService;
    @Resource
    private HomeRequestService homeRequestService;
    @Resource
    private AssetService assetService;
    @Resource
    private FinanceService financeService;
    /**
     * 获取账户列表
     */
    @GetMapping("/getAccountList")
    public ResultUtil<Object> getAccountList() {
        //调用service层的方法，获取账户列表
        List<User> accountList = accountService.getAccountList();
        return ResultUtil.success("获取账户列表成功", accountList);
    }

    /**
     * 通过ID获取账户详情
     *
     * @param id
     */
    @GetMapping("/getAccountDetail/{id}")
    public ResultUtil<Object> getAccountDetail(@PathVariable("id") int id) {
        //调用service层的方法，获取账户详情
        User accountDetail = accountService.getAccountDetail(id);
        return ResultUtil.success("获取账户详情成功", accountDetail);
    }

    /**
     * 根据家庭序列号获取账户列表
     *
     * @param homeSerialNumber
     */
    @GetMapping("/getAccountsByHSN/{homeSerialNumber}")
    public ResultUtil<Object> getAccountsByHSN(@PathVariable("homeSerialNumber") String homeSerialNumber) {
        //查看home表中是否存在该家庭序列号
        if (!homeService.isHomeExist(homeSerialNumber)) {
            return ResultUtil.error("该家庭序列号不存在！");
        }
        //调用service层的方法，获取账户列表
        List<User> accountList = accountService.getAccountsByHSN(homeSerialNumber);
        return ResultUtil.success("获取账户列表成功", accountList);
    }

    /**
     * 添加账户
     *
     * @param user
     */
    @PostMapping("/addAccount")
    public ResultUtil<Object> addAccount(User user) {
        accountService.addAccount(user);
        return ResultUtil.success("添加成功！");
    }

    /**
     * 修改账户
     *
     * @param user
     */
    @PostMapping("/updateAccount")
    public ResultUtil<Object> updateAccount(@RequestBody User user) {
        accountService.updateAccount(user);
        return ResultUtil.success("修改成功！");
    }

    /**
     * 删除账户 慎重使用
     *
     * @param id
     */
    @PostMapping("/deleteAccount")
    public ResultUtil<Object> deleteAccount(@RequestParam("userId") int id) {
        accountService.deleteAccount(id);
        return ResultUtil.success("删除成功！");
    }

    /**
     * 批量删除账户 慎重使用
     *
     * @param ids
     */
    @PostMapping("/deleteAccountList")
    public ResultUtil<Object> deleteAccountList(@RequestBody int[] ids) {
        accountService.deleteAccountList(ids);
        return ResultUtil.success("删除成功！");
    }

    /**
     * 创建家庭
     */
    @PostMapping("/createHome/{requestId}")
    public ResultUtil<Object> createHome(@PathVariable("requestId") int requestId, String homeName) {
        //获取请求者的家庭序列号，判断该序列号是否为空，若不为空，则已经创建过家庭，无法再次创建
        if (homeService.getHSNByUserId(requestId).equals(null)) {
            return ResultUtil.error("您已创建过家庭，无法再次创建！");
        }
        //获取当前时间，作为家庭创建时间
        Date createdDate = new Date(System.currentTimeMillis());
        //调用service层的方法，创建家庭
        String homeSerialNumber = UUID.randomUUID().toString();
        homeService.createHome(requestId, homeName,homeSerialNumber, createdDate);
        //将请求者的家庭序列号更新为创建的家庭序列号
        accountService.updateUserHSN(requestId, homeSerialNumber);
        //将请求者的资产和财务的家庭序列号更新为创建的家庭序列号
        assetService.updateAssetsHSN(requestId, homeSerialNumber);
        financeService.updateFinanceHSN(requestId, homeSerialNumber);
        return ResultUtil.success("创建家庭成功！");
    }

    /**
     * 将用户从家庭中移除
     */
    @PostMapping("/removeUserFromHome")
    public ResultUtil<Object> removeUserFromHome(
            @RequestParam("requestId") int requestId,
            @RequestParam("removeId") int removeId) {
        //获取请求者的家庭序列号，根据该序列号获取其创建者的id，判断请求者是否为家庭创建者，若不是，则无权移除
        String homeSerialNumber = homeService.getHSNByUserId(requestId);
        int creatorId = homeService.getCreatorIdByHSN(homeSerialNumber);
        if (creatorId != requestId) {
            return ResultUtil.error("您不是家庭创建者，无权移除！");
        }
        //获取被移除者的家庭序列号，判断该序列号是否与请求者的家庭序列号相同，若不同，则无权移除
        String removeHomeSerialNumber = accountService.getHSNByUserId(removeId);
        if (!homeSerialNumber.equals(removeHomeSerialNumber)) {
            return ResultUtil.error("该用户不在您的家庭中，无法移除！");
        }
        //调用service层的方法，将用户从家庭中移除，即将其家庭序列号置空
        accountService.removeUserFromHome(removeId);
        return ResultUtil.success("移除成功！");
    }

    /**
     * 更新用户的家庭序列号，即将用户加入家庭
     */
    @PostMapping("/updateUserHSN/{userId}")
    public ResultUtil<Object> updateUserHSN(@PathVariable("userId") int userId, String homeSerialNumber) {
        //查看home表中是否存在该家庭序列号，以及用户是否已经加入家庭
        if (!homeService.isHomeExist(homeSerialNumber)) {
            return ResultUtil.error("该家庭序列号不存在！");
        }
        if (accountService.userHasJoinedHome(userId)) {
            return ResultUtil.error("该用户已加入家庭！");
        }
        //调用service层的方法，更新用户的家庭序列号
        accountService.updateUserHSN(userId, homeSerialNumber);
        return ResultUtil.success("更新成功！");
    }

    /**
     * 解散家庭
     */
    @PostMapping("/disbandHome/{userId}")
    public ResultUtil<Object> disbandHome(@PathVariable("userId") int userId) {
        //获取用户的家庭序列号，根据该序列号获取其创建者的id，判断请求者是否为家庭创建者，若不是，则无权解散
        String homeSerialNumber = homeService.getHSNByUserId(userId);
        int creatorId = homeService.getCreatorIdByHSN(homeSerialNumber);
        if (creatorId != userId) {
            return ResultUtil.error("您不是家庭创建者，无权解散！");
        }
        //调用service层的方法，解散家庭
        accountService.disbandHome(homeSerialNumber);
        return ResultUtil.success("解散成功！");
    }

    /**
     * 通过家庭序列号获取家庭成员列表
     *
     * @param homeSerialNumber
     */
    @GetMapping("/getHomeMembersByHSN/{homeSerialNumber}")
    public ResultUtil<Object> getHomeMembersByHSN(@PathVariable("homeSerialNumber") String homeSerialNumber) {
        //查看home表中是否存在该家庭序列号
        if (!homeService.isHomeExist(homeSerialNumber)) {
            return ResultUtil.error("该家庭序列号不存在！");
        }
        //调用service层的方法，获取家庭成员列表
        List<User> homeMembers = accountService.getAccountsByHSN(homeSerialNumber);
        return ResultUtil.success("获取家庭成员列表成功", homeMembers);
    }

    /**
     * 退出家庭
     */
    @PostMapping("/quitHome/{userId}")
    public ResultUtil<Object> quitHome(@PathVariable("userId") int userId) {
        //获取用户的家庭序列号，判断用户是否已经加入家庭
        String homeSerialNumber = homeService.getHSNByUserId(userId);
        if (homeSerialNumber == "") {
            return ResultUtil.error("您还未加入家庭！");
        }
        //调用service层的方法，退出家庭
        accountService.updateUserHSN(userId, "");
        return ResultUtil.success("退出成功！");
    }

}
