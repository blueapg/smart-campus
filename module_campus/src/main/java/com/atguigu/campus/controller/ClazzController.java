package com.atguigu.campus.controller;

import com.atguigu.campus.pojo.Clazz;
import com.atguigu.campus.service.ClazzService;
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
@RequestMapping("/sms/clazzController")
@Api(tags = "班级控制器")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    @GetMapping("/getClazzs")
    @ApiOperation("查询所有班级信息")
    public Result getClazzs() {
        List<Clazz> clazzs = clazzService.getClazzs();
        return Result.ok(clazzs);
    }

    @DeleteMapping("/deleteClazz")
    @ApiOperation("对班级进行删除和批量删除操作")
    public Result deleteClazz(
            @ApiParam("要删除的ids 集合") @RequestBody List<Integer> ids
    ) {
        clazzService.removeByIds(ids);
        return Result.ok();
    }

    @PostMapping("/saveOrUpdateClazz")
    @ApiOperation("添加或修改班级信息")
    public Result saveOrUpdateClazz(
            @ApiParam("json格式的班级信息") @RequestBody Clazz clazz
    ) {
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    @ApiOperation("查询班级信息  带分页查询")
    public Result getClazzByOpr(@ApiParam("分页查询的当前页数") @PathVariable Integer pageNo,
                                @ApiParam("分页查询每页数据的大小") @PathVariable Integer pageSize,
                                @ApiParam("json格式的班级信息") Clazz clazz
    ) {
        //分页查询
        Page<Clazz> page = new Page<>(pageNo, pageSize);
        //通过服务层查询数据 带分页
        IPage<Clazz> pageRs = clazzService.getClazzByOpr(page, clazz);
        return Result.ok(pageRs);
    }
}
