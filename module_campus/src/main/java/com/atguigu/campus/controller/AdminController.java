package com.atguigu.campus.controller;

import com.atguigu.campus.pojo.Admin;
import com.atguigu.campus.pojo.Grade;
import com.atguigu.campus.service.AdminService;
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
@RequestMapping("/sms/adminController")
@Api("管理员操作层")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @DeleteMapping("/deleteAdmin")
    @ApiOperation("删除admin对象 单个删除、批量删除")
    public Result deleteAdmin(
            @ApiParam("要删除的admin对象id集合") @RequestBody List<Integer> ids
    ){
        adminService.removeByIds(ids);
        return Result.ok();
    }

    @PostMapping("/saveOrUpdateAdmin")
    @ApiOperation("添加新的admin、修改admin信息")
    public Result saveOrUpdateAdmin(@RequestBody Admin admin){
        //接收参数 json==》反序列化问grade对象
        //调用服务层方法
        admin.setPassword(MD5.encrypt(admin.getPassword()));
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    @ApiOperation("获取所有管理员的信息 分页查询、模糊查询")
    public Result getAllAdmin(
            @ApiParam("分页查询的页码 第几页") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询每页显示数据的大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("模糊查询的admin名字") String adminName
    ){
        //分页 带条件查询
        Page<Admin> page = new Page<>(pageNo, pageSize);
        //通过服务层查询分页数据
        IPage<Admin> pageRs = adminService.getAdminByOpr(page, adminName);

        //封装Result对象并返回
        return Result.ok(pageRs);
    }


}
