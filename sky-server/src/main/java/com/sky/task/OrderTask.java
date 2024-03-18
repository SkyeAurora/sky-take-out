package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理订单超时
     */
    @Scheduled(cron = "0 * * * * ?") //每分钟触发一次
    public void processTimeOut() {
        log.info("定时处理超时订单:{}", LocalDateTime.now());

        //查询哪些订单处于待付款超过 15min
        //select * from orders where status = ? and order_time < (当前时间 -15min)
        List<Orders> orders = orderMapper.selectByStatus(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));

        if (orders != null && orders.size() > 0) {
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时，自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    /**
     * 处理持续派送中订单
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void processDeliveryOrder(){
        log.info("定时处理持续处于派送中的订单:{}",LocalDateTime.now());

        List<Orders> orders = orderMapper.selectByStatus(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusHours(-1));

        if (orders != null && orders.size() > 0) {
            for (Orders order : orders) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }
}
