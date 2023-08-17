package plus.plus.service;

import plus.plus.spring.Autowired;
import plus.plus.spring.Component;
import plus.plus.spring.Scope;

/**
 * description
 * Component 指定bean的名称
 * @author ZhangChen
 * @date Created in 2022/10/31 21:19
 */
@Component("userService")
@Scope
public class UserService {

    @Autowired
    private SmsService smsService;

    public void printSmsService(){
        System.out.println(smsService);
    }
}
