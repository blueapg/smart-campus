package com.atguigu.campus.service;

import com.atguigu.campus.pojo.LoginForm;
import com.atguigu.campus.pojo.Student;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author WOLF
* @description 针对表【tb_student】的数据库操作Service
* @createDate 2023-03-08 21:35:35
*/
public interface StudentService extends IService<Student> {

    Student login(LoginForm loginForm);

    Student getStudentById(Long userId);

    IPage<Student> getStudentByOpr(Page<Student> page, Student student);
}
