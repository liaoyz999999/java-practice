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
@RequestMapping("/e-wx/user")
@Slf4j
public class User {
    @Resource
    private QyWxAuthorization qyWxAuthorization;

    @GetMapping("/get")
    public JSONObject get(String corpId, String userId) {
        // 获取企业微信凭证
        String corpAccessToken = qyWxAuthorization.getCorpAccessToken(corpId);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get"
                + "?access_token=" + corpAccessToken
                + "&userid=" + userId;
        String resStr = HttpUtil.get(url,null);
        JSONObject res = JSONObject.parseObject(resStr);
        log.info("读取成员：{}", resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res;
    }
}
