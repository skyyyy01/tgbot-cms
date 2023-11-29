package com.all.powerful.bot.controller;

import com.all.powerful.bot.domain.PayoutRecord;
import com.all.powerful.bot.service.IPayoutRecordService;
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
 * 下发记录Controller
 *
 * @author all powerful
 * @date 2023-11-25
 */
@Controller
@RequestMapping("/bot/payout")
public class PayoutRecordController extends BaseController {
    private String prefix = "bot/payout";

    @Autowired
    private IPayoutRecordService payoutRecordService;

    @RequiresPermissions("bot:payout:view")
    @GetMapping()
    public String payout() {
        return prefix + "/payout";
    }

    /**
     * 查询下发记录列表
     */
    @RequiresPermissions("bot:payout:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PayoutRecord payoutRecord) {
        startPage();
        List<PayoutRecord> list = payoutRecordService.selectPayoutRecordList(payoutRecord);
        return getDataTable(list);
    }

    @RequiresPermissions("monitor:job:detail")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long jobId, ModelMap mmap) {
        mmap.put("name", "payout");
        mmap.put("payout", payoutRecordService.selectPayoutRecordById(jobId));
        return prefix + "/detail";
    }

    /**
     * 导出下发记录列表
     */
    @RequiresPermissions("bot:payout:export")
    @Log(title = "下发记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PayoutRecord payoutRecord) {
        List<PayoutRecord> list = payoutRecordService.selectPayoutRecordList(payoutRecord);
        ExcelUtil<PayoutRecord> util = new ExcelUtil<PayoutRecord>(PayoutRecord.class);
        return util.exportExcel(list, "下发记录数据");
    }

    /**
     * 新增下发记录
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存下发记录
     */
    @RequiresPermissions("bot:payout:add")
    @Log(title = "下发记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(PayoutRecord payoutRecord) {
        return toAjax(payoutRecordService.insertPayoutRecord(payoutRecord));
    }

    /**
     * 修改下发记录
     */
    @RequiresPermissions("bot:payout:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        PayoutRecord payoutRecord = payoutRecordService.selectPayoutRecordById(id);
        mmap.put("payoutRecord", payoutRecord);
        return prefix + "/edit";
    }

    /**
     * 修改保存下发记录
     */
    @RequiresPermissions("bot:payout:edit")
    @Log(title = "下发记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(PayoutRecord payoutRecord) {
        return toAjax(payoutRecordService.updatePayoutRecord(payoutRecord));
    }

    /**
     * 删除下发记录
     */
    @RequiresPermissions("bot:payout:remove")
    @Log(title = "下发记录", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(payoutRecordService.deletePayoutRecordByIds(ids));
    }
}
