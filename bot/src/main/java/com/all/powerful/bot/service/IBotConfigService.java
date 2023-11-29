package com.all.powerful.bot.service;

import com.all.powerful.bot.domain.BotConfig;
import com.all.powerful.common.core.domain.Ztree;

import java.util.List;

/**
 * 机器人配置Service接口
 *
 * @author all powerful
 * @date 2023-11-19
 */
public interface IBotConfigService {
    /**
     * 查询机器人配置
     *
     * @param configId 机器人配置主键
     * @return 机器人配置
     */
    public BotConfig selectBotConfigByConfigId(Long configId);

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
     * 批量删除机器人配置
     *
     * @param configIds 需要删除的机器人配置主键集合
     * @return 结果
     */
    public int deleteBotConfigByConfigIds(String configIds);

    /**
     * 删除机器人配置信息
     *
     * @param configId 机器人配置主键
     * @return 结果
     */
    public int deleteBotConfigByConfigId(Long configId);

    /**
     * 查询配置集合
     *
     * @return 所有菜单信息
     */
    public List<BotConfig> selectConfigAll();

    /**
     * 查询所有配置信息
     *
     * @return 菜单列表
     */
    public List<Ztree> configTreeData();

    BotConfig selectBotConfigByShowName(String showName);

    BotConfig selectBotConfigByCallbackText(String callbackText);

    BotConfig selectBotConfigByCallbackData(String callbackData);
}
