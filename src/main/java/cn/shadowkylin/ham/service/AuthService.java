package cn.shadowkylin.ham.service;

import cn.shadowkylin.ham.dao.AuthDao;
import cn.shadowkylin.ham.dao.HomeDao;
import cn.shadowkylin.ham.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */

@Service
public class AuthService {
    @Resource
    private AuthDao authDao;
    @Resource
    private HomeDao homeDao;
    //登录
    public User login(String phone,String password){

        User loginUser=authDao.login(phone,password);
        //根据家庭序列号获取家庭名称
        if(loginUser!=null){
            loginUser.setHomeName(homeDao.getHomeName(loginUser.getHomeSerialNumber()));
        }
        return loginUser;
    }
    //检查手机号是否已经注册
    public boolean isExist(String phone){
        return authDao.isExist(phone);
    }
    //注册
    public int register(User user){
        return authDao.register(user);
    }
    //修改密码

    public void updatePassword(User user) {
        authDao.updatePassword(user);
    }

    public void updatePassword(int userId, String newPwd) {
        authDao.updatePassword(userId, newPwd);
    }

    public String getPassword(int userId) {
        return authDao.getPassword(userId);
    }
}
