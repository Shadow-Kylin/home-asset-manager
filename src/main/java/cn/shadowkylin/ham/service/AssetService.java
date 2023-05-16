package cn.shadowkylin.ham.service;

import cn.shadowkylin.ham.dao.AssetDao;
import cn.shadowkylin.ham.model.Asset;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public List<Asset> getAssetList(int userId,String homeSerialNumber, String searchType, String searchValue,
                                    String types) {
        return assetDao.getAssetList(userId,homeSerialNumber,searchType,searchValue,types);
    }
    /**
     * 获取资产详情
     */
    public Asset getAssetDetail(int assetId) {
        return assetDao.getAssetDetail(assetId);
    }
    /**
     * 添加资产
     */
    public void addAsset(Asset asset) {
        assetDao.addAsset(asset);
    }
    /**
     * 批量添加资产
     */
    public void addAssetList(List<Asset> assetList) {
        assetDao.addAssetList(assetList);
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

    public List<Asset> getFixedAsset(int userId, String homeSerialNumber) {
        return assetDao.getFixedAsset(userId, homeSerialNumber);
    }

    public List<Asset> getFluidAsset(int userId, String homeSerialNumber) {
        return assetDao.getFluidAsset(userId, homeSerialNumber);
    }

    public boolean checkAsset(Integer userId, String homeSerialNumber, String assetSerialNumber) {
        return assetDao.checkAsset(userId, homeSerialNumber, assetSerialNumber)!=null;
    }

    public void updateAssetsHSN(int requestId, String homeSerialNumber) {
        assetDao.updateAssetsHSN(requestId, homeSerialNumber);
    }

    public void clearHomeAsset(String homeSerialNumber) {
        assetDao.clearHomeAsset(homeSerialNumber);
    }
}
