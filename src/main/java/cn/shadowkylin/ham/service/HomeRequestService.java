package cn.shadowkylin.ham.service;

import cn.shadowkylin.ham.dao.HomeRequestDao;
import cn.shadowkylin.ham.model.HomeRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/4/11
 * @描述
 */
@Service
public class HomeRequestService {
    @Resource
    private HomeRequestDao homeRequestDao;

    public void agreeJoinHome(String homeSerialNumber, int joinId) {
        //改变请求状态为1
        homeRequestDao.agreeJoinHome(homeSerialNumber, joinId);
    }

    public void refuseJoinHome(String homeSerialNumber, int joinId) {
        homeRequestDao.refuseJoinHome(homeSerialNumber, joinId);
    }

    public List<HomeRequest> getHomeRequestList(int userId) {
        return homeRequestDao.getHomeRequestList(userId);
    }

    public boolean isRequestExist(int userId, String homeSerialNumber) {
        System.out.println(homeRequestDao.isRequestExist(userId, homeSerialNumber)!=null);
        return homeRequestDao.isRequestExist(userId, homeSerialNumber) != null;
    }

    public void joinHome(int userId, int creatorId, String homeSerialNumber, Date createdDate) {
        homeRequestDao.joinHome(userId, creatorId, homeSerialNumber, createdDate);
    }

    public List<HomeRequest> getUnhandledRequestList(int userId) {
        return homeRequestDao.getUnhandledRequestList(userId);
    }

    public List<HomeRequest> getHandledRequestList(int userId) {
        return homeRequestDao.getHandledRequestList(userId);
    }

    public boolean hasRequest(int userId) {
        return homeRequestDao.getHomeRequestList(userId) != null;
    }

    public void setRequestStatus(String homeSerialNumber, int application_id, int status) {
        homeRequestDao.setRequestStatus(homeSerialNumber, application_id, status);
    }

    public boolean hasActiveRequest(int userId,String homeSerialNumber) {
        return homeRequestDao.getActiveRequest(userId,homeSerialNumber) != null;
    }

    public boolean hasInactiveRequest(int userId, String homeSerialNumber) {
        return homeRequestDao.getInactiveRequest(userId, homeSerialNumber) != null;
    }
}
