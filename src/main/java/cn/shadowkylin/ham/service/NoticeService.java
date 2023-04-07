package cn.shadowkylin.ham.service;

import cn.shadowkylin.ham.dao.NoticeDao;
import cn.shadowkylin.ham.model.Notice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/4/5
 * @描述
 */

@Service
public class NoticeService {
    @Resource
    private NoticeDao noticeDao;
    /**
     * 获取公告
     */
    public List<Notice> getNotices() {
        return noticeDao.getNotices();
    }
}
