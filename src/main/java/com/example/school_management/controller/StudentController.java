package com.example.school_management.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school_management.entity.Student;
import com.example.school_management.service.StudentService;
import com.example.school_management.util.MD5;
import com.example.school_management.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            Student student
    ){
        //分页信息封装
        Page<Student> pageParam = new Page<>(pageNo,pageSize);
        //进行查询
        IPage<Student> studentPage =  studentService.getStudentByOpr(pageParam, student);
        return Result.ok(studentPage);
    }


    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@RequestBody Student student){
        Integer id = student.getId();
        if(null == id || 0 == id){
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @DeleteMapping("/delStudentById")
    public Result delStudentById(@RequestBody List<Integer> ids){
        studentService.removeByIds(ids);
        return Result.ok();
    }
}
