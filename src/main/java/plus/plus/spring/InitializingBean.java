package plus.plus.spring;

/**
 * description
 * Bean初始化机制用到的接口
 * @author ZhangChen
 * @date Created in 2022/11/2 13:03
 */
public interface InitializingBean {

    void afterPropertiesSet();
}
