package cn.shadowkylin.ham.dao;

import cn.shadowkylin.ham.model.HomeRequest;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/4/11
 * @描述
 */
@Mapper
public interface HomeRequestDao {
    void agreeJoinHome(String homeSerialNumber, int joinId);

    void refuseJoinHome(String homeSerialNumber, int joinId);

    List<HomeRequest> getHomeRequestList(int userId);

    HomeRequest isRequestExist(int userId, String homeSerialNumber);

    void joinHome(int userId, int creatorId, String homeSerialNumber, Date createdDate);

    List<HomeRequest> getUnhandledRequestList(int userId);

    List<HomeRequest> getHandledRequestList(int userId);

    void setRequestStatus(String homeSerialNumber, int applicationId, int status);

    HomeRequest getActiveRequest(int userId,String homeSerialNumber);

    HomeRequest getInactiveRequest(int userId, String homeSerialNumber);
}
