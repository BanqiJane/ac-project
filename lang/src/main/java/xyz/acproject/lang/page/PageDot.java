package xyz.acproject.lang.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jane
 * @ClassName PagePos
 * @Description TODO
 * @date 2021/2/25 13:05
 * @Copyright:2021
 */
public class PageDot extends Page implements Serializable {

    private static final long serialVersionUID = 2311724855958450621L;
    private int begin=1;
    private int end = super.getTotalPageCount();
    private List<Integer> betweenPageList = new ArrayList<Integer>();
    private boolean beginDot;
    private boolean endDot;

    public PageDot() {
    }

    public PageDot(int pageNow, int totalCount) {
        super(pageNow, totalCount);
    }

    public PageDot(int pageNow, int pageSize, int totalCount, int totalPageCount, int begin, int end, List<Integer> betweenPageList, boolean beginDot, boolean endDot) {
        super(pageNow, pageSize, totalCount, totalPageCount);
        this.begin = begin;
        this.end = end;
        this.betweenPageList = betweenPageList;
        this.beginDot = beginDot;
        this.endDot = endDot;
    }

    public int getBegin() {
        if(getTotalPageCount()<=5) {
            this.begin=1;
            this.end=getTotalPageCount();
        }else {
            if(getPageNow()<=3) {
                this.begin=1;
                this.end=5;
            }else {
                this.begin=getPageNow()-2;
                this.end=getPageNow()+2;
                if(getEnd()>getTotalPageCount()) {
                    this.end=getTotalPageCount();
                    this.begin=getEnd()-3;
                }
            }
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
    public List<Integer> getBetweenPageList() {
        for(int i=getBegin();i<=getEnd();i++) {
            betweenPageList.add(i);
        }
        return betweenPageList;
    }
    public void setBetweenPageList(List<Integer> betweenPageList) {
        this.betweenPageList = betweenPageList;
    }
    public boolean isBeginDot() {
        return (getBegin()>2)?true:false;
    }
    public void setBeginDot(boolean beginDot) {
        this.beginDot = beginDot;
    }
    public boolean isEndDot() {
        return (getEnd()<getTotalPageCount()-1)?true:false;
    }
    public void setEndDot(boolean endDot) {
        this.endDot = endDot;
    }

    @Override
    public String toString() {
        return "PageDot{" +
                "begin=" + begin +
                ", end=" + end +
                ", betweenPageList=" + betweenPageList +
                ", beginDot=" + beginDot +
                ", endDot=" + endDot +
                '}';
    }
}
