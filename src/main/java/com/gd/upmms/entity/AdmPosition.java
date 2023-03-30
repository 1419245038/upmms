package com.gd.upmms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdmPosition {

    @TableId(value = "position_id")
    private Integer positionId;

    private String positionTitle;

    private String positionDesc;
}
