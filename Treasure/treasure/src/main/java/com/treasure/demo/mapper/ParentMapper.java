package com.treasure.demo.mapper;

import com.treasure.demo.entity.Parent;

import java.util.List;

/**
 * @ClassName ParentMapper
 * @Description
 * @Author SkySong
 * @Date 2020-04-14 17:28
 */

public interface ParentMapper {
    public List<Parent> selectAllParent();
}
