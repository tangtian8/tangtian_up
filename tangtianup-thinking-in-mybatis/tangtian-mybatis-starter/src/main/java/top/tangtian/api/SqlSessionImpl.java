package top.tangtian.api;

import top.tangtian.Executor;
import top.tangtian.core.binding.MappedStatement;
import top.tangtian.core.entity.Environment;
import top.tangtian.core.mapping.MyResultSetHandler;

import java.util.List;
/**
 * @author tangtian
 * @description
 * @date 2022/11/23 8:05
 */
public class SqlSessionImpl implements SqlSession{

    //每次Sql会话连接，必须要有数据库配置信息
    private Environment environment;
    private MappedStatement mappedStatement;

    public SqlSessionImpl(Environment configuration,MappedStatement mappedStatement) {
        this.environment = configuration;
        this.mappedStatement = mappedStatement;
    }

    @Override
    public <T> List<T> selectList(String sql) throws Exception {
        Executor executor = new Executor(environment,mappedStatement);

        return (List<T>) executor.executeQuery(sql);
    }


    @Override
    public <T> List<T> selectList(String sql, MyResultSetHandler setHandler) throws Exception {
        Executor executor = new Executor(environment,mappedStatement);

        return (List<T>) executor.executeQuery(sql,setHandler);
    }
}
