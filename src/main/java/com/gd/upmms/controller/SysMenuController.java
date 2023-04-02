package com.gd.upmms.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gd.upmms.common.CustomException;
import com.gd.upmms.common.R;
import com.gd.upmms.dto.SysMenuDto;
import com.gd.upmms.entity.SysConfig;
import com.gd.upmms.entity.SysMenu;
import com.gd.upmms.service.SysConfService;
import com.gd.upmms.service.SysMenuService;
import com.gd.upmms.utils.TreeUtil;
import com.gd.upmms.vo.TreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysConfService sysConfService;

    @GetMapping("/init")
    public Map<String,Object> init(){

        //创建Map集合，保存MenuInfo菜单信息
        Map<String,Object> map = new LinkedHashMap<>();
        //创建Map集合，保存homeInfo信息
        Map<String,Object> homeInfo = new LinkedHashMap<>();
        //创建Map集合，保存logoInfo信息
        Map<String,Object> logoInfo = new LinkedHashMap<>();

        SysConfig sysConfig = sysConfService.getOne(null);
        if (sysConfig!=null){
            //HomeInfo信息
            homeInfo.put("title", StrUtil.isNotEmpty(sysConfig.getHomeTitle())?sysConfig.getHomeTitle():"首页");
            homeInfo.put("href",StrUtil.isNotEmpty(sysConfig.getHomeUrl())?sysConfig.getHomeUrl():"page/welcome-1.html?t=1");
            //logoInfo信息
            logoInfo.put("title",StrUtil.isNotEmpty(sysConfig.getLogoTitle())?sysConfig.getLogoTitle():"党员管理");
            logoInfo.put("image",StrUtil.isNotEmpty(sysConfig.getLogoImage())
                    && !sysConfig.getLogoImage().equals("/common/download?name=")?sysConfig.getLogoImage():"images/logo/logo.png");
            logoInfo.put("href",StrUtil.isNotEmpty(sysConfig.getLogoUrl())?sysConfig.getLogoUrl():"");
        }else{
            //HomeInfo信息
            homeInfo.put("title","首页");
            homeInfo.put("href","page/welcome-1.html?t=1");
            //logoInfo信息
            logoInfo.put("title","党员管理");
            logoInfo.put("image","images/logo/logo.png");
            logoInfo.put("href","");
        }
        map.put("menuInfo", TreeUtil.toTree(sysMenuService.findAll(),0));
        map.put("homeInfo",homeInfo);
        map.put("logoInfo",logoInfo);

        return map;
    }

    @GetMapping("/getAll")
    public R<List<SysMenuDto>> getMenu(){
        return R.success(sysMenuService.findAll());
    }

    @PostMapping("/parant")
    public R<List<TreeVO>> getParent(){
        List<TreeVO> treeVOS=new ArrayList<>();
        List<SysMenuDto> menuDtos = sysMenuService.findAll();
        for (SysMenuDto menuDto : menuDtos) {
            TreeVO treeVO=new TreeVO();
            treeVO.setId(menuDto.getId());
            treeVO.setPid(menuDto.getPid());
            treeVO.setTitle(menuDto.getTitle());
            treeVO.setSpread(false);
            treeVOS.add(treeVO);
        }
        return R.success(treeVOS);
    }

    @PostMapping("/del")
    public R<String> del(Integer id) throws CustomException{
        LambdaQueryWrapper<SysMenu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getPid,id);
        int count = sysMenuService.count(queryWrapper);
        if (count>0){
            throw new CustomException("此菜单下存在子菜单，不可删除!");
        }

        boolean remove = sysMenuService.removeById(id);

        return remove?R.success("删除成功!"):R.error("删除失败");
    }

    @PostMapping("/add")
    public R<String> add(SysMenu sysMenu){
        boolean save = sysMenuService.save(sysMenu);
        return save?R.success("保存成功!"):R.error("保存失败!");
    }

    @PostMapping("/update")
    private R<String> update(SysMenu sysMenu){
        boolean update = sysMenuService.updateById(sysMenu);
        return update?R.success("更新成功!"):R.error("更新失败!");
    }

}
