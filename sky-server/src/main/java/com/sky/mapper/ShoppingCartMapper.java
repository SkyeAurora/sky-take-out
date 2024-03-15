package com.sky.mapper;


import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 添加购物车
     *
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 动态条件查询数据
     *
     * @param shoppingCart
     */
    List<ShoppingCart> selectByExample(ShoppingCart shoppingCart);


    /**
     * 对已存在的商品数量自增
     *
     * @param id
     * @param number
     */
    void updateNumberById(Long id, int number, BigDecimal amount);

    /**
     * 根据userId查询购物车菜品
     *
     * @param userId
     * @return
     */
    @Select("select * from shopping_cart where user_id = #{userId}")
    List<ShoppingCart> selectByUserId(Long userId);

    /**
     * 删除指定商品
     * @param shoppingCart
     * @return
     */

    void deleteById(ShoppingCart shoppingCart);
}
