package com.all.powerful.bot.service.impl;

import com.all.powerful.bot.domain.PaymentRecord;
import com.all.powerful.bot.domain.PayoutRecord;
import com.all.powerful.bot.mapper.PaymentRecordMapper;
import com.all.powerful.bot.service.IPaymentRecordService;
import com.all.powerful.bot.service.IPayoutRecordService;
import com.all.powerful.common.core.text.Convert;
import com.all.powerful.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 支付记录Service业务层处理
 *
 * @author all powerful
 * @date 2023-11-25
 */
@Service
public class PaymentRecordServiceImpl implements IPaymentRecordService {
    @Autowired
    private PaymentRecordMapper paymentRecordMapper;

    @Autowired
    private IPayoutRecordService payoutRecordService;

    /**
     * 查询支付记录
     *
     * @param id 支付记录主键
     * @return 支付记录
     */
    @Override
    public PaymentRecord selectPaymentRecordById(Long id) {
        return paymentRecordMapper.selectPaymentRecordById(id);
    }

    /**
     * 查询支付记录列表
     *
     * @param paymentRecord 支付记录
     * @return 支付记录
     */
    @Override
    public List<PaymentRecord> selectPaymentRecordList(PaymentRecord paymentRecord) {
        return paymentRecordMapper.selectPaymentRecordList(paymentRecord);
    }

    /**
     * 新增支付记录
     *
     * @param paymentRecord 支付记录
     * @return 结果
     */
    @Override
    public int insertPaymentRecord(PaymentRecord paymentRecord) {
        paymentRecord.setCreateTime(DateUtils.getNowDate());
        return paymentRecordMapper.insertPaymentRecord(paymentRecord);
    }

    /**
     * 修改支付记录
     *
     * @param paymentRecord 支付记录
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePaymentRecord(PaymentRecord paymentRecord) {
        paymentRecord.setUpdateTime(DateUtils.getNowDate());
        PaymentRecord record = paymentRecordMapper.selectPaymentRecordById(paymentRecord.getId());
        if (record != null) {
            PayoutRecord payoutRecord = new PayoutRecord();
            payoutRecord.setOrderId(record.getOrderId());
            payoutRecord.setStatus(paymentRecord.getStatus());
            if (paymentRecord.getStatus().equals("0")){
                payoutRecord.setpStatus("2");
                payoutRecord.setHash(record.getBlockId());
            }
            payoutRecord.setUpdateTime(paymentRecord.getUpdateTime());
            payoutRecord.setUpdateBy(paymentRecord.getUpdateBy());
            payoutRecordService.updatePayoutRecordByOrderId(payoutRecord);
        }
        return paymentRecordMapper.updatePaymentRecord(paymentRecord);
    }

    /**
     * 批量删除支付记录
     *
     * @param ids 需要删除的支付记录主键
     * @return 结果
     */
    @Override
    public int deletePaymentRecordByIds(String ids) {
        return paymentRecordMapper.deletePaymentRecordByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除支付记录信息
     *
     * @param id 支付记录主键
     * @return 结果
     */
    @Override
    public int deletePaymentRecordById(Long id) {
        return paymentRecordMapper.deletePaymentRecordById(id);
    }
}
