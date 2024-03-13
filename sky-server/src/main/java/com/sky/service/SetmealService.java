package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

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

    /**
     * 根据主键Id查询套餐
     *
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    void modifySetmeal(SetmealDTO setmealDTO);

    /**
     * 设置套餐起售、停售
     *
     * @param status
     * @param id
     */
    void setStatus(Integer status, Long id);
}
