package com.all.powerful.bot.domain;

import java.math.BigDecimal;
import com.all.powerful.common.annotation.Excel;
import com.all.powerful.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 汇率管理对象 exchange_rate
 *
 * @author all powerful
 * @date 2023-11-23
 */
public class ExchangeRate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 起始值 */
    @Excel(name = "起始值")
    private BigDecimal min;

    /** 结束值 */
    @Excel(name = "结束值")
    private BigDecimal max;

    /** 汇率 */
    @Excel(name = "汇率")
    private BigDecimal rate;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMin(BigDecimal min)
    {
        this.min = min;
    }

    public BigDecimal getMin()
    {
        return min;
    }
    public void setMax(BigDecimal max)
    {
        this.max = max;
    }

    public BigDecimal getMax()
    {
        return max;
    }
    public void setRate(BigDecimal rate)
    {
        this.rate = rate;
    }

    public BigDecimal getRate()
    {
        return rate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("min", getMin())
            .append("max", getMax())
            .append("rate", getRate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
