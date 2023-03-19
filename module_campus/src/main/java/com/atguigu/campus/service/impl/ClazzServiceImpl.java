package com.atguigu.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.campus.pojo.Clazz;
import com.atguigu.campus.service.ClazzService;
import com.atguigu.campus.mapper.ClazzMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author WOLF
 * @description 针对表【tb_clazz】的数据库操作Service实现
 * @createDate 2023-03-08 21:35:21
 */
@Service
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz>
        implements ClazzService {

    @Override
    public IPage<Clazz> getClazzByOpr(Page<Clazz> page, Clazz clazz) {
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();
        String gradeName = clazz.getGradeName();
        String clazzName = clazz.getName();
        if (!("".equalsIgnoreCase(gradeName) || null == gradeName)) {
            queryWrapper.like("grade_name", gradeName);
        }
        if (!("".equalsIgnoreCase(clazzName) || null == clazzName)) {
            queryWrapper.like("name", clazzName);
        }
        queryWrapper.orderByDesc("id");

        Page<Clazz> clazzPage = baseMapper.selectPage(page, queryWrapper);
        return clazzPage;
    }

    @Override
    public List<Clazz> getClazzs() {
        return baseMapper.selectList(null);
    }
}




