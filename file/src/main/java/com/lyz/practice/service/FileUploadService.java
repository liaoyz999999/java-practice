package com.lyz.practice.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author CodeGenerator
 * @date 2021/06/16 14:19
 */
public interface FileUploadService {
    void upload(MultipartFile file,
                String suffix,
                Integer shardIndex,
                Integer shardSize,
                Integer shardTotal,
                String fileKey);

    JSONObject check(String fileKey);

    void merge(String fileKey,Integer size);
}
