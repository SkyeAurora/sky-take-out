package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单明细数据
     * @param list
     */
    void insertBatch(List<OrderDetail> list);

    /**
     * 根据OrderId查询订单明细数据
     * @param orderId
     * @return
     */
    @Select("select * from order_detail where order_id = #{orderId} ")
    List<OrderDetail> selectByOrderId(Long orderId);
}
