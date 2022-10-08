package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import asia.dreamdropsakura.reggie.dto.OrderDto;
import asia.dreamdropsakura.reggie.entity.Orders;
import asia.dreamdropsakura.reggie.entity.ShoppingCart;
import asia.dreamdropsakura.reggie.service.OrdersService;
import asia.dreamdropsakura.reggie.service.ShoppingCartService;
import asia.dreamdropsakura.reggie.util.LocalThreadVariablePoolUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author 童话的爱
 * @since 2022-09-25
 */
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 分页获取订单
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public Result<IPage> getPaginatedOrders(@RequestParam Long page, @RequestParam Long pageSize) {
        if (page != null && pageSize != null) {
            return Result.success(ordersService.paginationOrders(page, pageSize));
        }
        return Result.error("");
    }

    /**
     * 新增订单（下单）
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public Result<String> postOrders(@RequestBody Orders orders) {
        if (orders != null) {
            return ordersService.submitOrders(orders) ? Result.success("订单提交成功!") : Result.success("订单提交失败!");
        }

        return Result.error("订单内容为空");
    }
}
