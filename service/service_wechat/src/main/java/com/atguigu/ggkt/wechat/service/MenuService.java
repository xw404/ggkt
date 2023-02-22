package com.atguigu.ggkt.wechat.service;


import com.atguigu.ggkt.model.wechat.Menu;
import com.atguigu.ggkt.vo.wechat.MenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 服务类
 * </p>
 *
 * @author 小吴
 * @since 2023-02-21
 */
public interface MenuService extends IService<Menu> {

    List<MenuVo> findMenuInfo();

    List<Menu> findMenuOneInfo();

    void syncMenu();

    void removeMenu();
}
