package top.tangtian.api;

import top.tangtian.core.mapping.MyResultSetHandler;

import java.util.List;

/**
 * @author tangtian
 * @description
 * @date 2022/11/23 7:55
 */
public interface SqlSession {
    /**
     * 查询所有用户
     * T 代表泛型类型，T(type缩写)
     */
    <T> List<T> selectList(String sql) throws Exception;

    <T> List<T> selectList(String sql, MyResultSetHandler setHandler) throws Exception;
}
