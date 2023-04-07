package cn.shadowkylin.ham.model;

import java.sql.Date;

/**
 * @创建人 li cong
 * @创建时间 2023/4/5
 * @描述
 */
public class Notice {
    String content;
    Date date;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
