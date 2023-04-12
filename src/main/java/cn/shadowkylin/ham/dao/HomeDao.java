package cn.shadowkylin.ham.dao;

import cn.shadowkylin.ham.model.Home;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;

/**
 * @创建人 li cong
 * @创建时间 2023/4/11
 * @描述
 */

@Mapper
public interface HomeDao {

    Home isHomeExist(String homeSerialNumber);

    String getHSNByUserId(int requestId);
    void createHome(int requestId, String homeName, String homeSerialNumber, Date createdDate);

    void deleteHome(String homeSerialNumber);

    Home isHomeCreator(String homeSerialNumber, int userId);

    String getHomeName(String homeSerialNumber);

    int getCreatorIdByHSN(String homeSerialNumber);
}
