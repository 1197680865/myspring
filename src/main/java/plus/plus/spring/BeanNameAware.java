package plus.plus.spring;

/**
 * description
 * BeanName回调接口，告诉Bean 自己在Spring容器叫啥名称
 * 实际使用很少
 * @author ZhangChen
 * @date Created in 2022/11/2 0:39
 */
public interface BeanNameAware {

    public void setBeanName(String beanName);
}
