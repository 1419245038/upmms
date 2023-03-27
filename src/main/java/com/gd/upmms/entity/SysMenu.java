package com.gd.upmms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenu {

    private Integer id;

    private Integer pid;

    private String title;

    private String icon;

    private String href;

    private String target;

    private Integer sort;

    private Integer status;

}
