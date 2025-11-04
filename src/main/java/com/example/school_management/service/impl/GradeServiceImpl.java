package com.example.school_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school_management.entity.Grade;
import com.example.school_management.mapper.GradeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.school_management.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Transactional
@Service("gradeServiceImpl")
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    @Override
    public IPage<Grade> getGreadByOPr(Page<Grade> pageParam, String gradename) {

        QueryWrapper<Grade> queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(gradename)){
            queryWrapper.like("name", gradename);
        }
        queryWrapper.orderByDesc("id");

        Page<Grade> page = baseMapper.selectPage(pageParam, queryWrapper);

        return page;
    }

    @Override
    public List<Grade> getGrades() {
        return baseMapper.selectList(null);
    }
}
