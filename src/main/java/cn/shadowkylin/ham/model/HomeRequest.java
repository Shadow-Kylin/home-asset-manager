package cn.shadowkylin.ham.model;

/**
 * @创建人 li cong
 * @创建时间 2023/4/11
 * @描述
 */
public class HomeRequest {
    //请求记录ID
    private int id;
    //请求者ID
    private int applicationId;
    //请求者用户名
    private String applicationName;
    //被请求者ID
    private int recipientId;
    //被请求者用户名
    private String recipientName;
    //被请求加入的家庭序列号
    private String homeSerialNumber;
    //请求状态
    private String status;
    //请求日期
    private String createDate;
    //备注
    private String notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getHomeSerialNumber() {
        return homeSerialNumber;
    }

    public void setHomeSerialNumber(String homeSerialNumber) {
        this.homeSerialNumber = homeSerialNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
