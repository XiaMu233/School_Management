package com.example.school_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.school_management.entity.LoginForm;
import com.example.school_management.mapper.TeacherMapper;
import com.example.school_management.entity.Teacher;
import com.example.school_management.service.TeacherService;
import com.example.school_management.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("teacherServiceImpl")
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Teacher teacher = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    @Override
    public Teacher getTeacherById(Long userID) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<Teacher>();
        queryWrapper.eq("id", userID);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<Teacher> getTeacherByOpr(Page<Teacher> pageParam, Teacher teacher) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(teacher.getName())){
            queryWrapper.like("name", teacher.getName());
        }
        if(!StringUtils.isEmpty(teacher.getClazzName())){
            queryWrapper.like("clazz_name", teacher.getClazzName());
        }
        queryWrapper.orderByDesc("id");
        Page<Teacher> teacherPage = baseMapper.selectPage(pageParam, queryWrapper);
        return teacherPage;
    }
}
