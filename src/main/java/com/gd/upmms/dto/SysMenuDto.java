package com.gd.upmms.dto;

import com.gd.upmms.entity.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMenuDto extends SysMenu {

    private List<SysMenuDto> child;
}
