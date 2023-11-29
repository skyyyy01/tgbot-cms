package com.all.powerful.bot.mapper;

import java.util.List;
import com.all.powerful.bot.domain.BotList;

/**
 * 机器人列表Mapper接口
 *
 * @author all powerful
 * @date 2023-11-19
 */
public interface BotListMapper
{
    /**
     * 查询机器人列表
     *
     * @param botId 机器人列表主键
     * @return 机器人列表
     */
    public BotList selectBotListByBotId(Long botId);

    /**
     * 查询机器人列表列表
     *
     * @param botList 机器人列表
     * @return 机器人列表集合
     */
    public List<BotList> selectBotListList(BotList botList);

    /**
     * 新增机器人列表
     *
     * @param botList 机器人列表
     * @return 结果
     */
    public int insertBotList(BotList botList);

    /**
     * 修改机器人列表
     *
     * @param botList 机器人列表
     * @return 结果
     */
    public int updateBotList(BotList botList);

    public int updateBotStatus(BotList botList);

    /**
     * 删除机器人列表
     *
     * @param botId 机器人列表主键
     * @return 结果
     */
    public int deleteBotListByBotId(Long botId);

    /**
     * 批量删除机器人列表
     *
     * @param botIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBotListByBotIds(String[] botIds);
}
