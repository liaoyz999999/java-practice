package com.timing.qywx.demo.test;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liaoyz
 * @version v1.0
 * @description: 类说明 （必填）
 * @date 2021/04/14 13:44
 * @change 2021/04/14 13:44 liaoyz@v1.0 创建
 */
@RestController
@RequestMapping("/e-wx/group-chat")
@Slf4j
public class GroupChat {
    @Resource
    private QyWxAuthorization qyWxAuthorization;

    @GetMapping("/list")
    public JSONObject list(String corpId, @RequestBody JSONObject data) {
        // 获取企业微信凭证
        String corpAccessToken = qyWxAuthorization.getCorpAccessToken(corpId);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/list"
                + "?access_token=" + corpAccessToken;
        String resStr = HttpUtil.sendJsonPost(url, data);
        JSONObject res = JSONObject.parseObject(resStr);
        log.info("获取客户群列表：{}", resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res;
    }

    @GetMapping("/get")
    public JSONObject get(String corpId, @RequestBody JSONObject data) {
        // 获取企业微信凭证
        String corpAccessToken = qyWxAuthorization.getCorpAccessToken(corpId);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/get"
                + "?access_token=" + corpAccessToken;
        String resStr = HttpUtil.sendJsonPost(url, data);
        JSONObject res = JSONObject.parseObject(resStr);
        log.info("获取客户群详情：{}", resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res;
    }
}
