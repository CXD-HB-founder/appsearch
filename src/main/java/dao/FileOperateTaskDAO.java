/**
 * Copyright (C), 2015-2020, XXX有限公司
 * <p>
 * FileName: FileOperateTaskDAO
 * <p>
 * Author:   HASEE
 * <p>
 * Date:     2020/1/20 19:37
 * <p>
 * Description:
 * <p>
 * History:
 *
 * <XD>          <time>          <1.1>          <javaDamo>
 */
package dao;

import app.FileMeta;
import util.DBUtil;
import util.Pinyin4jUtil;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author HASEE

 * @create 2020/1/20

 * @since 1.0.0

 */

public class FileOperateTaskDAO {
    public static List<FileMeta> query(String dirpath) {
        //1.数据库连接
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<FileMeta> metas = new ArrayList<>();
        try{
            connection = DBUtil.getConnection();
            String sql = "select name, path, size, last_modified, is_directory"+
                    " from file_meta where path=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, dirpath);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String path = resultSet.getString("path");
                long size = resultSet.getLong("size");
                long last_modified = resultSet.getLong("last_modified");
                boolean is_directory = resultSet.getBoolean("is_directory");
                FileMeta meta = new FileMeta(name, path, size, last_modified, is_directory);
                metas.add(meta);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return metas;
    }

//    public static void main(String[] args) {
//        delete(new FileMeta("图书", "C:\\Users\\HASEE\\Desktop\\myvedio", 0L, 0L, true));
//        //System.out.println(query("C:\\Users\\HASEE\\Desktop\\myvedio\\图书"));
//    }

    public static void delete(FileMeta meta) {
        //1.数据库连接
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            String sql = "delete from file_meta where name=?" +
                    " and path=? and is_directory=?";
            statement = connection.prepareStatement(sql);
            //填充占位符
            statement.setString(1, meta.getName());
            statement.setString(2, meta.getPath());
            statement.setBoolean(3, meta.getDirectory());

            statement.executeUpdate();

            if(meta.getDirectory()){
                sql = "delete from file_meta where path=? or path like ?";
                statement = connection.prepareStatement(sql);
                String Path = meta.getPath()+ File.separator+ meta.getName();
                statement.setString(1, Path);
                statement.setString(2, Path+ File.separator+ "%");
                statement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            DBUtil.close(connection, statement);
        }
    }

    public static void insert(FileMeta localMeta) {
        //1.数据库连接
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            try {
                connection = DBUtil.getConnection();
                String sql = "insert into file_meta" +
                        "(name, path, size, last_modified, " +
                        "pinyin, pinyin_first, is_directory)" +
                        "values (?,?,?,?,?,?,?)";
                statement = connection.prepareStatement(sql);
                //填充占位符
                statement.setString(1, localMeta.getName());
                statement.setString(2, localMeta.getPath());
                statement.setLong(3, localMeta.getSize());
                statement.setTimestamp(4,
                        new Timestamp(localMeta.getLastModified()));
                String pinyin = null;
                String pinyin_first = null;
                if(Pinyin4jUtil.containsChinese(localMeta.getName())){
                    String[] pinyins = Pinyin4jUtil.get(localMeta.getName());
                    pinyin = pinyins[0];
                    pinyin_first = pinyins[1];
                }
                statement.setString(5, pinyin);
                statement.setString(6, pinyin_first);
                statement.setBoolean(7, localMeta.getDirectory());
                //执行sql语句
                statement.executeUpdate();
            } finally {
                DBUtil.close(connection, statement);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static List<FileMeta> search(String dir, String text) {
        List<FileMeta> metas = new ArrayList<>();
        //1.数据库连接
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            boolean empty = dir==null || dir.trim().length()==0;
            connection = DBUtil.getConnection();
            String sql = "select name, path, size, last_modified, is_directory"+
                    " from file_meta where (name like ? or pinyin like ?" +
                    " or pinyin_first like ?)"
                    + ( empty?
                    "" : " and (path=? or path like ?)");
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%"+ text+ "%");
            statement.setString(2, "%"+ text+ "%");
            statement.setString(3, "%"+ text+ "%");
            if(!empty){
                statement.setString(4, dir);
                statement.setString(5, dir+File.separator+"%");
            }

            resultSet = statement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String path = resultSet.getString("path");
                long size = resultSet.getLong("size");
                long last_modified = resultSet.getLong("last_modified");
                boolean is_directory = resultSet.getBoolean("is_directory");
                FileMeta meta = new FileMeta(name, path, size, last_modified, is_directory);
                metas.add(meta);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return metas;
    }
}