package com.example.school_management.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school_management.entity.Grade;
import com.example.school_management.service.GradeService;
import com.example.school_management.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(@RequestBody Grade grade){
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@RequestBody List<Integer> ids){
        gradeService.removeByIds(ids);
        return Result.ok();
    }


    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            @RequestParam(value = "gradeName", required = false) String gradeName
    ){
        //分页 带条件查询
        Page<Grade> page = new Page<>(pageNo, pageSize);
        //通过服务层
        IPage<Grade> pageRS = gradeService.getGreadByOPr(page, gradeName);

        //封装 Result 对象并返回
        return Result.ok(pageRS);
    }

    //班级管理
    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }

}
