package com.example.school_management.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.school_management.entity.Admin;
import com.example.school_management.entity.LoginForm;
import com.example.school_management.entity.Student;
import com.example.school_management.entity.Teacher;
import com.example.school_management.service.AdminService;
import com.example.school_management.service.StudentService;
import com.example.school_management.service.TeacherService;
import com.example.school_management.util.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private  AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;


    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
        boolean expiration = JwtHelper.isExpiration(token);
        if(expiration){
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
        //从 token 中解析出用户的 id 和用户类型
        Long userID = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String,Object> map = new LinkedHashMap<>();
        switch (userType){
            case 1:
                Admin admin = adminService.getAdminById(userID);
                map.put("userType", 1);
                map.put("user", admin);
                break;
            case 2:
                Student student = studentService.getStuedntById(userID);
                map.put("userType", 2);
                map.put("user", student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userID);
                map.put("userType", 3);
                map.put("user", teacher);
                break;
        }

        return Result.ok(map);
    }



    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        //验证码校验
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if("".equals(sessionVerifiCode) || null == sessionVerifiCode) {
            return Result.fail().message("验证码失效,请刷新后重试");
        }
        if(!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)) {
            return Result.fail().message("验证码错误,请小心输入后重试");
        }
        //从 session 域中移除现有验证码
        session.removeAttribute("verifiCode");
        //分用户类型进行校验


        //准备一个 map 用于存放响应的数据
        Map<String,Object> map = new LinkedHashMap<>();
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if(null != admin) {
                        //用户的类型和用户的 id 转换成一个密文,以 token 的名称向客户端反馈
                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        map.put("token", token);
                    }
                    else{
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if(null != student) {
                        //用户的类型和用户的 id 转换成一个密文,以 token 的名称向客户端反馈
                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
                        map.put("token", token);
                    }
                    else{
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if(null != teacher) {
                        //用户的类型和用户的 id 转换成一个密文,以 token 的名称向客户端反馈
                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                        map.put("token", token);
                    }
                    else{
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("查无此用户");
    }

    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            //学生管理头像上传
            @RequestPart("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request
    ){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String newFilename = uuid + originalFilename.substring(i);

        //保存文件
        String portraitPath = "E:/javaproject/Stdio_Management/target/classes/public/upload/".concat(newFilename);
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //响应图片路径
        String path = "upload/".concat(newFilename);
        return Result.ok(path);
    }


    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取图片上的验证码
        String verificode = new String( CreateVerifiCodeImage.getVerifiCode() );
       //将验证码文本放入 session 域，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode", verificode);
        //将验证码图片响应给浏览器
        try {
            ImageIO.write(verifiCodeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @RequestHeader("token") String token,
            @PathVariable("oldPwd") String oldPwd,
            @PathVariable("newPwd") String newPwd
    ){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            //token 过期
            return Result.fail().message("token 失效, 请重新登录后修改密码");
        }
        //获取用户 id 和用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);

        switch (userType){
            case 1:
                QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", userId.intValue());
                queryWrapper.eq("password", oldPwd);
                Admin admin  = adminService.getOne(queryWrapper);
                if (admin != null) {
                    //修改
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }
                else{
                    return Result.fail().message("原密码有误");
                }
                break;

            case 2:
                QueryWrapper<Student> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id", userId.intValue());
                queryWrapper2.eq("password", oldPwd);
                Student student  = studentService.getOne(queryWrapper2);
                if (student != null) {
                    //修改
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }
                else{
                    return Result.fail().message("原密码有误");
                }
                break;

            case 3:
                QueryWrapper<Teacher> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("id", userId.intValue());
                queryWrapper3.eq("password", oldPwd);
                Teacher teacher  = teacherService.getOne(queryWrapper3);
                if (teacher != null) {
                    //修改
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }
                else{
                    return Result.fail().message("原密码有误");
                }
                break;
        }
        return Result.ok();
    }

}
