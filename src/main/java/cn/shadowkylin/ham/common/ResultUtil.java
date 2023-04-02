package cn.shadowkylin.ham.common;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */
public class ResultUtil<T> {
    private boolean success; // 操作是否成功
    private int code; // 操作结果代码
    private String message; // 操作结果消息
    private T data; // 操作结果数据

    // 构造方法
    public ResultUtil() {
    }

    public ResultUtil(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // getter 和 setter 方法
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // 成功返回的静态方法
    public static <T> ResultUtil<T> success() {
        return new ResultUtil<>(true, 200, "操作成功", null);
    }

    public static <T> ResultUtil<T> success(String message) {
        return new ResultUtil<>(true, 200, message, null);
    }

    public static <T> ResultUtil<T> success(T data) {
        return new ResultUtil<>(true, 200, "操作成功", data);
    }

    public static <T> ResultUtil<T> success(String message, T data) {
        return new ResultUtil<>(true, 200, message, data);
    }

    public static <T> ResultUtil<T> success(String message, T data, int code) {
        return new ResultUtil<>(true, code, message, data);
    }

    // 失败返回的静态方法
    public static <T> ResultUtil<T> error() {
        return new ResultUtil<>(false,500, "操作失败", null);
    }

    public static <T> ResultUtil<T> error(String message) {
        return new ResultUtil<>(false,500, message, null);
    }

    public static <T> ResultUtil<T> error(T data) {
        return new ResultUtil<>(false,500, "操作失败", data);
    }

    public static <T> ResultUtil<T> error(String message, T data) {
        return new ResultUtil<>(false,500, message, data);
    }
    
    public static <T> ResultUtil<T> error(String message, T data, int code) {
        return new ResultUtil<>(false, code, message, data);
    }
}
