package cn.shadowkylin.ham.dao;

import cn.shadowkylin.ham.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */

@Mapper
public interface AuthDao {
    /**
     * 登录
     */
    User login(String phone,String password);
    /**
     * 检查手机号是否已经注册
     * @param phone
     */
    boolean isExist(String phone);
    /**
     * 注册
     * @param user
     */
    int register(User user);
    /**
     * 修改密码
     * @param user
     */
    void updatePassword(User user);

    void updatePassword(int userId, String newPwd);

    String getPassword(int userId);

    void updatePasswordByPhone(String phone, String password);
}
