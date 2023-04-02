package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.*;
import cn.shadowkylin.ham.model.Result;
import cn.shadowkylin.ham.model.User;
import cn.shadowkylin.ham.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述 用户认证
 */

@RestController
@RequestMapping("/auth")
public class AuthController {
    //Resource和Autowired的区别：Resource是J2EE的注解，Autowired是Spring的注解，Resource默认按照名称装配，而Autowired默认按照类型装配
    //如果没有指定name属性，当注解写在字段上，即默认取字段名进行按照名称查找；如果注解写在setter方法上，默认取属性名进行装配。
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private AuthService authService;
    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<Object> login(@RequestBody User user) {
        if (user == null) {
            return ResultUtil.error("参数错误！");
        }
        User user1 = authService.login(user.getPhone(), user.getPassword());
        if (user1 == null) {
            return ResultUtil.error("用户名或密码错误！");
        }
        //生成token
        String token = jwtTokenUtil.genToken(user1);
        //保存token到redis
        redisUtil.set("token-" + user1.getId(), token, 30 * 60);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", user1);
        return ResultUtil.success("登录成功！",map);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result<Object> register(@RequestBody User user,@RequestParam("code") String code) {
        if (user == null || user.getPhone() == null || user.getPassword() == null) {
            return ResultUtil.error("参数错误！");
        }
        //判断手机号是否已经注册
        if (authService.isExist(user.getPhone())) {
            return ResultUtil.error("该手机号已经注册！", HttpStatus.PHONE_EXIST);
        }
        //判断验证码是否正确
        String code1 = (String) redisUtil.get("code-" + user.getPhone());
        if (StringUtils.isBlank(code1)) {
            return ResultUtil.error("验证码已过期！");
        }
        if (!code.equals(code1)) {
            return ResultUtil.error("验证码错误！");
        }
        //注册
        //authService.register(user);
        return ResultUtil.success(null, "注册成功！");
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result<Object> logout(@RequestHeader("Authorization") String token) {
        int userId =jwtTokenUtil.getUserIdFromToken(token);
        redisUtil.del("token-" + userId);
        return ResultUtil.success("登出成功！");
    }

    /**
     * 修改密码
     */
    @PostMapping("/updatePassword")
    public Result<Object> updatePassword(@RequestBody User user) {
        if (user == null || user.getPhone() == null || user.getPassword() == null) {
            return ResultUtil.error("参数错误！");
        }
        //修改密码
        authService.updatePassword(user);
        return ResultUtil.success(null, "修改密码成功！");
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/sendSMS")
    public Result<Object> sendSms(@RequestBody User user) {
        if (user == null || user.getPhone() == null) {
            return ResultUtil.error("参数错误！");
        }
        //判断手机号是否已经注册
        if (authService.isExist(user.getPhone())) {
            return ResultUtil.error("该手机号已经注册！", HttpStatus.PHONE_EXIST);
        }
        //产生随机六位数验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        System.out.println(code);
        //发送短信验证码
        //String result = ALYSmsUtil.sendSms(user.getPhone(), code);

        //序列化，使用容联云时使用
        //Gson gson = new Gson();
        //Map<String, Object> map = gson.fromJson(result, Map.class);
        //System.out.println(map.get("statusMsg"));
        //return ResultUtil.success(null, result);

        //if(result.equals("OK")){
        //    //将验证码存入redis
        //    redisUtil.set("code-" + user.getPhone(), code, 60 * 5);
        //    return ResultUtil.success(null, "发送成功！");
        //}
        //else
        //    return ResultUtil.error(result);

        //测试用下面语句，发送次数有限
        redisUtil.set("code-" + user.getPhone(), code, 60 * 5);
        return ResultUtil.success(null, "发送成功！");
    }
}
