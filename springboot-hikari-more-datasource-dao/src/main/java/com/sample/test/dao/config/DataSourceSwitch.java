/**
 * FileName: DataSourceSwitch
 * Author:   huang.yj
 * Date:     2019/12/10 21:06
 * Description: 数据源操作类
 */
package com.sample.test.dao.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈数据源操作类〉
 *
 * @author huang.yj
 * @create 2019/12/10
 * @since 0.0.1
 */
public class DataSourceSwitch {
    //存放当前线程使用的数据源类型信息
    private static ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    //存放数据源id
    public static List<String> dataSourceIds = new ArrayList<String>();

    /**
     * 获取当前线程的数据源路由的key
     */
    public static String getDataSourceType ()    {
        String key = contextHolder.get();
        return key;
    }

    /**
     * 绑定当前线程数据源路由的key
     * 使用完成后必须调用removeRouteKey()方法删除
     */
    public static void  setDataSourceType (String key)    {
        contextHolder.set(key);
    }

    /**
     * 删除与当前线程绑定的数据源路由的key
     */
    public static void clearDataSourceType ()    {
        contextHolder.remove();
    }

    //判断当前数据源是否存在
    public static boolean isContainsDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }
}