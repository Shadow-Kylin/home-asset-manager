package cn.shadowkylin.ham.service;

import cn.shadowkylin.ham.dao.AssetTypeDao;
import cn.shadowkylin.ham.model.AssetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/3/31
 * @描述
 */

@Service //Service注解，用于标注业务层组件，即Service组件，用于处理业务逻辑，可以调用多个Dao组件，组合业务逻辑
public class AssetTypeService {
    @Autowired //自动注入
    private AssetTypeDao assetTypeDao;
    /**
     * 获取资产类型列表
     */
    public List<AssetType> getAssetTypeList(){
        return assetTypeDao.getAssetTypeList();
    }
    /**
     * 获取资产类型详情
     */
    public AssetType getAssetTypeDetail(int id) {
        return assetTypeDao.getAssetTypeDetail(id);
    }
    /**
     * 增加资产类型
     */
    public int addAssetType(String name){
        return assetTypeDao.addAssetType(name);
    }
    /**
     * 修改资产类型
     */
    public int updateAssetType(int id,String name) {
        return assetTypeDao.updateAssetType(id, name);
    }
    /**
     * 删除资产类型
     */
    public int deleteAssetType(int id) {
        return assetTypeDao.deleteAssetType(id);
    }
}
