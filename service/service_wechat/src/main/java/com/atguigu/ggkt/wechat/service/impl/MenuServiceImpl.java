package com.atguigu.ggkt.wechat.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.wechat.Menu;
import com.atguigu.ggkt.vo.wechat.MenuVo;
import com.atguigu.ggkt.wechat.mapper.MenuMapper;
import com.atguigu.ggkt.wechat.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author 小吴
 * @since 2023-02-21
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private WxMpService wxMpService;

    //同步菜单
    @Override
    public void syncMenu() {
        //先获取所有的菜单数据
        List<MenuVo> menuVoList = this.findMenuInfo();
        //先封装button里面的结构（数组）
        JSONArray buttonList = new JSONArray();
        for (MenuVo oneMenuVo : menuVoList) {
            //json对象  一级对象
            JSONObject one = new JSONObject();
            one.put("name",oneMenuVo.getName());
            //json数组 封装二级
            JSONArray subButton = new JSONArray();
            for (MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                JSONObject view = new JSONObject();
                view.put("type", twoMenuVo.getType());
                if(twoMenuVo.getType().equals("view")) {
                    view.put("name", twoMenuVo.getName());
                    view.put("url", "http://ggkt2.vipgz1.91tunnel.com/#"
                            +twoMenuVo.getUrl());
                } else {
                    view.put("name", twoMenuVo.getName());
                    view.put("key", twoMenuVo.getMeunKey());
                }
                subButton.add(view);
            }
            one.put("sub_button",subButton);
            buttonList.add(one);
        }
        //封装最外层的button部分
        JSONObject button = new JSONObject();
        button.put("button",buttonList);

        try {
            String menuId = this.wxMpService.getMenuService().menuCreate(button.toJSONString());
            System.out.println("menuId ="+menuId);
        } catch (WxErrorException e) {
            throw new GgktException(20001,"菜单同步异常");
        }

    }

    //删除菜单方法
    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            throw new GgktException(20001,"菜单删除异常");
        }
    }

    //获取所有菜单，按照一级和二级菜单封装
    @Override
    public List<MenuVo> findMenuInfo() {
        //1 创建List集合，用于最终数据封装
        List<MenuVo> finalMenuList = new ArrayList<>();

        //2 查询出所有一级二级菜单数据
        List<Menu> menuList = baseMapper.selectList(null);

        //3 从所有菜单的数据中获取所有一级菜单的数据
        List<Menu> oneMenuList = menuList.stream().
                filter(menu -> menu.getParentId().longValue() == 0).
                collect(Collectors.toList());

        //4 封装一级菜单数据，放入最终list集合
        //遍历一级菜单集合
        for (Menu oneMenu:oneMenuList){
            //Menu  --> MenuVo
            MenuVo onMenuVo = new MenuVo();
            BeanUtils.copyProperties(oneMenu,onMenuVo);

            //5 封装二级菜单数据（判断一级菜单的id和二级菜单的parent_id是否一样）
            //如果相同，把二级菜单放入一级菜单
            List<Menu> twoMenuList = menuList.stream().
                    filter(menu -> menu.getParentId().longValue() == oneMenu.getId()).
                    collect(Collectors.toList());
            //List<Menu>  ---->List<MenuVo>
            List<MenuVo> children = new ArrayList<>();
            for(Menu twoMenu:twoMenuList){
                MenuVo twoMenuVo = new MenuVo();
                BeanUtils.copyProperties(twoMenu,twoMenuVo);
                children.add(twoMenuVo);
            }
            //把二级菜单放入一级菜单
            onMenuVo.setChildren(children);

            //把数据放入最终集合
            finalMenuList.add(onMenuVo);
        }
        return finalMenuList;
    }

    //获取所有一级菜单
    @Override
    public List<Menu> findMenuOneInfo() {

        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",0);
        List<Menu> list = baseMapper.selectList(wrapper);
        return list;
    }


}
