package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;


    /**
     * 添加菜品或套餐到购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //得到当前用户的userId
        //在 com.sky.interceptor.JwtTokenUserInterceptor 中保存了当前登录用户的userId
        Long userId = BaseContext.getCurrentId();
        //判断当前商品是否在 当前用户的购物车 中存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(userId);

        List<ShoppingCart> carts = shoppingCartMapper.selectByExample(shoppingCart);
        //如果是，执行update操作更新number和amount
        if (carts != null && carts.size() != 0) {
            ShoppingCart cart = carts.get(0);

            BigDecimal amount = cart.getAmount();
            if (shoppingCartDTO.getDishId() != null) {
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                BigDecimal price = dish.getPrice();
                amount = amount.add(price);
            } else {
                Setmeal setmeal = setmealMapper.getById(cart.getSetmealId());
                BigDecimal price = setmeal.getPrice();
                amount = amount.add(price);
            }
            shoppingCartMapper.updateNumberById(cart.getId(), cart.getNumber() + 1, amount);
        } else {
            //如果不存在，初始化一个购物车数据 shoppingCart 取得userId  number初始化为1 createTime = now
            //这里不能使用AutoFill来自动初始化createTime,因为这个Insert操作不涉及到createUser操作
            if (shoppingCartDTO.getDishId() != null) {
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());

            } else {
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看当前用户的购物车菜品
     *
     * @return
     */
    @Override
    public List<ShoppingCart> selectByUserId() {
        Long userId = BaseContext.getCurrentId();

        return shoppingCartMapper.selectByUserId(userId);
    }

    /**
     * 删除购物车中的单个商品
     *
     * @param shoppingCartDTO
     */
    @Override
    @Transactional //涉及多次表操作，设置事务属性
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        //封装到ShoppingCart对象中再删除（因为还有userId）
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //直接删除 并得到删除的这个对象（要判断个数是否大于1，然后决定是否重新插入）
        List<ShoppingCart> carts = shoppingCartMapper.selectByExample(shoppingCart);
        ShoppingCart cart = carts.get(0);
        shoppingCartMapper.deleteById(shoppingCart);
        if(cart.getNumber() > 1)
        {
            //修改number 和 amount 并重新插入
            BigDecimal amount = cart.getAmount();
            if (shoppingCartDTO.getDishId() != null) {
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                BigDecimal price = dish.getPrice();
                amount = amount.subtract(price);
            } else {
                Setmeal setmeal = setmealMapper.getById(cart.getSetmealId());
                BigDecimal price = setmeal.getPrice();
                amount = amount.subtract(price);
            }
            cart.setNumber(cart.getNumber() - 1);
            cart.setAmount(amount);
            shoppingCartMapper.insert(cart);
        }
    }
}
