package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {

    /**
     * 新增分类
     *
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 根据 Id 删除分类
     *
     * @param id
     */
    void deleteCategory(Long id);

    /**
     * 分页查询分类数据
     *
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageSelectCategory(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用、禁用分类
     * @param status
     * @param id
     */
    void setStatus(Integer status,Long id);

    /**
     * 修改分类
     * @param categoryDTO
     */
    void modifyCategory(CategoryDTO categoryDTO);

    /**
     * 查询所有分类数据
     *
     * @return
     */
    List<Category> getCategory();
}
