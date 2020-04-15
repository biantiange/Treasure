package com.treasure.demo.service;

import com.treasure.demo.mapper.ParentMapper;
import com.treasure.demo.entity.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ParentService
 * @Description
 * @Author SkySong
 * @Date 2020-04-14 17:45
 */
@Service("parentService")
public class ParentService {
    @Autowired
    private ParentMapper parentMapper;

    public List<Parent> selectAllParent(){
       return parentMapper.selectAllParent();
    }
}
