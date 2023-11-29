package com.all.powerful.bot.domain;

import com.all.powerful.common.annotation.Excel;
import com.all.powerful.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户列表对象 tg_user
 *
 * @author all powerful
 * @date 2023-11-22
 */
public class TgUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private String userId;

    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String username;

    /**
     * 昵称
     */
    @Excel(name = "昵称")
    private String nickname;

    /**
     * 是否管理员
     */
    @Excel(name = "是否管理员")
    private String isAdmin;

    /**
     * 总支付成功金额
     */
    @Excel(name = "共支付")
    private BigDecimal payAmount;

    /**
     * 总支付成功次数
     */
    @Excel(name = "成功交易")
    private Long payCount;

    @Excel(name = "共下发")
    private BigDecimal payoutCount;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;

    /**
     * 状态
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 状态
     */
    @Excel(name = "创建时间")
    private Date createTime;

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public BigDecimal getPayoutCount() {
        return payoutCount;
    }

    public void setPayoutCount(BigDecimal payoutCount) {
        this.payoutCount = payoutCount;
    }

    public void setPayCount(Long payCount) {
        this.payCount = payCount;
    }

    public Long getPayCount() {
        return payCount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("username", getUsername())
                .append("nickname", getNickname())
                .append("isAdmin", getIsAdmin())
                .append("payAmount", getPayAmount())
                .append("payCount", getPayCount())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
