package com.all.powerful.bot.enums;

/**
 * 配置类型
 *
 * @author all powerful
 */
public enum TriggerType {
    /**
     * 打开指定链接
     */
    URL,

    /**
     * 发送指定图片 + 文本
     */
    CALLBACK_IMG,

    /**
     * 发送指定视频 + 文本
     */
    CALLBACK_VIDEO
}
