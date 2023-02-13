package xyz.acproject.router.controller;

import xyz.acproject.lang.enums.HttpCodeEnum;
import xyz.acproject.lang.page.PageBean;
import xyz.acproject.lang.response.Response;

/**
 * @author Jane
 * @ClassName Contoller
 * @Description TODO
 * @date 2022/4/15 11:49
 * @Copyright:2022
 */
public abstract class Controller {

    protected static void data(String key, Object value) {
        Response.data(key, value);
    }

    protected Response success() {
        return new Response().success();
    }

    protected <T> Response success(T t) {
        return new Response().success().data(t);
    }

    protected Response custom(HttpCodeEnum httpCodeEnum) {
        return new Response ().custom(httpCodeEnum);
    }

    protected Response page(PageBean pageBean) {
        return new Response().page(pageBean.getPage(), pageBean.getList());
    }
}
