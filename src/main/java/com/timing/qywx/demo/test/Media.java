package com.timing.qywx.demo.test;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author liaoyz
 * @version v1.0
 * @description: 类说明 （必填）
 * @date 2021/04/14 13:44
 * @change 2021/04/14 13:44 liaoyz@v1.0 创建
 */
@RestController
@RequestMapping("/e-wx/media")
@Slf4j
public class Media {
    @Resource
    private QyWxAuthorization qyWxAuthorization;

    @PostMapping("/upload")
    public JSONObject upload(String corpId, String type, MultipartFile file) {
        // 获取企业微信凭证
        String corpAccessToken = qyWxAuthorization.getCorpAccessToken(corpId);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/media/upload"
                + "?access_token=" + corpAccessToken
                + "&type=" + type;
        String resStr = null;
        try {
            resStr = uploadFile(url, file);
        } catch (Exception e) {
            log.error("上传失败", e);
        }
        JSONObject res = JSONObject.parseObject(resStr);
        log.info("上传素材：{}", resStr);
        if (res != null && res.containsKey("errcode") && res.getInteger("errcode") != 0) {
            throw new RuntimeException();
        }
        return res;
    }

    public String uploadFile(String requestUrl, MultipartFile multipartFile) throws Exception {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
        URL url = new URL(requestUrl);
        String result;
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        // post方式不能使用缓存
        con.setUseCaches(false);
        // 以Post方式提交表单，默认get方式
        con.setRequestMethod("POST");
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String boundary = "-----------------------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", " multipart/form-data; boundary="
                + boundary);
        // 请求正文信息
        // 第一部分：
        // 必须多两道线
        String sb = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"media\";filename=\"" + file.getName() + "\"; filelength=" + file.length() + "\r\n" +
                "Content-Type: application/octet-stream\r\n\r\n";
        byte[] head = sb.getBytes(StandardCharsets.UTF_8);
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件以流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();
        // 结尾部分
        // 定义最后数据分隔线
        byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8);
        out.write(foot);
        out.flush();
        out.close();
        //5.将微信服务器返回的输入流转换成字符串
        InputStream inputStream = con.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder buffer = new StringBuilder();
        try {
            // 定义BufferedReader输入流来读取URL的响应
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
            throw new IOException("数据读取异常");
        }
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        con.disconnect();
        return result;
    }
}
