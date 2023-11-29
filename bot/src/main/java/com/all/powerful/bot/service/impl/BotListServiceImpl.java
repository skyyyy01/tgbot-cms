package com.all.powerful.bot.service.impl;

import com.all.powerful.bot.MultiBotManager;
import com.all.powerful.bot.domain.BotList;
import com.all.powerful.bot.mapper.BotListMapper;
import com.all.powerful.bot.service.IBotListService;
import com.all.powerful.common.core.text.Convert;
import com.all.powerful.common.utils.DateUtils;
import com.all.powerful.common.utils.ShiroUtils;
import com.all.powerful.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 机器人列表Service业务层处理
 *
 * @author all powerful
 * @date 2023-11-19
 */
@Service
public class BotListServiceImpl implements IBotListService {
    @Autowired
    private BotListMapper botListMapper;
    @Autowired
    private MultiBotManager multiBotManager;

    /**
     * 查询机器人列表
     *
     * @param botId 机器人列表主键
     * @return 机器人列表
     */
    @Override
    public BotList selectBotListByBotId(Long botId) {
        return botListMapper.selectBotListByBotId(botId);
    }

    /**
     * 查询机器人列表列表
     *
     * @param botList 机器人列表
     * @return 机器人列表
     */
    @Override
    public List<BotList> selectBotListList(BotList botList) {
        return botListMapper.selectBotListList(botList);
    }

    /**
     * 新增机器人列表
     *
     * @param botList 机器人列表
     * @return 结果
     */
    @Override
    public int insertBotList(BotList botList) {
        botList.setCreateTime(DateUtils.getNowDate());
        botList.setCreateBy(ShiroUtils.getLoginName());
        return botListMapper.insertBotList(botList);
    }

    /**
     * 修改机器人列表
     *
     * @param botList 机器人列表
     * @return 结果
     */
    @Override
    public int updateBotList(BotList botList) {
        botList.setUpdateTime(DateUtils.getNowDate());
        botList.setUpdateBy(ShiroUtils.getLoginName());
        return botListMapper.updateBotList(botList);
    }

    /**
     * 批量删除机器人列表
     *
     * @param botIds 需要删除的机器人列表主键
     * @return 结果
     */
    @Override
    public int deleteBotListByBotIds(String botIds) {
        return botListMapper.deleteBotListByBotIds(Convert.toStrArray(botIds));
    }

    /**
     * 删除机器人列表信息
     *
     * @param botId 机器人列表主键
     * @return 结果
     */
    @Override
    public int deleteBotListByBotId(Long botId) {
        return botListMapper.deleteBotListByBotId(botId);
    }

    @Override
    public int changeStatus(BotList botList) throws Exception {
        BotList bot = botListMapper.selectBotListByBotId(botList.getBotId());
        if (botList.getStatus().equals("0")) {
            if (multiBotManager.getBot(bot.getBotToken()) == null) {
                BotList botList1 = new BotList();
                botList1.setStatus("0");
                List<BotList> botLists = botListMapper.selectBotListList(botList1);
                if (botLists.size() > 0) {
                    throw new Exception("同时只能运行一个bot");
                }
                multiBotManager.registerBot(bot.getBotUserName(), bot.getBotToken());
            }
        } else {
            if (multiBotManager.getBot(bot.getBotToken()) != null) {
                multiBotManager.closeBot(bot.getBotToken());
            }
        }
        botList.setUpdateTime(DateUtils.getNowDate());
        botList.setUpdateBy(ShiroUtils.getSysUser().getLoginName());
        return botListMapper.updateBotStatus(botList);
    }
}
