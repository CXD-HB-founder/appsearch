package app; /**
 * Copyright (C), 2015-2020, XXX有限公司
 * <p>
 * FileName: app.FileMeta
 * <p>
 * Author:   HASEE
 * <p>
 * Date:     2020/1/14 17:53
 * <p>
 * Description:
 * <p>
 * History:
 *
 * <XD>          <time>          <1.1>          <javaDamo>
 */

import util.Util;

import java.io.File;
import java.util.Objects;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author HASEE

 * @create 2020/1/14

 * @since 1.0.0

 */

public class FileMeta {

    private String name;
    private String path;
    private Long size;
    private Long lastModified;
    private Boolean isDirectory;
    private String sizeText;
    private String lastModifiedText;

    public FileMeta(String name, String path, Long size, Long lastModified,
                    boolean isDirectory) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.isDirectory = isDirectory;
        this.lastModified = lastModified;
        this.sizeText = Util.parseSize(size);
        this.lastModifiedText = Util.parseData(lastModified);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMeta fileMeta = (FileMeta) o;
        return Objects.equals(name, fileMeta.name) &&
                Objects.equals(path, fileMeta.path) &&
                Objects.equals(isDirectory, fileMeta.isDirectory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, isDirectory);
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", isDirectory=" + isDirectory +
                ", sizeText='" + sizeText + '\'' +
                ", lastModifiedText='" + lastModifiedText + '\'' +
                '}';
    }

    public FileMeta(File child) {
        this(child.getName(),child.getParent(),child.length(),child.lastModified(),
                child.isDirectory());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public Boolean getDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }

    public String getSizeText() {
        return sizeText;
    }

    public void setSizeText(String sizeText) {
        this.sizeText = sizeText;
    }

    public String getLastModifiedText() {
        return lastModifiedText;
    }

    public void setLastModifiedText(String lastModifiedText) {
        this.lastModifiedText = lastModifiedText;
    }
}