package per.cby.system.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import per.cby.system.constant.DataAuthType;

/**
 * 数据权限注解
 * 
 * @author chenboyang
 *
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataAuth {

    /**
     * 数据权限验证
     * 
     * @return 验证内容
     */
    DataAuthType[] value() default { DataAuthType.ORG };

}
