package cn.shadowkylin.ham.model;

/**
 * @创建人 li cong
 * @创建时间 4/7/2023
 * @描述 分页类
 */
public class Pagination<T> {
    private int total;
    private int page;
    private int size;
    private T data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
