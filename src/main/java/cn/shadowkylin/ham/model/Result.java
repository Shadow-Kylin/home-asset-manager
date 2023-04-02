package cn.shadowkylin.ham.model;

/**
 * @创建人 li cong
 * @创建时间 2023/3/26
 * @描述
 */
public class Result<T> {
    private Integer code;
    private Boolean success;
    private String msg;
    private T data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public <T> void setData(T data) {
    }
}
