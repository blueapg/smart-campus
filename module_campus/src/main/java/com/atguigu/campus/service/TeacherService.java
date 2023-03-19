package com.atguigu.campus.service;

import com.atguigu.campus.pojo.LoginForm;
import com.atguigu.campus.pojo.Teacher;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author WOLF
 * @description 针对表【tb_teacher】的数据库操作Service
 * @createDate 2023-03-08 21:35:39
 */
public interface TeacherService extends IService<Teacher> {

    Teacher login(LoginForm loginForm);

    Teacher getTeacherById(Long userId);

    IPage<Teacher> getTeacherByOpr(Page<Teacher> page, Teacher teacher);
}
