package com.atguigu.campus.controller;

import com.atguigu.campus.pojo.Student;
import com.atguigu.campus.service.StudentService;
import com.atguigu.campus.utils.MD5;
import com.atguigu.campus.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@RequestMapping("/sms/studentController")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @DeleteMapping("/delStudentById")
    @ApiOperation("删除和批量删除学生信息")
    public Result deleteStudent(
            @ApiParam("要删除的ids 集合") @RequestBody List<Integer> ids
    ){
        studentService.removeByIds(ids);
        return Result.ok();
    }

    @PostMapping("/addOrUpdateStudent")
    @ApiOperation("teacher、admin权限下的添加修改学生信息")
    public Result addOrUpdateStudent(@RequestBody Student student){
        student.setPassword(MD5.encrypt(student.getPassword()));
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    @ApiOperation("查询所有学生信息 分页查询")
    public Result getStudentByOpr(
            @PathVariable Integer pageNo,
            @PathVariable Integer pageSize,
            Student student
    ){
        Page<Student> page = new Page<>(pageNo, pageSize);
        IPage<Student> studentPages =studentService.getStudentByOpr(page,student);
        return Result.ok(studentPages);
    }
}
