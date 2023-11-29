package com.all.powerful.bot.enums;

/**
 * 用户状态
 *
 * @author all powerful
 */
public enum MessageType {
    START("/start"),
    HELP("/help"),
    VALUATION("/valuation"),
    BUY("/buy"),
    PAY_CANCEL("/pay_cancel"),
    PAY_CONFIRM("/pay_confirm"),
    PAY_INFO("/pay_info");

    private final String code;

    MessageType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
