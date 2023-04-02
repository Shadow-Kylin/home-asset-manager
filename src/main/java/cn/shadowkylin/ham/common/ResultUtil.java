package cn.shadowkylin.ham.common;

import cn.shadowkylin.ham.model.Result;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */
public class ResultUtil<T> {
    public static <T> Result<T> success(String msg){
        return success(msg,200,null);
    }
    public static <T> Result<T> success(String msg,T data){
        return success(msg,200,data);
    }
    public static <T> Result<T> success(String msg,int code,T data){
        Result<T> result = new Result<>();
        result.setMsg(msg);
        result.setCode(code);
        result.setSuccess(true);
        result.setData(data);
        return result;
    }
    public static <T> Result<T> error(String msg){
        return error(msg,500);
    }
    public static <T> Result<T> error(String msg,int code){
        Result<T> result = new Result<>();
        result.setMsg(msg);
        result.setCode(code);
        result.setSuccess(false);
        return result;
    }
}
