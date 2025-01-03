package com.all.powerful.bot.mapper;

import com.all.powerful.bot.domain.TgUser;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 客户列表Mapper接口
 *
 * @author all powerful
 * @date 2023-11-22
 */
public interface TgUserMapper {
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
     * 删除客户列表
     *
     * @param id 客户列表主键
     * @return 结果
     */
    public int deleteTgUserById(Long id);

    /**
     * 批量删除客户列表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTgUserByIds(String[] ids);

    void updateUserPayCount(@Param("userId") String userId, @Param("payAmount") BigDecimal payAmount);

    void updateUserPayoutAmount(@Param("userId") String userId, @Param("payAmount") BigDecimal payAmount);

    //      <!--      调整start      -->
    public int clearTgUserByIds(String[] ids);
    //      <!--      调整end      -->
}
