package plus.plus.service;

import plus.plus.spring.PlusApplicationContext;

/**
 * description
 *
 * @author ZhangChen
 * @date Created in 2022/10/31 21:19
 */
public class Test {
    public static void main(String[] args) {
        PlusApplicationContext plusApplicationContext = new PlusApplicationContext(AppConfig.class);
        UserService userService = (UserService) plusApplicationContext.getBean("userService");
        userService.printSmsService();

        PayService payService = (PayService) plusApplicationContext.getBean("payService");
        payService.test();
    }
}
