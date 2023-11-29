package com.all.powerful.bot.mapper;

import com.all.powerful.bot.domain.BotConfig;

import java.util.List;

/**
 * 机器人配置Mapper接口
 *
 * @author all powerful
 * @date 2023-11-19
 */
public interface BotConfigMapper {
    /**
     * 查询机器人配置
     *
     * @param configId 机器人配置主键
     * @return 机器人配置
     */
    public BotConfig selectBotConfigByConfigId(Long configId);

    public BotConfig selectBotConfigByShowName(String showName);

    public BotConfig selectBotConfigByCallbackText(String callbackText);

    public BotConfig selectBotConfigByCallbackData(String callbackData);

    /**
     * 查询机器人配置列表
     *
     * @param botConfig 机器人配置
     * @return 机器人配置集合
     */
    public List<BotConfig> selectBotConfigList(BotConfig botConfig);

    /**
     * 新增机器人配置
     *
     * @param botConfig 机器人配置
     * @return 结果
     */
    public int insertBotConfig(BotConfig botConfig);

    /**
     * 修改机器人配置
     *
     * @param botConfig 机器人配置
     * @return 结果
     */
    public int updateBotConfig(BotConfig botConfig);

    /**
     * 删除机器人配置
     *
     * @param configId 机器人配置主键
     * @return 结果
     */
    public int deleteBotConfigByConfigId(Long configId);

    /**
     * 批量删除机器人配置
     *
     * @param configIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBotConfigByConfigIds(String[] configIds);

    /**
     * 查询系统所有配置
     *
     * @return 配置列表
     */
    public List<BotConfig> selectConfigAll();
}
