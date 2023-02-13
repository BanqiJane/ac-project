package xyz.acproject.router_flux.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.beans.PropertyEditorSupport;

/**
 * @author Admin
 * @ClassName XssEscapeEditor
 * @Description TODO
 * @date 2022/12/16 14:56
 * @Copyright:2022
 */
@Component
public class XssEscapeEditor extends PropertyEditorSupport {

    public XssEscapeEditor() {
        super();
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        return value != null ? value.toString() : "";
    }

    @Override
    public void setAsText(String text) {
        String escapedText = null;
        if(!StringUtils.isEmpty(text)) {
            escapedText = HtmlUtils.htmlEscape(text,"UTF-8");
        }
        setValue(escapedText);
    }
}