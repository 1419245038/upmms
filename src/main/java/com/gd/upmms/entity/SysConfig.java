package com.gd.upmms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysConfig {

    @TableId(value = "config_id")
    private Integer configId;

    private String homeTitle;

    private String homeUrl;

    private String logoTitle;

    private String logoImage;

    private String logoUrl;
}
