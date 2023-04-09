package com.gd.upmms.dto;

import com.gd.upmms.entity.AdmApplyForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdmApplyFormDto extends AdmApplyForm {

    private String result;

    private String reason;

    private Integer orderNo;
}
