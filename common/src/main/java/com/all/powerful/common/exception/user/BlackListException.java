package com.all.powerful.common.exception.user;

/**
 * 黑名单IP异常类
 *
 * @author all powerful
 */
public class BlackListException extends UserException
{
    private static final long serialVersionUID = 1L;

    public BlackListException()
    {
        super("login.blocked", null);
    }
}
