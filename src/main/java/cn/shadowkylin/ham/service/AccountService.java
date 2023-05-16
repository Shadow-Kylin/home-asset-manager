package cn.shadowkylin.ham.service;

import cn.shadowkylin.ham.dao.AccountDao;
import cn.shadowkylin.ham.dao.HomeDao;
import cn.shadowkylin.ham.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/3/31
 * @描述
 */

@Service
public class AccountService {
    @Resource
    private AccountDao accountDao;
    @Resource
    private HomeDao homeDao;

    /**
     * 获取账户列表
     */
    public List<User> getAccountList() {
        return accountDao.getAccountList();
    }

    /**
     * 获取账户详情
     */
    public User getAccountDetail(int id) {
        return accountDao.getAccountDetail(id);
    }

    /**
     * 添加账户
     */
    public void addAccount(User user) {
        accountDao.addAccount(user);
    }

    /**
     * 修改账户
     */
    public void updateAccount(User user) {
        accountDao.updateAccount(user);
    }

    /**
     * 删除账户
     */
    public void deleteAccount(int id) {
        accountDao.deleteAccount(id);
    }

    /**
     * 批量删除账户
     */
    public void deleteAccountList(int[] idList) {
        accountDao.deleteAccountList(idList);
    }

    public List<User> getAccountsByHSN(String homeSerialNumber) {
        return accountDao.getAccountsByHSN(homeSerialNumber);
    }

    public void removeUserFromHome(int removeId) {
        accountDao.removeUserFromHome(removeId);
    }

    public boolean userHasJoinedHome(int userId) {
        return accountDao.userHasJoinedHome(userId) != "";
    }

    public void updateUserHSN(int userId, String homeSerialNumber) {
        accountDao.updateUserHSN(userId, homeSerialNumber);
    }

    public void disbandHome(String homeSerialNumber) {
        //将家庭成员们的家庭序列号置空
        accountDao.disbandHome(homeSerialNumber);
        //从家庭表中删除该家庭
        homeDao.deleteHome(homeSerialNumber);
    }

    public String getHSNByUserId(int userId) {
        return accountDao.getHSNByUserId(userId);
    }

    public String getUserName(int userId) {
        return accountDao.getUserName(userId);
    }

    public String getUserPhone(int userId) {
        return accountDao.getUserPhone(userId);
    }
}
