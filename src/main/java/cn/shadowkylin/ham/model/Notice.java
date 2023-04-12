package cn.shadowkylin.ham.model;

import java.sql.Date;

/**
 * @创建人 li cong
 * @创建时间 2023/4/5
 * @描述
 */
public class Notice {
    //公告ID
    Integer id;
    //公告内容
    String content;
    //创建日期
    Date createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
