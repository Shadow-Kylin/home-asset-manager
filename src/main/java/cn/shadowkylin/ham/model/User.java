package cn.shadowkylin.ham.model;

import java.sql.Date;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */
public class User {
    //用户ID
    private Integer id;
    //用户名
    private String username;
    //密码
    private String password;
    //手机号
    private String phone;
    //创建时间
    private Date createdDate;
    //签名
    private String signature;
    //所属家庭序列号
    private String homeSerialNumber;
    //家庭名称
    private String homeName;
    //角色
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getHomeSerialNumber() {
        return homeSerialNumber;
    }

    public void setHomeSerialNumber(String homeSerialNumber) {
        this.homeSerialNumber = homeSerialNumber;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
