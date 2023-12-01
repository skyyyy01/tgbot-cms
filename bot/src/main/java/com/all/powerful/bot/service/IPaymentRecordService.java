package com.all.powerful.bot.service;

import java.util.Date;
import java.util.List;
import com.all.powerful.bot.domain.PaymentRecord;

/**
 * 支付记录Service接口
 *
 * @author all powerful
 * @date 2023-11-25
 */
public interface IPaymentRecordService
{
    /**
     * 查询支付记录
     *
     * @param id 支付记录主键
     * @return 支付记录
     */
    public PaymentRecord selectPaymentRecordById(Long id);

    /**
     * 查询支付记录列表
     *
     * @param paymentRecord 支付记录
     * @return 支付记录集合
     */
    public List<PaymentRecord> selectPaymentRecordList(PaymentRecord paymentRecord);

    /**
     * 新增支付记录
     *
     * @param paymentRecord 支付记录
     * @return 结果
     */
    public int insertPaymentRecord(PaymentRecord paymentRecord);

    /**
     * 修改支付记录
     *
     * @param paymentRecord 支付记录
     * @return 结果
     */
    public int updatePaymentRecord(PaymentRecord paymentRecord);

    /**
     * 批量删除支付记录
     *
     * @param ids 需要删除的支付记录主键集合
     * @return 结果
     */
    public int deletePaymentRecordByIds(String ids);

    /**
     * 删除支付记录信息
     *
     * @param id 支付记录主键
     * @return 结果
     */
    public int deletePaymentRecordById(Long id);

    String selectBlockTransactionId(String orderId);
}
