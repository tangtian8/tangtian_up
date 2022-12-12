package top.tangtian.core.factory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import top.tangtian.configration.DataSourceProperties;
import top.tangtian.core.binding.MapperMethod;
import top.tangtian.core.entity.ConfigurationInfo;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tangtian
 * @description
 * @date 2022/11/23 8:04
 */
public class SqlSessionFactoryBuilder {

    private DataSourceProperties properties;

    public SqlSessionFactoryBuilder(DataSourceProperties dataSourceProperties) {
        this.properties = dataSourceProperties;
    }

    /**
     * 构建工厂对象
     * 参数：SqlMapConfig.xml配置文件的输入流对象
     */
    public SqlSessionFactory build(InputStream inputStream) throws DocumentException, SQLException {
        ConfigurationInfo configurationInfo = new ConfigurationInfo();
        //解析配置文件
        loadXmlConfig(configurationInfo,inputStream);
        return new SqlSessionFactory(configurationInfo);
    }

    /**
     * 解析框架使用者传入的配置文件
     */
    private void loadXmlConfig(ConfigurationInfo configurationInfo, InputStream inputStream) throws DocumentException {
        //创建解析XML文件对象SAXReader
        SAXReader saxReader = new SAXReader();
        //读取SqlMapConfig.xml配置文件流资源，获取文档对象
        Document document = saxReader.read(inputStream);
        //获取SqlMapConfig.xml 配置文件内所有property标签元素
        List<Element> selectNodes = document.selectNodes("//property");
        //循环解析property标签内容，抽取配置信息
        for (Element element : selectNodes) {
            String name = element.attributeValue("name");
            if ("driver".equals(name)){//数据库驱动
                configurationInfo.setDriver(properties.getDriver());
//                configurationInfo.setDriver(element.attributeValue("value"));
            } else if ("url".equals(name)){//数据库地址
                configurationInfo.setUrl(properties.getUrl());
//                configurationInfo.setUrl(element.attributeValue("value"));
            } else if ("username".equals(name)){//用户名
                configurationInfo.setUsername(properties.getUsername());
//                configurationInfo.setUsername(element.attributeValue("value"));
            } else if ("password".equals(name)){//密码
                configurationInfo.setPassword(properties.getPassword());
//                configurationInfo.setPassword(element.attributeValue("value"));
            }
        }
        //解析SqlMapConfig.xml 映射器配置信息
        List<Element> list = document.selectNodes("//mapper");
        for (Element element : list) {
            //SQL映射配置文件路径
            String resource = element.attributeValue("resource");
            //解析SQL映射配置文件
            loadSqlConfig(resource, configurationInfo);
         }
    }
    /**
     * 解析SQL配置文件
     */
    private void loadSqlConfig(String resource, ConfigurationInfo configurationInfo) throws DocumentException {
        //根据SQL映射配置文件路径，读取流资源。classpath路径下
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resource);
        //创建解析XML文件的对象SAXReader
        SAXReader saxReader = new SAXReader();
        //读取UserMapper.xml配置文件文档对象
        Document document = saxReader.read(inputStream);
        //获取文档对象根节点：<mapper namespace="test">
        Element rootElement = document.getRootElement();
        //取出根节点的命名空间
        String namespace = rootElement.attributeValue("namespace");
        //获取当前SQL映射文件所有查询语句标签
        List<Element> selectNodes = document.selectNodes("//select");
        //循环解析查询标签select，抽取SQL语句
        List<MapperMethod> mapperMethods = new ArrayList<>();
        for (Element element : selectNodes) {
            //查询语句唯一标识
            String id = element.attributeValue("id");
            //当前查询语句返回结果集对象类型
            String resultType = element.attributeValue("resultType");
            //查询语句
            String sql = element.getText();
            //创建Mapper对象
            MapperMethod mapper = new MapperMethod();
            mapper.setSql(sql);
            mapper.setResultType(resultType);
            mapper.setId(id);
            //在configuration中设置mapper类，key：(命名空间+.+SQL语句唯一标识符)
            mapperMethods.add(mapper);
        }
        configurationInfo.getSqlSourceMap().put(namespace,mapperMethods);
    }

    public DataSourceProperties getProperties() {
        return properties;
    }
}
