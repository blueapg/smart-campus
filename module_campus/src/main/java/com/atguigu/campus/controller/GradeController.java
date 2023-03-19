package com.atguigu.campus.controller;

import com.atguigu.campus.pojo.Grade;
import com.atguigu.campus.service.GradeService;
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
@RequestMapping("/sms/gradeController")
@Api(tags = "Grade年级管理器")
public class GradeController {
    @Autowired
    private GradeService gradeService;

    @GetMapping("/getGrades")
    @ApiOperation("获取所有年级信息")
    public Result getGrades(){
        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }

    @DeleteMapping("/deleteGrade")
    @ApiOperation("删除年级操作")
    public Result deleteGrade(
            @ApiParam("要删除的年级ids List集合") @RequestBody List<Integer> ids
    ){
        gradeService.removeByIds(ids);
        return Result.ok();
    }

    @PostMapping("/saveOrUpdateGrade")
    @ApiOperation("添加新年级、修改年级信息")
    public Result saveOrUpdateGrade(@RequestBody Grade grade) {
        //接收参数 json==》反序列化问grade对象

        //调用服务层方法
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    @ApiOperation("获取所有年级的信息 分页查询、模糊查询")
    public Result getGrades(@ApiParam("分页查询的页码 第几页") @PathVariable("pageNo") Integer pageNo,
                            @ApiParam("分页查询每页显示数据的大小") @PathVariable("pageSize") Integer pageSize,
                            @ApiParam("模糊查询的年级名字") String gradeName
    ) {
        //分页 带条件查询
        Page<Grade> page = new Page<>(pageNo, pageSize);
        //通过服务层查询分页数据
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page, gradeName);

        //封装Result对象并返回
        return Result.ok(pageRs);
    }
}
