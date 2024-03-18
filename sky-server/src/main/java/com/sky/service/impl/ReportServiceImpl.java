package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 指定时间区间内营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> dateList = getDateList(begin, end);
        //计算并存放每天的营业额 -> turnoverList
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            //根据LocalDate构造具体的LocalDateTime对象
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);//获得date这一天的开始时间对象
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);//获得date这一天的结束时间对象
            //查表得到这一天的营业额 使用聚合函数
            //select sum(amount) from orders where order_time > ? and order_time < ? and status = 5
            //用Map封装搜索条件
            Map map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            //营业额为0，返回空
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 用户数据统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> dateList = getDateList(begin, end);

        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            //根据LocalDate构造具体的LocalDateTime对象
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);//获得date这一天的开始时间对象
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);//获得date这一天的结束时间对象
            Map map = new HashMap<>();
            map.put("end", endTime);
            //统计总用户数量
            Integer totalUser = orderMapper.countByMap(map);
            //统计新用户数量
            map.put("begin", beginTime);
            Integer newUser = orderMapper.countByMap(map);

            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);
        List<Integer> orderCountList = new ArrayList<>();    //每日订单数
        List<Integer> validOrderCountList = new ArrayList<>();   //每日有效订单数
        Integer totalOrderCount = 0;    //订单总数
        Integer validOrderCount = 0;    //有效订单数
        Double orderCompletionRate=0.0;//订单完成率
        for (LocalDate date : dateList) {
            //根据LocalDate构造具体的LocalDateTime对象
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);//获得date这一天的开始时间对象
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);//获得date这一天的结束时间对象
            Map map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            //select count * from orders where order_time < ? and order_time > ?
            //总订单数
            Integer totalOrders = orderMapper.countOrdersByMap(map);
            map.put("status", Orders.COMPLETED);
            //有效订单数
            Integer validOrders = orderMapper.countOrdersByMap(map);
            totalOrderCount += totalOrders;
            validOrderCount += validOrders;
            orderCountList.add(totalOrders);
            validOrderCountList.add(validOrders);
        }
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        //校验
        if (begin.isAfter(end)) {
            return null;
        }
        //begin -> end 之间的所有日期
        List<LocalDate> dateList = new ArrayList<>();
        //为dateList赋值
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }
}
