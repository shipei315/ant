package com.pine.ant.shiro;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.util.Assert;

/**
 * 主要为了处理静态资源的访问控制问题
 * @author shipe
 *
 */
public class MyShiroFactoryBean extends ShiroFilterFactoryBean {

    // 对于ShiroFilter来说需要直接忽略的请求
    private Set<String> ignoreExt = new HashSet<String>();

    public Set<String> getIgnoreExt() {
        return ignoreExt;
    }

    public void setIgnoreExt(Set<String> ignoreExt) {
        this.ignoreExt = ignoreExt;
    }

    public MyShiroFactoryBean() {
        super();
        ignoreExt.add(".jpg");
        ignoreExt.add(".png");
        ignoreExt.add(".gif");
        ignoreExt.add(".bmp");
        ignoreExt.add(".js");
        ignoreExt.add(".css");
        ignoreExt.add(".htm");
    }

    protected AbstractShiroFilter createInstance() throws Exception {
        SecurityManager securityManager = getSecurityManager();
        Assert.isTrue(Objects.nonNull(securityManager), "SecurityManager property must not be null!");
    
        if(!(securityManager instanceof WebSecurityManager)){
            throw new BeanInitializationException("The SecurityManager does not implements the WebSecurityManager interface");
        }
        
        FilterChainManager manager =createFilterChainManager();
                
        PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
        chainResolver.setFilterChainManager(manager);
        
        return new MySpringFilter((WebSecurityManager) securityManager,chainResolver);
    }

    private final class MySpringFilter extends AbstractShiroFilter {

        protected MySpringFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
            super();
            if (webSecurityManager == null) {
                throw new IllegalArgumentException("webSecurityManager cannot be null");
            }
            setSecurityManager(webSecurityManager);
            if (resolver != null) {
                setFilterChainResolver(resolver);
            }
        }

        protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse,
                FilterChain filterChain) throws ServletException, IOException {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String str = request.getRequestURI().toLowerCase();

            boolean flag = true;
            int idx = 0;
            if((idx=str.indexOf("."))>0){
               str=str.substring(idx);
               if(ignoreExt.contains(str.toLowerCase())){
                   flag=false;
               }
            }
            if(flag){
                super.doFilterInternal(servletRequest, servletResponse, filterChain);
            }else{
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }

}
