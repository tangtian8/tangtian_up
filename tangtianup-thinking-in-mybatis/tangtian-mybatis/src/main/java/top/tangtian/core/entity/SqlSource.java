package top.tangtian.core.entity;

/**
 * @author tangtian
 * @description
 * @date 2022/11/23 7:57
 */
public class SqlSource {
    private String sql;
    private String resultType;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
