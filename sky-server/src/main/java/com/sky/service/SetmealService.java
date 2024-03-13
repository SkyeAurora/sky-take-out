package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐
     *
     * @param setmealDTO
     */
    void addSetmeal(SetmealDTO setmealDTO);

    /**
     * 分页查询套餐数据
     *
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageSelectSetmeal(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     *
     * @param ids
     */
    void deleteSetmeals(List<Long> ids);
}
