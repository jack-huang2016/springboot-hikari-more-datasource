/**
 * FileName: DynamicDataSourceAspect
 * Author:   huang.yj
 * Date:     2019/12/11 14:18
 * Description: 动态数据源注解切面类
 */
package com.sample.test.web.aop;

import com.sample.test.common.util.SwitchDataSource;
import com.sample.test.dao.config.DataSourceSwitch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 〈动态数据源注解切面类〉
 *
 * @author huang.yj
 * @create 2019/12/11
 * @since 0.0.1
 */
@Slf4j
@Aspect
@Component
public class DynamicDataSourceAspect {

    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, SwitchDataSource ds) throws Throwable {
        String dsId = ds.value();
        if (DataSourceSwitch.dataSourceIds.contains(dsId)) {
            log.debug("切换数据源 :{} >", dsId, point.getSignature());
            DataSourceSwitch.setDataSourceType(dsId);
        } else {
            log.info("数据源[{}]不存在，使用默认数据源 >{}", dsId, point.getSignature());
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, SwitchDataSource ds) {
        log.debug("清除数据源缓存 : " + ds.value() + " > " + point.getSignature());
        DataSourceSwitch.clearDataSourceType();
    }
}