package com.all.powerful.bot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.all.powerful.bot.domain.*;
import com.all.powerful.bot.enums.ConfigType;
import com.all.powerful.bot.enums.MessageType;
import com.all.powerful.bot.enums.PayoutStatus;
import com.all.powerful.bot.enums.TriggerType;
import com.all.powerful.bot.service.*;
import com.all.powerful.common.constant.Constants;
import com.all.powerful.common.enums.UserStatus;
import com.all.powerful.common.utils.DateUtils;
import com.all.powerful.common.utils.StringUtils;
import com.all.powerful.common.utils.file.FileUploadUtils;
import com.all.powerful.common.utils.http.HttpUtils;
import com.all.powerful.common.utils.spring.SpringUtils;
import com.all.powerful.system.domain.SysConfig;
import com.all.powerful.system.service.ISysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicTelegramBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(DynamicTelegramBot.class);
    // Bot的用户名
    private String botUsername;

    // Bot的Token
    private String botToken;

    private static final String ONLINE_KEY = "online:record";

    private static final String DATE_FORMAT = "yyyyMMddHHmmss";

    private static final String INPUT_NUM = "请输入您要购买的数量(不支持小数位)";
    private static final String INPUT_ADDRESS = "请输入您的 USDT（TRC20）接收地址";
    private static final String ILLEGAL = "您的输入不合法,请重新输入";
    private static final AtomicInteger counter = new AtomicInteger(0);

    public DynamicTelegramBot(String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (ObjectUtils.isEmpty(update)) {
                return;
            }

            if (update.hasMessage() && update.getMessage().hasText()) {
                saveUser(update);
                String chatId = update.getMessage().getChatId().toString();
                checkUser(chatId);
                String messageText = update.getMessage().getText();

                String commandText = messageText.replace("@" + this.botUsername, "").trim();

                BotConfig botConfig = SpringUtils.getBean(IBotConfigService.class).selectBotConfigByShowName(commandText);
                if (botConfig == null) {
                    botConfig = SpringUtils.getBean(IBotConfigService.class).selectBotConfigByCallbackText(commandText);
                }
                if (botConfig != null) {
                    if (botConfig.getTriggerType().equals(TriggerType.CALLBACK_IMG.name())) {
                        sendPhotoMessage(chatId, botConfig);
                    } else if (botConfig.getTriggerType().equals(TriggerType.CALLBACK_VIDEO.name())) {
                        sendVideoMessage(chatId, botConfig);
                    }
                    return;
                }

                String amountSessionId = getAmountSessionId(chatId);
                if (amountSessionId != null) {
                    if (isValidAmount(messageText.trim())) {
                        String orderId = generateOrderNumber();
                        setOrderId(chatId, orderId);
                        setAmount(chatId + orderId, messageText.trim());
//                        deleteMessage(chatId, replyToMessage.getMessageId());
                        payConfirm(chatId);
                    } else {
                        updateAmountSessionExpiration(chatId);
//                        deleteMessage(chatId, replyToMessage.getMessageId());
                        sendTextMessage(chatId, ILLEGAL, null);
                    }
                    return;
                }

                String accountSessionId = getAccountSessionId(chatId);
                if (accountSessionId != null) {
                    if (isValidUSDTTRC20Address(messageText.trim())) {
                        setAccount(chatId + getOrderId(chatId), messageText.trim());
                        setReceiveAddress(chatId + getOrderId(chatId), messageText.trim());
                        deleteAccountSessionId(chatId);
//                        deleteMessage(chatId, replyToMessage.getMessageId());
                        //                    pay.check.status
                        if (checkLimit(chatId, messageText.trim())) {
                            payInfo(chatId);
                        } else {
                            deleteOrderId(chatId);
                        }
                    } else {
                        updateAccountSessionExpiration(chatId);
//                        deleteMessage(chatId, replyToMessage.getMessageId());
                        sendTextMessage(chatId, ILLEGAL, null);
                    }
                    return;
                }

                String valuationSessionId = getValuationSessionId(chatId);
                if (valuationSessionId != null) {
                    if (isValidAmount(messageText.trim())) {
                        deleteValuationSessionId(chatId);
                        setAmount(chatId, messageText.trim());
//                        deleteMessage(chatId, replyToMessage.getMessageId());
                        processValuation(update);
                    } else {
                        updateAmountSessionExpiration(chatId);
//                        deleteMessage(chatId, replyToMessage.getMessageId());
                        sendTextMessage(chatId, ILLEGAL, null);
                    }
                }
            } else if (update.hasMessage() && update.getMessage().hasEntities()) {
                String chatId = update.getMessage().getChatId().toString();
                checkUser(chatId);
                if (update.getMessage().getEntities().size() > 0) {
                    if (update.getMessage().getEntities().get(0).getType().equals("bot_command")) {//判断是否是命令
                        String commandText = update.getMessage().getText().toLowerCase().replace("@" + this.botUsername, "").trim();

                        BotConfig botConfig = SpringUtils.getBean(IBotConfigService.class).selectBotConfigByCallbackText(commandText);
                        if (botConfig != null) {
                            if (botConfig.getTriggerType().equals(TriggerType.CALLBACK_IMG.name())) {
                                sendPhotoMessage(chatId, botConfig);
                            } else if (botConfig.getTriggerType().equals(TriggerType.CALLBACK_VIDEO.name())) {
                                sendVideoMessage(chatId, botConfig);
                            }
                        }
                    }
                }
            } else if (update.hasCallbackQuery()) {
                log.info("update.getCallbackQuery() != null");
                String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
                checkUser(chatId);
                String commandText = update.getCallbackQuery().getData();
                if (commandText.equalsIgnoreCase(MessageType.VALUATION.getCode())) {
                    setValuationSessionId(chatId, generateWithdrawalSessionId());
                }
                if (commandText.equalsIgnoreCase(MessageType.BUY.getCode())) {
                    if (getOrderId(chatId) != null) {
                        List<InlineKeyboardButton> row = new ArrayList<>();
                        InlineKeyboardButton keyButton = new InlineKeyboardButton();
                        keyButton.setText("❌放弃订单");
                        keyButton.setCallbackData(MessageType.PAY_CANCEL.getCode());
                        row.add(keyButton);
                        List<List<InlineKeyboardButton>> backInlineKeyboardButtonList = new ArrayList<>();
                        backInlineKeyboardButtonList.add(row);
                        sendTextMessage(chatId, "您有一笔订单尚未完成", new InlineKeyboardMarkup(backInlineKeyboardButtonList));
                        return;
                    }
                    setAmountSessionId(chatId, generateWithdrawalSessionId());
                }
                if (commandText.equals(MessageType.PAY_CANCEL.getCode())) {
                    Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
                    deleteAmountSessionId(chatId);
                    deleteAccountSessionId(chatId);
                    deleteMessage(chatId, messageId);
                    cancelOrder(chatId);
                    deleteOrderId(chatId);
                }

                if (commandText.equalsIgnoreCase(MessageType.PAY_CONFIRM.getCode())) {
                    Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
                    setAccountSessionId(chatId, generateWithdrawalSessionId());
                    deleteAmountSessionId(chatId);
                    deleteMessage(chatId, messageId);
                    askAddress(chatId);
                    return;
                }

                BotConfig botConfig = SpringUtils.getBean(IBotConfigService.class).selectBotConfigByCallbackData(commandText);
                if (botConfig != null) {
                    if (botConfig.getTriggerType().equals(TriggerType.CALLBACK_IMG.name())) {
                        sendPhotoMessage(chatId, botConfig);
                    } else if (botConfig.getTriggerType().equals(TriggerType.CALLBACK_VIDEO.name())) {
                        sendVideoMessage(chatId, botConfig);
                    }
                    if (commandText.equalsIgnoreCase(MessageType.VALUATION.getCode())
                            || commandText.equalsIgnoreCase(MessageType.BUY.getCode())) {
                        sendTextMessage(chatId, INPUT_NUM, null);
                    }
                }
            } else {
                log.info("msg unknown:{}", update);
            }

        } catch (Exception e) {
            log.error("执行异常：", e);
        }
    }

    private boolean checkLimit(String chatId, String address) throws TelegramApiException {
        SysConfig config = SpringUtils.getBean(ISysConfigService.class).selectConfigByConfigKey("pay.check.status");
        if (config == null || config.getConfigValue().equals("1")) {
            return true;
        }
        // 获取当前时间
        Date currentDate = new Date();

        // 创建 Calendar 对象，并设置为当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // 减去24小时
        calendar.add(Calendar.HOUR_OF_DAY, -24);

        // 获取结果时间
        Date resultDate = calendar.getTime();
        int i = SpringUtils.getBean(IPayoutRecordService.class).checkLimit(address, resultDate);
        if (i > 0) {
            sendTextMessage(chatId, "接收地址：" + address + "\n同一接收地址，24小时内不能创建新的订单", null);
        }
        return i <= 0;
    }

    private void cancelOrder(String chatId) {
        String orderId = getOrderId(chatId);
        if (orderId != null) {
            PaymentRecord paymentRecord = new PaymentRecord();
            paymentRecord.setOrderId(orderId);
            List<PaymentRecord> records = SpringUtils.getBean(IPaymentRecordService.class).selectPaymentRecordList(paymentRecord);
            if (records.size() > 0) {
                PaymentRecord record = records.get(0);
                if (!record.getStatus().equals("0")){
                    record.setStatus("3");
                    SpringUtils.getBean(IPaymentRecordService.class).updatePaymentRecord(record);
                }
            }
        }
    }

    private void payConfirm(String chatId) throws TelegramApiException {
        String amount = getAmount(chatId + getOrderId(chatId));
        List<InlineKeyboardButton> row = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("购买").append(amount).append(" | ");
        BigDecimal value = new BigDecimal(amount);
        InlineKeyboardButton keyButton = new InlineKeyboardButton();
        keyButton.setText("\uD83D\uDEAB取消");
        keyButton.setCallbackData(MessageType.PAY_CANCEL.getCode());
        row.add(keyButton);
        BigDecimal rate = SpringUtils.getBean(IExchangeRateService.class).selectExchangeRate(value);
        if (rate != null) {
            setRate(chatId + getOrderId(chatId), rate.toPlainString());
            setReceive(chatId + getOrderId(chatId), value.multiply(rate).toPlainString());
            sb.append("汇率").append(rate.toPlainString()).append(" | ");
            sb.append("您收到").append(value.multiply(rate).toPlainString());
            InlineKeyboardButton keyButton2 = new InlineKeyboardButton();
            keyButton2.setText("✅确定");
            keyButton2.setCallbackData(MessageType.PAY_CONFIRM.getCode());
            row.add(keyButton2);
        } else {
            sb.append("未获取到对应数量的汇率\n").append("请重新输入购买数量");
            updateAmountSessionExpiration(chatId);
        }

        List<List<InlineKeyboardButton>> backInlineKeyboardButtonList = new ArrayList<>();
        backInlineKeyboardButtonList.add(row);
        sendTextMessage(chatId, sb.toString(), new InlineKeyboardMarkup(backInlineKeyboardButtonList));
    }

    private void askAddress(String chatId) throws TelegramApiException {
        sendTextMessage(chatId, INPUT_ADDRESS, null);
    }

    @Transactional
    public void payInfo(String chatId) throws TelegramApiException {
        try {
            String orderId = getOrderId(chatId);
            String amount = getAmount(chatId + getOrderId(chatId));
            PayResponse transaction = SpringUtils.getBean(IPayService.class).createTransaction(orderId, new BigDecimal(amount));
            if (transaction == null) {
                throw new Exception("请求创建订单失败");
            }
            setWalletAddress(chatId + getOrderId(chatId), transaction.token);
            setPayAmount(chatId + getOrderId(chatId), transaction.actual_amount.toPlainString());
            TgUser tgUser = SpringUtils.getBean(ITgUserService.class).selectTgUserByUserId(chatId);
            PaymentRecord paymentRecord = new PaymentRecord();
            paymentRecord.setOrderId(orderId);
            paymentRecord.setAddress(getReceiveAddress(chatId + orderId));
            paymentRecord.setUserId(tgUser.getUserId());
            paymentRecord.setUsername(tgUser.getUsername());
            paymentRecord.setNickname(tgUser.getNickname());
            paymentRecord.setAmount(new BigDecimal(amount));
            paymentRecord.setActualAmount(transaction.actual_amount);
            paymentRecord.setCreateBy(tgUser.getUsername());
            paymentRecord.setExpirationTime(new Date(transaction.expiration_time * 1000));
            SpringUtils.getBean(IPaymentRecordService.class).insertPaymentRecord(paymentRecord);

            PayoutRecord payoutRecord = new PayoutRecord();
            payoutRecord.setOrderId(orderId);
            payoutRecord.setAddress(getReceiveAddress(chatId + orderId));
            payoutRecord.setUserId(tgUser.getUserId());
            payoutRecord.setUsername(tgUser.getUsername());
            payoutRecord.setNickname(tgUser.getNickname());
            payoutRecord.setAmount(new BigDecimal(amount));
            payoutRecord.setActualAmount(transaction.actual_amount);
            payoutRecord.setPayAmount(new BigDecimal(getReceive(chatId + orderId)));
            payoutRecord.setCreateBy(tgUser.getUsername());
            SpringUtils.getBean(IPayoutRecordService.class).insertPayoutRecord(payoutRecord);
            BotConfig botConfig = SpringUtils.getBean(IBotConfigService.class).selectBotConfigByCallbackText(MessageType.PAY_INFO.getCode());

            if (botConfig != null) {
                if (botConfig.getTriggerType().equals(TriggerType.CALLBACK_IMG.name())) {
                    sendPhotoMessage(chatId, botConfig);
                } else if (botConfig.getTriggerType().equals(TriggerType.CALLBACK_VIDEO.name())) {
                    sendVideoMessage(chatId, botConfig);
                }
            }
        } catch (Exception e) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton keyButton = new InlineKeyboardButton();
            keyButton.setText("\uD83D\uDEAB点击取消");
            keyButton.setCallbackData(MessageType.PAY_CANCEL.getCode());
            row.add(keyButton);
            InlineKeyboardButton keyButton1 = new InlineKeyboardButton();
            keyButton1.setText("♻️重新创建");
            keyButton1.setCallbackData(MessageType.BUY.getCode());
            row.add(keyButton1);
            List<List<InlineKeyboardButton>> backInlineKeyboardButtonList = new ArrayList<>();
            backInlineKeyboardButtonList.add(row);
            sendTextMessage(chatId, "订单创建失败", new InlineKeyboardMarkup(backInlineKeyboardButtonList));
        }

    }

    public void processValuation(Update update) throws TelegramApiException {
        String userId = update.getMessage().getFrom().getId().toString();
        String amount = getAmount(userId);
        StringBuilder sb = new StringBuilder();
        sb.append("购买").append(amount).append("|");
        BigDecimal value = new BigDecimal(amount);
        BigDecimal rate = SpringUtils.getBean(IExchangeRateService.class).selectExchangeRate(value);
        if (rate != null) {
            sb.append("汇率").append(rate.toPlainString()).append("|");
            sb.append("您收到").append(value.multiply(rate).toPlainString());
        } else {
            sb.append("未获取到对应数量的汇率");
        }
        sendTextMessage(userId, sb.toString(), null);
    }

    private ReplyKeyboardMarkup getMenu() {
        BotConfig botConfig = new BotConfig();
        botConfig.setConfigType("F");
        botConfig.setVisible("0");
        List<BotConfig> configs = SpringUtils.getBean(IBotConfigService.class).selectBotConfigList(botConfig);
        if (configs.size() <= 0) {
            return null;
        }
        List<List<BotConfig>> groupedLists = split(configs, 3);
        ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboardMarkup(groupedLists);

        if (replyKeyboardMarkup != null) {
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(false);
            replyKeyboardMarkup.setInputFieldPlaceholder("请输入指令");
            return replyKeyboardMarkup;
        }
        return null;
    }

    private void deleteMessage(String chatId, Integer messageId) throws TelegramApiException {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        execute(deleteMessage);
    }

    private void sendTextMessage(String chatId, String text, ReplyKeyboard replyKeyboard) throws TelegramApiException {
        if (StringUtils.isNotBlank(text)) {
            text = replaceText(chatId, text);
        } else {
            text = "1";
        }
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode(ParseMode.MARKDOWN);
        if (replyKeyboard != null) {
            message.setReplyMarkup(replyKeyboard);
        } else {
            ReplyKeyboardMarkup replyKeyboardMarkup = getMenu();
            if (replyKeyboardMarkup != null) {
                message.setReplyMarkup(replyKeyboardMarkup);
            }
        }
        execute(message);
    }

    private void sendPhotoMessage(String chatId, BotConfig config) throws TelegramApiException {
        String text = "";
        String img = null;
        InlineKeyboardMarkup inlineKeyboardMarkup = null;
        if (config != null) {
            List<BotConfig> botConfigs = getChildConfigs(config.getConfigId(), ConfigType.T.name());
            if (botConfigs != null) {
                if (StringUtils.isNotBlank(botConfigs.get(0).getText())) {
                    text = botConfigs.get(0).getText();
                }
            }
            List<BotConfig> botConfigs2 = getChildConfigs(config.getConfigId(), ConfigType.I.name());
            if (botConfigs2 != null) {
                if (StringUtils.isNotBlank(botConfigs2.get(0).getImage())) {
                    img = botConfigs2.get(0).getImage();
                }
            }

            List<BotConfig> botConfigs3 = getChildConfigs(config.getConfigId(), ConfigType.B.name());
            if (botConfigs3 != null) {
                List<List<BotConfig>> split = split(botConfigs3, 2);
                inlineKeyboardMarkup = getInlineKeyboardButton(split);
            }
            text = replaceText(chatId, text);
        }
        if (img != null) {
            SendPhoto photo = new SendPhoto();
            photo.setChatId(chatId);
            File file = new File(img.replace(Constants.RESOURCE_PREFIX, FileUploadUtils.getDefaultBaseDir()));
            photo.setPhoto(new InputFile(file));
            photo.setCaption(text);
            photo.setParseMode(ParseMode.MARKDOWN);
            if (inlineKeyboardMarkup != null) {
                photo.setReplyMarkup(inlineKeyboardMarkup);
            } else {
                ReplyKeyboardMarkup replyKeyboardMarkup = getMenu();
                if (replyKeyboardMarkup != null) {
                    photo.setReplyMarkup(replyKeyboardMarkup);
                }
            }
            execute(photo);
        } else {
            sendTextMessage(chatId, text, inlineKeyboardMarkup);
        }
    }

    private void sendVideoMessage(String chatId, BotConfig config) throws TelegramApiException {
        String text = "";
        String video = null;
        InlineKeyboardMarkup inlineKeyboardMarkup = null;
        if (config != null) {
            List<BotConfig> botConfigs = getChildConfigs(config.getConfigId(), ConfigType.T.name());
            if (botConfigs != null) {
                if (StringUtils.isNotBlank(botConfigs.get(0).getText())) {
                    text = botConfigs.get(0).getText();
                }
            }
            List<BotConfig> botConfigs2 = getChildConfigs(config.getConfigId(), ConfigType.V.name());
            if (botConfigs2 != null) {
                if (StringUtils.isNotBlank(botConfigs2.get(0).getVideo())) {
                    video = botConfigs2.get(0).getVideo();
                }
            }

            List<BotConfig> botConfigs3 = getChildConfigs(config.getConfigId(), ConfigType.B.name());
            if (botConfigs3 != null) {
                List<List<BotConfig>> split = split(botConfigs3, 2);
                inlineKeyboardMarkup = getInlineKeyboardButton(split);
            }

            text = replaceText(chatId, text);
        }
        if (video != null) {
            SendVideo sendVideo = new SendVideo();
            sendVideo.setChatId(chatId);
            File file = new File(video.replace(Constants.RESOURCE_PREFIX, FileUploadUtils.getDefaultBaseDir()));
            sendVideo.setVideo(new InputFile(file));
            sendVideo.setCaption(text);
            if (inlineKeyboardMarkup != null) {
                sendVideo.setReplyMarkup(inlineKeyboardMarkup);
            } else {
                ReplyKeyboardMarkup replyKeyboardMarkup = getMenu();
                if (replyKeyboardMarkup != null) {
                    sendVideo.setReplyMarkup(replyKeyboardMarkup);
                }
            }
            execute(sendVideo);
        } else {
            sendTextMessage(chatId, text, inlineKeyboardMarkup);
        }
    }

    private String replaceText(String userId, String text) {
        String records = getOnlineRecords();
        TgUser tgUser = SpringUtils.getBean(ITgUserService.class).selectTgUserByUserId(userId);
        text = text.replace("{record}", records)
                .replace("{user_id}", tgUser != null ? (tgUser.getUserId() != null ? tgUser.getUserId() : "") : "")
                .replace("{username}", tgUser != null ? (tgUser.getUsername() != null ? tgUser.getUsername() : "") : "")
                .replace("{nickname}", tgUser != null ? (tgUser.getNickname() != null ? tgUser.getNickname() : "") : "")
                .replace("{register_time}", tgUser != null ? (tgUser.getCreateTime() != null ? DateUtils.parseDateToStr("yyyy-MM-dd", tgUser.getCreateTime()) : "") : "")
                .replace("{total_buy}", tgUser != null ? (tgUser.getPayAmount() != null ? tgUser.getPayAmount().toPlainString() : "") : "")
                .replace("{total_count}", tgUser != null ? (tgUser.getPayCount() != null ? tgUser.getPayCount().toString() : "") : "")
                .replace("{total_send}", tgUser != null ? (tgUser.getPayoutCount() != null ? tgUser.getPayoutCount().toString() : "") : "")
                .replace("{order}", getMyOrder(userId))
                .replace("{order_id}", getOrderId(userId) != null ? getOrderId(userId) : "")
                .replace("{amount}", getAmount(userId + getOrderId(userId)) != null ? getAmount(userId + getOrderId(userId)) : "")
                .replace("{rate}", getRate(userId + getOrderId(userId)) != null ? getRate(userId + getOrderId(userId)) : "")
                .replace("{receive}", getReceive(userId + getOrderId(userId)) != null ? getReceive(userId + getOrderId(userId)) : "")
                .replace("{receive_address}", getReceiveAddress(userId + getOrderId(userId)) != null ? getReceiveAddress(userId + getOrderId(userId)) : "")
                .replace("{wallet_address}", getWalletAddress(userId + getOrderId(userId)) != null ? getWalletAddress(userId + getOrderId(userId)) : "")
                .replace("{pay_amount}", getPayAmount(userId + getOrderId(userId)) != null ? getPayAmount(userId + getOrderId(userId)) : "");
        return text;
    }

    @Retryable(value = {Exception.class})
    private String getOnlineRecords() {
        boolean flag = true;
        StringBuilder sb = new StringBuilder();
        String onlineSessionId = getOnlineSessionId();
        if (onlineSessionId != null) {
            return onlineSessionId;
        }
        String url = SpringUtils.getBean(ISysConfigService.class).selectConfigByKey("online.api.url");
        if (StringUtils.isNotBlank(url)) {
            try {
                // 发送请求并获取响应
                String rspStr = HttpUtils.sendGet(url);
//                log.info("请求地址：{}, 响应code：{}", url, rspStr);
                // 处理响应
                if (StringUtils.isNotBlank(rspStr)) {
                    JSONObject jsonObject = JSON.parseObject(rspStr);
                    JSONArray transfers = jsonObject.getJSONArray("token_transfers");
                    SysConfig config = SpringUtils.getBean(ISysConfigService.class).selectConfigByConfigKey("online.record.num");
                    int num = 4;
                    if (config != null) {
                        num = Integer.parseInt(config.getConfigValue()) - 1;
                    }
                    for (int i = 0; i < transfers.size(); i++) {
                        JSONObject transfer = transfers.getJSONObject(i);
                        BigInteger quant = transfer.getBigInteger("quant");
                        BigInteger blockTs = transfer.getBigInteger("block_ts");
                        BigDecimal amount = new BigDecimal(quant)
                                .setScale(6, BigDecimal.ROUND_DOWN)
                                .divide(new BigDecimal(1000000), BigDecimal.ROUND_DOWN)
                                .setScale(2, BigDecimal.ROUND_DOWN);
                        Date date = new Date(blockTs.longValue());
                        String dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd | HH:mm:ss |", date);
//                        if (record.getpStatus().equals("0")) {
//                            sb.append("[\uD83D\uDCB0 ").append(record.getPayAmount().toPlainString()).append("]")
//                                    .append("(https://tronscan.org/#/transaction/").append(record.getHash()).append(")");
//                        }
                        sb.append("[").append(dateToStr).append(" ").append("\uD83D\uDCB0金额: ").append(amount.toPlainString()).append("]")
                                .append("(https://tronscan.org/#/transaction/").append(transfer.getString("transaction_id")).append(")").append("\n");
                        if (i >= num) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                flag = false;
                String text = SpringUtils.getBean(ISysConfigService.class).selectConfigByKey("online.record");
                sb.append(text);
                log.error("获取链上记录失败", e);
            }
        }
        if (flag) {
            SysConfig sysConfig = SpringUtils.getBean(ISysConfigService.class).selectConfigByConfigKey("online.record");
            if (sysConfig != null) {
                sysConfig.setConfigValue(sb.toString());
                SpringUtils.getBean(ISysConfigService.class).updateConfig(sysConfig);
            }
        }
        setOnlineSessionId(sb.toString());
        return sb.toString();
    }

    private String getMyOrder(String userId) {
        PayoutRecord payoutRecord = new PayoutRecord();
        payoutRecord.setUserId(userId);
        List<PayoutRecord> records = SpringUtils.getBean(IPayoutRecordService.class).selectPayoutRecordListForUser(payoutRecord);
        StringBuilder sb = new StringBuilder();
        if (records.size() > 0) {
            for (int i = 0; i < records.size(); i++) {
                PayoutRecord record = records.get(i);
                String dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd", record.getCreateTime());
                //[超链接](http://www.example.com/)
                sb.append(dateToStr).append(" | ")
                        .append(PayoutStatus.getInfoByCode(Integer.parseInt(record.getpStatus()))).append(" | ")
                        .append("\uD83D\uDCB0").append(record.getAmount().toPlainString()).append(" | ")
                        .append("\uD83D\uDCB8").append(record.getPayAmount().toPlainString()).append("\n");
                if (i >= 9) {
                    break;
                }
            }
        }
        return sb.toString();
    }

    private void setOnlineSessionId(String text) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        valueOperations.set(ONLINE_KEY, text, 2, TimeUnit.MINUTES);
    }

    private String getOnlineSessionId() {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        return valueOperations.get(ONLINE_KEY);
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(List<List<BotConfig>> split) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (List<BotConfig> configs : split) {
            KeyboardRow keyboardRow = new KeyboardRow(configs.size());
            for (BotConfig botConfig : configs) {
                // 创建一行按钮
                KeyboardButton button = new KeyboardButton(botConfig.getShowName());
                keyboardRow.add(button);
            }
            keyboardRows.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        if (keyboardRows.size() > 0) {
            return replyKeyboardMarkup;
        }
        return null;
    }

    private InlineKeyboardMarkup getInlineKeyboardButton(List<List<BotConfig>> split) {
        List<List<InlineKeyboardButton>> backInlineKeyboardButtonList = new ArrayList<>();
        for (List<BotConfig> configs : split) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (BotConfig botConfig : configs) {
                InlineKeyboardButton keyButton = new InlineKeyboardButton();
                keyButton.setText(botConfig.getShowName());
                if (botConfig.getTriggerType().equals(TriggerType.URL.name())) {
                    keyButton.setUrl(botConfig.getUrl());
                } else {
                    keyButton.setCallbackData(botConfig.getCallbackData());
                }
                row.add(keyButton);
            }
            backInlineKeyboardButtonList.add(row);
        }
        if (backInlineKeyboardButtonList.size() > 0) {
            return new InlineKeyboardMarkup(backInlineKeyboardButtonList);
        }
        return null;
    }

    private List<List<BotConfig>> split(List<BotConfig> configs, int num) {
        List<List<BotConfig>> groupedLists = new ArrayList<>();
        for (int i = 0; i < configs.size(); i += num) {
            int end = Math.min(i + num, configs.size());
            List<BotConfig> group = configs.subList(i, end);
            groupedLists.add(group);
        }
        return groupedLists;
    }

    private List<BotConfig> getChildConfigs(Long parentId, String configType) {
        BotConfig botConfig = new BotConfig();
        botConfig.setVisible("0");
        botConfig.setParentId(parentId);
        botConfig.setConfigType(configType);
        List<BotConfig> configs = SpringUtils.getBean(IBotConfigService.class).selectBotConfigList(botConfig);
        if (configs.size() > 0) {
            return configs;
        }
        return null;
    }

    public void saveUser(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        String userName = String.valueOf(update.getMessage().getFrom().getUserName());
        TgUser tgUser = SpringUtils.getBean(ITgUserService.class).selectTgUserByUserId(chatId);
        if (tgUser == null) {
            tgUser = new TgUser();
            tgUser.setUserId(chatId);
            tgUser.setUsername(userName);
            String firstName = update.getMessage().getFrom().getFirstName();
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(update.getMessage().getFrom().getLastName())) {
                firstName = firstName + " " + update.getMessage().getFrom().getLastName();
            }
            tgUser.setNickname(firstName);
            tgUser.setCreateTime(DateUtils.getNowDate());
            tgUser.setCreateBy(userName);
            SpringUtils.getBean(ITgUserService.class).insertTgUser(tgUser);
        }
    }

    private void checkUser(String userId) throws Exception {
        TgUser tgUser = SpringUtils.getBean(ITgUserService.class).selectTgUserByUserId(userId);
        if (tgUser != null) {
            if (UserStatus.DISABLE.getCode().equals(tgUser.getStatus())) {
                throw new Exception(tgUser.getUsername() + " -> 用户被禁用");
            }
        }
    }

    private boolean isValidUSDTTRC20Address(String address) {
        String regex = "^T[1-9A-HJ-NP-Za-km-z]{33}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);
        return matcher.matches();
    }

    private boolean isValidAmount(String amount) {
        String regex = "^[1-9]\\d*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(amount);
        return matcher.matches();
    }

    public static String generateOrderNumber() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String timestamp = dateFormat.format(new Date());

        int sequenceNumber = counter.getAndIncrement();
        String paddedSequenceNumber = String.format("%04d", sequenceNumber);

        return timestamp + paddedSequenceNumber;
    }

    private void updateAccountSessionExpiration(String chatId) {
        String key = getAccountSessionKey(chatId);
        ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).expire(key, 1, TimeUnit.MINUTES);
    }

    private void updateAmountSessionExpiration(String chatId) {
        String key = getAmountSessionKey(chatId);
        ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).expire(key, 1, TimeUnit.MINUTES);
    }

    private String generateWithdrawalSessionId() {
        return String.valueOf(System.currentTimeMillis());
    }

    private void setAccountSessionId(String chatId, String withdrawalSessionId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getAccountSessionKey(chatId);
        valueOperations.set(key, withdrawalSessionId, 1, TimeUnit.MINUTES);
    }

    private void setAmountSessionId(String chatId, String withdrawalSessionId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getAmountSessionKey(chatId);
        valueOperations.set(key, withdrawalSessionId, 1, TimeUnit.MINUTES);
    }

    private void setValuationSessionId(String chatId, String valuationSessionId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getValuationSessionKey(chatId);
        valueOperations.set(key, valuationSessionId, 1, TimeUnit.MINUTES);
    }

    private void setAccount(String chatId, String account) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getAccountKey(chatId);
        valueOperations.set(key, account, 10, TimeUnit.MINUTES);
    }

    private void setOrderId(String chatId, String orderId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getOrderIdKey(chatId);
        valueOperations.set(key, orderId, 10, TimeUnit.MINUTES);
    }

    private String getOrderId(String chatId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getOrderIdKey(chatId);
        return valueOperations.get(key);
    }

    private void setRate(String chatId, String rate) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getRateKey(chatId);
        valueOperations.set(key, rate, 10, TimeUnit.MINUTES);
    }

    private String getRate(String chatId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getRateKey(chatId);
        return valueOperations.get(key);
    }

    private void setReceive(String chatId, String receive) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getReceiveKey(chatId);
        valueOperations.set(key, receive, 10, TimeUnit.MINUTES);
    }

    private String getReceive(String chatId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getReceiveKey(chatId);
        return valueOperations.get(key);
    }

    private void setReceiveAddress(String chatId, String receiveAddress) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getReceiveAddressKey(chatId);
        valueOperations.set(key, receiveAddress, 10, TimeUnit.MINUTES);
    }

    private String getReceiveAddress(String chatId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getReceiveAddressKey(chatId);
        return valueOperations.get(key);
    }

    private void setWalletAddress(String chatId, String walletAddress) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getWalletAddressKey(chatId);
        valueOperations.set(key, walletAddress, 10, TimeUnit.MINUTES);
    }

    private String getWalletAddress(String chatId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getWalletAddressKey(chatId);
        return valueOperations.get(key);
    }

    private void setPayAmount(String chatId, String payAmount) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getPayAmountKey(chatId);
        valueOperations.set(key, payAmount, 10, TimeUnit.MINUTES);
    }

    private String getPayAmount(String chatId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getPayAmountKey(chatId);
        return valueOperations.get(key);
    }

    private void setAmount(String chatId, String amount) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getAmountKey(chatId);
        valueOperations.set(key, amount, 10, TimeUnit.MINUTES);
    }

    private String getAccount(String chatId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getAccountKey(chatId);
        return valueOperations.get(key);
    }

    private String getAmount(String chatId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getAmountKey(chatId);
        return valueOperations.get(key);
    }

    private String getAccountSessionId(String chatId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getAccountSessionKey(chatId);
        return valueOperations.get(key);
    }

    private String getAmountSessionId(String chatId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getAmountSessionKey(chatId);
        return valueOperations.get(key);
    }

    private String getValuationSessionId(String chatId) {
        ValueOperations<String, String> valueOperations = ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).opsForValue();
        String key = getValuationSessionKey(chatId);
        return valueOperations.get(key);
    }

    private void deleteAccountSessionId(String chatId) {
        String key = getAccountSessionKey(chatId);
        ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).delete(key);
    }

    private void deleteAmountSessionId(String chatId) {
        String key = getAmountSessionKey(chatId);
        ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).delete(key);
    }

    private void deleteValuationSessionId(String chatId) {
        String key = getValuationSessionKey(chatId);
        ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).delete(key);
    }

    private void deleteOrderId(String chatId) {
        String key = getOrderIdKey(chatId);
        ((RedisTemplate<String, String>) SpringUtils.getBean("redisTemplate")).delete(key);
    }

    private String getValuationSessionKey(String chatId) {
        return "valuation:session:" + chatId;
    }

    private String getAccountSessionKey(String chatId) {
        return "account:session:" + chatId;
    }

    private String getAmountSessionKey(String chatId) {
        return "amount:session:" + chatId;
    }

    private String getAccountKey(String chatId) {
        return "account:" + chatId;
    }

    private String getAmountKey(String chatId) {
        return "amount:" + chatId;
    }

    private String getOrderIdKey(String chatId) {
        return "orderId:" + chatId;
    }

    private String getRateKey(String chatId) {
        return "rate:" + chatId;
    }

    private String getReceiveKey(String chatId) {
        return "receive:" + chatId;
    }

    private String getReceiveAddressKey(String chatId) {
        return "receive_address:" + chatId;
    }

    private String getWalletAddressKey(String chatId) {
        return "wallet_address:" + chatId;
    }

    private String getPayAmountKey(String chatId) {
        return "pay_amount:" + chatId;
    }
}
