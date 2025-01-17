package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.HttpStatus;
import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.common.WebSocket;
import cn.shadowkylin.ham.model.User;
import cn.shadowkylin.ham.service.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    @Resource
    private HomeService homeService;
    @Resource
    private HomeRequestService homeRequestService;
    @Resource
    private AssetService assetService;
    @Resource
    private FinanceService financeService;
    @Resource
    private WebSocket webSocket;
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
        User user = accountService.getAccountDetail(id);
        //根据家庭序列号获取家庭名称
        user.setHomeName(homeService.getHomeName(user.getHomeSerialNumber()));
        return ResultUtil.success("获取账户详情成功", user);
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
        //获取家庭名称
        String homeName = homeService.getHomeName(homeSerialNumber);
        //设置家庭名称
        for (User user : accountList) {
            user.setHomeName(homeName);
        }
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
            return ResultUtil.error("该用户不在您的家庭中，无法移除！", HttpStatus.UNAUTHORIZED);
        }
        //调用service层的方法，将用户从家庭中移除，即将其家庭序列号置空
        accountService.removeUserFromHome(removeId);
        //还要将该用户的资产和财务信息中的家庭序列号置空
        assetService.updateAssetsHSN(removeId, null);
        financeService.updateFinancesHSN(removeId, null);
        //设置请求状态码为3，表示被移除
        homeRequestService.setRequestStatus(homeSerialNumber, removeId,3);
        sendMsgToUser(removeId,"",removeId+"已被移出家庭！");
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
        //将家庭下的所有资产和财务记录的家庭序列号置空
        assetService.clearHomeAsset(homeSerialNumber);
        financeService.clearHomeFinance(homeSerialNumber);
        //将家庭下的所有请求删除
        homeRequestService.delRequestsByHSN(homeSerialNumber);
        //根据家庭序列号获取家庭成员列表
        List<User> homeMembers = accountService.getAccountsByHSN(homeSerialNumber);
        //根据家庭序列号获取家庭名称
        //调用webSocket的sendMessageToUser方法，向家庭成员发送homeMembersJson
        for (User user : homeMembers) {
            //使用gson将homeMembers对象转换为json字符串，允许NULL值
            user.setHomeName(null);
            user.setHomeSerialNumber(null);
            Gson gson = new GsonBuilder().serializeNulls().create();
            String homeMemberJson = gson.toJson(user);
            webSocket.sendMessageToUser(String.valueOf(user.getId()), homeMemberJson);
        }
        //调用service层的方法，解散家庭
        accountService.disbandHome(homeSerialNumber);
        System.out.println("已向家庭成员发送解散通知！");
        return ResultUtil.success("解散成功！");
    }

    /**
     * 通过家庭序列号获取家庭成员列表
     * @param homeSerialNumber
     */
    @GetMapping("/getHomeMembersByHSN/{homeSerialNumber}")
    public ResultUtil<Object> getHomeMembersByHSN(@PathVariable("homeSerialNumber") String homeSerialNumber) {
        //查看home表中是否存在该家庭序列号
        //if (!homeService.isHomeExist(homeSerialNumber)) {
        //    return ResultUtil.error("该家庭序列号不存在！");
        //}
        //调用service层的方法，获取家庭成员列表
        List<User> homeMembers = accountService.getAccountsByHSN(homeSerialNumber);
        //webSocket.sendMessageToUser("sid:1;有人获取了家庭成员列表","1");
        //System.out.println("有人获取了家庭成员列表");
        return ResultUtil.success("获取家庭成员列表成功", homeMembers);
    }

    /**
     * 退出家庭
     */
    @PostMapping("/quitHome/{userId}")
    public ResultUtil<Object> quitHome(@PathVariable("userId") int userId) {
        //获取用户的家庭序列号，判断用户是否已经加入家庭
        String homeSerialNumber = accountService.getHSNByUserId(userId);
        if (homeSerialNumber == "") {
            return ResultUtil.error("您还未加入家庭！");
        }
        //将该用户的资产和财务信息中的家庭序列号置空
        assetService.updateAssetsHSN(userId, null);
        financeService.updateFinancesHSN(userId, null);
        //将请求状态码置为4，表示退出
        homeRequestService.setRequestStatus(homeSerialNumber, userId,4);
        //调用service层的方法，退出家庭
        accountService.updateUserHSN(userId, null);
        sendMsgToUser(userId,"",userId+"已退出家庭！");
        //根据家庭序列号获取家庭成员列表
        List<User> homeMembers = accountService.getAccountsByHSN(homeSerialNumber);
        //通知每个成员更新当前页面数据
        for (User user : homeMembers) {
            //使用gson将homeMembers对象转换为json字符串，允许NULL值
            Gson gson = new GsonBuilder().serializeNulls().create();
            String homeMemberJson = gson.toJson(user);
            webSocket.sendMessageToUser(String.valueOf(user.getId()), homeMemberJson);
        }
        System.out.println("已向家庭成员发送退出通知！");
        return ResultUtil.success("退出成功！");
    }

    private void sendMsgToUser(int userId, String homeSerialNumber,String msg) {
        //根据removeId获取用户详情
        User user = accountService.getAccountDetail(userId);
        //由家庭序列号获取家庭名
        String homeName = homeService.getHomeName(homeSerialNumber);
        user.setHomeName(homeName);
        //使用gson将user对象转换为json字符串，允许NULL值
        Gson gson = new GsonBuilder().serializeNulls().create();
        String userJson = gson.toJson(user);
        //调用webSocket的sendMessageToUser方法，向被移除者发送userJson
        webSocket.sendMessageToUser(String.valueOf(userId), userJson);
        System.out.println(msg);
    }

    //获取家庭成员数量
    @GetMapping("/getFamilyMemberNum/{homeSerialNumber}")
    public ResultUtil<Object> getFamilyMemberNum(@PathVariable("homeSerialNumber") String homeSerialNumber) {
        //查看home表中是否存在该家庭序列号
        if (!homeService.isHomeExist(homeSerialNumber)) {
            return ResultUtil.error("该家庭序列号不存在！");
        }
        //调用service层的方法，获取家庭成员列表
        int familyMemberNum = accountService.getAccountsByHSN(homeSerialNumber).size();
        return ResultUtil.success("获取家庭成员数成功", familyMemberNum);
    }
}
