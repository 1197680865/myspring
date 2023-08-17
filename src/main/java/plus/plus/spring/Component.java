package plus.plus.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description
 * Spring Bean注解
 * @author ZhangChen
 * @date Created in 2022/10/31 21:20
 */
@Retention(RetentionPolicy.RUNTIME)
// TYPE  只能应用在类上
@Target(ElementType.TYPE)
public @interface Component {

    /**
     * @return 指定bean的名称
     */
    String value() default "";
}
