package plus.plus.service;

import plus.plus.spring.*;

/**
 * description
 * Component 指定bean的名称
 * @author ZhangChen
 * @date Created in 2022/10/31 21:19
 */
@Component("userService2")
public class UserService2 implements BeanNameAware, InitializingBean {

    private String beanName;

    @Autowired
    private SmsService smsService;

    public void printSmsService(){
        System.out.println(smsService);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() {
        // 可以在Bean创建时执行一些操作，比如缓存预热等等
    }
}
