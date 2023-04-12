package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.HttpStatus;
import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.model.HomeRequest;
import cn.shadowkylin.ham.service.AccountService;
import cn.shadowkylin.ham.service.HomeRequestService;
import cn.shadowkylin.ham.service.HomeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/4/11
 * @描述
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

    /**
     * 根据用户ID获取他/她创建的家庭的待处理请求加入列表，待处理即status为0
     */
    @GetMapping("/getHomeRequestList/{userId}")
    public ResultUtil<Object> getHomeRequestList(@PathVariable("userId") int userId) {
        //获取用户所在的家庭，判断其是否是家庭创建者
        String homeSerialNumber = accountService.getHSNByUserId(userId);
        if (homeSerialNumber == null) {
            return ResultUtil.error("该用户不在任何家庭中！");
        }
        if (!homeService.isHomeCreator(homeSerialNumber, userId)) {
            return ResultUtil.error("该用户不是家庭创建者！");
        }
        //获取待处理的请求列表(status=0)并返回
        List<HomeRequest> homeRequestList = homeRequestService.getHomeRequestList(homeSerialNumber);
        //没有待处理的请求
        if (homeRequestList == null) {
            return ResultUtil.error("没有待处理的请求！");
        }
        return ResultUtil.success("获取待处理的请求列表成功！", homeRequestList);
    }

    /**
     * 同意用户加入创建者的家庭
     */
    @GetMapping("/agreeJoinHome")
    public ResultUtil<Object> agreeJoinHome(@RequestParam("requestId") int requestId, @RequestParam("joinId") int joinId) {
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
        return ResultUtil.success("同意用户加入家庭成功！");
    }

    /**
     * 拒绝用户加入创建者的家庭
     */
    @GetMapping("/refuseJoinHome")
    public ResultUtil<Object> refuseJoinHome(@RequestParam("requestId") int userId, @RequestParam("joinId") int joinId) {
        //获取用户所在的家庭，判断其是否是家庭创建者
        String homeSerialNumber = accountService.getHSNByUserId(userId);
        if (homeSerialNumber == null) {
            return ResultUtil.error("该用户不在任何家庭中！");
        }
        if (!homeService.isHomeCreator(homeSerialNumber, userId)) {
            return ResultUtil.error("该用户不是家庭创建者！");
        }
        //拒绝用户加入家庭，即设置status为0
        homeRequestService.refuseJoinHome(homeSerialNumber, joinId);
        return ResultUtil.success("拒绝用户加入家庭成功！");
    }

    /**
     * 用户申请加入家庭
     */
    @GetMapping("/joinHome")
    public ResultUtil<Object> joinHome(@RequestParam("userId") int userId, @RequestParam("homeSerialNumber") String homeSerialNumber) {
        //判断该用户是否已经在家庭中以及是否已经申请加入该家庭
        if (accountService.getHSNByUserId(userId) != null &
        homeRequestService.isRequestExist(userId, homeSerialNumber)){
            //违法操作，该用户已经在家庭中或是否已经申请加入该家庭
            return ResultUtil.error("该用户已经在家庭中！", null, HttpStatus.ILLEGAL_OPERATION);
        }
        //判断该家庭是否存在
        if (!homeService.isHomeExist(homeSerialNumber)) {
            return ResultUtil.error("该家庭不存在！", null, HttpStatus.HOME_NOT_EXIST);
        }
        //获取该家庭的创建者ID
        int creatorId = homeService.getCreatorIdByHSN(homeSerialNumber);
        //获取当前系统的日期
        Date date = new Date(System.currentTimeMillis());
        //申请加入家庭
        homeRequestService.joinHome(userId,creatorId, homeSerialNumber,date);
        return ResultUtil.success("申请加入家庭成功！");
    }
}
