package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.HttpStatus;
import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.common.WebSocket;
import cn.shadowkylin.ham.model.HomeRequest;
import cn.shadowkylin.ham.model.User;
import cn.shadowkylin.ham.service.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/4/11
 * @描述 家庭请求控制器
 */
@RestController
@RequestMapping("/homeRequest")
public class HomeRequestController {
    @Resource
    private HomeRequestService homeRequestService;
    @Resource
    private HomeService homeService;
    @Resource
    private AccountService accountService;
    @Resource
    private AssetService assetService;
    @Resource
    private FinanceService financeService;
    @Resource
    private WebSocket webSocket;

    /**
     * 根据用户ID获取所有请求列表
     */
    @GetMapping("/getRequestList/{userId}")
    public ResultUtil<Object> getHomeRequestList(@PathVariable("userId") int userId) {
        //获取待处理的请求列表(status=0)并返回
        List<HomeRequest> homeRequestList = homeRequestService.getHomeRequestList(userId);
        //没有待处理的请求
        if (homeRequestList == null) {
            return ResultUtil.error("没有待处理的请求！");
        }
        //将审核人的ID转换为审核人的用户名
        for (HomeRequest homeRequest : homeRequestList) {
            homeRequest.setRecipientName(accountService.getUserName(homeRequest.getRecipientId()));
        }
        return ResultUtil.success("获取待处理的请求列表成功！", homeRequestList);
    }

    /**
     * 根据用户ID获取他/她创建的家庭的待处理请求加入列表，待处理即status为0
     */
    @GetMapping("/getUnhandledRequestList/{userId}")
    public ResultUtil<Object> getUnhandledRequestList(@PathVariable("userId") int userId) {
        //获取用户所在的家庭，判断其是否是家庭创建者
        String homeSerialNumber = accountService.getHSNByUserId(userId);
        if (homeSerialNumber == null) {
            return ResultUtil.error("该用户不在任何家庭中！", HttpStatus.UNAUTHORIZED);
        }
        if (!homeService.isHomeCreator(homeSerialNumber, userId)) {
            return ResultUtil.error("该用户不是家庭创建者！", HttpStatus.UNAUTHORIZED);
        }
        //获取待处理的请求列表(status=0)并返回
        List<HomeRequest> homeRequestList = homeRequestService.getUnhandledRequestList(userId);
        //没有待处理的请求
        if (homeRequestList == null) {
            return ResultUtil.error("没有待处理的请求！");
        }
        //获取请求者的用户名和手机号
        for (HomeRequest homeRequest : homeRequestList) {
            homeRequest.setApplicationName(accountService.getUserName(homeRequest.getApplicationId()));
            homeRequest.setApplicationPhone(accountService.getUserPhone(homeRequest.getApplicationId()));
        }
        return ResultUtil.success("获取待处理的请求列表成功！", homeRequestList);
    }

    /**
     * 根据用户ID获取他/她创建的家庭的已处理请求加入列表，已处理即status不为0
     */
    @GetMapping("/getHandledRequestList/{userId}")
    public ResultUtil<Object> getHandledRequestList(@PathVariable("userId") int userId) {
        //获取用户所在的家庭，判断其是否是家庭创建者
        String homeSerialNumber = accountService.getHSNByUserId(userId);
        if (homeSerialNumber == null) {
            return ResultUtil.error("该用户不在任何家庭中！");
        }
        if (!homeService.isHomeCreator(homeSerialNumber, userId)) {
            return ResultUtil.error("该用户不是家庭创建者！");
        }
        //获取已处理的请求列表(status!=0)并返回
        List<HomeRequest> homeRequestList = homeRequestService.getHandledRequestList(userId);
        //没有已处理的请求
        if (homeRequestList == null) {
            return ResultUtil.error("没有已处理的请求！");
        }
        //获取请求者的用户名和手机号
        for (HomeRequest homeRequest : homeRequestList) {
            homeRequest.setApplicationName(accountService.getUserName(homeRequest.getApplicationId()));
            homeRequest.setApplicationPhone(accountService.getUserPhone(homeRequest.getApplicationId()));
        }
        return ResultUtil.success("获取已处理的请求列表成功！", homeRequestList);
    }

    /**
     * 同意用户加入创建者的家庭
     */
    @PostMapping("/agreeJoinHome")
    public ResultUtil<Object> agreeJoinHome(@RequestParam("requestId") int requestId,
                                            @RequestParam("joinId") int joinId) {
        //获取用户所在的家庭，判断其是否是家庭创建者
        String homeSerialNumber = accountService.getHSNByUserId(requestId);
        if (homeSerialNumber == null) {
            return ResultUtil.error("该用户不在任何家庭中！", null, HttpStatus.UNAUTHORIZED);
        }
        if (!homeService.isHomeCreator(homeSerialNumber, requestId)) {
            return ResultUtil.error("该用户不是家庭创建者！", null, HttpStatus.UNAUTHORIZED);
        }
        //判断该用户是否已经在家庭中
        if (accountService.getHSNByUserId(joinId) != null) {
            return ResultUtil.error("该用户已经在家庭中！");
        }
        //同意用户加入家庭，即设置status为1
        homeRequestService.agreeJoinHome(homeSerialNumber, joinId);
        //将用户家庭序列号设置为创建者的家庭序列号
        accountService.updateUserHSN(joinId, homeSerialNumber);
        //将用户的资产和财务的家庭序列号设置为创建者的家庭序列号
        assetService.updateAssetsHSN(joinId, homeSerialNumber);
        financeService.updateFinancesHSN(joinId, homeSerialNumber);
        sendMsgToUser(joinId, homeSerialNumber,requestId+"同意用户"+joinId+"加入家庭成功！");
        return ResultUtil.success("同意用户加入家庭成功！");
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

    /**
     * 拒绝用户加入创建者的家庭
     */
    @PostMapping("/refuseJoinHome")
    public ResultUtil<Object> refuseJoinHome(@RequestParam("requestId") int userId, @RequestParam("joinId") int joinId) {
        //获取用户所在的家庭，判断其是否是家庭创建者
        String homeSerialNumber = accountService.getHSNByUserId(userId);
        if (homeSerialNumber == null) {
            return ResultUtil.error("该用户不在任何家庭中！");
        }
        if (!homeService.isHomeCreator(homeSerialNumber, userId)) {
            return ResultUtil.error("该用户不是家庭创建者！");
        }
        //拒绝用户加入家庭，即设置status为2
        homeRequestService.refuseJoinHome(homeSerialNumber, joinId);
        sendMsgToUser(joinId, homeSerialNumber,userId+"拒绝用户"+joinId+"加入家庭成功！");
        return ResultUtil.success("拒绝用户加入家庭成功！");
    }

    /**
     * 用户申请加入家庭
     */
    @PostMapping("/joinHome/{userId}")
    public ResultUtil<Object> joinHome(@PathVariable("userId") int userId,
                                       @RequestParam("homeSerialNumber") String homeSerialNumber) {
        //用户不能重复申请加入该家庭
        if (accountService.getHSNByUserId(userId) != null ||
                homeRequestService.hasActiveRequest(userId, homeSerialNumber)) {
            //违法操作，该用户已经申请加入该家庭
            return ResultUtil.error("该用户已经发起过请求！", null, HttpStatus.ILLEGAL_OPERATION);
        }
        //判断该家庭是否存在
        if (!homeService.isHomeExist(homeSerialNumber)) {
            return ResultUtil.error("该家庭不存在！", null, HttpStatus.HOME_NOT_EXIST);
        }
        //获取该家庭的创建者ID
        int creatorId = homeService.getCreatorIdByHSN(homeSerialNumber);
        sendMsgToUser(creatorId, homeSerialNumber,"用户"+userId+"申请加入家庭成功！");
        //用户曾申请加入该家庭但被拒绝，更新该请求的状态为0
        if (homeRequestService.hasInactiveRequest(userId, homeSerialNumber)) {
            System.out.println("用户曾申请加入该家庭但被拒绝，更新该请求的状态为0");
            homeRequestService.setRequestStatus(homeSerialNumber, userId, 0);
            return ResultUtil.success("申请加入家庭成功！");
        }
        //获取当前系统的日期
        Date date = new Date(System.currentTimeMillis());
        //申请加入家庭
        homeRequestService.joinHome(userId, creatorId, homeSerialNumber, date);
        return ResultUtil.success("申请加入家庭成功！");
    }
}
