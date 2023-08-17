package plus.plus.spring;

/**
 * description
 * Bean的定义，用于描述Bean
 * @author ZhangChen
 * @date Created in 2022/11/1 23:04
 */
public class BeanDefinition {

    private String beanName;

    /**
     * 类对象
     */
    private Class clazz;

    /**
     * Bean 作用域
     */
    private ScopeType scope;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public ScopeType getScope() {
        return scope;
    }

    public void setScope(ScopeType scope) {
        this.scope = scope;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
