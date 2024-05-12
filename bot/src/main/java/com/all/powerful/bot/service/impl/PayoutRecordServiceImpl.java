package com.all.powerful.bot.service.impl;

import com.all.powerful.bot.MultiBotManager;
import com.all.powerful.bot.domain.BotList;
import com.all.powerful.bot.domain.PayoutRecord;
import com.all.powerful.bot.mapper.PayoutRecordMapper;
import com.all.powerful.bot.service.IBotListService;
import com.all.powerful.bot.service.IPayoutRecordService;
import com.all.powerful.bot.service.ITgUserService;
import com.all.powerful.common.core.text.Convert;
import com.all.powerful.common.utils.DateUtils;
import com.all.powerful.common.utils.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 下发记录Service业务层处理
 *
 * @author all powerful
 * @date 2023-11-25
 */
@Service
public class PayoutRecordServiceImpl implements IPayoutRecordService {
    private static final Logger log = LoggerFactory.getLogger(PayoutRecordServiceImpl.class);
    @Autowired
    private PayoutRecordMapper payoutRecordMapper;

    @Autowired
    private IBotListService botListService;

    @Autowired
    private MultiBotManager multiBotManager;

    @Autowired
    private ITgUserService tgUserService;

    /**
     * 查询下发记录
     *
     * @param id 下发记录主键
     * @return 下发记录
     */
    @Override
    public PayoutRecord selectPayoutRecordById(Long id) {
        return payoutRecordMapper.selectPayoutRecordById(id);
    }

    /**
     * 查询下发记录列表
     *
     * @param payoutRecord 下发记录
     * @return 下发记录
     */
    @Override
    public List<PayoutRecord> selectPayoutRecordList(PayoutRecord payoutRecord) {
        return payoutRecordMapper.selectPayoutRecordList(payoutRecord);
    }

    @Override
    public List<PayoutRecord> selectPayoutRecordListForUser(PayoutRecord payoutRecord) {
        return payoutRecordMapper.selectPayoutRecordListForUser(payoutRecord);
    }

    /**
     * 新增下发记录
     *
     * @param payoutRecord 下发记录
     * @return 结果
     */
    @Override
    public int insertPayoutRecord(PayoutRecord payoutRecord) {
        payoutRecord.setCreateTime(DateUtils.getNowDate());
        return payoutRecordMapper.insertPayoutRecord(payoutRecord);
    }

    /**
     * 修改下发记录
     *
     * @param payoutRecord 下发记录
     * @return 结果
     */
    @Override
    public int updatePayoutRecord(PayoutRecord payoutRecord) {
        payoutRecord.setUpdateTime(DateUtils.getNowDate());
        payoutRecord.setUpdateBy(ShiroUtils.getLoginName());
        PayoutRecord record = payoutRecordMapper.selectPayoutRecordById(payoutRecord.getId());
        if (record.getpStatus().equals("0")){
            payoutRecord.setpStatus("0");
            return payoutRecordMapper.updatePayoutRecord(payoutRecord);
        }
        if ("0".equals(payoutRecord.getpStatus())) {
            try {
                BotList botList = new BotList();
                botList.setStatus("0");
                List<BotList> botLists = botListService.selectBotListList(botList);
                if (botLists.size() > 0) {
                    BotList bot = botLists.get(0);
                    TelegramLongPollingBot tgBot = multiBotManager.getBot(bot.getBotToken());
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(record.getUserId());
//                            "<pre>订单号：" + record.getOrderId() + "</pre>\n" +
//                            "<pre>请求支付金额：" + record.getAmount() + "</pre>\n" +
//                            "<pre>实际支付金额：" + record.getActualAmount() + "</pre>\n" +
//                            "<pre>实际下发金额：" + record.getPayAmount() + "</pre>\n" +
//                            "<pre>钱包地址：" + record.getAddress() + "</pre>\n" +
//                            "<pre>订单创建时间：" + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", record.getCreateTime()) + "</pre>\n" +
//                            "<pre>下发成功时间：" + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", record.getUpdateTime()) + "</pre>"
                    String sb = "<b>\uD83D\uDCE3您有新的【下发成功】请查看我的订单</b>";
                    sendMessage.setText(sb);
                    sendMessage.setParseMode(ParseMode.HTML);
                    tgBot.execute(sendMessage);
                    updateUserPayoutAmount(record.getUserId(), record.getPayAmount());
                }
            } catch (Exception e) {
                log.error("通知失败,{}", payoutRecord);
            }
        }
        return payoutRecordMapper.updatePayoutRecord(payoutRecord);
    }

    private void updateUserPayoutAmount(String userId, BigDecimal amount) {
        tgUserService.updateUserPayoutAmount(userId, amount);
    }

    /**
     * 批量删除下发记录
     *
     * @param ids 需要删除的下发记录主键
     * @return 结果
     */
    @Override
    public int deletePayoutRecordByIds(String ids) {
        return payoutRecordMapper.deletePayoutRecordByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除下发记录信息
     *
     * @param id 下发记录主键
     * @return 结果
     */
    @Override
    public int deletePayoutRecordById(Long id) {
        return payoutRecordMapper.deletePayoutRecordById(id);
    }

    @Override
    public int updatePayoutRecordByOrderId(PayoutRecord payoutRecord) {
        return payoutRecordMapper.updatePayoutRecordByOrderId(payoutRecord);
    }

    @Override
    public int checkLimit(String address, Date resultDate) {
        return payoutRecordMapper.checkLimit(address, resultDate);
    }
}
