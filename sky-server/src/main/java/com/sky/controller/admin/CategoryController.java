package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result addCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类:{}", categoryDTO);

        categoryService.addCategory(categoryDTO);

        return Result.success();
    }

    /**
     * 根据 Id 删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据Id删除分类")
    public Result deleteCategory(Long id) {
        log.info("根据Id删除分类，Id:{}", id);

        categoryService.deleteCategory(id);

        return Result.success();
    }

    /**
     * 分页查询分类数据
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询分类数据")
    public Result<PageResult> pageSelectCategory(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分页查询分类数据，参数:{}", categoryPageQueryDTO);

        PageResult pageResult = categoryService.pageSelectCategory(categoryPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 启用、禁用分类
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用分类")
    public Result setStatus(@PathVariable Integer status, Long id) {
        log.info("启用、禁用分类,status: {},id: {}", status, id);

        categoryService.setStatus(status, id);

        return Result.success();
    }

    /**
     * 修改分类
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result modifyCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类，参数:{}", categoryDTO);

        categoryService.modifyCategory(categoryDTO);

        return Result.success();
    }

    /**
     * 根据种类获取分类数据 1 菜品 2 套餐
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类数据")
    public Result<List> getCategoryByType(Integer type) {
        log.info("查询分类数据");

        List<Category> list = categoryService.getCategoryById(type);

        return Result.success(list);
    }
}
