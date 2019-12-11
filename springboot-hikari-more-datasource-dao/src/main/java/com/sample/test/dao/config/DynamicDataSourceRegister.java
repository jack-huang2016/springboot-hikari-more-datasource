/**
 * FileName: DynamicDataSourceRegister
 * Author:   huang.yj
 * Date:     2019/12/10 21:35
 * Description: 注册动态数据源
 */
package com.sample.test.dao.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈注册动态数据源〉
 * 实现 ImportBeanDefinitionRegistrar 实现数据源注册
 * 实现 EnvironmentAware 用于读取application.yml配置
 *
 * @author huang.yj
 * @create 2019/12/10
 * @since 0.0.1
 */
@Slf4j
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    /**
     * 配置上下文（也可以理解为配置文件的获取工具）
     */
    private Environment evn;

    /**
     * 别名
     */
    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();

    /**
     * 由于部分数据源配置不同，所以在此处添加别名，避免切换数据源出现某些参数无法注入的情况
     */
    static {
        aliases.addAliases("url", new String[]{"jdbc-url"});
        aliases.addAliases("username", new String[]{"user"});
    }

    /**
     * 默认数据源
     */
    private DataSource defaultDataSource;

    /**
     * 存储我们注册的数据源
     */
    private Map<String, DataSource> slaveDataSources = new HashMap<>();

    /**
     * 参数绑定工具 springboot2.0新推出
     */
    private Binder binder;

    /**
     * EnvironmentAware接口的实现方法，通过aware的方式注入，此处是environment对象
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        log.info("开始注册数据源");
        this.evn = environment;
        // 绑定配置器
        binder = Binder.get(evn);
    }

    /**
     *@描述  ImportBeanDefinitionRegistrar接口的实现方法，通过该方法可以按照自己的方式注册bean
     *@参数  [annotationMetadata, beanDefinitionRegistry]
     *@返回值  void
     *@创建人  huang.yj
     *@创建时间  2019/12/11
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        // 注册默认数据源
        defaultDataSource = initMasterDataSource();
        // 注册更多数据源（不包含默认数据源）
        initSlaveDataSources();
        // bean定义类
        GenericBeanDefinition define = new GenericBeanDefinition();
        // 设置bean的类型，此处DynamicRoutingDataSource是继承AbstractRoutingDataSource的实现类
        define.setBeanClass(DynamicDataSource.class);
        // 需要注入的参数
        MutablePropertyValues mpv = define.getPropertyValues();
        // 添加默认数据源，避免key不存在的情况没有数据源可用
        mpv.add("defaultTargetDataSource", defaultDataSource);
        // 添加其他数据源
        mpv.add("targetDataSources", slaveDataSources);
        // 将该bean注册为datasource，不使用springboot自动生成的datasource
        beanDefinitionRegistry.registerBeanDefinition("datasource", define);
        log.info("注册数据源成功，一共注册{}个数据源", slaveDataSources.keySet().size() + 1);
    }

    /**
     *@描述  注册更多数据源（不包含默认数据源）
     *@参数  []
     *@返回值  void
     *@创建人  huang.yj
     *@创建时间  2019/12/11
     */
    private void initSlaveDataSources() {
        Map config;
        Class<? extends DataSource> clazz;
        Map defauleDataSourceProperties;
        DataSource slaveDatasource;
        // 获取其他数据源配置
        List<Map> configs = binder.bind("spring.datasource.slave", Bindable.listOf(Map.class)).get();
        // 遍历从数据源
        for (int i = 0; i < configs.size(); i++) {
            config = configs.get(i);
            clazz = getDataSourceType((String) config.get("type"));
            defauleDataSourceProperties = config;
            // 绑定参数
            slaveDatasource = bind(clazz, defauleDataSourceProperties);
            // 获取数据源的key，以便通过该key可以定位到数据源
            String key = config.get("key").toString();
            slaveDataSources.put(key, slaveDatasource);
            // 数据源上下文，用于管理数据源与记录已经注册的数据源key
            DataSourceSwitch.dataSourceIds.add(key);
            log.info("注册数据源{}成功", key);
            logDS(slaveDatasource);
        }
    }

    /**
     *@描述 注册默认数据源
     *@参数  []
     *@返回值  javax.sql.DataSource
     *@创建人  huang.yj
     *@创建时间  2019/12/11
     */
    private DataSource initMasterDataSource() {
        Map defauleDataSourceProperties = binder.bind("spring.datasource.master", Map.class).get();
        // 获取数据源类型
        String typeStr = evn.getProperty("spring.datasource.master.type");
        // 获取数据源类型
        Class<? extends DataSource> clazz = getDataSourceType(typeStr);
        // 绑定默认数据源参数 也就是主数据源
        DataSource defaultDatasource = bind(clazz, defauleDataSourceProperties);
        DataSourceSwitch.dataSourceIds.add("master");
        log.info("注册默认数据源成功");
        logDS(defaultDatasource);
        return defaultDatasource;
    }

    /**
     * 通过字符串获取数据源class对象
     *
     * @param typeStr
     * @return
     */
    private Class<? extends DataSource> getDataSourceType(String typeStr) {
        Class<? extends DataSource> type;
        try {
            if (StringUtils.hasLength(typeStr)) {
                // 字符串不为空则通过反射获取class对象
                type = (Class<? extends DataSource>) Class.forName(typeStr);
            } else {
                // 默认为hikariCP数据源，与springboot默认数据源保持一致
                type = HikariDataSource.class;
            }
            return type;
        } catch (Exception e) {
            throw new IllegalArgumentException("can not resolve class with type: " + typeStr); //无法通过反射获取class对象的情况则抛出异常，该情况一般是写错了，所以此次抛出一个runtimeexception
        }
    }

    /**
     * 绑定参数，以下三个方法都是参考DataSourceBuilder的bind方法实现的，目的是尽量保证我们自己添加的数据源构造过程与springboot保持一致
     *
     * @param result
     * @param properties
     */
    private void bind(DataSource result, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
        // 将参数绑定到对象
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result));
    }

    private <T extends DataSource> T bind(Class<T> clazz, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
        // 通过类型绑定参数并获得实例对象
        return binder.bind(ConfigurationPropertyName.EMPTY, Bindable.of(clazz)).get();
    }

    /**
     * @param clazz
     * @param sourcePath 参数路径，对应配置文件中的值，如: spring.datasource
     * @param <T>
     * @return
     */
    private <T extends DataSource> T bind(Class<T> clazz, String sourcePath) {
        Map properties = binder.bind(sourcePath, Map.class).get();
        return bind(clazz, properties);
    }

    /**
     *@描述 显示数据库连接池信息
     *@参数  [dataSource]
     *@返回值  void
     *@创建人  huang.yj
     *@创建时间  2019/12/11
     */
    private void logDS(DataSource dataSource) {
        HikariDataSource hds = (HikariDataSource) dataSource;
        String info = "\n\n\tHikariCP连接池配置\n\t连接池名称：" +
                hds.getPoolName() +
                "\n\t最小空闲连接数：" +
                hds.getMinimumIdle() +
                "\n\t最大连接数：" +
                hds.getMaximumPoolSize() +
                "\n\t连接超时时间：" +
                hds.getConnectionTimeout() +
                "ms\n\t空闲连接超时时间：" +
                hds.getIdleTimeout() +
                "ms\n\t连接最长生命周期：" +
                hds.getMaxLifetime() +
                "ms\n";
        log.info(info);
    }
}