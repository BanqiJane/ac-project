package xyz.acproject.lang.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jane
 * @ClassName PageBetween
 * @Description TODO
 * @date 2021/2/25 13:27
 * @Copyright:2021
 */
public class PageBetween extends Page implements Serializable {

    private static final long serialVersionUID = -2000893955155290290L;

    private int begin = 1;
    private int end = super.getTotalPageCount();
    private List<Integer> pageList = new ArrayList<Integer>();

    public PageBetween() {
    }

    public PageBetween(int pageNow, int totalCount) {
        super(pageNow, totalCount);
    }

    public PageBetween(int pageNow, int pageSize, int totalCount, int totalPageCount, int begin, int end, List<Integer> pageList) {
        super(pageNow, pageSize, totalCount, totalPageCount);
        this.begin = begin;
        this.end = end;
        this.pageList = pageList;
    }

    public int getBegin() {
        if (getPageNow() < getTotalPageCount() - 2) {
            this.end = getPageNow() + 2;
            if (getPageNow() > 3) {
                this.begin = getPageNow() - 2;
            } else {
                this.begin = 1;
                this.end = 5;
            }
        } else {
            if (getPageNow() > 3) {
                this.begin = getPageNow() - 2;

                if (getPageNow() == getTotalPageCount() - 1) {
                    this.begin = getPageNow() - 3;
                }
                if (getPageNow() == getTotalPageCount()) {
                    this.begin = getPageNow() - 4;
                }
            }
            this.end = getTotalPageCount();
        }
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        end = super.getTotalPageCount();
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List<Integer> getPageList() {
        for (int i = getBegin(); i <= getEnd(); i++) {
            pageList.add(i);
        }
        return pageList;
    }

    public void setPageList(List<Integer> pageList) {
        this.pageList = pageList;
    }

    @Override
    public String toString() {
        return "PageBetween{" +
                "begin=" + begin +
                ", end=" + end +
                ", pageList=" + pageList +
                '}';
    }
}
