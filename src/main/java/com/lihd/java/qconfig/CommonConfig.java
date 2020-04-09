package com.lihd.java.qconfig;

/**
 * @program: zookeeper-sample
 * @description:
 * @author: li_hd
 * @create: 2020-03-11 18:14
 **/
public class CommonConfig {

    // 数据库连接配置
    private String dbUrl;
    private String username;
    private String password;
    private String driverClass;

    public CommonConfig() {
    }

    public CommonConfig(String dbUrl, String username, String password, String driverClass) {
        super();
        this.dbUrl = dbUrl;
        this.username = username;
        this.password = password;
        this.driverClass = driverClass;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
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

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    @Override
    public String toString() {
        return "CommonConfig:{dbUrl:" + this.dbUrl +
                ", username:" + this.username +
                ", password:" + this.password +
                ", driverClass:" + this.driverClass + "}";

    }



}
