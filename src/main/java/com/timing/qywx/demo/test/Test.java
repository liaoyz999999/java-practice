package com.timing.qywx.demo.test;

import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import weixin.popular.util.XMLConverUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author liaoyz
 * @version v1.0
 * @description: 类说明 （必填）
 * @date 2021/04/07 13:45
 * @change 2021/04/07 13:45 liaoyz@v1.0 创建
 */
@RestController
@RequestMapping("/e-wx")
@Slf4j
public class Test {
    public static final String SUITE_ID = "ww9662dceeb03bddf9";
    public static final String CORP_ID = "ww315000abc03da72e";
    public static final String SUITE_SECRET = "maXNkjGMz-OTTS3Mud9w9lBLS8XJmYn0xrZuyuOK3cQ";
    public static final String TOKEN = "4g8HzA72CmcJrkkaVbuxPfx0wZfint7c";
    public static final String ENCODING_AES_KEY = "6YWxxyu9ZAnY4t5UWN38YTeCWEN5t9BzAlECPPz7sYw";

    @GetMapping("/data/callback")
    public void dataCallbackGet(@RequestParam(name = "msg_signature") final String msgSignature,
                                @RequestParam(name = "timestamp") final String timestamp,
                                @RequestParam(name = "nonce") final String nonce,
                                @RequestParam(name = "echostr") final String echostr,
                                final HttpServletResponse response) {
        try {
            //企业回调的url-----该url不做任何的业务逻辑，仅仅微信查看是否可以调通.
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(TOKEN, ENCODING_AES_KEY, CORP_ID);
            // 随机字符串
            String sEchoStr = wxcpt.verifyUrl(msgSignature, timestamp, nonce, echostr);
            PrintWriter out = response.getWriter();
            //必须要返回解密之后的明文
            if (sEchoStr == null || "".equals(sEchoStr.trim())) {
                System.out.println("URL验证失败");
            } else {
                System.out.println("验证成功!");
            }
            System.out.println(sEchoStr);
            out.write(sEchoStr);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/notice/callback")
    public void noticeCallbackGet(@RequestParam(name = "msg_signature") final String msgSignature,
                                  @RequestParam(name = "timestamp") final String timestamp,
                                  @RequestParam(name = "nonce") final String nonce,
                                  @RequestParam(name = "echostr") final String echostr,
                                  final HttpServletResponse response) {
        PrintWriter out = null;
        try {
            //企业回调的url-----该url不做任何的业务逻辑，仅仅微信查看是否可以调通.
            WXBizMsgCrypt wxCpt = new WXBizMsgCrypt(TOKEN, ENCODING_AES_KEY, CORP_ID);
            // 随机字符串
            String decryptEchoStr = wxCpt.verifyUrl(msgSignature, timestamp, nonce, echostr);
            out = response.getWriter();
            //必须要返回解密之后的明文
            if (decryptEchoStr == null || "".equals(decryptEchoStr.trim())) {
                log.error("URL验证失败!");
            } else {
                log.debug("验证成功!");
                out.write(decryptEchoStr);
                out.flush();
            }
        } catch (Exception e) {
            log.error("URL验证失败!");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/notice/callback")
    public String callback(final HttpServletRequest request,
                           @RequestParam(name = "msg_signature") final String msgSignature,
                           @RequestParam(name = "timestamp") final String timeStamp,
                           @RequestParam(name = "nonce") final String nonce) {

        try {
            InputStream inputStream = request.getInputStream();
            //加密的信息
            String postData = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            log.info("指令回调>>>>>>>>>>>>>>>");
            Map<String, String> dataMap = decrypt(msgSignature, timeStamp, nonce, postData);
            String infoType = dataMap.get("InfoType");
            if ("suite_ticket".equals(infoType)) {
                //然后去操作你的业务逻辑
                stringRedisTemplate.opsForValue().set("e-wx:suite_ticket:" + dataMap.get("SuiteId"), dataMap.get("SuiteTicket"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    private Map<String, String> decrypt(String msgSignature, String timeStamp, String nonce, String postData) throws AesException {
        Map<String, String> encryptData = XMLConverUtil.convertToMap(postData);
        String toUserName = encryptData.get("ToUserName");
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(TOKEN, ENCODING_AES_KEY, toUserName);
        log.info("msgSignature：{}", msgSignature);
        log.info("timeStamp：{}", timeStamp);
        log.info("nonce：{}", nonce);
        log.info("postData：{}", postData);
        //解密
        String msg = wxcpt.decryptMsg(msgSignature, timeStamp, nonce, postData);
        //将post数据转换为map
        Map<String, String> dataMap = XMLConverUtil.convertToMap(msg);
        log.info("企业微信回调信息：{}", JSONObject.toJSONString(dataMap));
        return dataMap;

    }

    @PostMapping("/data/callback")
    public String dataCallback(final HttpServletRequest request,
                               @RequestParam(name = "msg_signature") final String msgSignature,
                               @RequestParam(name = "timestamp") final String timeStamp,
                               @RequestParam(name = "nonce") final String nonce) {

        try {
            InputStream inputStream = request.getInputStream();
            //加密的信息
            String postData = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            log.info("数据回调>>>>>>>>>>>>>>>");
            Map<String, String> dataMap = decrypt(msgSignature, timeStamp, nonce, postData);
            //然后去操作你的业务逻辑
//            redisTemplate.opsForValue().set("e-wx:suite_ticket:" + dataMap.get("SuiteId"), dataMap.get("SuiteTicket"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
