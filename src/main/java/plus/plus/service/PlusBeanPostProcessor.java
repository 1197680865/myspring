package plus.plus.service;

import plus.plus.spring.BeanPostProcessor;
import plus.plus.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * description
 * bean 后置处理器的实现
 * AOP的示例：见 postProcessAfterInitialization的Proxy
 * 注意：代理后，需要返回代理对象，而不能返回原对象。
 * 在createBean时，需要保存代理对象
 * @author ZhangChen
 * @date Created in 2022/11/2 13:22
 */
@Component
public class PlusBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        if (beanName.equals("userService2")){
            System.out.println("执行userService2的postProcessBeforeInitialization");
        }
        return bean;

    }
    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {

        if (beanName.equals("payService")){
            // JDK动态代理 创建代理对象
            // 定义InvocationHandler，拦截方法，实现切面
            Object proxyInstance = Proxy.newProxyInstance(PlusBeanPostProcessor.class.getClassLoader(),
                    bean.getClass().getInterfaces(), new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            // 执行切面逻辑，这里仅简单println
                            System.out.println("执行自定义切面逻辑");
                            // 执行原对象的方法
                            return method.invoke(bean,args);
                        }
                    });
            // 返回代理对象
            return proxyInstance;
        }

        return bean;
    }
}
