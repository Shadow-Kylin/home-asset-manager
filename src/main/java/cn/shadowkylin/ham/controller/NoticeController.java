package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.service.NoticeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @创建人 li cong
 * @创建时间 2023/4/5
 * @描述
 */

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    private NoticeService noticeService;
    /**
     * 获取公告
     */
    @GetMapping("/getNotices")
    public ResultUtil<Object> getNotices() {
        return ResultUtil.success("获取公告成功", noticeService.getNotices());
    }
}
