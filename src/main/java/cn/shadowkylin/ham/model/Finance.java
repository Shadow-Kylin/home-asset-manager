package cn.shadowkylin.ham.model;

import java.sql.Date;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */
public class Finance {
    private Integer id;
    //资产序列号
    private String assetSerialNumber;
    //财务名称
    private String name;
    private Integer userId;
    private Date date;
    private byte type;
    private Double amount;
    private String notes;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
