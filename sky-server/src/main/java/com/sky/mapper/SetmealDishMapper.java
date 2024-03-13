package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品Id查询对应的套餐Id
     * @return
     */
    public List<Long> getSetmealIdByDishIds(List<Long> dishIds);

    /**
     * 插入多条套餐相关菜品数据
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
