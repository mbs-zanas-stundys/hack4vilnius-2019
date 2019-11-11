package lt.vilnius.waste.container.upload;

public class PageRequest {
    private int pageNo;
    private int size;

    public PageRequest() {

    }

    public PageRequest(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.size = pageSize;
    }

    public static PageRequest of(int size) {
        PageRequest request = new PageRequest();
        request.size = size;
        request.pageNo = 1;
        return request;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public PageRequest next() {
        return new PageRequest(pageNo + 1, size);
    }
}
