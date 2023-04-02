package cn.shadowkylin.ham.service;

import cn.shadowkylin.ham.dao.AuthDao;
import cn.shadowkylin.ham.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */

@Service
public class AuthService {
    @Autowired
    private AuthDao authDao;
    //登录
    public User login(String phone,String password){
        return authDao.login(phone,password);
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
}
