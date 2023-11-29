package com.all.powerful.bot.controller;

import java.util.List;

import com.all.powerful.bot.domain.BotList;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.all.powerful.common.annotation.Log;
import com.all.powerful.common.enums.BusinessType;
import com.all.powerful.bot.domain.TgUser;
import com.all.powerful.bot.service.ITgUserService;
import com.all.powerful.common.core.controller.BaseController;
import com.all.powerful.common.core.domain.AjaxResult;
import com.all.powerful.common.utils.poi.ExcelUtil;
import com.all.powerful.common.core.page.TableDataInfo;

/**
 * 客户列表Controller
 *
 * @author all powerful
 * @date 2023-11-22
 */
@Controller
@RequestMapping("/bot/user")
public class TgUserController extends BaseController
{
    private String prefix = "bot/user";

    @Autowired
    private ITgUserService tgUserService;

    @RequiresPermissions("bot:user:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/user";
    }

    /**
     * 查询客户列表列表
     */
    @RequiresPermissions("bot:user:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TgUser tgUser)
    {
        startPage();
        List<TgUser> list = tgUserService.selectTgUserList(tgUser);
        return getDataTable(list);
    }

    /**
     * 导出客户列表列表
     */
    @RequiresPermissions("bot:user:export")
    @Log(title = "客户列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TgUser tgUser)
    {
        List<TgUser> list = tgUserService.selectTgUserList(tgUser);
        ExcelUtil<TgUser> util = new ExcelUtil<TgUser>(TgUser.class);
        return util.exportExcel(list, "客户列表数据");
    }

    /**
     * 新增客户列表
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存客户列表
     */
    @RequiresPermissions("bot:user:add")
    @Log(title = "客户列表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TgUser tgUser)
    {
        return toAjax(tgUserService.insertTgUser(tgUser));
    }

    /**
     * 修改客户列表
     */
    @RequiresPermissions("bot:user:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        TgUser tgUser = tgUserService.selectTgUserById(id);
        mmap.put("tgUser", tgUser);
        return prefix + "/edit";
    }

    /**
     * 修改保存客户列表
     */
    @RequiresPermissions("bot:user:edit")
    @Log(title = "客户列表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TgUser tgUser)
    {
        return toAjax(tgUserService.updateTgUser(tgUser));
    }

    /**
     * 删除客户列表
     */
    @RequiresPermissions("bot:user:remove")
    @Log(title = "客户列表", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tgUserService.deleteTgUserByIds(ids));
    }


    /**
     * 状态修改
     */
    @Log(title = "机器人列表", businessType = BusinessType.UPDATE)
    @RequiresPermissions("bot:user:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(TgUser tgUser) throws Exception {
        return toAjax(tgUserService.changeStatus(tgUser));
    }
}
