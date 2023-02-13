package xyz.acproject.utils.html;

import xyz.acproject.utils.filter.HTMLFilter;

/**
 * @author Admin
 * @ClassName HtmlUtils
 * @Description TODO
 * @date 2022/12/9 11:36
 * @Copyright:2022
 */
public class HtmlUtils {
    public static String clean(String content) {
        return new HTMLFilter().filter(content);
    }

}
