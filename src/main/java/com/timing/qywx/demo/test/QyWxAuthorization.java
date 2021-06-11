package com.timing.qywx.demo.test;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

/**
 * @author liaoyz
 * @version v1.0
 * @description: 类说明 （必填）
 * @date 2021/04/07 18:08
 * @change 2021/04/07 18:08 liaoyz@v1.0 创建
 */
@RestController
@RequestMapping("/e-wx/auth")
@Slf4j
public class QyWxAuthorization {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/external-contact/get-groupmsg-task")
    public JSONObject externalContactGetMsgResult(String corpId, @RequestBody JSONObject data) {
        // 获取企业微信凭证
        String corpAccessToken = getCorpAccessToken(corpId);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get_groupmsg_task"
                + "?access_token=" + corpAccessToken;
        String resStr = HttpUtil.sendJsonPost(url, data);
        JSONObject res = JSONObject.parseObject(resStr);
        log.info("获取群发成员发送任务列表：{}", resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res;
    }

    @GetMapping("/external-contact/add_msg")
    public JSONObject externalContactAddMsg(String corpId, @RequestBody JSONObject data) {
        // 获取企业微信凭证
        String corpAccessToken = getCorpAccessToken(corpId);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/add_msg_template"
                + "?access_token=" + corpAccessToken;
        String resStr = HttpUtil.sendJsonPost(url, data);
        JSONObject res = JSONObject.parseObject(resStr);
        log.info("创建企业群发：{}", resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res;
    }

    @GetMapping("/external-contact/group-chat")
    public JSONObject externalContactGroupChat(String corpId, Long beginTime, Long endTime) {
        // 获取企业微信凭证
        String corpAccessToken = getCorpAccessToken(corpId);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/groupchat/statistic_group_by_day"
                + "?access_token=" + corpAccessToken;
        JSONObject param = new JSONObject(4);
        param.put("day_begin_time", beginTime / 1000);
        if (endTime != null) {
            param.put("day_end_time", endTime / 1000);
        }
        String resStr = HttpUtil.sendJsonPost(url, param);
        JSONObject res = JSONObject.parseObject(resStr);
        log.info("获取「群聊数据统计」数据：{}", resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res;
    }


    @GetMapping("/external-contact/info")
    public JSONObject externalContactInfo(String corpId, String externalUserId) {
        // 获取企业微信凭证
        String corpAccessToken = getCorpAccessToken(corpId);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/get"
                + "?access_token=" + corpAccessToken
                + "&external_userid=" + externalUserId;
        String resStr = HttpUtil.get(url, null);
        JSONObject res = JSONObject.parseObject(resStr);
        log.info("获取客户详情结果：{}", resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res;
    }

    @GetMapping("/external-contact/transitionId")
    public JSONObject externalContactTransitionId(String corpId, String unionId) {
        // 获取企业微信凭证
        String corpAccessToken = getCorpAccessToken(corpId);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/unionid_to_external_userid"
                + "?access_token=" + corpAccessToken;
        JSONObject param = new JSONObject(4);
        param.put("unionid", unionId);
        String resStr = HttpUtil.sendJsonPost(url, param);
        JSONObject res = JSONObject.parseObject(resStr);
        log.info("外部联系人unionid转换结果：{}", resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res;
    }


    @GetMapping("/external-contact/list")
    public JSONObject externalContactList(String corpId, String userId) {
        // 获取企业微信凭证
        String corpAccessToken = getCorpAccessToken(corpId);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/externalcontact/list"
                + "?access_token=" + corpAccessToken
                + "&userid=" + userId;
        String resStr = HttpUtil.get(url, null);
        JSONObject res = JSONObject.parseObject(resStr);
        log.info("外部联系人列表结果：{}", resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res;
    }

    @GetMapping("/user/simple-list")
    public JSONObject userSimpleList(String corpId, String departmentId) {
        // 获取企业微信凭证
        String corpAccessToken = getCorpAccessToken(corpId);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist"
                + "?access_token=" + corpAccessToken
                + "&department_id=" + departmentId
                + "&fetch_child=1";
        String resStr = HttpUtil.get(url, null);
        log.info("获取部门成员：" + resStr);
        JSONObject res = JSONObject.parseObject(resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res;
    }

    @GetMapping("/department/list")
    public JSONObject departmentList(String corpId) {
        // 获取企业微信凭证
        String corpAccessToken = getCorpAccessToken(corpId);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list" +
                "?access_token=" + corpAccessToken;
        String resStr = HttpUtil.get(url, null);
        JSONObject res = JSONObject.parseObject(resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res;
    }

    @GetMapping("/get-permanent-code")
    public JSONObject getPermanentCode(String authCode) {
        // 获取第三方凭证
        String suiteAccessToken = getSuiteAccessToken();
        String url = "https://qyapi.weixin.qq.com/cgi-bin/service/get_permanent_code" +
                "?suite_access_token=" + suiteAccessToken;
        JSONObject param = new JSONObject(4);
        param.put("auth_code", authCode);
        String resStr = HttpUtil.sendJsonPost(url, param);
        JSONObject res = JSONObject.parseObject(resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        String authCorpId = res.getJSONObject("auth_corp_info").getString("corpid");
        stringRedisTemplate.opsForValue().set("e-wx:authCorpId:" + authCorpId, resStr);

        String permanentCode = res.getString("permanent_code");
        String corpAccessToken = res.getString("access_token");
        Integer tokenExpiresIn = res.getInteger("expires_in");
        // 缓存accessToken和永久授权码
        stringRedisTemplate.opsForValue().set("e-wx:corpAccessToken:" + authCorpId, corpAccessToken,
                tokenExpiresIn - 600, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set("e-wx:corpPermanentCode:" + authCorpId, permanentCode);
        return res;
    }

    protected String getCorpAccessToken(String corpId) {
        String accessToken = stringRedisTemplate.opsForValue().get("e-wx:corpAccessToken:" + corpId);
        if (accessToken != null) {
            return accessToken;
        }
        // 永久授权码
        String permanentCode = stringRedisTemplate.opsForValue().get("e-wx:corpPermanentCode:" + corpId);
        if (permanentCode == null) {
            throw new RuntimeException("未获取到【企业微信】企业永久授权码：" + corpId);
        }
        // 获取第三方凭证
        String suiteAccessToken = getSuiteAccessToken();
        String url = "https://qyapi.weixin.qq.com/cgi-bin/service/get_corp_token" +
                "?suite_access_token=" + suiteAccessToken;
        JSONObject param = new JSONObject(4);
        param.put("auth_corpid", corpId);
        param.put("permanent_code", permanentCode);
        String resStr = HttpUtil.sendJsonPost(url, param);
        JSONObject res = JSONObject.parseObject(resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        accessToken = res.getString("access_token");
        Integer expiresIn = res.getInteger("expires_in");
        stringRedisTemplate.opsForValue().set("e-wx:corpAccessToken:" + corpId, accessToken,
                expiresIn - 600, TimeUnit.SECONDS);
        return accessToken;
    }

    @GetMapping("/get-url")
    public String getUrl() {
        // 获取第三方凭证
        String suiteAccessToken = getSuiteAccessToken();
        // 获取预授权码
        String preAuthCode = getPreAuthCode(suiteAccessToken);
        // 回调地址
        String redirectUri = "https://www.timingmar.com";
        // 拼接授权路径
        try {
            String resUrl = "https://open.work.weixin.qq.com/3rdapp/install" +
                    "?suite_id=" + Test.SUITE_ID +
                    "&pre_auth_code=" + preAuthCode +
                    "&redirect_uri=" + URLEncoder.encode(redirectUri, "utf-8") +
                    "&state=STATE";
            log.info("授权url：{}", resUrl);
            return resUrl;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    private String getPreAuthCode(String suiteAccessToken) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/service/get_pre_auth_code?suite_access_token=";
        url += suiteAccessToken;
        String resStr = HttpUtil.get(url, null);
        log.info("获取预授权码：{}", resStr);
        JSONObject res = JSONObject.parseObject(resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res.getString("pre_auth_code");
    }


    private String getSuiteAccessToken() {
        String suiteAccessToken = stringRedisTemplate.opsForValue().get("e-wx:suiteAccessToken:" + Test.SUITE_ID);
        if (suiteAccessToken != null) {
            return suiteAccessToken;
        }
        String url = "https://qyapi.weixin.qq.com/cgi-bin/service/get_suite_token";
        JSONObject param = new JSONObject(4);
        param.put("suite_id", Test.SUITE_ID);
        param.put("suite_secret", Test.SUITE_SECRET);
        String suiteTicket = stringRedisTemplate.opsForValue().get("e-wx:suite_ticket:" + Test.SUITE_ID);
        param.put("suite_ticket", suiteTicket);
        String resStr = HttpUtil.sendJsonPost(url, param);
        log.info("获取第三方应用凭证：{}", resStr);
        JSONObject res = JSONObject.parseObject(resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        suiteAccessToken = res.getString("suite_access_token");
        stringRedisTemplate.opsForValue().set("e-wx:suiteAccessToken:" + Test.SUITE_ID, suiteAccessToken, 110, TimeUnit.MINUTES);
        return suiteAccessToken;
    }
}
