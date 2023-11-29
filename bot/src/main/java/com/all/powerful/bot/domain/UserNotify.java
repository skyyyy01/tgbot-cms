package com.all.powerful.bot.domain;

import com.all.powerful.common.annotation.Excel;
import com.all.powerful.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 通知管理对象 user_notify
 *
 * @author all powerful
 * @date 2023-11-27
 */
public class UserNotify extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 通知类型 */
    @Excel(name = "通知类型")
    private String notifyType;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    /** 通知图片 */
    @Excel(name = "通知图片")
    private String image;

    /** 通知内容 */
    @Excel(name = "通知内容")
    private String text;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setNotifyType(String notifyType)
    {
        this.notifyType = notifyType;
    }

    public String getNotifyType()
    {
        return notifyType;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }
    public void setImage(String image)
    {
        this.image = image;
    }

    public String getImage()
    {
        return image;
    }
    public void setText(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("notifyType", getNotifyType())
            .append("userId", getUserId())
            .append("image", getImage())
            .append("text", getText())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
