package com.treasure.demo.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.treasure.demo.entity.Parent;
import com.treasure.demo.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ParentController
 * @Description
 * @Author SkySong
 * @Date 2020-04-14 17:48
 */
@RestController
@RequestMapping("/parent")
public class ParentController {
    @Autowired
    private ParentService parentService;

    @RequestMapping("/all")
    private List<Parent> findAll(){
        return parentService.selectAllParent();
    }
}
