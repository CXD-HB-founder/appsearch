/**
 * Copyright (C), 2015-2020, XXX有限公司
 * <p>
 * FileName: DBInit
 * <p>
 * Author:   HASEE
 * <p>
 * Date:     2020/1/12 17:35
 * <p>
 * Description:
 * <p>
 * History:
 *
 * <XD>          <time>          <1.1>          <javaDamo>
 */
package task;

import util.DBUtil;

import java.io.*;
import java.sql.Connection;
import java.sql.Statement;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author HASEE

 * @create 2020/1/12

 * @since 1.0.0

 */

public class DBInit {

    public static void init(){
        try {
            //获取数据库初始化文件的输入流
            InputStream is = DBInit.class.getClassLoader()
                    .getResourceAsStream("init.sql");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line=in.readLine()) != null){
                //忽略注释--代码
                int idx = line.indexOf("--");
                if(idx != -1){
                    line = line.substring(0, idx);
                }
                sb.append(line);
            }
            String[] sqls = sb.toString().split(";");
            Connection connection = null;
            Statement statement = null;
            try {
                for (String sql : sqls) {
                    connection = DBUtil.getConnection();
                    statement = connection.createStatement();
                    statement.executeUpdate(sql);
                }
            }finally {
                DBUtil.close(connection, statement);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据初始化任务错误");
        }
    }

    public static void main(String[] args){
        init();
    }
}