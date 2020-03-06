package task;

import java.io.File;

/**
 * 文件扫描的回调
 */
public interface FileScanCallack {
    void execute(File dir);
}

