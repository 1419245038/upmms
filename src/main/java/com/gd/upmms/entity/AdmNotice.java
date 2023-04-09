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
public class AdmNotice {

    @TableId("notice_id")
    private Integer noticeId;

    @TableField("receiver_id")
    private Integer receiverId;

    @TableField("part_id")
    private Integer partId;

    private String content;

    private Date createTime;
}
