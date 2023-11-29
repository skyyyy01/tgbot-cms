package com.all.powerful.bot.mapper;

import java.math.BigDecimal;
import java.util.List;
import com.all.powerful.bot.domain.ExchangeRate;

/**
 * 汇率管理Mapper接口
 *
 * @author all powerful
 * @date 2023-11-23
 */
public interface ExchangeRateMapper
{
    /**
     * 查询汇率管理
     *
     * @param id 汇率管理主键
     * @return 汇率管理
     */
    public ExchangeRate selectExchangeRateById(Long id);

    /**
     * 查询汇率管理列表
     *
     * @param exchangeRate 汇率管理
     * @return 汇率管理集合
     */
    public List<ExchangeRate> selectExchangeRateList(ExchangeRate exchangeRate);

    /**
     * 新增汇率管理
     *
     * @param exchangeRate 汇率管理
     * @return 结果
     */
    public int insertExchangeRate(ExchangeRate exchangeRate);

    /**
     * 修改汇率管理
     *
     * @param exchangeRate 汇率管理
     * @return 结果
     */
    public int updateExchangeRate(ExchangeRate exchangeRate);

    /**
     * 删除汇率管理
     *
     * @param id 汇率管理主键
     * @return 结果
     */
    public int deleteExchangeRateById(Long id);

    /**
     * 批量删除汇率管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExchangeRateByIds(String[] ids);

    BigDecimal selectExchangeRate(BigDecimal amount);
}
