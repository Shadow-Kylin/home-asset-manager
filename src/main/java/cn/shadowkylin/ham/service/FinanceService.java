package cn.shadowkylin.ham.service;

import cn.shadowkylin.ham.dao.FinanceDao;
import cn.shadowkylin.ham.model.Finance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @创建人 li cong
 * @创建时间 2023/4/2
 * @描述 财务service
 */
@Service
public class FinanceService {
    @Resource
    private FinanceDao financeDao;
    /**
     * 获取财务列表
     */
    public Object getFinanceList(int userId) {
        return financeDao.getFinanceList(userId);
    }
    /**
     * 获取财务详情
     */
    public Object getFinanceDetail(int financeId) {
        return financeDao.getFinanceDetail(financeId);
    }
    /**
     * 添加财务
     */
    public void addFinance(Finance finance) {
        financeDao.addFinance(finance);
    }
    /**
     * 修改财务
     */
    public void updateFinance(Finance finance) {
        financeDao.updateFinance(finance);
    }
    /**
     * 删除财务
     */
    public void deleteFinance(int financeId) {
        financeDao.deleteFinance(financeId);
    }
    /**
     * 批量删除财务
     */
    public void deleteFinanceList(int[] financeIdList) {
        financeDao.deleteFinanceList(financeIdList);
    }
}
