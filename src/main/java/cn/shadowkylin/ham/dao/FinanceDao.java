package cn.shadowkylin.ham.dao;

import cn.shadowkylin.ham.model.Finance;
import org.apache.ibatis.annotations.Mapper;

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
    Object getFinanceList(int userId);
    /**
     * 获取财务详情
     */
    Object getFinanceDetail(int financeId);
    /**
     * 添加财务
     */
    void addFinance(Finance finance);
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
}
