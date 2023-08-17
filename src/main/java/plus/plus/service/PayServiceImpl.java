package plus.plus.service;

import plus.plus.spring.Component;

/**
 * description
 *
 * @author ZhangChen
 * @date Created in 2022/11/2 15:51
 */
@Component("payService")
public class PayServiceImpl implements PayService{
    @Override
    public void test() {
        System.out.println("支付完成");
    }
}
