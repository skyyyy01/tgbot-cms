package com.all.powerful.bot.domain;

import com.all.powerful.common.annotation.Excel;
import com.all.powerful.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 下发记录对象 payout_record
 *
 * @author all powerful
 * @date 2023-11-25
 */
public class PayoutRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String orderId;

    /**
     * 电报ID
     */
    @Excel(name = "电报ID")
    private String userId;

    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String username;

    /**
     * 网名
     */
    @Excel(name = "网名")
    private String nickname;

    /**
     * 支付金额
     */
    @Excel(name = "支付金额")
    private BigDecimal amount;

    /** 实际支付金额 */
    @Excel(name = "实际支付金额")
    private BigDecimal actualAmount;

    /**
     * 支付状态
     */
    @Excel(name = "支付状态")
    private String status;

    /**
     * 应下发
     */
    @Excel(name = "应下发")
    private BigDecimal payAmount;

    /**
     * 下发状态
     */
    @Excel(name = "下发状态")
    private String pStatus;

    /**
     * 标红
     */
    @Excel(name = "标红")
    private String isRed;

    /**
     * 下发地址
     */
    @Excel(name = "下发地址")
    private String address;

    /**
     * 交易哈希
     */
    @Excel(name = "交易哈希")
    private String hash;

    public String getIsRed() {
        return isRed;
    }

    public void setIsRed(String isRed) {
        this.isRed = isRed;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setpStatus(String pStatus) {
        this.pStatus = pStatus;
    }

    public String getpStatus() {
        return pStatus;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("orderId", getOrderId())
                .append("userId", getUserId())
                .append("username", getUsername())
                .append("nickname", getNickname())
                .append("amount", getAmount())
                .append("status", getStatus())
                .append("payAmount", getPayAmount())
                .append("pStatus", getpStatus())
                .append("address", getAddress())
                .append("hash", getHash())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
