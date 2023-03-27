package com.gd.upmms.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeVO {

    private Integer id;//菜单节点编号
    @JsonProperty(value = "parentId")
    private Integer pid;//父节点菜单编号
    private String title;//菜单节点名称
    private String icon;//菜单节点图标
    private String href;//菜单路径
    private Boolean spread;//是否展开
    //子节点菜单
    private List<TreeVO> children = new ArrayList<>();

    //复选框是否被选中
    private String checkArr = "0";//默认不选中
}
