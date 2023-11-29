package com.all.powerful.bot.domain;

import com.all.powerful.common.annotation.Excel;
import com.all.powerful.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.web.multipart.MultipartFile;

/**
 * 机器人配置对象 bot_config
 *
 * @author all powerful
 * @date 2023-11-19
 */
public class BotConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    private Long configId;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置名称
     */
    private String parentName;

    /**
     * 按钮显示名称
     */
    @Excel(name = "按钮显示名称")
    private String showName;

    /**
     * 配置类型
     */
    @Excel(name = "配置类型")
    private String configType;

    /**
     * 父配置ID
     */
    @Excel(name = "父配置ID")
    private Long parentId;

    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    private Long orderNum;

    /**
     * 自定义请求地址
     */
    @Excel(name = "自定义请求地址")
    private String url;

    /**
     * 自定义图片
     */
    @Excel(name = "自定义图片")
    private String image;

    /**
     * 自定义视频
     */
    @Excel(name = "自定义视频")
    private String video;

    /**
     * 自定义文字
     */
    @Excel(name = "自定义文字")
    private String text;

    /**
     * 触发方式
     */
    @Excel(name = "触发方式")
    private String triggerType;

    /**
     * 按钮回调编码
     */
    @Excel(name = "按钮回调编码")
    private String callbackData;

    /**
     * 信息回调文本
     */
    @Excel(name = "信息回调文本")
    private String callbackText;

    /**
     * 配置状态
     */
    @Excel(name = "配置状态")
    private String visible;

    private MultipartFile file;

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public Long getConfigId() {
        return configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowName() {
        return showName;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getConfigType() {
        return configType;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideo() {
        return video;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public void setCallbackText(String callbackText) {
        this.callbackText = callbackText;
    }

    public String getCallbackText() {
        return callbackText;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getVisible() {
        return visible;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("configId", getConfigId())
                .append("showName", getShowName())
                .append("configType", getConfigType())
                .append("parentId", getParentId())
                .append("orderNum", getOrderNum())
                .append("url", getUrl())
                .append("image", getImage())
                .append("video", getVideo())
                .append("text", getText())
                .append("triggerType", getTriggerType())
                .append("callbackData", getCallbackData())
                .append("callbackText", getCallbackText())
                .append("visible", getVisible())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
