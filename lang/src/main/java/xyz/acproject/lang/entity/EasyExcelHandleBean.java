package xyz.acproject.lang.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jane
 * @ClassName EasyExcelHandleBean
 * @Description TODO
 * @date 2022/4/13 15:31
 * @Copyright:2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EasyExcelHandleBean<T> {
    private int success;
    private int fail;
    private int total;
    private List<Detail<T>> success_list;
    private List<Detail<T>> fail_list;

    public EasyExcelHandleBean init() {
        success = 0;
        fail = 0;
        total = 0;
        success_list = new ArrayList<>();
        fail_list = new ArrayList<>();
        return this;
    }

    public EasyExcelHandleBean addSuccess(){
        this.success++;
        this.total++;
        return this;
    }
    public EasyExcelHandleBean addFail() {
        this.fail++;
        this.total++;
        return this;
    }
    public EasyExcelHandleBean addTotal() {
        this.total++;
        return this;
    }


    public EasyExcelHandleBean addSuccessList(T t) {
        if(this.success_list == null)this.success_list = new ArrayList<>();
        this.success_list.add(new Detail<T>("",t));
        this.addSuccess();
        return this;
    }

    public EasyExcelHandleBean addSuccessList(Detail<T> d) {
        if(this.success_list == null)this.success_list = new ArrayList<>();
        this.success_list.add(d);
        this.addSuccess();
        return this;
    }


    public EasyExcelHandleBean addFailList(String error,T t) {
        if(this.fail_list == null)this.fail_list = new ArrayList<>();
        this.fail_list.add(new Detail<T>(error,t));
        this.addFail();
        return this;
    }


    public EasyExcelHandleBean addFailList(Detail<T> d) {
        if(this.fail_list == null)this.fail_list = new ArrayList<>();
        this.fail_list.add(d);
        this.addFail();
        return this;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail<T>{
        private String error;
        private T t;
    }
}
