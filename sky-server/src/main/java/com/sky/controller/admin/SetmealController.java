package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation("新增套餐")
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐:{}", setmealDTO);

        setmealService.addSetmeal(setmealDTO);

        return Result.success();
    }

    /**
     * 分页查询套餐数据
     *
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询套餐数据")
    public Result<PageResult> pageSelectSetmeal(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询套餐数据,{}", setmealPageQueryDTO);

        PageResult pageResult = setmealService.pageSelectSetmeal(setmealPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 批量删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public Result deleteSetmeals(@RequestParam List<Long> ids) {
        log.info("批量删除套餐:{}", ids);

        setmealService.deleteSetmeals(ids);

        return Result.success();
    }

    /**
     * 根据主键Id查询套餐
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据主键Id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据主键Id查询套餐,id:{}", id);

        SetmealVO setmealVO = setmealService.getById(id);

        return Result.success(setmealVO);
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改套餐")
    public Result modifySetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐:{}",setmealDTO);

        setmealService.modifySetmeal(setmealDTO);

        return Result.success();
    }

    /**
     * 设置套餐起售、停售
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("设置套餐起售、停售")
    public Result setStatus(@PathVariable Integer status,Long id){
        log.info("修改套餐状态,id:{},status:{}",id,status);

        setmealService.setStatus(status,id);

        return Result.success();
    }
}
