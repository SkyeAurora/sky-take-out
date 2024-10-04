package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 密码比对
        // 对前端传入的明文密码进行md5加密处理
        String securePassword = employee.getPassword();
        String[] split = securePassword.split("\\$");
        String salt = split[1];
        String finalPassword = split[0];
        if (!finalPassword.equals(DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8)))) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     */
    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //使用对象属性拷贝 employeeDTO -> employee
        BeanUtils.copyProperties(employeeDTO, employee);

        employee.setStatus(StatusConstant.ENABLE);  //账号默认正常状态 StatusConstant.ENABLE
        //设置默认密码 123456 md5加密后存入数据库
        String salt = UUID.randomUUID().toString();
        employee.setPassword(DigestUtils.md5DigestAsHex((PasswordConstant.DEFAULT_PASSWORD + salt).getBytes(StandardCharsets.UTF_8)) + "$" + salt);
        //设置当前记录创建人id和修改人id
        employeeMapper.insert(employee);
    }

    /**
     * 分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageSelectEmployee(EmployeePageQueryDTO employeePageQueryDTO) {
        //分页查询底层原理: select * from employee limit 0,10
        //使用 PageHelper 简化分页操作 调用 PageHelper.startPage() 方法，传入参数 page ，pageSize 进行自动分页
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        //PageHelper 底层也是基于 ThreadLocal 传递分页参数来实现的
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        //调用这两个参数获取结果
        long total = page.getTotal();
        List<Employee> records = page.getResult();
        //将结果封装到PageResult中并返回
        return new PageResult(total, records);
    }

    /**
     * 设置员工账号状态
     *
     * @param status
     * @param id
     */
    @Override
    public void setStatus(Integer status, Long id) {
        // update employee set status = ? where id = ?

        /*   原始方法
        Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);
        */

        // build 方法   (需加 @Builder 注解)
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        employeeMapper.update(employee);
    }

    /**
     * 编辑员工信息
     *
     * @param employeeDTO
     */
    @Override
    public void modifyEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        BeanUtils.copyProperties(employeeDTO, employee);

        employeeMapper.update(employee);
    }

    /**
     * 根据 Id 查询员工信息
     *
     * @param id
     * @return
     */
    @Override
    public Employee getEmployeeById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        employee.setPassword("******");
        return employee;
    }
}
