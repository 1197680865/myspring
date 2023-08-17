package plus.plus.spring;


import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlusApplicationContext {

    private Class configClass;

    /**
     * key: bean的名称
     * value：beanDefinition
     */
    private ConcurrentHashMap<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();


    /**
     * 单例Bean池
     * key：bean名称
     * value：bean对象
     */
    private ConcurrentHashMap<String,Object> singleBeanMap = new ConcurrentHashMap<>();

    /**
     * BeanPostProcessor的实例列表
     */
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();


    public PlusApplicationContext(Class configClass) {

        this.configClass = configClass;

        // ------ 演示配置扫描开始 ------
        /**
         * 思路
         * 1.判断class上是否有@ComponentScan
         * 2.获取@ComponentScan的属性（如扫描路径），并格式化为可用的路径
         * 3.使用类加载器，获取扫描路径下的资源，将资源转换为File
         * 4.如果File为Directory，遍历目录下的文件，判断是否为class对象（使用类加载器加载），判断是否存在@Componet，如果存在，则为bean
         * ---------------------------
         * 注1：扫描的是编译后的class文件，而非IDE中编辑的java文件。（解释为什么如此定义扫描路径和扫描文件）
         * \xxx\SpringTheory\target\classes\plus\plus
         */

        if(configClass.isAnnotationPresent(ComponentScan.class)){
            ComponentScan componentScanAnnotation  = (ComponentScan) configClass.getAnnotation(ComponentScan.class);

            // path <-> @ComponentScan("plus.plus.service")
            String path = componentScanAnnotation.value();

            // 期望的路径 xxx/plus/plus/service
            path = path.replace(".", "/");

            ClassLoader classLoader = PlusApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File( resource.getFile());
            System.out.println("扫描路径：" + file);



            if (file.isDirectory()){
                // 扫描此目录下的所有Class ->bean
                File[] files = file.listFiles();

                // 遍历目录下的所有class，需要递归，这里只是演示，所有不加递归
                for(File f :files){
                    // 判断是否后缀为class
                    String absolutePath = f.getAbsolutePath();
                    if (absolutePath.endsWith(".class")){

                        // 判断类上是否有@Component 注解，使用classLoader加载类对象
                        // 参数的格式为 plus.plus.service.classname
                        String className = absolutePath.substring(absolutePath.indexOf("plus"), absolutePath.indexOf(".class"));
                        className = className.replace("\\",".");

                        System.out.println("已扫描Class文件:"+className);

                        Class<?> aClass = null;
                        try {
                            aClass = classLoader.loadClass(className);
                            // 判断类对象上是否有Component注解
                            if (aClass.isAnnotationPresent(Component.class)){
                                Component componentAnnotation = aClass.getAnnotation(Component.class);
                                String beanName = componentAnnotation.value();
                                // bean 已被扫描到
                                System.out.println("已扫描Bean:"+aClass);

                                // 检查class 是否派生自beanPostProcessor
                                if (BeanPostProcessor.class.isAssignableFrom(aClass)){
                                    // 创建BeanPostProcessor的实例，并添加至 BeanPostProcessorList
                                    BeanPostProcessor beanPostProcessor = (BeanPostProcessor) aClass.newInstance();
                                    beanPostProcessorList.add(beanPostProcessor);
                                }


                                // 创建 BeanDefinition
                                BeanDefinition beanDefinition = creatBeanDefinition(aClass);
                                // 保存 BeanDefinition 到 Map
                                beanDefinitionMap.put(beanDefinition.getBeanName(),beanDefinition);

                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }
        }
        // ------ 演示配置扫描结束 ------

        // ------ 演示单例池创建开始 ------
        /**
         * 创建单例池
         * 思路
         * 1.从beanDefinitionMap遍历，依次创建Bean
         * 2.执行创建Bean的方法，创建的bean放入单例池singleBeanMap
         */

        for (BeanDefinition beanDefinition : beanDefinitionMap.values()) {
            ScopeType scope = beanDefinition.getScope();
            if (scope == ScopeType.SINGLETON){
                // 开始创建单例bean
                Object bean = createBean(beanDefinition);
                singleBeanMap.put(beanDefinition.getBeanName(),bean);
            }
        }
        // ------ 演示单例池创建结束 ------

    }

    /**
     * 创建BeanDefinition对象
     * @param clazz
     * @return
     */
    private BeanDefinition creatBeanDefinition(Class clazz){
        BeanDefinition beanDefinition =new BeanDefinition();
        beanDefinition.setClazz(clazz);

        // bean 的作用域
        boolean scopeAnnotationPresent = clazz.isAnnotationPresent(Scope.class);
        ScopeType scope = scopeAnnotationPresent ?
                ((Scope)clazz.getAnnotation(Scope.class)).value() :
                ScopeType.SINGLETON;
        beanDefinition.setScope(scope);
        Component componentAnnotation = (Component) clazz.getAnnotation(Component.class);

        // bean的名称
        String beanName = componentAnnotation.value();
        if ("".equals(beanName)){
            // bean未指定名称时，使用Class首字母小写的名称代替
            beanName = Introspector.decapitalize(clazz.getSimpleName());
        }

        beanDefinition.setBeanName(componentAnnotation.value());
        return beanDefinition;
    }

    /**
     * 简单版方式创建Bean对象（线程不安全）
     * 思路：clazz.getConstructor().newInstance()
     * @param beanDefinition
     * @return
     */
    private Object basicCreateBean( BeanDefinition beanDefinition){

        Class clazz = beanDefinition.getClazz();
        Object bean;
        try {
            // 获取class的无参构造方法
            bean = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bean;
    }

    /**
     * 创建Bean对象（线程安全）
     * 思路：clazz.getConstructor().newInstance()
     * @param beanDefinition
     * @return
     */
    private Object createBean( BeanDefinition beanDefinition){

        Class clazz = beanDefinition.getClazz();
        Object bean;
        try {
            // 1.获取class的无参构造方法，并创建bean
            bean = clazz.getConstructor().newInstance();

            // 2.依赖注入
            autowired(clazz,bean);

            // 3.BeanNameAware (如果实现了BeanNameAware接口，则去执行setName)
            if (bean instanceof BeanNameAware){
                ((BeanNameAware)bean).setBeanName(beanDefinition.getBeanName());
            }

            // 5.1bean初始化前执行 postProcessBeforeInitialization
            for (BeanPostProcessor postProcessor : beanPostProcessorList){
                // 返回代理对象
                bean = postProcessor.postProcessBeforeInitialization(beanDefinition.getBeanName(), bean);
            }

            // 4.bean初始化
            if (bean instanceof InitializingBean){
                ((InitializingBean)bean).afterPropertiesSet();
            }

            // 5.2bean初始化后执行 postProcessAfterInitialization
            for (BeanPostProcessor postProcessor : beanPostProcessorList){
                // 返回代理对象
                bean = postProcessor.postProcessAfterInitialization(beanDefinition.getBeanName(),bean);
            }

            return bean;

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 依赖注入 autowired实现
     * 注入的时机：在bean的实例被创建后
     * 思路：
     * 1 通过反射获取bean中声明的属性
     * 2 判断属性上是否有@Autowired注解
     * 3 如果有@Autowired，通过bean名从getBean方法中获取bean
     * 4 通过反射为bean注入属性
     * @param clazz
     * @param bean
     */
    private void autowired(Class clazz,Object bean){
        // 1 通过反射获取bean中声明的属性
        Field[] declaredFields = clazz.getDeclaredFields();
        // 2 判断属性上是否有@Autowired注解
        for (Field f: declaredFields) {
            // 3 如果有@Autowired，通过bean名从getBean方法中获取bean
            boolean annotationPresent = f.isAnnotationPresent(Autowired.class);
            if (!annotationPresent){
                break;
            }
            String fieldName = f.getName();
            // 设置属性可修改
            f.setAccessible(true);
            // getBean
            Object autowiredBean = getBean(fieldName);
            // 4 通过反射为bean注入属性
            try {
                f.set(bean,autowiredBean);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 获取Bean（不考虑懒加载）
     * 思路：
     * 1.从beanDefinitionMap中获取以beanName为key的 beanDefinition
     * 2.根据beanDefinition的scope 开始获取bean
     *  2.1 如果是单例，从单例池中获取（单例池在Bean被全部扫描后开始创建出来）.如果单例池获取为null，则继续执行创建bean的方法（要求此方法为线程安全的）
     *  2.2 如果是多例，直接创建Bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName){

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null){
            throw new NullPointerException("根据beanName 获取不到BeanDefinition");
        }

        ScopeType scope = beanDefinition.getScope();

        switch (scope){
            case PROTOTYPE:
                // 多例
                return createBean(beanDefinition);
            case SINGLETON:
                // 单例
                Object bean  = singleBeanMap.get(beanName);
                // 如果获取不到，则执行创建bean的方法（线程安全）
                if (bean == null){
                    bean = createBean( beanDefinition);
                    singleBeanMap.put(beanName,bean);
                }
                return bean;
        }
        return null;
    }

}
