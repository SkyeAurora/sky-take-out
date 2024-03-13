package com.sky.mapper;

import com.sky.annoation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {

    /**
     * 统计分类下 category_id 对应的菜品的个数
     *
     * @param categoryId
     */
    @Select("select count(*) from setmeal where category_id = #{categoryId}")
    public Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品
     *
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);
}
