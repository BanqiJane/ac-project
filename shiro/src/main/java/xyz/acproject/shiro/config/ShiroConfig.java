package xyz.acproject.shiro.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.DelegatingFilterProxy;
import xyz.acproject.shiro.filter.JwtFilter;
import xyz.acproject.shiro.realm.ExampleRealm;
import xyz.acproject.utils.SpringUtils;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Admin
 * @ClassName ShiroConfig
 * @Description TODO
 * @date 2022/12/21 8:48
 * @Copyright:2022
 */
@Configuration
public class ShiroConfig {



    /**
     *
     * @Description 代理
     * @author JANE
     * @date 2022/12/21 10:32
     * @params
     * @return
     *
     * @Copyright
     */
    @Bean("delegatingFilterProxy")
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetBeanName("shiroFilter");
        proxy.setTargetFilterLifecycle(true);
        registration.setFilter(proxy);
        registration.setAsyncSupported(true);
        registration.setDispatcherTypes(DispatcherType.ASYNC, DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR);
        return registration;
    }

    @Bean
    public ExampleRealm exampleRealm() {
        ExampleRealm exampleRealm = new ExampleRealm();
        return exampleRealm;
    }


    /**
     * 禁用session, 不保存用户登录状态。保证每次请求都重新认证
     */
    @Bean
    public SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }


    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> realms = new ArrayList<>();
        realms.add(exampleRealm());
        securityManager.setRealms(realms);
        //关闭shiro自带session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(this.sessionStorageEvaluator());
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvokingFactoryBean.setArguments(this.securityManager());
        return methodInvokingFactoryBean;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
        return lifecycleBeanPostProcessor;
    }

    /**
     * jwt生命周期
     *
     * @return
     */
    @Bean
    @DependsOn(value = "lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 开启shiro注解
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(this.securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(this.securityManager());
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwt", new JwtFilter());
        factoryBean.setFilters(filterMap);
        //拦截操作
        Map<String, String> chainMap = new LinkedHashMap<>();
        //忽略拦截swagger
        chainMap.put("/", "anon");
        chainMap.put("/swagger-ui/**", "anon");
        chainMap.put("/swagger-resources/**", "anon");
        chainMap.put("/webjars/**", "anon");
        chainMap.put("/**/api-docs", "anon");
        chainMap.put("/swagger-ui.html/**", "anon");
        chainMap.put("/error", "anon");
        chainMap.put("/csrf", "anon");

        //druid页面不进行拦截
        chainMap.put("/druid/**", "anon");
        chainMap.put("/favicon.ico", "anon");

        List<String> excludePathList= getExcludePath();
        if (!CollectionUtils.isEmpty(excludePathList)) {
            for (String path : excludePathList) {
                chainMap.put(path, "anon");
            }
        }

        chainMap.put("/logout", "logout");//退出登陆
        chainMap.put("/**", "jwt");//使用jwt进行拦截
        factoryBean.setFilterChainDefinitionMap(chainMap);
        return factoryBean;
    }

    private List<String> getExcludePath() {
        String key = SpringUtils.getConfigByKey("acproject.shiro.exclude");
        if (StringUtils.isBlank(key)) {
            return new ArrayList<>();
        }
        List<String> excludePathList = Arrays.stream(key.split(","))
                .filter(s -> StringUtils.isNotBlank(s))
                .collect(Collectors.toList());
        return excludePathList;
    }

}
