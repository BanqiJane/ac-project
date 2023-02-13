package xyz.acproject.lang.page;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author Jane
 * @ClassName Page
 * @Description TODO
 * @date 2021/2/3 11:29
 * @Copyright:2021
 */
public class Page implements Serializable {
    private static final long serialVersionUID = 350285627846964440L;
    private int pageNow=1;
    private int pageSize=5;
    private int totalCount;
    private int totalPageCount;

    public Page() {
        super();
        // TODO 自动生成的构造函数存根
    }


    public Page(int pageNow, int totalCount) {
        super();
        this.pageNow = pageNow;
        this.totalCount = totalCount;
    }


    public Page(int pageNow, int pageSize, int totalCount, int totalPageCount) {
        this.pageNow = pageNow;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPageCount = totalPageCount;
    }

    public int getPageNow() {
        if(this.pageNow<=0){
            this.pageNow=1;
        }
        return pageNow;
    }
    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    @JsonIgnore
    @JSONField(serialize = false)
    public long getOffset() {
        return (getPageNow()-1)*getPageSize();
    }
    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    public int getTotalPageCount() {
        return (int)Math.ceil((double)getTotalCount()/(double)getPageSize());
        // return (getTotalCount()%getPageSize()):(getTotalCount()/getPageSize()):(getTotalCount()/getPageSize()+1);
    }
    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public int getListFromIndex() {
        if (getPageNow() <= getTotalPageCount()) {
            return (this.pageNow - 1) * this.pageSize;
        } else {
            return this.totalCount;
        }
    }
    @JsonIgnore
    @JSONField(serialize = false)
    public int getListToIndex() {
        if (getPageNow() <= getTotalPageCount()&&getPageSize()*getPageNow()<=getTotalCount()) {
            return getListFromIndex() + this.pageSize;
        } else {
            return this.totalCount;
        }
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public int getListToIndexByRedis() {
        if (getPageNow() <= getTotalPageCount()&&getPageSize()*getPageNow()<=getTotalCount()) {
            return getListFromIndex() + this.pageSize-1;
        } else {
            return this.totalCount-1;
        }
    }

    @JsonIgnore
    @JSONField(serialize = false)
    public int getThreadsSize(){
        return this.getTotalCount()/4;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNow=" + pageNow +
                ", pageSize=" + pageSize +
                ", totalCount=" + totalCount +
                ", totalPageCount=" + totalPageCount +
                '}';
    }
}
