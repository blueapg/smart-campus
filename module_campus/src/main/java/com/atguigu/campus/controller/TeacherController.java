package com.atguigu.campus.controller;

import com.atguigu.campus.pojo.Teacher;
import com.atguigu.campus.service.TeacherService;
import com.atguigu.campus.utils.MD5;
import com.atguigu.campus.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Linda
 * @version 1.0
 */
@RestController
@RequestMapping("/sms/teacherController")
@Api(tags = "教师控制器")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(List<Integer> ids){
        teacherService.removeByIds(ids);
        return Result.ok();
    }

    @PostMapping("/saveOrUpdateTeacher")
    @ApiOperation("添加新教师、修改教师信息")
    public Result saveOrUpdateTeacher(@RequestBody Teacher teacher){
        teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }


    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    @ApiOperation("获取老师的信息 模糊查询、分页查询")
    public Result getTeachers(
            @ApiParam("分页当前页码数") @PathVariable Integer pageNo,
            @ApiParam("分页每页显示数据大小") @PathVariable Integer pageSize,
            Teacher teacher
    ) {
        Page<Teacher> page = new Page<>(pageNo, pageSize);
        IPage<Teacher> pageRs = teacherService.getTeacherByOpr(page, teacher);
        return Result.ok(pageRs);
    }
}
