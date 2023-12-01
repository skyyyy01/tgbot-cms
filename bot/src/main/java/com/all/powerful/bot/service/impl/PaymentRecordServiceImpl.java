package com.all.powerful.bot.service.impl;

import com.all.powerful.bot.MultiBotManager;
import com.all.powerful.bot.domain.BotList;
import com.all.powerful.bot.domain.PaymentRecord;
import com.all.powerful.bot.domain.PayoutRecord;
import com.all.powerful.bot.domain.TgUser;
import com.all.powerful.bot.mapper.PaymentRecordMapper;
import com.all.powerful.bot.service.IBotListService;
import com.all.powerful.bot.service.IPaymentRecordService;
import com.all.powerful.bot.service.IPayoutRecordService;
import com.all.powerful.bot.service.ITgUserService;
import com.all.powerful.common.core.text.Convert;
import com.all.powerful.common.utils.DateUtils;
import com.all.powerful.common.utils.ShiroUtils;
import com.all.powerful.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * 支付记录Service业务层处理
 *
 * @author all powerful
 * @date 2023-11-25
 */
@Service
public class PaymentRecordServiceImpl implements IPaymentRecordService {

    private static final Logger log = LoggerFactory.getLogger(PaymentRecordServiceImpl.class);
    @Autowired
    private PaymentRecordMapper paymentRecordMapper;

    @Autowired
    private IPayoutRecordService payoutRecordService;

    @Autowired
    private ITgUserService tgUserService;

    @Autowired
    private IBotListService botListService;
    @Autowired
    private MultiBotManager multiBotManager;

    /**
     * 查询支付记录
     *
     * @param id 支付记录主键
     * @return 支付记录
     */
    @Override
    public PaymentRecord selectPaymentRecordById(Long id) {
        return paymentRecordMapper.selectPaymentRecordById(id);
    }

    /**
     * 查询支付记录列表
     *
     * @param paymentRecord 支付记录
     * @return 支付记录
     */
    @Override
    public List<PaymentRecord> selectPaymentRecordList(PaymentRecord paymentRecord) {
        return paymentRecordMapper.selectPaymentRecordList(paymentRecord);
    }

    /**
     * 新增支付记录
     *
     * @param paymentRecord 支付记录
     * @return 结果
     */
    @Override
    public int insertPaymentRecord(PaymentRecord paymentRecord) {
        paymentRecord.setCreateTime(DateUtils.getNowDate());
        return paymentRecordMapper.insertPaymentRecord(paymentRecord);
    }

    /**
     * 修改支付记录
     *
     * @param paymentRecord 支付记录
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePaymentRecord(PaymentRecord paymentRecord) {
        paymentRecord.setUpdateTime(DateUtils.getNowDate());
        PaymentRecord record = paymentRecordMapper.selectPaymentRecordById(paymentRecord.getId());
        if (record != null) {
            if (record.getStatus().equals("0")) {
                return 0;
            }
            PayoutRecord payoutRecord = new PayoutRecord();
            payoutRecord.setOrderId(record.getOrderId());
            payoutRecord.setStatus(paymentRecord.getStatus());
            if (paymentRecord.getStatus().equals("0")) {
                payoutRecord.setpStatus("2");
                payoutRecord.setHash(record.getBlockId());
                BotList botList = new BotList();
                botList.setStatus("0");
                List<BotList> botLists = botListService.selectBotListList(botList);
                if (botLists.size() > 0) {
                    BotList bot = botLists.get(0);
                    TelegramLongPollingBot tgBot = multiBotManager.getBot(bot.getBotToken());
                    try {
                        notifyAdmin(record, tgBot);
                    } catch (TelegramApiException e) {
                        log.error("通知异常 ->", e);
                    }
                }
                tgUserService.updateUserPayCount(record.getUserId(), record.getActualAmount());
            }
            payoutRecord.setUpdateTime(paymentRecord.getUpdateTime());
            payoutRecord.setUpdateBy(StringUtils.isBlank(paymentRecord.getUpdateBy()) ? "admin" : paymentRecord.getUpdateBy());
            payoutRecordService.updatePayoutRecordByOrderId(payoutRecord);
        }
        return paymentRecordMapper.updatePaymentRecord(paymentRecord);
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
//                        "<pre>支付成功时间：" + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", DateUtils.getNowDate()) + "</pre>";
                sendMessage.setText(sb);
                sendMessage.setParseMode(ParseMode.HTML);
                tgBot.execute(sendMessage);
            }
        }
    }

    /**
     * 批量删除支付记录
     *
     * @param ids 需要删除的支付记录主键
     * @return 结果
     */
    @Override
    public int deletePaymentRecordByIds(String ids) {
        return paymentRecordMapper.deletePaymentRecordByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付记录信息
     *
     * @param id 支付记录主键
     * @return 结果
     */
    @Override
    public int deletePaymentRecordById(Long id) {
        return paymentRecordMapper.deletePaymentRecordById(id);
    }

    @Override
    public String selectBlockTransactionId(String orderId) {
        return paymentRecordMapper.selectBlockTransactionId(orderId);
    }
}
