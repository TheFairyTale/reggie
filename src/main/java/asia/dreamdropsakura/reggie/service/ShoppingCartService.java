package asia.dreamdropsakura.reggie.service;

import asia.dreamdropsakura.reggie.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author 童话的爱
 * @since 2022-09-25
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 向购物车添加物品
     *
     * @param map
     * @return
     */
    ShoppingCart addDishOrSetmealByMapParam(Map map);

    /**
     * 给指定的菜肴加1个数量
     *
     * @param userId
     * @param dishId
     */
    boolean increaseNumberForDishById(Long userId, Long dishId);

    /**
     * 给指定的菜肴减1个数量
     *
     * @param userId
     * @param dishId
     */
    ShoppingCart decreaseNumberForDishById(Long userId, Long dishId);

    /**
     * 给指定的套餐加1个数量
     *
     * @param userId
     * @param dishId
     */
    boolean increaseNumberForSetmealById(Long userId, Long dishId);

    /**
     * 给指定的套餐减1个数量
     *
     * @param userId
     * @param dishId
     */
    ShoppingCart decreaseNumberForSetmealById(Long userId, Long dishId);
}
