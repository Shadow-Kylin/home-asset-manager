package cn.shadowkylin.ham.service;

import cn.shadowkylin.ham.dao.AccountDao;
import cn.shadowkylin.ham.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/3/31
 * @描述
 */

@Service
public class AccountService {
    @Autowired
    private AccountDao accountDao;

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
}
