package com.pine.ant.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pine.ant.shiro.MyShiroFactoryBean;
import com.pine.ant.shiro.MyShiroRealm;

@Configuration
public class ShiroConfiguration {

    private Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

    @Bean
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em=new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return em;
    }

    @Bean(name = "myShiroRealm")
    public MyShiroRealm myShiroRealm(EhCacheManager cacheManager){
        MyShiroRealm realm = new MyShiroRealm();
        realm.setCacheManager(cacheManager);
        return realm;
    }
    
    @Bean
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }
    
    @Bean(name ="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(MyShiroRealm myShiroRealm){
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(myShiroRealm);
        dwsm.setCacheManager(getEhCacheManager());
        return dwsm;
    }
    
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor aasa=new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }
    
    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean){
        /////////////////////// 下面这些规则配置最好配置到配置文件中 ///////////////////////
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // authc：该过滤器下的页面必须验证后才能访问，它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
        filterChainDefinitionMap.put("/user", "authc");// 这里为了测试，只限制/user，实际开发中请修改为具体拦截的请求规则
        // anon：它对应的过滤器里面是空的,什么都没做
        logger.info("##################从数据库读取权限规则，加载到shiroFilter中##################");
        filterChainDefinitionMap.put("/user/edit/**", "authc,perms[user:edit]");// 这里为了测试，固定写死的值，也可以从数据库或其他配置中读取

        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/**", "anon");//anon 可以理解为不拦截
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }
    
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new MyShiroFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/user");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        loadShiroFilterChain(shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }
}
