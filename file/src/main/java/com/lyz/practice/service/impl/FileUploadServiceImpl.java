package com.lyz.practice.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.lyz.practice.dao.FileInfoMapper;
import com.lyz.practice.dao.FileShardMapper;
import com.lyz.practice.model.FileInfo;
import com.lyz.practice.model.FileShard;
import com.lyz.practice.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author CodeGenerator
 * @date 2021/06/16 14:19
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {
    @Resource
    private FileShardMapper fileShardMapper;
    @Resource
    private FileInfoMapper fileInfoMapper;

    private static final String BASE_PATH = "/usr/local/practice/file/";

    @Override
    public void upload(MultipartFile file, String suffix, Integer shardIndex, Integer shardSize, Integer shardTotal,
                       String fileKey) {
        String shardName = fileKey + "." + suffix + "." + shardIndex;
        String shardPath = BASE_PATH + shardName;
        if (fileShardMapper.ignoreInsert(shardPath, shardName, fileKey, shardSize, shardTotal, shardIndex) == 0) {
            // 已存在
            return;
        }
        File targetFile = new File(shardPath, shardName);
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            throw new RuntimeException("上传失败");
        }
    }

    @Override
    public JSONObject check(String fileKey) {
        JSONObject res = new JSONObject(4);
        List<FileShard> shardList = fileShardMapper.select(new FileShard() {{
            setShardKey(fileKey);
        }});
        if (shardList.size() == 0) {
            // 不存在
            res.put("exist", 0);
            return res;
        }
        Integer shardTotal = shardList.get(0).getShardTotal();
        if (shardTotal == shardList.size()) {
            // 全部存在只是没合并
            // 合并操作
            res.put("exist", 1);
            return res;
        }
        // 提取出缺少的部分
        Integer[] partyArray = new Integer[shardTotal];
        for (FileShard fileShard : shardList) {
            partyArray[fileShard.getShardIndex()] = 1;
        }
        List<Integer> missing = new ArrayList<>();
        int i = 0;
        for (Integer v : partyArray) {
            if (v == 0) {
                missing.add(i);
            }
            i++;
        }
        res.put("exist", 2);
        res.put("missing", missing);
        return res;
    }

    @Override
    public void merge(String fileKey, Integer size) {
        // 获取最早上传的时间
        FileShard pioneer = fileShardMapper.selectPioneerByFileKey(fileKey);
        Date startTime = pioneer.getCreatedAt();
        String shardName = pioneer.getName();
        String[] nameArray = shardName.split("\\.");
        String fileName = nameArray[0] + "." + nameArray[1];
        File newFile = new File(BASE_PATH + fileName);
        // 总切片数
        Integer shardTotal = pioneer.getShardTotal();
        // 持久化
        FileInfo fileInfo = new FileInfo();
        fileInfo.setShardPrefix(nameArray[0]);
        // 文件内容md5值
        fileInfo.setContentKey("待处理");
        fileInfo.setStartAt(startTime);
        fileInfo.setFinishAt(new Date());
        fileInfo.setName(fileName);
        fileInfo.setPath(BASE_PATH + fileKey);
        fileInfo.setSize(size);
        fileInfo.setSuffix(nameArray[1]);
        fileInfoMapper.insertSelective(fileInfo);
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        byte[] byt = new byte[10 * 1024 * 1024];
        int len;
        try {
            outputStream = new FileOutputStream(newFile, true);
            for (int i = 0; i < shardTotal; i++) {
                inputStream = new FileInputStream(new File(BASE_PATH + fileName + "." + i));
                while ((len = inputStream.read(byt)) != -1) {
                    outputStream.write(byt, 0, len);
                }
            }
        } catch (IOException e) {
            log.error("合并文件异常", e);
            throw new RuntimeException("合并文件异常");
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("流关闭异常", e);
            }
        }
    }
}
