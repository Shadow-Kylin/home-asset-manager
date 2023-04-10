package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.model.Asset;
import cn.shadowkylin.ham.service.AssetService;
import cn.shadowkylin.ham.service.AssetTypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */

@RestController
@RequestMapping("/asset")
public class AssetController {
    @Resource
    private AssetService assetService;

    @Resource
    private AssetTypeService assetTypeService;

    /**
     * 获取资产列表
     * PathVariable：获取url中的路径参数 例如/getAssetList/userId
     * RequestParam：获取url中的查询参数 例如/getAssetList?userId=1
     */
    @GetMapping("/getAssetList/{userId}")
    public ResultUtil<Object> getAssetList(
            @PathVariable("userId") int userId,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "searchType", defaultValue = "") String searchType,
            @RequestParam(value = "searchValue", defaultValue = "") String searchValue,
            @RequestParam(value = "type", defaultValue = "fixed") String type) {
        //使用PageHelper分页
        PageHelper.startPage(pageNum, pageSize);
        if (searchType.equals("assetType")) {//如果搜索类型为'类型'，则将搜索值转换为对应的asset_type_id
            searchValue = String.valueOf(assetTypeService.getAssetTypeId(searchValue));
        }
        //如果type为fixed并且没有搜索 '类型'，则获取固定资产列表，否则获取流动资产列表，
        // 固定资产ID为1,2,3，4,5，流动资产ID为6,7,8,9,10
        else if (type.equals("fixed") && !searchType.equals("类型")) {
            searchType = "assetType";
            searchValue = "1,2,3,4,5";
        } else if (type.equals("fluid") && !searchType.equals("类型")) {
            searchType = "assetType";
            searchValue = "6,7,8,9,10";
        }
        //获取资产列表
        List<Asset> assetList = assetService.getAssetList(userId, searchType, searchValue);
        //将Asset的asset_type_id转换为asset_type_name
        for (Asset asset : assetList) {
            asset.setAssetTypeName(assetTypeService.getAssetTypeDetail(asset.getAssetTypeId()).getName());
        }
        //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
        PageInfo pageInfo = new PageInfo(assetList);
        return ResultUtil.success("获取资产列表成功", pageInfo);
    }

    /**
     * 获取资产详情
     */
    @GetMapping("/getAssetDetail/{assetId}")
    public ResultUtil<Object> getAssetDetail(@PathVariable("assetId") int assetId) {
        return ResultUtil.success("获取资产详情成功", assetService.getAssetDetail(assetId));
    }

    /**
     * 添加资产
     */
    @PostMapping("/addAsset")
    public ResultUtil<Object> addAsset(@RequestBody Asset asset) {
        //使用UUID生成资产序列号
        asset.setAssetSerialNumber(UUID.randomUUID().toString());
        assetService.addAsset(asset);
        return ResultUtil.success("添加资产成功", null);
    }

    /**
     * 批量添加资产
     */
    @PostMapping("/addAssetList")
    public ResultUtil<Object> addAssetList(@RequestBody List<Asset> assetList) {
        assetService.addAssetList(assetList);
        return ResultUtil.success("批量添加资产成功", null);
    }

    /**
     * 修改资产
     */
    @PostMapping("/updateAsset")
    public ResultUtil<Object> updateAsset(@RequestBody Asset asset) {
        assetService.updateAsset(asset);
        return ResultUtil.success("修改资产成功", null);
    }

    /**
     * 删除资产
     */
    @PostMapping("/deleteAsset/{assetId}")
    public ResultUtil<Object> deleteAsset(@PathVariable("assetId") int assetId) {
        assetService.deleteAsset(assetId);
        return ResultUtil.success("删除资产成功", null);
    }

    /**
     * 批量删除资产
     */
    @PostMapping("/deleteAssetList")
    public ResultUtil<Object> deleteAssetList(@RequestBody int[] assetIdList) {
        assetService.deleteAssetList(assetIdList);
        return ResultUtil.success("批量删除资产成功", null);
    }
}
