package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annoation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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


    /**
     * 分页查询套餐数据
     *
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据主键查询套餐数据
     *
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 批量删除
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据主键Id查询套餐数据
     *
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    SetmealVO selectById(Long id);
}
