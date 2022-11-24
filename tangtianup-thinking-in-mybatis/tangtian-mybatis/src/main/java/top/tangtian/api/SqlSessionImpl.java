package top.tangtian.api;

import top.tangtian.Executor;
import top.tangtian.core.entity.Configuration;
import top.tangtian.resulthandler.MyResultSetHandler;

import java.util.List;
/**
 * @author tangtian
 * @description
 * @date 2022/11/23 8:05
 */
public class SqlSessionImpl implements SqlSession{

    //每次Sql会话连接，必须要有数据库配置信息
    private Configuration configuration;

    public SqlSessionImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> List<T> selectList(String sql) throws Exception {
        Executor executor = new Executor(configuration);

        return (List<T>) executor.executeQuery(sql);
    }


    @Override
    public <T> List<T> selectList(String sql, MyResultSetHandler setHandler) throws Exception {
        Executor executor = new Executor(configuration);

        return (List<T>) executor.executeQuery(sql,setHandler);
    }
}
