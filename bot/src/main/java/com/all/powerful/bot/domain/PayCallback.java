package com.all.powerful.bot.domain;

import com.all.powerful.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 支付回调对象
 *
 * @author all powerful
 * @date 2023-11-25
 */
public class PayCallback extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * trade_id
     */
    private String trade_id;

    /**
     * order_id
     */
    private String order_id;

    /**
     * amount
     */
    private BigDecimal amount;

    /**
     * actual_amount
     */
    private BigDecimal actual_amount;

    /**
     * token
     */
    private String token;

    /**
     * block_transaction_id
     */
    private String block_transaction_id;

    /**
     * signature
     */
    private String signature;

    /**
     * status
     */
    private Integer status;

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getActual_amount() {
        return actual_amount;
    }

    public void setActual_amount(BigDecimal actual_amount) {
        this.actual_amount = actual_amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBlock_transaction_id() {
        return block_transaction_id;
    }

    public void setBlock_transaction_id(String block_transaction_id) {
        this.block_transaction_id = block_transaction_id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PayCallback{" +
                "trade_id='" + trade_id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", amount=" + amount +
                ", actual_amount=" + actual_amount +
                ", token='" + token + '\'' +
                ", block_transaction_id='" + block_transaction_id + '\'' +
                ", signature='" + signature + '\'' +
                ", status=" + status +
                '}';
    }
}
