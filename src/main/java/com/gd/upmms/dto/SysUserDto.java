package com.gd.upmms.dto;

import com.gd.upmms.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserDto extends SysUser {

    private String partTitle;

    private String positionTitle;

    private String roleName;

    private Integer partId;

    private Integer positionId;

    private Integer roleId;
}
