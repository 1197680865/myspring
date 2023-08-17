package plus.plus.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description
 * Bean自动注入  注解
 * @author ZhangChen
 * @date Created in 2022/10/31 21:20
 */
@Retention(RetentionPolicy.RUNTIME)
// FIELD  应用在属性上
@Target(ElementType.FIELD)
public @interface Autowired {
}
