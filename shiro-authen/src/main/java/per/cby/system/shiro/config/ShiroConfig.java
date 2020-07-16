package per.cby.system.shiro.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import per.cby.frame.common.config.properties.SysProperties;
import per.cby.system.service.AuthenService;
import per.cby.system.shiro.cache.RedisCacheManager;
import per.cby.system.shiro.filter.ShiroTokenFilter;
import per.cby.system.shiro.realm.AuthRealm;
import per.cby.system.shiro.service.ShiroAuthenService;
import per.cby.system.shiro.session.CookieSessionIdGenerator;
import per.cby.system.shiro.session.RedisSessionDAO;
import per.cby.system.shiro.session.RestSessionManager;

import lombok.Getter;
import lombok.Setter;

/**
 * Shiro配置类
 * 
 * @author chenboyang
 *
 */
@Configuration("__SHIRO_CONFIG__")
public class ShiroConfig {

    /** 系统缓存 */
    private static final String SYSTEM_CACHE = "system";

    /** Redis缓存 */
    private static final String REDIS_CACHE = "redis";

    /** 缓存类型 */
    @Getter
    @Setter
    private String cacheType = SYSTEM_CACHE;

    @Autowired(required = false)
    private SysProperties sysProperties;

    @Bean
    @ConditionalOnMissingBean
    public ShiroFilterFactoryBean shiroFilter(
            @Autowired(required = false) @Qualifier("shiroFilters") Map<String, Filter> shiroFilters,
            @Autowired(required = false) @Qualifier("shiroFilterChainDefinitionMap") Map<String, String> shiroFilterChainDefinitionMap) {
        if (shiroFilters == null) {
            shiroFilters = new LinkedHashMap<String, Filter>();
            shiroFilters.put("token", new ShiroTokenFilter());
        }
        if (shiroFilterChainDefinitionMap == null) {
            shiroFilterChainDefinitionMap = new LinkedHashMap<String, String>();
            shiroFilterChainDefinitionMap.put("/isAuthen", "anon");
            shiroFilterChainDefinitionMap.put("/login", "anon");
            shiroFilterChainDefinitionMap.put("/logout", "anon");
            shiroFilterChainDefinitionMap.put("/**", "token");
        }
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager());
        shiroFilter.setFilters(shiroFilters);
        shiroFilter.setFilterChainDefinitionMap(shiroFilterChainDefinitionMap);
        return shiroFilter;
    }

    @Bean
    @ConditionalOnMissingBean
    public org.apache.shiro.mgt.SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(sessionManager());
        securityManager.setRealm(realm());
        return securityManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        switch (cacheType) {
            case REDIS_CACHE:
                return new RedisCacheManager();
            default:
                return new MemoryConstrainedCacheManager();
        }
    }

    @Bean
    @ConditionalOnMissingBean(RestSessionManager.class)
    public SessionManager sessionManager() {
        RestSessionManager sessionManager = new RestSessionManager();
        sessionManager.setGlobalSessionTimeout(sysProperties.getTimeout() * 60 * 1000);
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(cacheManager());
        return sessionManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionDAO sessionDAO() {
        AbstractSessionDAO sessionDAO = null;
        switch (cacheType) {
            case REDIS_CACHE:
                RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
                redisSessionDAO.setCacheManager(cacheManager());
                sessionDAO = redisSessionDAO;
                break;
            default:
                sessionDAO = new MemorySessionDAO();
                break;
        }
        sessionDAO.setSessionIdGenerator(new CookieSessionIdGenerator());
        return sessionDAO;
    }

    @Bean
    @ConditionalOnMissingBean
    @DependsOn("lifecycleBeanPostProcessor")
    public Realm realm() {
        AuthRealm authRealm = new AuthRealm();
        authRealm.setCredentialsMatcher(new HashedCredentialsMatcher(Md5Hash.ALGORITHM_NAME));
        authRealm.setAuthorizationCachingEnabled(false);
        return authRealm;
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvokingFactoryBean.setArguments(securityManager());
        return methodInvokingFactoryBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthenService authenService() {
        return new ShiroAuthenService();
    }

}
