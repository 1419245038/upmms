package com.gd.upmms.utils;

import com.gd.upmms.dto.SysMenuDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TreeUtil {

    /**
     * 构建菜单层级关系
     * @param treeList      菜单列表
     * @param pid           父级菜单编号，0表示父级菜单 (一级菜单)
     * @return
     */
    public static List<SysMenuDto> toTree(List<SysMenuDto> treeList, Integer pid) {
        List<SysMenuDto> retList = new ArrayList<>();
        for (SysMenuDto parent : treeList) {
            //如果当前父级菜单编号与循环的菜单列表中的父级菜单编号一致
            if (pid == parent.getPid()) {
                retList.add(findChildren(parent, treeList));
            }
        }
        return retList;
    }


    private static SysMenuDto findChildren(SysMenuDto parent, List<SysMenuDto> treeList) {
        for (SysMenuDto child : treeList) {
            if (Objects.equals(parent.getId(), child.getPid())) {
                if (parent.getChild() == null) {
                    parent.setChild(new ArrayList<>());
                }
                parent.getChild().add(findChildren(child, treeList));
            }
        }
        return parent;
    }
}
