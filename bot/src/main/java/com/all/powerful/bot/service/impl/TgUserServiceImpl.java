package com.all.powerful.bot.service.impl;

import com.all.powerful.bot.domain.TgUser;
import com.all.powerful.bot.mapper.TgUserMapper;
import com.all.powerful.bot.service.ITgUserService;
import com.all.powerful.common.core.text.Convert;
import com.all.powerful.common.utils.DateUtils;
import com.all.powerful.common.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 客户列表Service业务层处理
 *
 * @author all powerful
 * @date 2023-11-22
 */
@Service
public class TgUserServiceImpl implements ITgUserService {
    @Autowired
    private TgUserMapper tgUserMapper;

    /**
     * 查询客户列表
     *
     * @param id 客户列表主键
     * @return 客户列表
     */
    @Override
    public TgUser selectTgUserById(Long id) {
        return tgUserMapper.selectTgUserById(id);
    }

    /**
     * 查询客户列表
     *
     * @param userId 客户列表主键
     * @return 客户列表
     */
    @Override
    public TgUser selectTgUserByUserId(String userId) {
        return tgUserMapper.selectTgUserByUserId(userId);
    }


    /**
     * 查询客户列表列表
     *
     * @param tgUser 客户列表
     * @return 客户列表
     */
    @Override
    public List<TgUser> selectTgUserList(TgUser tgUser) {
        return tgUserMapper.selectTgUserList(tgUser);
    }

    /**
     * 新增客户列表
     *
     * @param tgUser 客户列表
     * @return 结果
     */
    @Override
    public int insertTgUser(TgUser tgUser) {
        tgUser.setCreateTime(DateUtils.getNowDate());
        return tgUserMapper.insertTgUser(tgUser);
    }

    /**
     * 修改客户列表
     *
     * @param tgUser 客户列表
     * @return 结果
     */
    @Override
    public int updateTgUser(TgUser tgUser) {
        tgUser.setUpdateTime(DateUtils.getNowDate());
        tgUser.setUpdateBy(ShiroUtils.getLoginName());
        return tgUserMapper.updateTgUser(tgUser);
    }

    /**
     * 批量删除客户列表
     *
     * @param ids 需要删除的客户列表主键
     * @return 结果
     */
    @Override
    public int deleteTgUserByIds(String ids) {
        return tgUserMapper.deleteTgUserByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除客户列表信息
     *
     * @param id 客户列表主键
     * @return 结果
     */
    @Override
    public int deleteTgUserById(Long id) {
        return tgUserMapper.deleteTgUserById(id);
    }


    @Override
    public int changeStatus(TgUser tgUser) {
        tgUser.setUpdateTime(DateUtils.getNowDate());
        tgUser.setUpdateBy(ShiroUtils.getSysUser().getLoginName());
        return tgUserMapper.updateTgUser(tgUser);
    }

    @Override
    public void updateUserPayCount(String userId, BigDecimal payAmount) {
        tgUserMapper.updateUserPayCount(userId, payAmount);
    }

    @Override
    public void updateUserPayoutAmount(String userId, BigDecimal payAmount) {
        tgUserMapper.updateUserPayoutAmount(userId, payAmount);
    }
}
