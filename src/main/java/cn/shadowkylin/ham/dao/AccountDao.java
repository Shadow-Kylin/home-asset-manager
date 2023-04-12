package cn.shadowkylin.ham.dao;

import cn.shadowkylin.ham.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/3/31
 * @描述
 */
@Mapper
public interface AccountDao {
    /**
     * 获取账户列表
     */
    List<User> getAccountList();
    /**
     * 获取账户详情
     */
    User getAccountDetail(int id);
    /**
     * 添加账户
     */
    void addAccount(User user);
    /**
     * 修改账户
     */
    void updateAccount(User user);
    /**
     * 删除账户
     */
    void deleteAccount(int id);
    /**
     * 批量删除账户
     */
    void deleteAccountList(int[] idList);

    List<User> getAccountsByHSN(String homeSerialNumber);

    void removeUserFromHome(int removeId);

    /**
     * 获取用户加入的家庭序列号，没加入就返回""
     * @param userId
     * @return
     */
    String userHasJoinedHome(int userId);

    void updateUserHSN(int userId, String homeSerialNumber);

    void disbandHome(String homeSerialNumber);

    String getHSNByUserId(int userId);
}
