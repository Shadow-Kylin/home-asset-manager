package cn.shadowkylin.ham.service;

import cn.shadowkylin.ham.dao.AssetDao;
import cn.shadowkylin.ham.model.Asset;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @创建人 li cong
 * @创建时间 2023/4/2
 * @描述
 */

@Service
public class AssetService {
    @Resource
    private AssetDao assetDao;
    /**
     * 获取资产列表
     */
    public Object getAssetList(int userId) {
        return assetDao.getAssetList(userId);
    }
    /**
     * 获取资产详情
     */
    public Object getAssetDetail(int assetId) {
        return assetDao.getAssetDetail(assetId);
    }
    /**
     * 添加资产
     */
    public void addAsset(Asset asset) {
        assetDao.addAsset(asset);
    }
    /**
     * 修改资产
     */
    public void updateAsset(Asset asset) {
        assetDao.updateAsset(asset);
    }
    /**
     * 删除资产
     */
    public void deleteAsset(int assetId) {
        assetDao.deleteAsset(assetId);
    }
    /**
     * 批量删除资产
     */
    public void deleteAssetList(int[] assetIdList) {
        assetDao.deleteAssetList(assetIdList);
    }
}
