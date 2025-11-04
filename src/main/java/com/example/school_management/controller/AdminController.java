package com.example.school_management.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school_management.entity.Admin;
import com.example.school_management.service.AdminService;
import com.example.school_management.util.MD5;
import com.example.school_management.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/adminController")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/getAllAdmin/{PageNo}/{PageSize}")
    public Result getAllAdmin(
            @PathVariable("PageNo") Integer PageNo,
            @PathVariable("PageSize") Integer PageSize,
            String adminName
    ){
        Page<Admin> pageParam = new Page<Admin>(PageNo, PageSize);
        IPage<Admin> iPage = adminService.getAdminByOpr(pageParam, adminName);
        return Result.ok(iPage);
    }

    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            //JSON 格式的 Admin 对象
            @RequestBody Admin admin

    ){
        Integer id = admin.getId();
        if (id == null || 0 == id) {
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
            @RequestBody List<Integer> ids
    ){
        adminService.removeByIds(ids);
        return Result.ok();
    }

}
