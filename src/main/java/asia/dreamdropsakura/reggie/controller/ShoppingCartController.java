package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import asia.dreamdropsakura.reggie.entity.ShoppingCart;
import asia.dreamdropsakura.reggie.service.DishService;
import asia.dreamdropsakura.reggie.service.SetmealService;
import asia.dreamdropsakura.reggie.service.ShoppingCartService;
import asia.dreamdropsakura.reggie.util.LocalThreadVariablePoolUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车 前端控制器
 * </p>
 *
 * @author 童话的爱
 * @since 2022-09-25
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 获取购物车所有内容
     *
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> listShoppingCart() {
        return Result.success(shoppingCartService.list(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, LocalThreadVariablePoolUtil.getCurrentThreadUserid())));
    }

    /**
     * 添加一个菜品或套餐到购物车当中, 然后返回当前购物车中的所有物品
     *
     * @param map
     * @return
     */
    @PostMapping("/add")
    public Result<ShoppingCart> addDishOrSetmealToShoppingCart(@RequestBody Map map) {
        if (map != null) {
            ShoppingCart shoppingCart = shoppingCartService.addDishOrSetmealByMapParam(map);

            return Result.success(shoppingCart);
        }

        return Result.error("添加套餐或菜肴失败，传入参数为空");
    }

    /**
     * 去除一件菜肴
     *
     * @return
     */
    @PostMapping("/sub")
    // todo 减少菜肴时有bug，3-1应为2，但前端显示直接全没了（数据库那边正常的减少成2 了）
    public Result<ShoppingCart> subDishById(@RequestBody ShoppingCart shoppingCart) {
        if (shoppingCart != null) {
            Long dishId = shoppingCart.getDishId();

            ShoppingCart shoppingCart1 = new ShoppingCart();
            if (dishId != null) {
                shoppingCart1 = shoppingCartService.decreaseNumberForDishById(LocalThreadVariablePoolUtil.getCurrentThreadUserid(), dishId);
            } else {
                Long setmealId = shoppingCart.getSetmealId();
                shoppingCart1 = shoppingCartService.decreaseNumberForSetmealById(LocalThreadVariablePoolUtil.getCurrentThreadUserid(), setmealId);
            }
            return Result.success(shoppingCart1);
        }
        return Result.error("无法删除指定菜肴，菜肴id 不能为空");
    }

    /**
     * 清空购物车
     *
     */
    @DeleteMapping("/clean")
    public Result<String> cleanShoppingCart() {
        boolean remove = shoppingCartService.remove(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, LocalThreadVariablePoolUtil.getCurrentThreadUserid()));
        return remove ? Result.success("清空成功") : Result.error("清空失败");
    }
}
