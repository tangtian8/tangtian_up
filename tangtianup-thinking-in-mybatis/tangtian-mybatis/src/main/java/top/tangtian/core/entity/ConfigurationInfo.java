package top.tangtian.core.entity;


import top.tangtian.core.binding.MapperMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tangtian
 * @description
 * @date 2022/11/23 7:57
 */
public class ConfigurationInfo {
    private String driver;
    private String url;
    private String username;
    private String password;
    private Map<String, List<MapperMethod>> sqlSourceMap = new HashMap<>();

    public Map<String, List<MapperMethod>> getSqlSourceMap() {
        return sqlSourceMap;
    }

    public void setSqlSourceMap(Map<String, List<MapperMethod>> sqlSourceMap) {
        this.sqlSourceMap = sqlSourceMap;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
