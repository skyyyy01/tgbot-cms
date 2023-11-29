package com.all.powerful.bot.domain;

import com.all.powerful.common.annotation.Excel;
import com.all.powerful.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 机器人列表对象 bot_list
 *
 * @author all powerful
 * @date 2023-11-19
 */
public class BotList extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 机器人ID
     */
    private Long botId;

    /**
     * 机器人用户名
     */
    @Excel(name = "机器人用户名")
    private String botUserName;

    /**
     * 机器人TOKEN
     */
    @Excel(name = "机器人TOKEN")
    private String botToken;

    /**
     * 机器人状态
     */
    @Excel(name = "机器人状态")
    private String status;

    public void setBotId(Long botId) {
        this.botId = botId;
    }

    public Long getBotId() {
        return botId;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public String getBotUserName() {
        return botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("botId", getBotId())
                .append("botUserName", getBotUserName())
                .append("botToken", getBotToken())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
