package cn.shadowkylin.ham.model;

import java.sql.Date;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */
public class Finance {
    //资产ID
    private Integer id;
    //资产序列号
    private String assetSerialNumber;
    //财务名称
    private String name;
    //创建者ID
    private Integer userId;
    //创建者名称
    private String userName;
    //创建时间
    private Date createdDate;
    //财务类型
    private byte type;
    //财务金额
    private Double amount;
    //备注
    private String notes;
    //所属家庭序列号
    private String homeSerialNumber;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHomeSerialNumber() {
        return homeSerialNumber;
    }

    public void setHomeSerialNumber(String homeSerialNumber) {
        this.homeSerialNumber = homeSerialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssetSerialNumber() {
        return assetSerialNumber;
    }

    public void setAssetSerialNumber(String assetSerialNumber) {
        this.assetSerialNumber = assetSerialNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
