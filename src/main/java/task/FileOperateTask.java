/**
 * Copyright (C), 2015-2020, XXX有限公司
 * <p>
 * FileName: FileOperateTask
 * <p>
 * Author:   HASEE
 * <p>
 * Date:     2020/1/20 16:55
 * <p>
 * Description:
 * <p>
 * History:
 *
 * <XD>          <time>          <1.1>          <javaDamo>
 */
package task;

import app.FileMeta;
import dao.FileOperateTaskDAO;
import task.FileScanCallack;
import util.DBUtil;
import util.Pinyin4jUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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

public class FileOperateTask implements FileScanCallack {

    @Override
    public void execute(File dir) {

        if (dir.isDirectory()) {
            File[] children = dir.listFiles();

            List<FileMeta> localMetas = compose(children);
            List<FileMeta> metas = FileOperateTaskDAO.query(dir.getPath());

            for (FileMeta meta : metas) {
                if (!localMetas.contains(meta)) {
                    FileOperateTaskDAO.delete(meta);
                }
            }

            for (FileMeta localMeta : localMetas) {
                if (!metas.contains(localMeta)) {
                    FileOperateTaskDAO.insert(localMeta);
                }
            }

        }

    }

    private List<FileMeta> compose(File[] children) {
        List<FileMeta> metas = new ArrayList<>();
        if(children != null){
            for(File child : children){
                metas.add(new FileMeta(child));
            }
        }
        return metas;
    }
}