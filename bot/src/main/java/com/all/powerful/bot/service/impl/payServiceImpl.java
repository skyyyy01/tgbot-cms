package com.all.powerful.bot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.all.powerful.bot.domain.PayResponse;
import com.all.powerful.bot.service.IPayService;
import com.all.powerful.common.utils.StringUtils;
import com.all.powerful.common.utils.spring.SpringUtils;
import com.all.powerful.system.service.ISysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author all powerful
 * @date 2023-11-19
 */
@Service
public class payServiceImpl implements IPayService {
    private static final Logger log = LoggerFactory.getLogger(payServiceImpl.class);

    @Override
    public PayResponse createTransaction(String orderId, BigDecimal amount) {
        try {
            String url = SpringUtils.getBean(ISysConfigService.class).selectConfigByKey("create.order.api");
            String notifyUrl = SpringUtils.getBean(ISysConfigService.class).selectConfigByKey("notify.url");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("order_id", orderId);
            jsonObject.put("amount", amount);
            jsonObject.put("notify_url", notifyUrl);
            jsonObject.put("redirect_url", notifyUrl);
            jsonObject.put("signature", getSignature(jsonObject));
            // 发送请求并获取响应
            log.info("请求地址：{}, 参数：{}", url, jsonObject.toJSONString());
            String response = sendJsonRequest(url, jsonObject.toJSONString());
            log.info("请求地址：{}, 响应：{}", url, response);
            if (StringUtils.isNotBlank(response)) {
                PayResponse payResponse = new PayResponse();
                JSONObject result = JSON.parseObject(response);
                if (result.getInteger("status_code") == 200) {
                    JSONObject data = result.getJSONObject("data");
                    payResponse.order_id = data.getString("order_id");
                    payResponse.token = data.getString("token");
                    payResponse.expiration_time = data.getLong("expiration_time");
                    payResponse.amount = data.getBigDecimal("amount");
                    payResponse.actual_amount = data.getBigDecimal("actual_amount");
                    return payResponse;
                }
            }
            return null;
        } catch (Exception e) {
            log.error("创建支付请求失败,orderId:{},amount:{}", orderId, amount, e);
        }
        return null;
    }

    private String getSignature(JSONObject jsonObject) {
        String key = SpringUtils.getBean(ISysConfigService.class).selectConfigByKey("api.auth.token");
        String signStr = jsonObject.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().toString().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        log.info("加密前参数：{}", signStr + key);
        String signature = md5Hash(signStr + key);
        log.info("加密后参数：{}", signature);
        return signature;
    }

    // 对字符串进行MD5加密
    private static String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array to a string representation
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 发送 JSON 请求并返回响应
     *
     * @param urlString   请求的 URL
     * @param jsonPayload JSON 数据
     * @return 服务器的响应
     * @throws Exception 如果发生异常
     */
    public String sendJsonRequest(String urlString, String jsonPayload) throws Exception {
        // 打开连接
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 设置请求方法为 POST
        connection.setRequestMethod("POST");

        // 启用输入输出，因为我们将在请求体中发送数据
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 设置请求头，通知服务器我们将发送 JSON 数据
        connection.setRequestProperty("Content-Type", "application/json");

        // 将 JSON 数据写入请求体
        try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
            out.writeBytes(jsonPayload);
            out.flush();
        }

        // 获取响应代码
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // 读取响应内容
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        // 关闭连接
        connection.disconnect();

        // 返回响应内容
        return response.toString();
    }

    public static void main(String[] args) {
//        String s = "amount=10&notify_url=http://localhost:8088/index&order_id=202311260350220000&redirect_url=http://localhost:8088/index";
        String s = "amount=42&notify_url=http://example.com/notify&order_id=20220201030210321&redirect_url=http://example.com/redirectYOUR_API_AUTH_TOKEN";

        System.out.println(md5Hash(s));
    }
}
