package com.gd.upmms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdmPositionUser {

    @TableId(value = "pu_id")
    private Integer puId;

    @TableField(value = "position_id")
    private Integer positionId;

    @TableField(value = "user_id")
    private Integer userId;
}
