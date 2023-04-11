package cn.shadowkylin.ham.dao;

import org.apache.ibatis.annotations.Mapper;

/**
 * @创建人 li cong
 * @创建时间 2023/4/11
 * @描述
 */

@Mapper
public interface HomeDao {

    boolean isHomeExist(String homeSerialNumber);

    String getHSNByUserId(int requestId);

    int getCreatorByHSN(String homeSerialNumber);
}
