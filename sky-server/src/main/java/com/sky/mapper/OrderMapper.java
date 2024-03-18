package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 新增一条订单数据
     *
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * 更新
     *
     * @param orderStatus
     * @param orderPaidStatus
     * @param check_out_time
     * @param id
     */
    @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} where id = #{id}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime check_out_time, Long id);


    /**
     * 根据userId及状态分页查询历史订单信息
     *
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> selectAllOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据主键查询订单详情
     *
     * @param id
     * @return
     */
    @Select("select * from orders where id =#{id}")
    Orders selectById(Long id);

    /**
     * 统计各个状态的订单数量
     *
     * @param status
     * @return
     */
    @Select("select count(*) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 查询某个状态的订单
     *
     * @param status
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{time} ")
    List<Orders> selectByStatus(Integer status, LocalDateTime time);

    /**
     * 设置订单状态
     *
     * @param id
     */
    @Update("update orders set status = #{status} where id = #{id}")
    void setStatus(Long id, Integer status);

    /**
     * 计算某一时间段内的营业额
     * @param map
     * @return
     */
    Double sumByMap(Map map);
}
