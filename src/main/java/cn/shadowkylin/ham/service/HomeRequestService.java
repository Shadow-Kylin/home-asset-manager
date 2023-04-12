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
        homeRequestDao.agreeJoinHome(homeSerialNumber, joinId);
    }

    public void refuseJoinHome(String homeSerialNumber, int joinId) {
        homeRequestDao.refuseJoinHome(homeSerialNumber, joinId);
    }

    public List<HomeRequest> getHomeRequestList(String homeSerialNumber) {
        return homeRequestDao.getHomeRequestList(homeSerialNumber);
    }

    public boolean isRequestExist(int userId, String homeSerialNumber) {
        return homeRequestDao.isRequestExist(userId, homeSerialNumber) != null;
    }

    public void joinHome(int userId, int creatorId, String homeSerialNumber, Date createdDate) {
        homeRequestDao.joinHome(userId, creatorId, homeSerialNumber,createdDate);
    }
}
