package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.common.WebSocket;
import cn.shadowkylin.ham.model.User;
import cn.shadowkylin.ham.service.AccountService;
import cn.shadowkylin.ham.service.AssetService;
import cn.shadowkylin.ham.service.FinanceService;
import cn.shadowkylin.ham.service.HomeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.UUID;

/**
 * @创建人 li cong
 * @创建时间 2023/4/11
 * @描述
 */

@RestController
@RequestMapping("/home")
public class HomeController {
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
     * 查询用户是否是家庭创建者
     */
    @GetMapping("/isHomeCreator")
    public ResultUtil<Object> isHomeCreator(@RequestParam("homeSerialNumber") String homeSerialNumber,
                                            @RequestParam("userId") int userId) {
        return ResultUtil.success("查询成功", homeService.isHomeCreator(homeSerialNumber, userId));
    }
    /**
     * 创建家庭
     */
    @PostMapping("/createHome/{requestId}")
    public ResultUtil<Object> createHome(@PathVariable("requestId") int requestId,
                                         @RequestParam("homeName") String homeName) {
        //获取请求者的家庭序列号，判断该序列号是否为空，若不为空，则已经创建过家庭，无法再次创建
        if (!StringUtils.isBlank(accountService.getHSNByUserId(requestId))) {
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
        financeService.updateFinancesHSN(requestId, homeSerialNumber);
        sendMsgToUser(requestId,homeSerialNumber,requestId+"已创建家庭" + homeName + "！");
        return ResultUtil.success("创建家庭成功！",homeSerialNumber);
    }
    /**
     * 家庭改名
     */
    @PostMapping("/renameHome/{requestId}")
    public ResultUtil<Object> renameHome(@PathVariable("requestId") int requestId,
                                         @RequestParam("homeSerialNumber") String homeSerialNumber,
                                         @RequestParam("homeName") String homeName) {
        //判断请求者是否是家庭创建者
        if (!homeService.isHomeCreator(homeSerialNumber, requestId)) {
            return ResultUtil.error("您不是家庭创建者，无法改名！");
        }
        //调用service层的方法，改名
        homeService.renameHome(homeSerialNumber, homeName);
        sendMsgToUser(requestId,homeSerialNumber,requestId+"的家庭名已改为" + homeName + "！");
        return ResultUtil.success("改名成功！");
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
}
