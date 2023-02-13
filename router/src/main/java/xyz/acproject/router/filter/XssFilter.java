package xyz.acproject.router.filter;


import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import xyz.acproject.utils.SpringUtils;
import xyz.acproject.utils.collections.ArrayUtils;
import xyz.acproject.utils.io.UrlUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XssFilter implements Filter {
    /**
     * 排除链接
     */
    public List<String> excludes = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String tempExcludes = filterConfig.getInitParameter("excludes");
        if (StringUtils.isNotEmpty(tempExcludes)) {
            String[] url = tempExcludes.split(",");
            for (int i = 0; url != null && i < url.length; i++) {
                excludes.add(url[i]);
            }
        }
        if(!ArrayUtils.isNotEmpty(getExcludePaths())){
            excludes.addAll((Collection<? extends String>) ArrayUtils.toList(getExcludePaths()));
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if(!getEnable()){
            chain.doFilter(request, response);
            return;
        }
        if (handleExcludeURL(req, resp)) {
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getServletPath();
        String method = request.getMethod();
        // GET DELETE 不过滤
        if (method == null || method.matches("GET") || method.matches("DELETE")) {
            return true;
        }
        return UrlUtils.matches(url, excludes);
    }

    @Override
    public void destroy() {

    }

    private static boolean getEnable() {
        String key = SpringUtils.getConfigByKey("acproject.webmvc.filter.xss.enable");
        if (StringUtils.isBlank(key)) {
            return false;
        }
        return Boolean.parseBoolean(key);
    }


    private static String[] getExcludePaths(){
        String key = SpringUtils.getConfigByKey("acproject.webmvc.filter.xss.excludePaths");
        if (StringUtils.isBlank(key)) {
            return new String[]{};
        }
        return key.split(",");
    }
}