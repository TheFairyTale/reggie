package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import asia.dreamdropsakura.reggie.entity.ShoppingCart;
import asia.dreamdropsakura.reggie.service.ShoppingCartService;
import asia.dreamdropsakura.reggie.util.LocalThreadVariablePoolUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    private ShoppingCartService shoppingCartService;

    /**
     * 获取购物车所有内容
     *
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> listShoppingCart() {
        System.out.println(LocalThreadVariablePoolUtil.getCurrentThreadUserid().longValue());
        return Result.success(shoppingCartService.list(
                new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, LocalThreadVariablePoolUtil.getCurrentThreadUserid())
        ));
    }
}
