package cn.shadowkylin.ham.controller;

import cn.shadowkylin.ham.common.ResultUtil;
import cn.shadowkylin.ham.model.AssetType;
import cn.shadowkylin.ham.service.AssetTypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */

//Controller和RestController区别：Controller返回的是页面，RestController返回的是json数据

@RestController
@RequestMapping("/assetType")
public class AssetTypeController {
    @Resource
    private AssetTypeService assetTypeService;
    /**
     * 获取资产类型列表
     * 什么时候用getMapping，什么时候用postMapping？
     * getMapping：获取数据，不会改变数据，不会产生副作用
     * postMapping：修改数据，会产生副作用
     */
    @GetMapping("/getAssetTypeList")
    public ResultUtil<Object> getAssetTypeList(){
        //调用service层的方法，获取资产类型列表
        List<String> assetTypeList = assetTypeService.getAssetTypeList();
        return ResultUtil.success("获取资产类型列表成功",assetTypeList);
    }

    /**
     * 添加资产类型
     */
    @PostMapping("/addAssetType")
    public ResultUtil<Object> addAssetType(@RequestBody AssetType assetType){
        assetTypeService.addAssetType(assetType.getName());
        return ResultUtil.success("添加成功！",null);
    }

    /**
     * 修改资产类型
     */
    @PostMapping("/updateAssetType")
    public ResultUtil<Object> updateAssetType(@RequestBody AssetType assetType){
        assetTypeService.updateAssetType(assetType.getId(),assetType.getName());
        return ResultUtil.success("修改成功！",null);
    }

    /**
     * 删除资产类型
     */
    @PostMapping("/deleteAssetType")
    public ResultUtil<Object> deleteAssetType(@RequestBody AssetType assetType){
        assetTypeService.deleteAssetType(assetType.getId());
        return ResultUtil.success("删除成功！",null);
    }
}
