package com.all.powerful.bot.mapper;

import java.util.List;
import com.all.powerful.bot.domain.UserNotify;

/**
 * 通知管理Mapper接口
 * 
 * @author all powerful
 * @date 2023-11-27
 */
public interface UserNotifyMapper 
{
    /**
     * 查询通知管理
     * 
     * @param id 通知管理主键
     * @return 通知管理
     */
    public UserNotify selectUserNotifyById(Long id);

    /**
     * 查询通知管理列表
     * 
     * @param userNotify 通知管理
     * @return 通知管理集合
     */
    public List<UserNotify> selectUserNotifyList(UserNotify userNotify);

    /**
     * 新增通知管理
     * 
     * @param userNotify 通知管理
     * @return 结果
     */
    public int insertUserNotify(UserNotify userNotify);

    /**
     * 修改通知管理
     * 
     * @param userNotify 通知管理
     * @return 结果
     */
    public int updateUserNotify(UserNotify userNotify);

    /**
     * 删除通知管理
     * 
     * @param id 通知管理主键
     * @return 结果
     */
    public int deleteUserNotifyById(Long id);

    /**
     * 批量删除通知管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserNotifyByIds(String[] ids);
}
