package com.lyz.practice.model;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Administrator
 */
@Table(name = "file_shard")
public class FileShard {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 分片名
     */
    private String name;

    /**
     * 文件创建时间
     */
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * 已上传分片
     */
    @Column(name = "shard_index")
    private Integer shardIndex;

    /**
     * 分片大小|B
     */
    @Column(name = "shard_size")
    private Integer shardSize;

    /**
     * 分片总数
     */
    @Column(name = "shard_total")
    private Integer shardTotal;

    /**
     * 分片前缀（由前端根据文件名、类型、最后修改时间等信息加密生成）
     */
    @Column(name = "shard_key")
    private String shardKey;
    
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取id
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取分片名
     *
     * @return name - 分片名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置分片名
     *
     * @param name 分片名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取文件创建时间
     *
     * @return created_at - 文件创建时间
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置文件创建时间
     *
     * @param createdAt 文件创建时间
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取已上传分片
     *
     * @return shard_index - 已上传分片
     */
    public Integer getShardIndex() {
        return shardIndex;
    }

    /**
     * 设置已上传分片
     *
     * @param shardIndex 已上传分片
     */
    public void setShardIndex(Integer shardIndex) {
        this.shardIndex = shardIndex;
    }

    /**
     * 获取分片大小|B
     *
     * @return shard_size - 分片大小|B
     */
    public Integer getShardSize() {
        return shardSize;
    }

    /**
     * 设置分片大小|B
     *
     * @param shardSize 分片大小|B
     */
    public void setShardSize(Integer shardSize) {
        this.shardSize = shardSize;
    }

    /**
     * 获取分片总数
     *
     * @return shard_total - 分片总数
     */
    public Integer getShardTotal() {
        return shardTotal;
    }

    /**
     * 设置分片总数
     *
     * @param shardTotal 分片总数
     */
    public void setShardTotal(Integer shardTotal) {
        this.shardTotal = shardTotal;
    }

    /**
     * 获取分片前缀（由前端根据文件名、类型、最后修改时间等信息加密生成）
     *
     * @return shard_key - 分片前缀（由前端根据文件名、类型、最后修改时间等信息加密生成）
     */
    public String getShardKey() {
        return shardKey;
    }

    /**
     * 设置分片前缀（由前端根据文件名、类型、最后修改时间等信息加密生成）
     *
     * @param shardKey 分片前缀（由前端根据文件名、类型、最后修改时间等信息加密生成）
     */
    public void setShardKey(String shardKey) {
        this.shardKey = shardKey;
    }
}