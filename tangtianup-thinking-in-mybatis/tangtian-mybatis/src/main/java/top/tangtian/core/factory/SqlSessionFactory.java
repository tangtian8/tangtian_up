package top.tangtian.core.factory;

import top.tangtian.api.SqlSession;
import top.tangtian.api.SqlSessionImpl;
import top.tangtian.core.entity.Configuration;

/**
 * @author tangtian
 * @description
 * @date 2022/11/23 8:03
 */
public class SqlSessionFactory {
    private Configuration configuration;

    public SqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 创建SqlSession会话
     */
    public SqlSession openSession(){
        return new SqlSessionImpl(configuration);
    }
}
