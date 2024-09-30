package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

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
        String key = "dish_" + dishDTO.getCategoryId();
        clearCache(key);

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
        clearCache("dish_*");

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
        clearCache("dish_*");

        return Result.success();
    }

    /**
     * 设置菜品状态 1 起售 0 停售
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("设置菜品起售、停售")
    public Result setStatus(@PathVariable Integer status, Long id) {

        log.info("设置菜品状态,{},{}", status, id);

        dishService.setStatus(status, id);

        clearCache("dish_*");

        return Result.success();
    }

    /**
     * 根据分类Id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类Id查询菜品")
    public Result<List> getByCategoryId(Long categoryId) {
        log.info("根据分类Id查询菜品,categoryId:{}", categoryId);

        List<Dish> list = dishService.getByCategoryId(categoryId);

        return Result.success(list);
    }

    /**
     * 清理 Redis 缓存数据
     *
     * @param pattern
     */
    private void clearCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
