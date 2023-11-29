package com.all.powerful.bot.service;

import com.all.powerful.bot.domain.PayResponse;

import java.math.BigDecimal;

/**
 * @author all powerful
 * @date 2023-11-19
 */
public interface IPayService {
    PayResponse createTransaction(String orderId, BigDecimal amount);
}
