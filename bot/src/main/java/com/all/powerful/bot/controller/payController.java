package com.all.powerful.bot.controller;

import com.alibaba.fastjson.JSONObject;
import com.all.powerful.bot.MultiBotManager;
import com.all.powerful.bot.domain.BotList;
import com.all.powerful.bot.domain.PayCallback;
import com.all.powerful.bot.domain.PaymentRecord;
import com.all.powerful.bot.domain.TgUser;
import com.all.powerful.bot.service.IBotListService;
import com.all.powerful.bot.service.IPaymentRecordService;
import com.all.powerful.bot.service.ITgUserService;
import com.all.powerful.common.core.controller.BaseController;
import com.all.powerful.common.utils.DateUtils;
import com.all.powerful.common.utils.spring.SpringUtils;
import com.all.powerful.system.service.ISysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 客户列表Controller
 *
 * @author all powerful
 * @date 2023-11-22
 */
@Controller
@RequestMapping("/pay/api")
public class payController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(payController.class);

    @Autowired
    private IPaymentRecordService paymentRecordService;
    @Autowired
    private IBotListService botListService;
    @Autowired
    private MultiBotManager multiBotManager;
    @Autowired
    private ITgUserService tgUserService;

    @PostMapping("/callback")
    @ResponseBody
    public String callback(@RequestBody PayCallback payCallback) {
        try {
            log.info("收到回调信息：{}", payCallback);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("trade_id", payCallback.getTrade_id());
            jsonObject.put("order_id", payCallback.getOrder_id());
            jsonObject.put("amount", payCallback.getAmount());
            jsonObject.put("actual_amount", payCallback.getActual_amount());
            jsonObject.put("token", payCallback.getToken());
            jsonObject.put("block_transaction_id", payCallback.getBlock_transaction_id());
            jsonObject.put("status", payCallback.getStatus());
            String signature = getSignature(jsonObject);
            if (signature.equals(payCallback.getSignature())) {
                PaymentRecord paymentRecord = new PaymentRecord();
                paymentRecord.setOrderId(payCallback.getOrder_id());
                List<PaymentRecord> records = paymentRecordService.selectPaymentRecordList(paymentRecord);
                if (records.size() > 0) {
                    PaymentRecord record = records.get(0);
                    if (payCallback.getActual_amount().compareTo(record.getActualAmount()) == 0
                            && payCallback.getAmount().compareTo(record.getAmount()) == 0) {
                        if (payCallback.getStatus() == 2) {
                            record.setStatus("0");
                            record.setBlockId(paymentRecordService.selectBlockTransactionId(record.getOrderId()));
                        } else if (payCallback.getStatus() == 3) {
                            record.setStatus("2");
                            record.setBlockId(payCallback.getBlock_transaction_id());
                        } else {
                            return "wait";
                        }
                        paymentRecordService.updatePaymentRecord(record);
                        try {
                            BotList botList = new BotList();
                            botList.setStatus("0");
                            List<BotList> botLists = botListService.selectBotListList(botList);
                            if (botLists.size() > 0) {
                                BotList bot = botLists.get(0);
                                TelegramLongPollingBot tgBot = multiBotManager.getBot(bot.getBotToken());
                                SendMessage sendMessage = new SendMessage();
                                sendMessage.setChatId(record.getUserId());
                                String sb = "<b>\uD83D\uDCE3您有新的【支付成功】请查看我的订单</b>";
//                                "<pre>订单号：" + record.getOrderId() + "</pre>\n" +
//                                        "<pre>请求支付金额：" + record.getAmount() + "</pre>\n" +
//                                        "<pre>实际支付金额：" + record.getActualAmount() + "</pre>\n" +
//                                        "<pre>钱包地址：" + record.getAddress() + "</pre>\n" +
//                                        "<pre>订单创建时间：" + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", record.getCreateTime()) + "</pre>\n" +
//                                        "<pre>支付成功时间：" + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", record.getUpdateTime()) + "</pre>";
                                sendMessage.setText(sb);
                                sendMessage.setParseMode(ParseMode.HTML);
                                tgBot.execute(sendMessage);
                                notifyAdmin(record, tgBot);
                                updateUserPayCount(record.getUserId(), record.getActualAmount());
                            }
                        } catch (Exception e) {
                            log.error("通知失败,{}", payCallback);
                        }

                        return "ok";
                    } else {
                        log.error("金额不匹配,{}", payCallback);
                    }
                } else {
                    log.error("订单不存在,{}", payCallback);
                }
            } else {
                log.error("签名验证失败,{}", payCallback);
                return "签名验证失败";
            }
        } catch (Exception e) {
            log.error("回调处理失败,{}", payCallback, e);
        }
        return "error";
    }

    private void updateUserPayCount(String userId, BigDecimal payAmount) {
        tgUserService.updateUserPayCount(userId, payAmount);
    }

    @Async
    public void notifyAdmin(PaymentRecord record, TelegramLongPollingBot tgBot) throws TelegramApiException {
        TgUser tgUser = new TgUser();
        tgUser.setIsAdmin("Y");
        List<TgUser> tgUsers = tgUserService.selectTgUserList(tgUser);
        if (tgUsers.size() > 0) {
            for (TgUser user : tgUsers) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(user.getUserId());
                String sb = "<b>\uD83D\uDCE2\uD83D\uDCE2有新的交易支付成功！</b>";
//                        "<pre>订单号：" + record.getOrderId() + "</pre>\n" +
//                        "<pre>用户名：" + record.getUsername() + "</pre>\n" +
//                        "<pre>用户ID：" + record.getUserId() + "</pre>\n" +
//                        "<pre>请求支付金额：" + record.getAmount() + "</pre>\n" +
//                        "<pre>实际支付金额：" + record.getActualAmount() + "</pre>\n" +
//                        "<pre>钱包地址：" + record.getAddress() + "</pre>\n" +
//                        "<pre>订单创建时间：" + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", record.getCreateTime()) + "</pre>\n" +
//                        "<pre>支付成功时间：" + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", record.getUpdateTime()) + "</pre>";
                sendMessage.setText(sb);
                sendMessage.setParseMode(ParseMode.HTML);
                tgBot.execute(sendMessage);
            }
        }
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
}
