package com.all.powerful.bot.schedule;


import com.all.powerful.bot.MultiBotManager;
import com.all.powerful.bot.domain.BotList;
import com.all.powerful.bot.domain.PaymentRecord;
import com.all.powerful.bot.service.IBotListService;
import com.all.powerful.bot.service.IPaymentRecordService;
import com.all.powerful.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Date;
import java.util.List;

@Component
public class OrderSchedule {
    private static final Logger log = LoggerFactory.getLogger(OrderSchedule.class);
    @Autowired
    private IPaymentRecordService paymentRecordService;
    @Autowired
    private IBotListService botListService;
    @Autowired
    private MultiBotManager multiBotManager;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkOrder() {
        try {
            PaymentRecord paymentRecord = new PaymentRecord();
            paymentRecord.setStatus("1");
            List<PaymentRecord> records = paymentRecordService.selectPaymentRecordList(paymentRecord);
            Date now = DateUtils.getNowDate();
            log.info("定时更新订单 -> {}", records);
            for (PaymentRecord record : records) {
                try {
                    Date expirationTime = record.getExpirationTime();
                    if (expirationTime == null) {
                        expirationTime = new Date(record.getCreateTime().getTime() + (60 * 10 * 1000));
                    }
                    if (now.compareTo(expirationTime) >= 0) {
                        record.setStatus("2");
                        paymentRecordService.updatePaymentRecord(record);
                        BotList botList = new BotList();
                        botList.setStatus("0");
                        List<BotList> botLists = botListService.selectBotListList(botList);
                        if (botLists.size() > 0) {
                            BotList bot = botLists.get(0);
                            TelegramLongPollingBot tgBot = multiBotManager.getBot(bot.getBotToken());
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(record.getUserId());
                            String sb = "<b>\uD83D\uDCE3您的订单支付超时，请重新发起支付！</b>";
//                                    "<pre>订单号：" + record.getOrderId() + "</pre>\n" +
//                                    "<pre>请求支付金额：" + record.getAmount() + "</pre>\n" +
//                                    "<pre>实际支付金额：" + record.getActualAmount() + "</pre>\n" +
//                                    "<pre>钱包地址：" + record.getAddress() + "</pre>\n" +
//                                    "<pre>订单创建时间：" + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", record.getCreateTime()) + "</pre>\n" +
//                                    "<pre>订单超时时间：" + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", record.getExpirationTime()) + "</pre>";
                            sendMessage.setText(sb);
                            sendMessage.setParseMode(ParseMode.HTML);
                            tgBot.execute(sendMessage);
                        }
                    }
                } catch (Exception e) {
                    log.error("定时更新异常 -> {}", record, e);
                }
            }
        } catch (Exception e) {
            log.error("定时更新异常", e);
        }
    }


}
