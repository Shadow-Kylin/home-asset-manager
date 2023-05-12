package cn.shadowkylin.ham.dao;

import cn.shadowkylin.ham.model.AssetType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/3/31
 * @描述
 */

@Mapper //Mapper注解，用于标注数据访问组件，即Dao组件
public interface AssetTypeDao {
    /**
     * 获取资产类型列表
     * 返回一个数组，数组里面是一个个资产类型
     */
    List<AssetType> getAssetTypeList();
    /**
     * 获取资产类型详情
     */
    AssetType getAssetTypeDetail(int id);
    /**
     * 增加资产类型
     */
    int addAssetType(String name);
    /**
     * 修改资产类型
     */
    int updateAssetType(int id,String name);
    /**
     * 删除资产类型
     */
    int deleteAssetType(int id);

    /**
     * 根据名称获取资产类型id
     * @param name
     * @return
     */
    Object getAssetTypeId(String name);
}
