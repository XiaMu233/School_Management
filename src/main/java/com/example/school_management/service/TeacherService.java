package com.example.school_management.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school_management.entity.LoginForm;
import com.example.school_management.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TeacherService extends IService<Teacher> {
    Teacher login(LoginForm loginForm);

    Teacher getTeacherById(Long userID);

    IPage<Teacher> getTeacherByOpr(Page<Teacher> pageParam, Teacher teacher);

}
