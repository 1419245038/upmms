package com.gd.upmms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AdmRecord {

    @TableId("record_id")
    private Integer recordId;

    @TableField("user_id")
    private Integer userId;

    private String username;

    private String payment;

    private String money;

    private String image;
}
