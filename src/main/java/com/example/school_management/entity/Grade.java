package com.example.school_management.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_grade")
public class Grade {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String manager;
    private String telephone;
    private String email;
    private String introducation;
}
