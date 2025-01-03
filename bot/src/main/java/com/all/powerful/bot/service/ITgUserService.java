package com.all.powerful.bot.service;

import com.all.powerful.bot.domain.TgUser;

import java.math.BigDecimal;
import java.util.List;

/**
 * 客户列表Service接口
 *
 * @author all powerful
 * @date 2023-11-22
 */
public interface ITgUserService {
    /**
     * 查询客户列表
     *
     * @param id 客户列表主键
     * @return 客户列表
     */
    public TgUser selectTgUserById(Long id);

    public TgUser selectTgUserByUserId(String userId);

    /**
     * 查询客户列表列表
     *
     * @param tgUser 客户列表
     * @return 客户列表集合
     */
    public List<TgUser> selectTgUserList(TgUser tgUser);

    /**
     * 新增客户列表
     *
     * @param tgUser 客户列表
     * @return 结果
     */
    public int insertTgUser(TgUser tgUser);

    /**
     * 修改客户列表
     *
     * @param tgUser 客户列表
     * @return 结果
     */
    public int updateTgUser(TgUser tgUser);

    /**
     * 批量删除客户列表
     *
     * @param ids 需要删除的客户列表主键集合
     * @return 结果
     */
    public int deleteTgUserByIds(String ids);

    /**
     * 删除客户列表信息
     *
     * @param id 客户列表主键
     * @return 结果
     */
    public int deleteTgUserById(Long id);

    public int changeStatus(TgUser tgUser);

    void updateUserPayCount(String userId, BigDecimal payAmount);

    void updateUserPayoutAmount(String userId, BigDecimal amount);

    //      <!--      调整start      -->
    public int clearAll(String ids);
    //      <!--      调整end      -->
}
