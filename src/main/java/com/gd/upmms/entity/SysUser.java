package com.gd.upmms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUser {

    @TableId(value = "user_id")
    private Integer userId;

    private String username;

    private String password;

    private Integer salt;

    private String email;

    private String idNumber;

    private String phoneNumber;

    private Integer sex;

    private String address;
}
