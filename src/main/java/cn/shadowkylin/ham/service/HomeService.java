package cn.shadowkylin.ham.service;

import cn.shadowkylin.ham.dao.HomeDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;

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
        //返回结果不为null，说明家庭已存在，返回true
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
        return homeDao.isHomeCreator(homeSerialNumber, userId)!=null;
    }

    public String getHomeName(String homeSerialNumber) {
        return homeDao.getHomeName(homeSerialNumber);
    }
}
