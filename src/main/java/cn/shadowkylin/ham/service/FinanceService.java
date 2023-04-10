package cn.shadowkylin.ham.service;

import cn.shadowkylin.ham.dao.FinanceDao;
import cn.shadowkylin.ham.model.Finance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public List<Finance> getFinanceList(int userId, String searchType, String searchValue, String type) {
        return financeDao.getFinanceList(userId, searchType, searchValue, type);
    }
    /**
     * 获取财务详情
     */
    public Finance getFinanceDetail(int financeId) {
        return financeDao.getFinanceDetail(financeId);
    }
    /**
     * 添加财务
     */
    public void addFinance(Finance finance) {
        financeDao.addFinance(finance);
    }
    /**
     * 批量添加财务
     */
    public void addFinanceList(List<Finance> financeList) {
        financeDao.addFinanceList(financeList);
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
    /**
     * 获取指定年份的财务收入列表
     */
    public double[] getIncomeByYear(int userId, int year) {
        List<Finance> financeList = financeDao.getIncomeByYear(userId, year);
        double[] incomeList = new double[12];
        //统计每个月的收入之和
        for (Finance finance : financeList) {
            incomeList[finance.getDate().getMonth()] += finance.getAmount();
        }
        return incomeList;
    }
    /**
     * 获取指定年份的财务支出列表
     */
    public double[] getExpenditureByYear(int userId, int year) {
        List<Finance> financeList = financeDao.getExpenditureByYear(userId, year);
        double[] expenditureList = new double[12];
        //统计每个月的支出之和
        for (Finance finance : financeList) {
            expenditureList[finance.getDate().getMonth()] += finance.getAmount();
        }
        return expenditureList;
    }
}
