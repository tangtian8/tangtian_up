# 环境初始化

## 数据库
### 管理端
使用`scripts/manager.sql`创建对应的数据库,修改manager/src/main/resources/application.yml中数据库连接地址

### 销售端
使用`scripts/seller.sql`, `scripts/seller-backup.sql`创建对应数据库,修改seller/src/main/resources/application.yml中数据库连接地址

## activemq
参考[activemq](http://activemq.apache.org/version-5-getting-started.html#Version5GettingStarted-InstallationProcedureforUnix)安装地址进行安装、启动
修改`manager/src/main/resources/application.yml`、`seller/src/main/resources/application.yml`中activemq相关配置
