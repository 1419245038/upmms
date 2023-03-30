package com.gd.upmms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：zhupenghe
 * @date ：created in 2023/03/28 13:10
 * @description :
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRole {

    @TableId(value = "role_id")
    private Integer roleId;

    private String roleName;

    private String roleDescription;
}
