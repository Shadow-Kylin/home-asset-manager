package cn.shadowkylin.ham.service;

import cn.shadowkylin.ham.dao.HomeDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.UUID;

/**
 * @创建人 li cong
 * @创建时间 2023/4/11
 * @描述
 */

@Service
public class HomeService {
    @Resource
    private HomeDao homeDao;

    public boolean isHomeExist(String homeSerialNumber) {
        return homeDao.isHomeExist(homeSerialNumber)!=null;
    }

    public String getHSNByUserId(int requestId) {
        return homeDao.getHSNByUserId(requestId);
    }

    public int getCreatorIdByHSN(String homeSerialNumber) {
        return homeDao.getCreatorIdByHSN(homeSerialNumber);
    }

    public void createHome(int requestId, String homeName, String homeSerialNumber, Date createdDate) {
        homeDao.createHome(requestId, homeName, homeSerialNumber,createdDate);
    }

    public boolean isHomeCreator(String homeSerialNumber, int userId) {
        return homeDao.isHomeCreator(homeSerialNumber, userId) != null;
    }
}
