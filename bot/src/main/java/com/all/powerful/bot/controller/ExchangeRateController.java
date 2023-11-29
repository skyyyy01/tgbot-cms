package com.all.powerful.bot.controller;

import java.util.List;
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
import com.all.powerful.bot.domain.ExchangeRate;
import com.all.powerful.bot.service.IExchangeRateService;
import com.all.powerful.common.core.controller.BaseController;
import com.all.powerful.common.core.domain.AjaxResult;
import com.all.powerful.common.utils.poi.ExcelUtil;
import com.all.powerful.common.core.page.TableDataInfo;

/**
 * 汇率管理Controller
 *
 * @author all powerful
 * @date 2023-11-23
 */
@Controller
@RequestMapping("/bot/rate")
public class ExchangeRateController extends BaseController
{
    private String prefix = "bot/rate";

    @Autowired
    private IExchangeRateService exchangeRateService;

    @RequiresPermissions("bot:rate:view")
    @GetMapping()
    public String rate()
    {
        return prefix + "/rate";
    }

    /**
     * 查询汇率管理列表
     */
    @RequiresPermissions("bot:rate:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ExchangeRate exchangeRate)
    {
        startPage();
        List<ExchangeRate> list = exchangeRateService.selectExchangeRateList(exchangeRate);
        return getDataTable(list);
    }

    /**
     * 导出汇率管理列表
     */
    @RequiresPermissions("bot:rate:export")
    @Log(title = "汇率管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ExchangeRate exchangeRate)
    {
        List<ExchangeRate> list = exchangeRateService.selectExchangeRateList(exchangeRate);
        ExcelUtil<ExchangeRate> util = new ExcelUtil<ExchangeRate>(ExchangeRate.class);
        return util.exportExcel(list, "汇率管理数据");
    }

    /**
     * 新增汇率管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存汇率管理
     */
    @RequiresPermissions("bot:rate:add")
    @Log(title = "汇率管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ExchangeRate exchangeRate)
    {
        return toAjax(exchangeRateService.insertExchangeRate(exchangeRate));
    }

    /**
     * 修改汇率管理
     */
    @RequiresPermissions("bot:rate:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ExchangeRate exchangeRate = exchangeRateService.selectExchangeRateById(id);
        mmap.put("exchangeRate", exchangeRate);
        return prefix + "/edit";
    }

    /**
     * 修改保存汇率管理
     */
    @RequiresPermissions("bot:rate:edit")
    @Log(title = "汇率管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ExchangeRate exchangeRate)
    {
        return toAjax(exchangeRateService.updateExchangeRate(exchangeRate));
    }

    /**
     * 删除汇率管理
     */
    @RequiresPermissions("bot:rate:remove")
    @Log(title = "汇率管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(exchangeRateService.deleteExchangeRateByIds(ids));
    }
}
