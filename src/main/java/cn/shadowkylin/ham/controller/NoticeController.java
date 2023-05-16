package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.model.Notice;
import cn.shadowkylin.ham.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Date;
import java.time.LocalDate;

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

    /**
     * 删除公告
     */
    @PostMapping("/deleteNotice/{id}")
    public ResultUtil<Object> deleteNotice(@PathVariable("id") int noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResultUtil.success("删除公告成功");
    }

    /**
     * 添加公告
     */
    @PostMapping("/addNotice")
    public ResultUtil<Object> addNotice(@RequestBody Notice notice) {
        //设置公告时间
        notice.setCreatedDate(Date.valueOf(LocalDate.now()));
        noticeService.addNotice(notice);
        return ResultUtil.success("添加公告成功");
    }

    /**
     * 修改公告
     */
    @PostMapping("/updateNotice")
    public ResultUtil<Object> updateNotice(@RequestBody Notice notice) {
        noticeService.updateNotice(notice);
        return ResultUtil.success("修改公告成功");
    }

}
