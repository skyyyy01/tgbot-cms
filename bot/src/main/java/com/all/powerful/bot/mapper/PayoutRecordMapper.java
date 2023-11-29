package com.all.powerful.bot.mapper;

import com.all.powerful.bot.domain.PayoutRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 下发记录Mapper接口
 *
 * @author all powerful
 * @date 2023-11-25
 */
public interface PayoutRecordMapper {
    /**
     * 查询下发记录
     *
     * @param id 下发记录主键
     * @return 下发记录
     */
    public PayoutRecord selectPayoutRecordById(Long id);

    /**
     * 查询下发记录列表
     *
     * @param payoutRecord 下发记录
     * @return 下发记录集合
     */
    public List<PayoutRecord> selectPayoutRecordList(PayoutRecord payoutRecord);

    /**
     * 新增下发记录
     *
     * @param payoutRecord 下发记录
     * @return 结果
     */
    public int insertPayoutRecord(PayoutRecord payoutRecord);

    /**
     * 修改下发记录
     *
     * @param payoutRecord 下发记录
     * @return 结果
     */
    public int updatePayoutRecord(PayoutRecord payoutRecord);

    /**
     * 删除下发记录
     *
     * @param id 下发记录主键
     * @return 结果
     */
    public int deletePayoutRecordById(Long id);

    /**
     * 批量删除下发记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePayoutRecordByIds(String[] ids);

    int updatePayoutRecordByOrderId(PayoutRecord payoutRecord);

    List<PayoutRecord> selectPayoutRecordListForUser(PayoutRecord payoutRecord);

    int checkLimit(@Param("address") String address, @Param("resultDate") Date resultDate);
}
