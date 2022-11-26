package top.tangtian.core.factory;

import top.tangtian.api.SqlSession;
import top.tangtian.api.SqlSessionImpl;
import top.tangtian.core.binding.MappedStatement;
import top.tangtian.core.binding.MapperMethod;
import top.tangtian.core.datasource.pooled.PooledConnection;
import top.tangtian.core.datasource.pooled.PooledDataSource;
import top.tangtian.core.datasource.unpooled.UnpooledDataSource;
import top.tangtian.core.entity.Configuration;
import top.tangtian.core.entity.Environment;
import top.tangtian.core.transation.ManagedTransactionFactory;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author tangtian
 * @description
 * @date 2022/11/23 8:03
 */
public class SqlSessionFactory {
    private Configuration configuration;
    private Environment environment;
    private MappedStatement mappedStatement;

    public SqlSessionFactory(Configuration configuration) throws SQLException {
        this.configuration = configuration;
        this.environment = buildEnvironment(configuration);
        this.mappedStatement = buildMappedStatement(configuration);
    }

    private MappedStatement buildMappedStatement(Configuration configuration) {
        Map<String, List<MapperMethod>> sqlSourceMap = configuration.getSqlSourceMap();
        return new MappedStatement(sqlSourceMap);

    }

    private Environment buildEnvironment(Configuration configuration) throws SQLException {
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource();
        unpooledDataSource.setDriver(configuration.getDriver());
        unpooledDataSource.setUrl(configuration.getUrl());
        unpooledDataSource.setUsername(configuration.getUsername());
        unpooledDataSource.setPassword(configuration.getPassword());
        PooledDataSource pooledDataSource = new PooledDataSource(unpooledDataSource);
        Environment environment = new Environment(UUID.randomUUID().toString(),new ManagedTransactionFactory(),pooledDataSource);
        return environment;
    }

    /**
     * 创建SqlSession会话
     */
    public SqlSession openSession(){
        return new SqlSessionImpl(environment,mappedStatement);
    }
}
