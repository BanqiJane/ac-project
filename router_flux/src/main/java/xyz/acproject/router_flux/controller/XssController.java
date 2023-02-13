package xyz.acproject.router_flux.controller;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import xyz.acproject.router_flux.web.XssEscapeEditor;

import javax.annotation.Resource;

/**
 * @author Admin
 * @ClassName XssController
 * @Description TODO
 * @date 2022/12/16 14:51
 * @Copyright:2022
 */
public abstract class XssController extends Controller{

    @Resource
    private XssEscapeEditor xssEscapeEditor;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, xssEscapeEditor);
    }
}
