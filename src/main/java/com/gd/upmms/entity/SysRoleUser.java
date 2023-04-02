package com.gd.upmms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleUser {

    @TableId("ru_id")
    private Integer ruId;

    @TableField("role_id")
    private Integer roleId;

    @TableField("user_id")
    private Integer userId;
}
