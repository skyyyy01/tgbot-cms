package com.all.powerful.bot.controller;

import com.all.powerful.bot.domain.PaymentRecord;
import com.all.powerful.bot.service.IPaymentRecordService;
import com.all.powerful.common.annotation.Log;
import com.all.powerful.common.core.controller.BaseController;
import com.all.powerful.common.core.domain.AjaxResult;
import com.all.powerful.common.core.page.TableDataInfo;
import com.all.powerful.common.enums.BusinessType;
import com.all.powerful.common.utils.ShiroUtils;
import com.all.powerful.common.utils.poi.ExcelUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付记录Controller
 *
 * @author all powerful
 * @date 2023-11-25
 */
@Controller
@RequestMapping("/bot/pay")
public class PaymentRecordController extends BaseController {
    private String prefix = "bot/pay";

    @Autowired
    private IPaymentRecordService paymentRecordService;

    @RequiresPermissions("bot:pay:view")
    @GetMapping()
    public String pay() {
        return prefix + "/pay";
    }

    /**
     * 查询支付记录列表
     */
    @RequiresPermissions("bot:pay:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PaymentRecord paymentRecord) {
        startPage();
        List<PaymentRecord> list = paymentRecordService.selectPaymentRecordList(paymentRecord);
        return getDataTable(list);
    }

    @RequiresPermissions("monitor:job:detail")
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long jobId, ModelMap mmap) {
        mmap.put("name", "pay");
        mmap.put("pay", paymentRecordService.selectPaymentRecordById(jobId));
        return prefix + "/detail";
    }

    /**
     * 导出支付记录列表
     */
    @RequiresPermissions("bot:pay:export")
    @Log(title = "支付记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(PaymentRecord paymentRecord) {
        List<PaymentRecord> list = paymentRecordService.selectPaymentRecordList(paymentRecord);
        ExcelUtil<PaymentRecord> util = new ExcelUtil<PaymentRecord>(PaymentRecord.class);
        return util.exportExcel(list, "支付记录数据");
    }

    /**
     * 新增支付记录
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存支付记录
     */
    @RequiresPermissions("bot:pay:add")
    @Log(title = "支付记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(PaymentRecord paymentRecord) {
        return toAjax(paymentRecordService.insertPaymentRecord(paymentRecord));
    }

    /**
     * 修改支付记录
     */
    @RequiresPermissions("bot:pay:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        PaymentRecord paymentRecord = paymentRecordService.selectPaymentRecordById(id);
        mmap.put("paymentRecord", paymentRecord);
        return prefix + "/edit";
    }

    /**
     * 修改保存支付记录
     */
    @RequiresPermissions("bot:pay:edit")
    @Log(title = "支付记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(PaymentRecord paymentRecord) {
        paymentRecord.setUsername(ShiroUtils.getLoginName());
        return toAjax(paymentRecordService.updatePaymentRecord(paymentRecord));
    }

    /**
     * 删除支付记录
     */
    @RequiresPermissions("bot:pay:remove")
    @Log(title = "支付记录", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(paymentRecordService.deletePaymentRecordByIds(ids));
    }
}
