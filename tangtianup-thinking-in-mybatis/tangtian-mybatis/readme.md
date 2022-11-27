# 手写的一个ORM框架

## Maven工程搭建

### pom文件配置

```
<groupId>top.tangtian</groupId>
<artifactId>tangtian-mybatis</artifactId>
<packaging>pom</packaging>
<version>1.0-SNAPSHOT</version>
```

```
<dependencies>
   <dependency>
      <groupId>dom4j</groupId>
      <artifactId>dom4j</artifactId>
      <version>1.6.1</version>
   </dependency>
   <dependency>
      <groupId>jaxen</groupId>
      <artifactId>jaxen</artifactId>
      <version>1.1.6</version>
   </dependency>
   <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.14</version>
   </dependency>
</dependencies>
```

### 目录结构

```text
├─api  #session会话 对外服务
│  ├─SqlSession.java
│  └─SqlSessionImpl.java
├─binding #Mapper文件封装
├─datasource #数据源（数据连接池）
│  ├─pooled
│  └─unpooled
├─entity #配置信息实体
├─factory #SqlSession 实例构建工厂
├─mapping # 结果集映射
└─transation # 事务
```

