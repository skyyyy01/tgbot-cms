package com.all.powerful.bot.domain;

import com.all.powerful.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * @author all powerful
 * @date 2023-11-19
 */
public class PayResponse extends BaseEntity {
    private static final long serialVersionUID = 1L;

    public String order_id;
    public BigDecimal amount;
    public BigDecimal actual_amount;
    public String token;
    public Long expiration_time;


}
