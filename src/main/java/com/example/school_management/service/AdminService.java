package com.example.school_management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.school_management.entity.Admin;
import com.example.school_management.entity.LoginForm;

public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);

    Admin getAdminById(Long userID);

    IPage<Admin> getAdminByOpr(Page<Admin> pageParam, String adminName);

}
