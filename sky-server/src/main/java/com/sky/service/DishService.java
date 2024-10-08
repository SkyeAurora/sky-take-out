package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;


public interface DishService {
    /**
     * 新增菜品
     */
    void addDish(DishDTO dishDTO);

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageSelectDish(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     *
     * @param ids
     */
    void deleteDishs(List<Long> ids);

    /**
     * 根据主键Id查询菜品数据
     * @param id
     * @return
     */
    DishVO selectById(Long id);

    /**
     * 修改菜品数据
     * @param dishDTO
     */
    void modifyDish(DishDTO dishDTO);

    /**
     * 设置菜品状态
     * @param status
     * @param id
     */
    void setStatus(Integer status, Long id);

    /**
     * 根据分类Id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> getByCategoryId(Long categoryId);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);


}
