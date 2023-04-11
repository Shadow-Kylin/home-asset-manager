package cn.shadowkylin.ham.dao;

import cn.shadowkylin.ham.model.Asset;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/4/2
 * @描述
 */
@Mapper
public interface AssetDao {
    /**
     * 获取资产列表
     */
    List<Asset> getAssetList(int userId, String homeSerialNumber, String searchType, String searchValue);

    /**
     * 获取资产详情
     */
    Asset getAssetDetail(int assetId);

    /**
     * 添加资产
     */
    void addAsset(Asset asset);

    /**
     * 批量添加资产
     */
    void addAssetList(List<Asset> assetList);

    /**
     * 修改资产
     */
    void updateAsset(Asset asset);

    /**
     * 删除资产
     */
    void deleteAsset(int assetId);

    /**
     * 批量删除资产
     */
    void deleteAssetList(int[] assetIdList);

    /**
     * 根据序列号获取资产
     */
    Asset getAssetByASN(int userId, String assetSerialNumber);

    /**
     * 获取固定资产
     */
    List<Asset> getFixedAsset(int userId, String homeSerialNumber);

    List<Asset> getFluidAsset(int userId, String homeSerialNumber);
}
