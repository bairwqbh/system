package per.cby.system.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import per.cby.system.service.AuthenService;
import per.cby.system.service.impl.SimpleAuthenService;

/**
 * 系统管理服务配置
 * 
 * @author chenboyang
 *
 */
@Configuration("__SYSTEM_SERVICE_CONFIG__")
public class SystemServiceConfig {

    @Bean
    @ConditionalOnMissingBean
    public AuthenService authenService() {
        return new SimpleAuthenService();
    }

}
