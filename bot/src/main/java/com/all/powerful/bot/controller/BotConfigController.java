package com.all.powerful.bot.controller;

import com.all.powerful.bot.domain.BotConfig;
import com.all.powerful.bot.service.IBotConfigService;
import com.all.powerful.common.annotation.Log;
import com.all.powerful.common.config.AllPowerfulConfig;
import com.all.powerful.common.core.controller.BaseController;
import com.all.powerful.common.core.domain.AjaxResult;
import com.all.powerful.common.core.domain.Ztree;
import com.all.powerful.common.enums.BusinessType;
import com.all.powerful.common.utils.StringUtils;
import com.all.powerful.common.utils.file.FileUploadUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 机器人配置Controller
 *
 * @author all powerful
 * @date 2023-11-19
 */
@Controller
@RequestMapping("/bot/config")
public class BotConfigController extends BaseController {
    private String prefix = "bot/config";

    @Autowired
    private IBotConfigService botConfigService;

    @RequiresPermissions("bot:config:view")
    @GetMapping()
    public String config() {
        return prefix + "/config";
    }

    /**
     * 查询机器人配置列表
     */
    @RequiresPermissions("bot:config:list")
    @PostMapping("/list")
    @ResponseBody
    public List<BotConfig> list(BotConfig botConfig) {
        return botConfigService.selectBotConfigList(botConfig);
    }

    /**
     * 导出机器人配置列表
     */
//    @RequiresPermissions("bot:config:export")
//    @Log(title = "机器人配置", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ResponseBody
//    public AjaxResult export(BotConfig botConfig) {
//        List<BotConfig> list = botConfigService.selectBotConfigList(botConfig);
//        ExcelUtil<BotConfig> util = new ExcelUtil<BotConfig>(BotConfig.class);
//        return util.exportExcel(list, "机器人配置数据");
//    }

    /**
     * 新增机器人配置
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap mmap) {
        BotConfig config = null;
        if (0L != parentId) {
            config = botConfigService.selectBotConfigByConfigId(parentId);
        } else {
            config = new BotConfig();
            config.setConfigId(0L);
            config.setConfigName("无");
        }
        mmap.put("config", config);
        return prefix + "/add";
    }

    /**
     * 新增保存机器人配置
     */
    @RequiresPermissions("bot:config:add")
    @Log(title = "机器人配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(MultipartFile imageFile, MultipartFile videoFile, BotConfig botConfig) throws IOException {

        if (botConfig.getConfigType().equals("F") && StringUtils.isNotBlank(botConfig.getShowName())) {
            BotConfig config = botConfigService.selectBotConfigByShowName(botConfig.getShowName());
            if (config != null) {
                return AjaxResult.error("显示名称已经存在");
            }
        }
        if (botConfig.getConfigType().equals("B") && StringUtils.isNotBlank(botConfig.getCallbackData())) {
            BotConfig config = botConfigService.selectBotConfigByCallbackData(botConfig.getCallbackData());
            if (config != null) {
                return AjaxResult.error("按钮编码已经存在");
            }
        }
        if (botConfig.getConfigType().equals("D") && StringUtils.isNotBlank(botConfig.getCallbackText())) {
            BotConfig config = botConfigService.selectBotConfigByCallbackText(botConfig.getCallbackText());
            if (config != null) {
                return AjaxResult.error("文本指令已经存在");
            }
        }
        if (imageFile != null) {
            // 上传文件路径
            String filePath = AllPowerfulConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, imageFile);
            botConfig.setImage(fileName);
        }

        if (videoFile != null) {
            // 上传文件路径
            String filePath = AllPowerfulConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, videoFile);
            botConfig.setVideo(fileName);
        }
        return toAjax(botConfigService.insertBotConfig(botConfig));
    }

    /**
     * 校验
     */
    @PostMapping("/checkShowNameUnique")
    @ResponseBody
    public boolean checkShowNameUnique(BotConfig botConfig) {
        if (botConfig.getConfigType().equals("F") && StringUtils.isNotBlank(botConfig.getShowName())) {
            BotConfig config = botConfigService.selectBotConfigByShowName(botConfig.getShowName());
            return config == null;
        }
        return true;
    }

    @PostMapping("/checkCallbackDataUnique")
    @ResponseBody
    public boolean checkCallbackDataUnique(BotConfig botConfig) {
        if (botConfig.getConfigType().equals("B") && StringUtils.isNotBlank(botConfig.getCallbackData())) {
            BotConfig config = botConfigService.selectBotConfigByCallbackData(botConfig.getCallbackData());
            return config == null;
        }
        return true;
    }

    @PostMapping("/checkCallbackTextUnique")
    @ResponseBody
    public boolean checkCallbackTextUnique(BotConfig botConfig) {
        if (botConfig.getConfigType().equals("D") && StringUtils.isNotBlank(botConfig.getCallbackText())) {
            BotConfig config = botConfigService.selectBotConfigByCallbackText(botConfig.getCallbackText());
            return config == null;
        }
        return true;
    }


    /**
     * 修改机器人配置
     */
    @RequiresPermissions("bot:config:edit")
    @GetMapping("/edit/{configId}")
    public String edit(@PathVariable("configId") Long configId, ModelMap mmap) {
        BotConfig botConfig = botConfigService.selectBotConfigByConfigId(configId);
        mmap.put("config", botConfig);
        return prefix + "/edit";
    }

    /**
     * 修改保存机器人配置
     */
    @RequiresPermissions("bot:config:edit")
    @Log(title = "机器人配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(MultipartFile imageFile, MultipartFile videoFile, BotConfig botConfig) throws IOException {
        if (imageFile != null) {
            // 上传文件路径
            String filePath = AllPowerfulConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, imageFile);
            botConfig.setImage(fileName);
        }

        if (videoFile != null) {
            // 上传文件路径
            String filePath = AllPowerfulConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, videoFile);
            botConfig.setVideo(fileName);
        }
        return toAjax(botConfigService.updateBotConfig(botConfig));
    }

    /**
     * 删除机器人配置
     */
    @RequiresPermissions("bot:config:remove")
    @Log(title = "机器人配置", businessType = BusinessType.DELETE)
    @GetMapping("/remove/{configId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("configId") Long configId) {
        return toAjax(botConfigService.deleteBotConfigByConfigId(configId));
    }


    /**
     * 加载所有配置列表树
     */
    @GetMapping("/configTreeData")
    @ResponseBody
    public List<Ztree> configTreeData() {
        List<Ztree> ztrees = botConfigService.configTreeData();
        return ztrees;
    }

    /**
     * 选择配置树
     */
    @GetMapping("/selectConfigTree/{configId}")
    public String selectConfigTree(@PathVariable("configId") Long configId, ModelMap mmap) {
        BotConfig config = botConfigService.selectBotConfigByConfigId(configId);
        if (config == null) {
            config = new BotConfig();
            config.setConfigId(0L);
        }
        mmap.put("config", config);
        return prefix + "/tree";
    }

    @GetMapping("/describe")
    public String describe(ModelMap mmap) {
        return prefix + "/describe";
    }
}
