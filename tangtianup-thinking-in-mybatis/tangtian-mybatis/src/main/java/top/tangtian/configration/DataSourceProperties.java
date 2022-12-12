package top.tangtian.configration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tangtian
 * @description
 * @date 2022/12/12 7:36
 */
@ConfigurationProperties(prefix = "tangtian.batis.dataSource")
public class DataSourceProperties {
    private String driver;
    private String url;
    private String username;
    private String password;

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
