

import org.junit.Before;
import org.junit.Test;
import pojo.User;
import top.tangtian.api.SqlSession;
import top.tangtian.core.factory.SqlSessionFactory;
import top.tangtian.core.factory.SqlSessionFactoryBuilder;
import top.tangtian.core.mapping.DefaultResultSetHandler;
import top.tangtian.core.mapping.MyResultSetHandler;

import java.io.InputStream;
import java.util.List;

/**
 * @author tangtian
 * @description
 * @date 2022/11/23 9:05
 */
public class CustomFrameTest {
    SqlSession sqlSession;
    @Before
    public void init() throws Exception{
        //1.创建SqlSessionFactoryBuilder对象
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        //2.builder对象构建工厂对象
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = builder.build(inputStream);
        //3.打开SqlSession会话
        sqlSession = sqlSessionFactory.openSession();
    }
    @Test
    public void test() throws Exception {
        //4.执行查询Sql语句
        MyResultSetHandler myResultSetHandler = new DefaultResultSetHandler();
        List<User> users = sqlSession.selectList("mapper.UserMapper.findAll",myResultSetHandler);
        //5.循环打印
        for (User u : users) {
            System.out.println(u);
        }
    }
}
