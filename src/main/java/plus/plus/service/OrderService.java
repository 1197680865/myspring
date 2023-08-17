package plus.plus.service;

import plus.plus.spring.Component;
import plus.plus.spring.Scope;
import plus.plus.spring.ScopeType;

/**
 * description
 * Component 指定bean的名称
 * @author ZhangChen
 * @date Created in 2022/10/31 21:19
 */
@Component("orderService")
@Scope(ScopeType.PROTOTYPE)
public class OrderService {
}
