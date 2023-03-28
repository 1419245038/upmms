package com.gd.upmms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：zhupenghe
 * @date ：created in 2023/03/28 12:38
 * @description :
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUser {

    private Integer userId;

    private String username;

    private String password;

    private Integer memberId;

    private Integer salt;

    private String email;
}
