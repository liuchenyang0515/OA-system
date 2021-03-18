package com.me.oa.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DruidDataSourceFactory extends UnpooledDataSourceFactory {
    public DruidDataSourceFactory() { // UnpooledDataSourceFactory内部默认赋值了一个不需要连接池就能访问的数据源，子类重写实例化DataSource对象
        // 1.创建空的数据源对象 2.执行setProperties读取数据原配置进行属性设置 3.（可选）重写getDataSource，进行datasource初始化
        // 执行以上3步骤后，mybatis在执行时就会利用指定的datasourcefactory来获取数据源了
        this.dataSource = new DruidDataSource();
    }

    @Override
    public DataSource getDataSource() {
        try {
            ((DruidDataSource) this.dataSource).init(); // 初始化Druid数据源
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.dataSource;
    }
}
