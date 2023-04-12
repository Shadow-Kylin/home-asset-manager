package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.service.HomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    /**
     * 查询用户是否是家庭创建者
     */
    @GetMapping("/isHomeCreator")
    public ResultUtil<Object> isHomeCreator(@RequestParam("homeSerialNumber") String homeSerialNumber,
                                            @RequestParam("userId") int userId) {
        return ResultUtil.success("查询成功", homeService.isHomeCreator(homeSerialNumber, userId));
    }
}
