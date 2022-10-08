package asia.dreamdropsakura.reggie.mapper;

import asia.dreamdropsakura.reggie.entity.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 购物车 Mapper 接口
 * </p>
 *
 * @author 童话的爱
 * @since 2022-09-25
 */
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    /**
     * 添加指定套餐id 的数量
     *
     * @return
     */
    @Update("update shopping_cart set number=number+1 where user_id=#{userId} and setmeal_id=#{setmealId}")
    int updateAddSetmealNumberIfExist(long userId, long setmealId);

    /**
     * 减少指定套餐id 的数量
     *
     * @return
     */
    @Update("update shopping_cart set number=number-1 where user_id=#{userId} and setmeal_id=#{setmealId}")
    int updateDecreaseSetmealNumberIfExist(long userId, long setmealId);

    /**
     * 添加指定菜肴id 的数量
     *
     * @return
     */
    @Update("update shopping_cart set number=number+1 where user_id=#{userId} and dish_id=#{dishId}")
    int updateAddDishNumberIfExist(long userId, long dishId);

    /**
     * 减少指定套餐id 的数量
     *
     * @return
     */
    @Update("update shopping_cart set number=number-1 where user_id=#{userId} and dish_id=#{dishId}")
    int updateDecreaseDishNumberIfExist(long userId, long dishId);
}
