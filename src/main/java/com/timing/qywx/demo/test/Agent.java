package com.timing.qywx.demo.test;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liaoyz
 * @version v1.0
 * @description: 类说明 （必填）
 * @date 2021/04/19 16:15
 * @change 2021/04/19 16:15 liaoyz@v1.0 创建
 */
@RestController
@RequestMapping("/e-wx/agent")
@Slf4j
public class Agent {
    @Resource
    private QyWxAuthorization qyWxAuthorization;

    @GetMapping("/list")
    public JSONObject list(String corpId) {
        // 获取企业微信凭证
        String corpAccessToken = qyWxAuthorization.getCorpAccessToken(corpId);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/agent/list"
                + "?access_token=" + corpAccessToken;
        String resStr = HttpUtil.get(url, null);
        JSONObject res = JSONObject.parseObject(resStr);
        log.info("获取应用列表：{}", resStr);
        if (res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res;
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String INFO = "{\n" +
            "   \"errcode\": 0,\n" +
            "   \"errmsg\": \"ok\",\n" +
            "   \"external_contact\":\n" +
            "   {\n" +
            "        \"external_userid\":\"woAJ2GCAAAXtWyujaWJHDDGi0mACHAAA\",\n" +
            "        \"name\":\"李四\",\n" +
            "        \"position\":\"Manager\",\n" +
            "        \"avatar\":\"http://p.qlogo.cn/bizmail/IcsdgagqefergqerhewSdage/0\",\n" +
            "        \"corp_name\":\"腾讯\",\n" +
            "        \"corp_full_name\":\"腾讯科技有限公司\",\n" +
            "        \"type\":2,\n" +
            "        \"gender\":1,\n" +
            "        \"unionid\":\"ozynqsulJFCZ2z1aYeS8h-nuasdAAA\",\n" +
            "        \"external_profile\":\n" +
            "        {\n" +
            "             \"external_attr\":\n" +
            "              [\n" +
            "                {\n" +
            "                  \"type\":0,\n" +
            "                  \"name\":\"文本名称\",\n" +
            "                   \"text\":\n" +
            "                    {\n" +
            "                       \"value\":\"文本\"\n" +
            "                    }\n" +
            "                },\n" +
            "                {\n" +
            "                  \"type\":1,\n" +
            "                  \"name\":\"网页名称\",\n" +
            "                  \"web\":\n" +
            "                  {\n" +
            "                      \"url\":\"http://www.test.com\",\n" +
            "                      \"title\":\"标题\"\n" +
            "                  }\n" +
            "                },\n" +
            "                {\n" +
            "                  \"type\":2,\n" +
            "                  \"name\":\"测试app\",\n" +
            "                  \"miniprogram\":\n" +
            "                  {\n" +
            "                      \"appid\": \"wx8bd80126147df384\",\n" +
            "                      \"pagepath\": \"/index\",\n" +
            "                      \"title\": \"my miniprogram\"\n" +
            "                  }\n" +
            "                }\n" +
            "              ]\n" +
            "      }\n" +
            "     },\n" +
            "     \"follow_user\":\n" +
            "      [\n" +
            "        {\n" +
            "          \"userid\":\"rocky\",\n" +
            "          \"remark\":\"李部长\",\n" +
            "          \"description\":\"对接采购事务\",\n" +
            "          \"createtime\":1525779812,\n" +
            "          \"tags\":\n" +
            "           [\n" +
            "               {\n" +
            "                  \"group_name\":\"标签分组名称\",\n" +
            "                  \"tag_name\":\"标签名称\",\n" +
            "                  \"tag_id\":\"etAJ2GCAAAXtWyujaWJHDDGi0mACHAAA\",\n" +
            "                  \"type\":1\n" +
            "               }\n" +
            "           ],\n" +
            "           \"remark_corp_name\":\"腾讯科技\",\n" +
            "           \"remark_mobiles\":\n" +
            "            [\n" +
            "              \"13800000001\",\n" +
            "              \"13000000002\"\n" +
            "            ],\n" +
            "           \"oper_userid\":\"rocky\",\n" +
            "           \"add_way\":1\n" +
            "        },\n" +
            "        {\n" +
            "          \"userid\":\"tommy\",\n" +
            "          \"remark\":\"李总\",\n" +
            "          \"description\":\"采购问题咨询\",\n" +
            "          \"createtime\":1525881637,\n" +
            "          \"state\":\"外联二维码1\",\n" +
            "          \"oper_userid\":\"woAJ2GCAAAXtWyujaWJHDDGi0mACHAAA\",\n" +
            "           \"add_way\":3\n" +
            "         }\n" +
            "     ],\n" +
            "     \"next_cursor\":\"NEXT_CUROSR\"\n" +
            "}";

    @GetMapping("/test")
    public JSONObject test() {
        String key = "test:";
        long setStartTime = System.currentTimeMillis();
        for (int i = 0; i < 400; i++) {
            stringRedisTemplate.opsForValue().set(key + i, INFO);
        }
        long setTime = System.currentTimeMillis() - setStartTime;
        JSONObject res = new JSONObject(4);
        res.put("setTime", setTime);
        long getStartTime = System.currentTimeMillis();
        for (int i = 0; i < 400; i++) {
            String info = stringRedisTemplate.opsForValue().get(key + i);
        }
        long getTime = System.currentTimeMillis() - getStartTime;
        res.put("getTime", getTime);
        return res;
    }
}
