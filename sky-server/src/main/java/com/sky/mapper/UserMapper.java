package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;


@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户是否存在
     *
     * @param openid
     * @return
     */
    @Select("select * from user where openid=#{openid}")
    User selectByOpenId(String openid);

    /**
     * 新增微信用户
     *
     * @param user
     */
    void insert(User user);


    /**
     * 根据userid查询用户
     *
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    /**
     * 计算用户数量
     *
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
