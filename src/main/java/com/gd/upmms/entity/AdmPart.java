package com.gd.upmms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdmPart {

    @TableId(value = "part_id")
    private Integer partId;

    private String partTitle;

    private String partAddr;

    private String partDesc;

}
