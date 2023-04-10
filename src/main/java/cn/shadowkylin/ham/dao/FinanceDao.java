package cn.shadowkylin.ham.dao;

import cn.shadowkylin.ham.model.Finance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/4/2
 * @描述
 */

@Mapper
public interface FinanceDao {
    /**
     * 获取财务列表
     */
    List<Finance> getFinanceList(int userId, String searchType, String searchValue, String type);
    /**
     * 获取财务详情
     */
    Finance getFinanceDetail(int financeId);
    /**
     * 添加财务
     */
    void addFinance(Finance finance);
    /**
     * 批量添加财务
     */
    void addFinanceList(List<Finance> financeList);
    /**
     * 修改财务
     */
    void updateFinance(Finance finance);
    /**
     * 删除财务
     */
    void deleteFinance(int financeId);
    /**
     * 批量删除财务
     */
    void deleteFinanceList(int[] financeIdList);
    /**
     * 获取指定年份的财务收入列表
     */
    List<Finance> getIncomeByYear(int userId, int year);
    /**
     * 获取指定年份的财务支出列表
     */
    List<Finance> getExpenditureByYear(int userId, int year);
}
