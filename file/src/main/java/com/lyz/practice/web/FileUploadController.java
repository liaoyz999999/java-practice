package com.lyz.practice.web;

import com.alibaba.fastjson.JSONObject;
import com.lyz.practice.common.Result;
import com.lyz.practice.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author CodeGenerator
 * @date 2021/06/16 14:19
 */
@RestController
@RequestMapping("/file/upload")
@Slf4j
public class FileUploadController {
    @Resource
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    public Result<Object> upload(MultipartFile file,
                                 String suffix,
                                 Integer shardIndex,
                                 Integer shardSize,
                                 Integer shardTotal,
                                 String fileKey) {
        log.info("\n----------------接收到文件---------------\n文件key：" + fileKey + "\n下标：" + shardIndex +
                " / " + shardTotal);
        fileUploadService.upload(file, suffix, shardIndex, shardSize, shardTotal, fileKey);
        return new Result<>();
    }

    @GetMapping("/check")
    public Result<JSONObject> check(String fileKey) {
        JSONObject res = fileUploadService.check(fileKey);
        return new Result<JSONObject>().setData(res);
    }

    @PostMapping("/merge")
    public Result<Object> merge(String fileKey, Integer size) {
        fileUploadService.merge(fileKey, size);
        return new Result<>();
    }
}
