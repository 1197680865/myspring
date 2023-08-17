package plus.plus.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description
 * Spring Bean 扫描注解（配置扫描路径）
 * @author ZhangChen
 * @date Created in 2022/10/31 21:20
 */

// 生效时间 RUNTIME
@Retention(RetentionPolicy.RUNTIME)
// TYPE  只能应用在类上
@Target(ElementType.TYPE)
public @interface ComponentScan {
    /**
     * @return  指定扫描路径
     */
    String value() default "";
}
