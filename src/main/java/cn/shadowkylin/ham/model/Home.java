package cn.shadowkylin.ham.model;

/**
 * @创建人 li cong
 * @创建时间 2023/4/11
 * @描述
 */
public class Home {
    //家庭序列号
    private String homeSerialNumber;
    //家庭名称
    private String name;
    //家庭创建者ID
    private int creator;
    //家庭创建者用户名
    private String creatorName;
    //家庭创建日期
    private String createdDate;

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
