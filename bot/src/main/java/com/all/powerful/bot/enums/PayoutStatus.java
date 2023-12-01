package com.all.powerful.bot.enums;

/**
 * 用户状态
 *
 * @author all powerful
 */
public enum PayoutStatus {
    DONE(0, "下发成功"),
    WAIT(1, "等待下发"),
    VERIFY(2, "等待下发"),
    FAIL(3, "审核失败");

    private final int code;
    private final String info;

    PayoutStatus(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static String getInfoByCode(int code) {
        for (PayoutStatus status : PayoutStatus.values()) {
            if (status.getCode() == code) {
                return status.info;
            }
        }
        return ""; // 如果没有找到匹配的 code，返回 null 或者抛出异常，取决于你的需求
    }
}
