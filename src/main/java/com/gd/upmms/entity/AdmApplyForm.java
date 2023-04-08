package com.gd.upmms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdmApplyForm {

    @TableId("form_id")
    private Integer formId;

    @TableField("user_id")
    private Integer userId;

    @TableField("part_id")
    private Integer partId;

    private String fileUrl;

    private String remark;

    private Date createTime;

    private String state;
}
