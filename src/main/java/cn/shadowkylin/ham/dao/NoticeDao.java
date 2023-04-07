package cn.shadowkylin.ham.dao;

import cn.shadowkylin.ham.model.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/4/5
 * @描述
 */

@Mapper
public interface NoticeDao {
    /**
     * 获取公告
     */
    List<Notice> getNotices();
}
