package com.example.school_management.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school_management.entity.Clazz;
import com.example.school_management.service.ClazzService;
import com.example.school_management.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @GetMapping("/getClazzsByOpr/{pageOn}/{pageSize}")
    public Result getClazzsByOpr(
            @PathVariable("pageOn") Integer pageOn,
            @PathVariable("pageSize") Integer pageSize,
            //分页查询的查询条件
            Clazz clazz
    ){

        Page<Clazz> page = new Page<>(pageOn,pageSize);
        IPage<Clazz> Ipage = clazzService.getClazzByOpr(page, clazz);
        return Result.ok(Ipage);
    }

    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(@RequestBody Clazz clazz){
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(@RequestBody List<Integer> ids){
        clazzService.removeByIds(ids);
        return Result.ok();
    }

    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> clazzs = clazzService.getClazz();
        return Result.ok(clazzs);
    }

}
