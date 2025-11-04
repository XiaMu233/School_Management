package com.example.school_management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.school_management.entity.LoginForm;
import com.example.school_management.entity.Student;

public interface StudentService extends IService<Student> {
    Student login(LoginForm loginForm);

    Student getStuedntById(Long userID);

    IPage<Student> getStudentByOpr(Page<Student> page, Student student);
}
