/**
 * Copyright (C), 2015-2020, XXX有限公司
 * <p>
 * FileName: DBUtil
 * <p>
 * Author:   HASEE
 * <p>
 * Date:     2020/1/12 15:17
 * <p>
 * Description:
 * <p>
 * History:
 *
 * <XD>          <time>          <1.1>          <javaDamo>
 */
package util;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author HASEE

 * @create 2020/1/12

 * @since 1.0.0

 */

public class DBUtil {
    private static volatile DataSource DATA_SOURCE;

    /**
     * 获取SQLite数据库本地文件路径(target目录下)
     * @return
     * @throws URISyntaxException
     */
    private static String getUrl() throws URISyntaxException {
        String dbName = "cxd-gq.db";
        URL url = DBUtil.class.getClassLoader().getResource(".");//获取类加载器路径
        return "jdbc:sqlite://" + new File(url.toURI()).getParent()
                + File.separator+dbName;
    }

    /**
     * 获取数据库连接池
     * @return
     * @throws URISyntaxException
     */
    private static DataSource getDataSource() throws URISyntaxException {
        if(DATA_SOURCE == null){
            synchronized (DBUtil.class){
                if(DATA_SOURCE == null){
                    //mysql日期格式：yyyy-MM-dd HH:mm:ss
                    //sqlite多:SSS
                    SQLiteConfig config = new SQLiteConfig();
                    config.setDateStringFormat(Util.DATA_PATTERN);
                    DATA_SOURCE = new SQLiteDataSource(config);
                    ((SQLiteDataSource)DATA_SOURCE).setUrl(getUrl());
                }
            }
        }
        return DATA_SOURCE;
    }

    /**
     * 获取数据库连接：
     * 1.Class.forName("驱动类全名")加载驱动，DriverMannger.getConnection()
     * 2.DataSoure
     */
    public static Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据库连接获取失败");
        }
    }

    /**
     * 释放数据库资源
     * @param connection
     * @param statement
     * @param resultSet
     */

    public static void close(Connection connection, Statement statement, ResultSet resultSet){
        try {
            if(resultSet != null){
                resultSet.close();
            }
            if(statement != null){
                statement.close();
            }
            if(connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("释放数据库资源错误");
        }
    }

    public static void close(Connection connection, Statement statement){
        close(connection, statement, null);
    }

    public static void main(String[] args) {
        Connection connection = getConnection();

    }
}