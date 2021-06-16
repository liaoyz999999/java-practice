package com.lyz.practice.dao;

import com.lyz.practice.model.FileShard;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Administrator
 */
public interface FileShardMapper extends Mapper<FileShard> {

    @Insert("insert ignore into file_shard( path, name, created_at, shard_index, shard_size, shard_total, shard_key) " +
            "values (#{path},#{name},now(),#{shardIndex},#{shardSize},#{shardTotal},#{shardKey})")
    int ignoreInsert(@Param("path") String path,
                     @Param("name") String name,
                     @Param("shardKey") String shardKey,
                     @Param("shardSize") Integer shardSize,
                     @Param("shardTotal") Integer shardTotal,
                     @Param("shardIndex") Integer shardIndex);

    @Select("select id,path, name, created_at, shard_index, shard_size, shard_total, shard_key from file_shard where " +
            "shard_key=#{shardKey} order by created_at limit 1")
    FileShard selectPioneerByFileKey(@Param("shardKey") String shardKey);
}