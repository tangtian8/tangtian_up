package run;
import mapper.BlogMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dom4j.DocumentException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pojo.Blog;
import pojo.User;

import top.tangtian.configration.DataSourceProperties;
import top.tangtian.core.factory.SqlSessionFactoryBuilder;
import top.tangtian.core.mapping.DefaultResultSetHandler;
import top.tangtian.core.mapping.MyResultSetHandler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * @author tangtian
 * @description
 * @date 2022/5/11 8:01
 */
@SpringBootApplication
public class MybatisTestMain {
    public static void main(String[] args) throws Exception {
//------------mybatis 框架实现
//        String resource = "mybatis-config.xml";
//        InputStream inputStream = null;
//        try {
//            inputStream = Resources.getResourceAsStream(resource);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
//        Blog blog = mapper.selectBlog("1");
//        System.out.println(blog.getId());
//------------自己实现的sql映射框架
        //1.创建SqlSessionFactoryBuilder对象
//        top.tangtian.core.factory.SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
//        //2.builder对象构建工厂对象
//        InputStream inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
//        SqlSessionFactory sqlSessionFactory = builder.build(inputStream);
//        //3.打开SqlSession会话
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//        //4.执行查询Sql语句
//        MyResultSetHandler myResultSetHandler = new DefaultResultSetHandler();
//        List<User> users = sqlSession.selectList("mapper.UserMapper.findAll",myResultSetHandler);
//        //5.循环打印
//        for (User u : users) {
//            System.out.println(u);
//        }

        ConfigurableApplicationContext run = SpringApplication.run(MybatisTestMain.class, args);
        SqlSessionFactoryBuilder factoryBuilder = run.getBean(SqlSessionFactoryBuilder.class);
        DataSourceProperties properties = factoryBuilder.getProperties();
        String driver = properties.getDriver();
        System.out.println(driver);
    }
}
