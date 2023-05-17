package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.model.Asset;
import cn.shadowkylin.ham.service.AccountService;
import cn.shadowkylin.ham.service.AssetService;
import cn.shadowkylin.ham.service.AssetTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private AccountService accountService;

    /**
     * 获取资产列表
     * PathVariable：获取url中的路径参数 例如/getAssetList/userId
     * RequestParam：获取url中的查询参数 例如/getAssetList?userId=1
     *
     * @param userId           用户ID
     * @param homeSerialNumber 家庭序列号
     * @param pageNum          页码
     * @param pageSize         每页数量
     * @param searchType       搜索类型
     * @param searchValue      搜索值
     * @param type             用于区分资产类型，fixed为固定资产，fluid为流动资产
     */
    @GetMapping("/getAssetList")
    public ResultUtil<Object> getAssetList(
            @RequestParam(value = "userId") int userId,
            @RequestParam(value = "homeSerialNumber") String homeSerialNumber,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "searchType", defaultValue = "") String searchType,
            @RequestParam(value = "searchValue", defaultValue = "") String searchValue,
            @RequestParam(value = "type", defaultValue = "fixed") String type) {
        //使用PageHelper分页
        PageHelper.startPage(pageNum, pageSize);
        String types = "";
        if (searchType.equals("assetType")) {//如果搜索类型为'类型'，则将搜索值转换为对应的asset_type_id
            searchValue = String.valueOf(assetTypeService.getAssetTypeId(searchValue));
            System.out.println(searchValue);
        }
        //如果type为fixed并且没有搜索 '类型'，则获取固定资产列表，否则获取流动资产列表，
        // 固定资产ID为1,2,3，4,5，流动资产ID为6,7,8,9,10
        if (type.equals("fixed")) {
            types = "1,2,3,4,5";
        } else if (type.equals("fluid")) {
            types = "6,7,8,9,10";
        }
        //获取用户家庭序列号
        //homeSerialNumber = accountService.getHSNByUserId(userId);
        List<Asset> assetList = assetService.getAssetList(userId, homeSerialNumber, searchType, searchValue,types);
        //将Asset的asset_type_id转换为asset_type_name，user_id转换为user_name
        for (Asset asset : assetList) {
            asset.setAssetTypeName(assetTypeService.getAssetTypeDetail(asset.getAssetTypeId()).getName());
            asset.setUserName(accountService.getAccountDetail(asset.getUserId()).getUsername());
        }
        //使用PageInfo包装查询结果，只需要将pageInfo交给页面就可以
        PageInfo pageInfo = new PageInfo(assetList);
        return ResultUtil.success("获取资产列表成功", pageInfo);
    }

    /**
     * 获取资产详情
     *
     * @param assetId 资产ID
     */
    @GetMapping("/getAssetDetail/{assetId}")
    public ResultUtil<Object> getAssetDetail(@PathVariable("assetId") int assetId) {
        return ResultUtil.success("获取资产详情成功", assetService.getAssetDetail(assetId));
    }

    /**
     * 添加资产
     *
     * @param asset 资产信息
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
     *
     * @param assetList 资产信息列表
     */
    @PostMapping("/addAssetList")
    public ResultUtil<Object> addAssetList(@RequestBody List<Asset> assetList) {
        assetService.addAssetList(assetList);
        return ResultUtil.success("批量添加资产成功");
    }

    /**
     * 修改资产
     *
     * @param asset 资产信息
     */
    @PostMapping("/updateAsset")
    public ResultUtil<Object> updateAsset(@RequestBody Asset asset) {
        assetService.updateAsset(asset);
        return ResultUtil.success("修改资产成功");
    }

    /**
     * 删除资产
     *
     * @param assetId 资产ID
     */
    @PostMapping("/deleteAsset/{assetId}")
    public ResultUtil<Object> deleteAsset(@PathVariable("assetId") int assetId) {
        assetService.deleteAsset(assetId);
        return ResultUtil.success("删除资产成功");
    }

    /**
     * 批量删除资产
     *
     * @param assetIdList 资产ID列表
     */
    @PostMapping("/deleteAssetList")
    public ResultUtil<Object> deleteAssetList(@RequestBody int[] assetIdList) {
        assetService.deleteAssetList(assetIdList);
        return ResultUtil.success("批量删除资产成功");
    }

    /**
     * 统计用户或者家庭每种固定类型资产的价值
     *
     * @param userId           用户ID
     * @param homeSerialNumber 家庭序列号
     */
    @GetMapping("/analyseFixedAsset")
    public ResultUtil<Object> analyseFixedAsset(
            @RequestParam(value = "userId") int userId,
            @RequestParam(value = "homeSerialNumber") String homeSerialNumber) {
        //通过用户ID或家庭序列号获取固定资产列表
        List<Asset> assetList = assetService.getFixedAsset(userId, homeSerialNumber);
        //将资产类型ID转换为资产类型名称
        for (Asset asset : assetList) {
            asset.setAssetTypeName(assetTypeService.getAssetTypeDetail(asset.getAssetTypeId()).getName());
        }
        //统计不同类型资产的价值
        Map<String, Double> currentValueMap = new HashMap<>();
        for (Asset asset : assetList) {
            if (currentValueMap.containsKey(asset.getAssetTypeName())) {
                currentValueMap.put(asset.getAssetTypeName(), currentValueMap.get(asset.getAssetTypeName()) + asset.getCurrentValue());
            } else {
                currentValueMap.put(asset.getAssetTypeName(), asset.getCurrentValue());
            }
        }
        //将统计结果转换为JSONArray
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();
        for (Map.Entry<String, Double> entry : currentValueMap.entrySet()) {
            ObjectNode jsonObject = mapper.createObjectNode();
            jsonObject.put("name", entry.getKey());
            jsonObject.put("value", entry.getValue());
            jsonArray.add(jsonObject);
        }
        return ResultUtil.success("统计用户或者家庭每种固定类型资产的价值成功", jsonArray);
    }

    /**
     * 统计用户或者家庭每种流动类型资产的价值
     *
     * @param userId           用户ID
     * @param homeSerialNumber 家庭序列号
     */
    @GetMapping("/analyseFluidAsset")
    public ResultUtil<Object> analyseFluidAsset(
            @RequestParam(value = "userId") int userId,
            @RequestParam(value = "homeSerialNumber") String homeSerialNumber) {
        //通过用户ID或家庭序列号获取流动资产列表
        List<Asset> assetList = assetService.getFluidAsset(userId, homeSerialNumber);
        //将资产类型ID转换为资产类型名称
        for (Asset asset : assetList) {
            asset.setAssetTypeName(assetTypeService.getAssetTypeDetail(asset.getAssetTypeId()).getName());
        }
        //统计不同类型资产的价值
        Map<String, Double> currentValueMap = new HashMap<>();
        for (Asset asset : assetList) {
            if (currentValueMap.containsKey(asset.getAssetTypeName())) {
                currentValueMap.put(asset.getAssetTypeName(), currentValueMap.get(asset.getAssetTypeName()) + asset.getCurrentValue());
            } else {
                currentValueMap.put(asset.getAssetTypeName(), asset.getCurrentValue());
            }
        }
        //将统计结果转换为JSONArray
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();
        for (Map.Entry<String, Double> entry : currentValueMap.entrySet()) {
            ObjectNode jsonObject = mapper.createObjectNode();
            jsonObject.put("name", entry.getKey());
            jsonObject.put("value", entry.getValue());
            jsonArray.add(jsonObject);
        }
        return ResultUtil.success("统计用户或者家庭每种流动类型资产的价值成功", jsonArray);
    }

}
