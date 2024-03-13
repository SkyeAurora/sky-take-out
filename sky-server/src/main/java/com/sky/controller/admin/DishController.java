package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO) {

        dishService.addDish(dishDTO);

        return Result.success();
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> pageSelectDish(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询，参数:{}", dishPageQueryDTO);

        PageResult pageResult = dishService.pageSelectDish(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteDishes(@RequestParam List<Long> ids) {   //@RequestParam 注解可以自动将输入的1,2,3类型的数据转换封装到List中

        log.info("批量删除菜品,id:{}", ids);

        dishService.deleteDishs(ids);

        return Result.success();
    }

    /**
     * 根据主键Id查询菜品数据
     *
     * @param id
     * @return DishVO
     */
    @GetMapping("/{id}")
    @ApiOperation("根据主键Id查询菜品数据")
    public Result<DishVO> selectById(@PathVariable Long id) {

        log.info("根据主键Id查询菜品数据,id:{}", id);

        DishVO dishVO = dishService.selectById(id);

        return Result.success(dishVO);
    }

    /**
     * 修改菜品数据
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品数据")
    public Result modifyDish(@RequestBody DishDTO dishDTO) {

        log.info("修改菜品数据:{}", dishDTO);

        dishService.modifyDish(dishDTO);

        return Result.success();
    }

}
