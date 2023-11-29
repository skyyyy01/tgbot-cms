package com.all.powerful.bot.service.impl;

import com.all.powerful.bot.MultiBotManager;
import com.all.powerful.bot.domain.BotList;
import com.all.powerful.bot.domain.TgUser;
import com.all.powerful.bot.domain.UserNotify;
import com.all.powerful.bot.mapper.UserNotifyMapper;
import com.all.powerful.bot.service.IBotListService;
import com.all.powerful.bot.service.ITgUserService;
import com.all.powerful.bot.service.IUserNotifyService;
import com.all.powerful.common.core.text.Convert;
import com.all.powerful.common.utils.DateUtils;
import com.all.powerful.common.utils.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通知管理Service业务层处理
 *
 * @author all powerful
 * @date 2023-11-27
 */
@Service
public class UserNotifyServiceImpl implements IUserNotifyService {

    private static final Logger log = LoggerFactory.getLogger(UserNotifyServiceImpl.class);
    @Autowired
    private UserNotifyMapper userNotifyMapper;
    @Autowired
    private IBotListService botListService;
    @Autowired
    private MultiBotManager multiBotManager;
    @Autowired
    private ITgUserService tgUserService;

    /**
     * 查询通知管理
     *
     * @param id 通知管理主键
     * @return 通知管理
     */
    @Override
    public UserNotify selectUserNotifyById(Long id) {
        return userNotifyMapper.selectUserNotifyById(id);
    }

    /**
     * 查询通知管理列表
     *
     * @param userNotify 通知管理
     * @return 通知管理
     */
    @Override
    public List<UserNotify> selectUserNotifyList(UserNotify userNotify) {
        return userNotifyMapper.selectUserNotifyList(userNotify);
    }

    /**
     * 新增通知管理
     *
     * @param userNotify 通知管理
     * @return 结果
     */
    @Override
    public int insertUserNotify(UserNotify userNotify, File image) {
        userNotify.setCreateTime(DateUtils.getNowDate());
        userNotify.setCreateBy(ShiroUtils.getLoginName());
        int row = userNotifyMapper.insertUserNotify(userNotify);
        List<String> userIds;
        if (userNotify.getNotifyType().equals("0")) {
            TgUser tgUser = new TgUser();
            tgUser.setStatus("0");
            List<TgUser> tgUsers = tgUserService.selectTgUserList(tgUser);
            userIds = tgUsers.stream().map(TgUser::getUserId).collect(Collectors.toList());
        } else {
            userIds = Arrays.stream(userNotify.getUserId().split(",")).collect(Collectors.toList());
        }
        sendMessageToUser(userIds, userNotify.getText(), image, userNotify);
        return row;
    }

    @Async
    public void sendMessageToUser(List<String> userIds, String text, File image, UserNotify userNotify) {
        for (String userId : userIds) {
            try {
                BotList botList = new BotList();
                botList.setStatus("0");
                List<BotList> botLists = botListService.selectBotListList(botList);
                if (botLists.size() > 0) {
                    BotList bot = botLists.get(0);
                    TelegramLongPollingBot tgBot = multiBotManager.getBot(bot.getBotToken());
                    if (image != null) {
                        SendPhoto sendPhoto = new SendPhoto();
                        sendPhoto.setChatId(userId);
                        sendPhoto.setPhoto(new InputFile(image));
                        sendPhoto.setCaption(text);
                        tgBot.execute(sendPhoto);
                    } else {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(userId);
                        sendMessage.setText(text);
                        tgBot.execute(sendMessage);
                    }

                }
            } catch (Exception e) {
                log.error("发送通知异常 -> {}", userId, e);
            }
        }
        userNotify.setStatus("0");
        userNotifyMapper.updateUserNotify(userNotify);
    }

    /**
     * 修改通知管理
     *
     * @param userNotify 通知管理
     * @return 结果
     */
    @Override
    public int updateUserNotify(UserNotify userNotify) {
        userNotify.setUpdateTime(DateUtils.getNowDate());
        return userNotifyMapper.updateUserNotify(userNotify);
    }

    /**
     * 批量删除通知管理
     *
     * @param ids 需要删除的通知管理主键
     * @return 结果
     */
    @Override
    public int deleteUserNotifyByIds(String ids) {
        return userNotifyMapper.deleteUserNotifyByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除通知管理信息
     *
     * @param id 通知管理主键
     * @return 结果
     */
    @Override
    public int deleteUserNotifyById(Long id) {
        return userNotifyMapper.deleteUserNotifyById(id);
    }
}
