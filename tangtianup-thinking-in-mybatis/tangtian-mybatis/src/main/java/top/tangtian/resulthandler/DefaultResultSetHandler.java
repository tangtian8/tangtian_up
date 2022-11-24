package top.tangtian.resulthandler;

import top.tangtian.core.entity.SqlSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tangtian
 * @description
 * @date 2022/11/23 8:37
 */
public class DefaultResultSetHandler implements MyResultSetHandler {
    @Override
    public <E> List<E> handleResultSets(String resultType, ResultSet resultSet) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> clazz = Class.forName(resultType);
        //循环 遍历所有列名
        //获取所有列的list集合
        //获取结果集元数据
        ResultSetMetaData metaData = resultSet.getMetaData();
        //获取总列数5
        int columnCount = metaData.getColumnCount();
        List<String> columnNames = new ArrayList<String>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        List list = new ArrayList();
        while (resultSet.next()) {
            //在代码运行的时候，为创建指定类的对象，并且可以调用对象的方法，而且无视权限修饰符！
            //在汽车奔跑的时候，为汽车更换轮子！
            //通过反射获取类的字节码对象，传入的参数是类的全限定名称
            //反射创建对象
            Object user = clazz.newInstance();
            //反射获取当前类的所有方法
            Method[] methods = clazz.getMethods();
            //循环 遍历所有列名
            for (String columnName : columnNames) {
                for (Method method : methods) {
                    String methodName = method.getName();
                    //判断方法的名称，与set+列名相等，那么就把列名对应的值设置到当前对象的set方法中
                    if (("set"+columnName).equalsIgnoreCase(methodName)){
                        //把列名column对应的值,设置到对象的set方法中,给属性赋值
                        method.invoke(user,resultSet.getObject(columnName));
                    }
                }
            }
            //将用户存入集合中
            list.add(user);
        }
        return list;
    }
}
