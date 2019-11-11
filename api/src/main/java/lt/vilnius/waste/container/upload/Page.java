package lt.vilnius.waste.container.upload;

import java.util.List;

public class Page<T> {
    private List<T> data;
    private int to;
    private int total;
    private PageRequest request;

    public Page() {

    }

    public Page(List<T> data) {
        this.data = data;
    }

    public static <T> Page<T> empty() {
        return new Page<>(List.of());
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public PageRequest getRequest() {
        return request;
    }

    public void setRequest(PageRequest request) {
        this.request = request;
    }

    public PageRequest next() {
        if (request != null && to < total) {
            return request.next();
        }
        return null;
    }

    public Page<T> withRequest(PageRequest request) {
        this.request = request;
        return this;
    }
}
