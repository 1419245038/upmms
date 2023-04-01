package com.gd.upmms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysRoleMenu {

    @TableId(value = "rm_id")
    private Integer rmId;

    @TableField(value = "role_id")
    private Integer roleId;

    @TableField(value = "menu_id")
    private Integer menuId;
}
