package com.lyz.practice.model;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Administrator
 */
@Table(name = "file_info")
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件内容MD5加密值（唯一）
     */
    @Column(name = "content_key")
    private String contentKey;

    /**
     * 文件分片前缀（由前端根据文件名、类型、最后修改时间等信息加密生成）
     */
    @Column(name = "shard_prefix")
    private String shardPrefix;

    /**
     * 文件大小|字节B
     */
    private Integer size;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 存储路径
     */
    private String path;

    @Column(name = "start_at")
    private Date startAt;

    @Column(name = "finish_at")
    private Date finishAt;

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(Date finishAt) {
        this.finishAt = finishAt;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取文件名称
     *
     * @return name - 文件名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置文件名称
     *
     * @param name 文件名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取文件内容MD5加密值（唯一）
     *
     * @return content_key - 文件内容MD5加密值（唯一）
     */
    public String getContentKey() {
        return contentKey;
    }

    /**
     * 设置文件内容MD5加密值（唯一）
     *
     * @param contentKey 文件内容MD5加密值（唯一）
     */
    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    /**
     * 获取文件分片前缀（由前端根据文件名、类型、最后修改时间等信息加密生成）
     *
     * @return shard_prefix - 文件分片前缀（由前端根据文件名、类型、最后修改时间等信息加密生成）
     */
    public String getShardPrefix() {
        return shardPrefix;
    }

    /**
     * 设置文件分片前缀（由前端根据文件名、类型、最后修改时间等信息加密生成）
     *
     * @param shardPrefix 文件分片前缀（由前端根据文件名、类型、最后修改时间等信息加密生成）
     */
    public void setShardPrefix(String shardPrefix) {
        this.shardPrefix = shardPrefix;
    }

    /**
     * 获取文件大小|字节B
     *
     * @return size - 文件大小|字节B
     */
    public Integer getSize() {
        return size;
    }

    /**
     * 设置文件大小|字节B
     *
     * @param size 文件大小|字节B
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * 获取文件后缀
     *
     * @return suffix - 文件后缀
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * 设置文件后缀
     *
     * @param suffix 文件后缀
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * 获取存储路径
     *
     * @return path - 存储路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置存储路径
     *
     * @param path 存储路径
     */
    public void setPath(String path) {
        this.path = path;
    }
}