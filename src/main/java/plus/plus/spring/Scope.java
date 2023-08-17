package plus.plus.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description
 * Spring Bean的作用域：如 单例 多例
 * @author ZhangChen
 * @date Created in 2022/10/31 21:20
 */
@Retention(RetentionPolicy.RUNTIME)
// TYPE  只能应用在类上
@Target(ElementType.TYPE)
public @interface Scope {

    /**
     * @return 指定bean的创建方式
     */
    ScopeType value() default ScopeType.SINGLETON;
}
