package com.gd.upmms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdmProcessFlow {

    @TableId("process_id")
    private Integer processId;

    @TableField("form_id")
    private Integer formId;

    @TableField("operator_id")
    private Integer operatorId;

    private String username;

    @TableField("part_id")
    private Integer partId;

    private String action;

    private String result;

    private String reason;

    private Date createTime;

    private Date auditTime;

    private Integer orderNo;

    private String state;

    private String isLast;
}
