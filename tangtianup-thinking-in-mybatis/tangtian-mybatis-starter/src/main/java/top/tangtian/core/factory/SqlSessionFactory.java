package top.tangtian.core.factory;

import top.tangtian.api.SqlSession;
import top.tangtian.api.SqlSessionImpl;
import top.tangtian.core.binding.MappedStatement;
import top.tangtian.core.binding.MapperMethod;
import top.tangtian.core.datasource.pooled.PooledDataSource;
import top.tangtian.core.datasource.unpooled.UnpooledDataSource;
import top.tangtian.core.entity.ConfigurationInfo;
import top.tangtian.core.entity.Environment;
import top.tangtian.core.transation.ManagedTransactionFactory;


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
    private ConfigurationInfo configurationInfo;
    private Environment environment;
    private MappedStatement mappedStatement;

    public SqlSessionFactory(ConfigurationInfo configurationInfo) throws SQLException {
        this.configurationInfo = configurationInfo;
        this.environment = buildEnvironment(configurationInfo);
        this.mappedStatement = buildMappedStatement(configurationInfo);
    }

    private MappedStatement buildMappedStatement(ConfigurationInfo configurationInfo) {
        Map<String, List<MapperMethod>> sqlSourceMap = configurationInfo.getSqlSourceMap();
        return new MappedStatement(sqlSourceMap);

    }

    private Environment buildEnvironment(ConfigurationInfo configurationInfo) throws SQLException {
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource();
        unpooledDataSource.setDriver(configurationInfo.getDriver());
        unpooledDataSource.setUrl(configurationInfo.getUrl());
        unpooledDataSource.setUsername(configurationInfo.getUsername());
        unpooledDataSource.setPassword(configurationInfo.getPassword());
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
