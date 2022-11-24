package top.tangtian.resulthandler;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface MyResultSetHandler {
    <E> List<E> handleResultSets(String resultType, ResultSet resultSet) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException;

}
