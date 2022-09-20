package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import asia.dreamdropsakura.reggie.entity.Dish;
import asia.dreamdropsakura.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜肴相关控制器类
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 分页获取所有菜肴
     *
     * @param page 当前页数
     * @param pageSize 每页记录数
     * @return
     */
    @GetMapping("page")
    public Result<IPage> getDishes(int page, int pageSize) {
        // 分页构造器
        Page<Dish> dishPage = new Page<>(page, pageSize);
        // 条件构造器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 根据菜肴所属菜系或套餐、或根据菜肴的添加日期、或根据菜肴名称排序。仅当前一位的排序相同时下一位才生效
        dishLambdaQueryWrapper.orderByAsc(Dish::getCategoryId).orderByAsc(Dish::getCreateTime).orderByAsc(Dish::getName);

        // 执行分页查询
        Page<Dish> page1 = dishService.page(dishPage, dishLambdaQueryWrapper);
        // 由于封装结果集时会将结果放入Page 对象中，其中就有包含前端分页所需的所有数据，故直接返回该对象即可
        return Result.success(dishPage);
    }
}
