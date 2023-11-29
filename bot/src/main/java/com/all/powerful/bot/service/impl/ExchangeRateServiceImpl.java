package com.all.powerful.bot.service.impl;

import com.all.powerful.bot.domain.ExchangeRate;
import com.all.powerful.bot.mapper.ExchangeRateMapper;
import com.all.powerful.bot.service.IExchangeRateService;
import com.all.powerful.common.core.text.Convert;
import com.all.powerful.common.utils.DateUtils;
import com.all.powerful.common.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 汇率管理Service业务层处理
 *
 * @author all powerful
 * @date 2023-11-23
 */
@Service
public class ExchangeRateServiceImpl implements IExchangeRateService {
    @Autowired
    private ExchangeRateMapper exchangeRateMapper;

    /**
     * 查询汇率管理
     *
     * @param id 汇率管理主键
     * @return 汇率管理
     */
    @Override
    public ExchangeRate selectExchangeRateById(Long id) {
        return exchangeRateMapper.selectExchangeRateById(id);
    }

    /**
     * 查询汇率管理列表
     *
     * @param exchangeRate 汇率管理
     * @return 汇率管理
     */
    @Override
    public List<ExchangeRate> selectExchangeRateList(ExchangeRate exchangeRate) {
        return exchangeRateMapper.selectExchangeRateList(exchangeRate);
    }

    /**
     * 新增汇率管理
     *
     * @param exchangeRate 汇率管理
     * @return 结果
     */
    @Override
    public int insertExchangeRate(ExchangeRate exchangeRate) {
        exchangeRate.setCreateTime(DateUtils.getNowDate());
        exchangeRate.setCreateBy(ShiroUtils.getLoginName());
        return exchangeRateMapper.insertExchangeRate(exchangeRate);
    }

    /**
     * 修改汇率管理
     *
     * @param exchangeRate 汇率管理
     * @return 结果
     */
    @Override
    public int updateExchangeRate(ExchangeRate exchangeRate) {
        exchangeRate.setUpdateTime(DateUtils.getNowDate());
        exchangeRate.setUpdateBy(ShiroUtils.getLoginName());
        return exchangeRateMapper.updateExchangeRate(exchangeRate);
    }

    /**
     * 批量删除汇率管理
     *
     * @param ids 需要删除的汇率管理主键
     * @return 结果
     */
    @Override
    public int deleteExchangeRateByIds(String ids) {
        return exchangeRateMapper.deleteExchangeRateByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除汇率管理信息
     *
     * @param id 汇率管理主键
     * @return 结果
     */
    @Override
    public int deleteExchangeRateById(Long id) {
        return exchangeRateMapper.deleteExchangeRateById(id);
    }

    @Override
    public BigDecimal selectExchangeRate(BigDecimal amount) {
        return exchangeRateMapper.selectExchangeRate(amount);
    }
}
