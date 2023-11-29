package com.all.powerful.bot.controller;

import com.all.powerful.bot.domain.BotList;
import com.all.powerful.bot.service.IBotListService;
import com.all.powerful.common.annotation.Log;
import com.all.powerful.common.core.controller.BaseController;
import com.all.powerful.common.core.domain.AjaxResult;
import com.all.powerful.common.core.page.TableDataInfo;
import com.all.powerful.common.enums.BusinessType;
import com.all.powerful.common.utils.poi.ExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 机器人列表Controller
 *
 * @author all powerful
 * @date 2023-11-19
 */
@Controller
@RequestMapping("/bot/list")
public class BotListController extends BaseController {
    private String prefix = "bot/list";

    @Autowired
    private IBotListService botListService;

    @RequiresPermissions("bot:list:view")
    @GetMapping()
    public String list() {
        return prefix + "/list";
    }

    /**
     * 查询机器人列表列表
     */
    @RequiresPermissions("bot:list:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BotList botList) {
        startPage();
        List<BotList> list = botListService.selectBotListList(botList);
        return getDataTable(list);
    }

    /**
     * 导出机器人列表列表
     */
    @RequiresPermissions("bot:list:export")
    @Log(title = "机器人列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BotList botList) {
        List<BotList> list = botListService.selectBotListList(botList);
        ExcelUtil<BotList> util = new ExcelUtil<BotList>(BotList.class);
        return util.exportExcel(list, "机器人列表数据");
    }

    /**
     * 新增机器人列表
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存机器人列表
     */
    @RequiresPermissions("bot:list:add")
    @Log(title = "机器人列表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BotList botList) {
        return toAjax(botListService.insertBotList(botList));
    }

    /**
     * 修改机器人列表
     */
    @RequiresPermissions("bot:list:edit")
    @GetMapping("/edit/{botId}")
    public String edit(@PathVariable("botId") Long botId, ModelMap mmap) {
        BotList botList = botListService.selectBotListByBotId(botId);
        mmap.put("botList", botList);
        return prefix + "/edit";
    }

    /**
     * 修改保存机器人列表
     */
    @RequiresPermissions("bot:list:edit")
    @Log(title = "机器人列表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BotList botList) {
        return toAjax(botListService.updateBotList(botList));
    }

    /**
     * 删除机器人列表
     */
    @RequiresPermissions("bot:list:remove")
    @Log(title = "机器人列表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(botListService.deleteBotListByBotIds(ids));
    }


    /**
     * 状态修改
     */
    @Log(title = "机器人列表", businessType = BusinessType.UPDATE)
    @RequiresPermissions("bot:list:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(BotList botList) throws Exception {
        return toAjax(botListService.changeStatus(botList));
    }
}
