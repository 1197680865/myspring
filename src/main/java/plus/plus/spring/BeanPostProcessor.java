package plus.plus.spring;

/**
 * description
 * Bean后置处理器，作用是在Bean对象在实例化和依赖注入完毕后，在显示调用初始化方法的前后添加我们自己的逻辑
 * 思路：
 * 1. 定义接口BeanPostProcessor ，PlusBeanPostProcessor实现接口，PlusBeanPostProcessor也是Bean
 * 2. 在bean被扫描到后，判断如果BeanPostProcessor 类派生当前class ，则实例化bean为BeanPostProcessor
 * 3. 将实例化的BeanPostProcessor 存储在 BeanPostProcessorList中
 * 4. 在createBean时，在InitializingBean前执行BeanPostProcessor.postProcessBeforeInitialization，
 *      在InitializingBean后执行BeanPostProcessor.postProcessAfterInitialization
 * @author ZhangChen
 * @date Created in 2022/11/2 13:12
 */
public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(String beanName,Object bean);
    Object postProcessAfterInitialization(String beanName,Object bean);
}
