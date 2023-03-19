package com.atguigu.campus.service;

import com.atguigu.campus.pojo.Clazz;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author WOLF
* @description 针对表【tb_clazz】的数据库操作Service
* @createDate 2023-03-08 21:35:21
*/
public interface ClazzService extends IService<Clazz> {

    IPage<Clazz> getClazzByOpr(Page<Clazz> page, Clazz clazz);

    List<Clazz> getClazzs();
}
