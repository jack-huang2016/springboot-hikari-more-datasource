/**
 * FileName: DynamicDataSource
 * Author:   huang.yj
 * Date:     2019/12/10 21:05
 * Description: 动态数据源
 */
package com.sample.test.dao.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 〈动态数据源〉
 *
 * @author huang.yj
 * @create 2019/12/10
 * @since 0.0.1
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceSwitch.getDataSourceType();
    }
}