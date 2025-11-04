package com.example.school_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.school_management.entity.Clazz;
import com.example.school_management.mapper.ClazzMapper;
import com.example.school_management.service.ClazzService;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("clazzServiceImpl")
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Override
    public IPage<Clazz> getClazzByOpr(Page<Clazz> pageParam, Clazz clazz) {
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper();
        String gradeName = clazz.getGradeName();
        if(!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("grade_name", gradeName);
        }
        String name = clazz.getName();
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name", name);
        }
        //根据 id 进行排序
        queryWrapper.orderByDesc("id");
       Page<Clazz> ClazzPage = baseMapper.selectPage(pageParam, queryWrapper);
       return ClazzPage;
    }

    @Override
    public List<Clazz> getClazz() {
        return baseMapper.selectList(null);
    }
}
