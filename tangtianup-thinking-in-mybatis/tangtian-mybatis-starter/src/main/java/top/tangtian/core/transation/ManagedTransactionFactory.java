package top.tangtian.core.transation;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author tangtian
 * @description
 * @date 2022/11/26 14:57
 */
public class ManagedTransactionFactory implements TransactionFactory{
    private boolean closeConnection = true;

    @Override
    public void setProperties(Properties props) {
        if (props != null) {
            String closeConnectionProperty = props.getProperty("closeConnection");
            if (closeConnectionProperty != null) {
                closeConnection = Boolean.parseBoolean(closeConnectionProperty);
            }
        }
    }

    @Override
    public Transaction newTransaction(Connection conn) {
        return new ManagedTransaction(conn, closeConnection);
    }


}
