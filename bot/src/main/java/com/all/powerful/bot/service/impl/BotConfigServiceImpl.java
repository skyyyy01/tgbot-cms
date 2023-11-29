package com.all.powerful.bot.service.impl;

import com.all.powerful.bot.domain.BotConfig;
import com.all.powerful.bot.mapper.BotConfigMapper;
import com.all.powerful.bot.service.IBotConfigService;
import com.all.powerful.common.core.domain.Ztree;
import com.all.powerful.common.core.text.Convert;
import com.all.powerful.common.utils.DateUtils;
import com.all.powerful.common.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 机器人配置Service业务层处理
 *
 * @author all powerful
 * @date 2023-11-19
 */
@Service
public class BotConfigServiceImpl implements IBotConfigService {
    @Autowired
    private BotConfigMapper botConfigMapper;

    /**
     * 查询机器人配置
     *
     * @param configId 机器人配置主键
     * @return 机器人配置
     */
    @Override
    public BotConfig selectBotConfigByConfigId(Long configId) {
        return botConfigMapper.selectBotConfigByConfigId(configId);
    }

    /**
     * 查询机器人配置列表
     *
     * @param botConfig 机器人配置
     * @return 机器人配置
     */
    @Override
    public List<BotConfig> selectBotConfigList(BotConfig botConfig) {
        return botConfigMapper.selectBotConfigList(botConfig);
    }

    /**
     * 新增机器人配置
     *
     * @param botConfig 机器人配置
     * @return 结果
     */
    @Override
    public int insertBotConfig(BotConfig botConfig) {
        botConfig.setCreateTime(DateUtils.getNowDate());
        botConfig.setCreateBy(ShiroUtils.getLoginName());
        return botConfigMapper.insertBotConfig(botConfig);
    }

    /**
     * 修改机器人配置
     *
     * @param botConfig 机器人配置
     * @return 结果
     */
    @Override
    public int updateBotConfig(BotConfig botConfig) {
        botConfig.setUpdateTime(DateUtils.getNowDate());
        botConfig.setUpdateBy(ShiroUtils.getLoginName());
        return botConfigMapper.updateBotConfig(botConfig);
    }

    /**
     * 批量删除机器人配置
     *
     * @param configIds 需要删除的机器人配置主键
     * @return 结果
     */
    @Override
    public int deleteBotConfigByConfigIds(String configIds) {
        List<String> ids = Arrays.asList(Convert.toStrArray(configIds));
        for (String id : ids) {
            BotConfig config = botConfigMapper.selectBotConfigByConfigId(Long.parseLong(id));
            if (config.getParentId() == 0) {
                BotConfig c = new BotConfig();
                c.setParentId(config.getConfigId());
                List<BotConfig> botConfigs = botConfigMapper.selectBotConfigList(c);
                List<String> collect = botConfigs.stream().map(BotConfig::getConfigId).map(String::valueOf).collect(Collectors.toList());
                ids.addAll(collect);
            }
        }
        return botConfigMapper.deleteBotConfigByConfigIds(ids.toArray(new String[0]));
    }

    /**
     * 删除机器人配置信息
     *
     * @param configId 机器人配置主键
     * @return 结果
     */
    @Override
    public int deleteBotConfigByConfigId(Long configId) {
        BotConfig config = botConfigMapper.selectBotConfigByConfigId(configId);
        List<Long> ids = new ArrayList<>();
        ids.add(configId);
        if (config.getParentId() == 0) {
            BotConfig c = new BotConfig();
            c.setParentId(config.getConfigId());
            List<BotConfig> botConfigs = botConfigMapper.selectBotConfigList(c);
            List<Long> collect = botConfigs.stream().map(BotConfig::getConfigId).collect(Collectors.toList());
            ids.addAll(collect);
        }
        return botConfigMapper.deleteBotConfigByConfigIds(ids.stream().map(String::valueOf).toArray(String[]::new));
    }

    /**
     * 查询配置集合
     *
     * @return 所有配置信息
     */
    @Override
    public List<BotConfig> selectConfigAll() {
        return botConfigMapper.selectConfigAll();
    }

    /**
     * 查询所有配置
     *
     * @return 配置列表
     */
    @Override
    public List<Ztree> configTreeData() {
        List<BotConfig> configList = selectConfigAll();
        return initZtree(configList);
    }

    /**
     * 对象转菜单树
     *
     * @param configList 菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<BotConfig> configList) {
        List<Ztree> ztrees = new ArrayList<Ztree>();
        for (BotConfig config : configList) {
            Ztree ztree = new Ztree();
            ztree.setId(config.getConfigId());
            ztree.setpId(config.getParentId());
            ztree.setName(config.getConfigName());
            ztree.setTitle(config.getConfigName());
            ztrees.add(ztree);
        }
        return ztrees;
    }

    @Override
    public BotConfig selectBotConfigByShowName(String showName) {
        return botConfigMapper.selectBotConfigByShowName(showName);
    }

    @Override
    public BotConfig selectBotConfigByCallbackText(String callbackText) {
        return botConfigMapper.selectBotConfigByCallbackText(callbackText);
    }

    @Override
    public BotConfig selectBotConfigByCallbackData(String callbackData) {
        return botConfigMapper.selectBotConfigByCallbackData(callbackData);
    }
}
