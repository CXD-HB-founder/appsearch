/**
 * Copyright (C), 2015-2020, XXX有限公司
 * <p>
 * FileName: Util
 * <p>
 * Author:   HASEE
 * <p>
 * Date:     2020/1/12 17:24
 * <p>
 * Description:
 * <p>
 * History:
 *
 * <XD>          <time>          <1.1>          <javaDamo>
 */
package util;








import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author HASEE

 * @create 2020/1/12

 * @since 1.0.0

 */

public class Util {

    public static final String DATA_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String[] size_Names = {"B", "KB", "MB", "GB"};
    public static final DateFormat DATA_FORMAT = new SimpleDateFormat(DATA_PATTERN);
    /**
     * 文件装换
     * @param size
     * @return
     */
    public static String parseSize(Long size) {
        int i = 0;
        while (size >= 1024){
            size=size/1024;
            i++;
        }
        return size+size_Names[i];
    }

    public static String parseData(Long lastModified) {

        return DATA_FORMAT.format(new Date(lastModified));
    }

    public static void main(String[] args) {

    }
}