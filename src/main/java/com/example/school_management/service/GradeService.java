package com.example.school_management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.school_management.entity.Grade;

import java.util.List;

public interface GradeService extends IService<Grade> {
    IPage<Grade> getGreadByOPr(Page<Grade> page, String gradename);

    List<Grade> getGrades();
}
