package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import asia.dreamdropsakura.reggie.dto.DishDto;
import asia.dreamdropsakura.reggie.dto.SetmealDto;
import asia.dreamdropsakura.reggie.entity.Dish;
import asia.dreamdropsakura.reggie.entity.DishFlavor;
import asia.dreamdropsakura.reggie.entity.Setmeal;
import asia.dreamdropsakura.reggie.entity.SetmealDish;
import asia.dreamdropsakura.reggie.service.DishFlavorService;
import asia.dreamdropsakura.reggie.service.DishService;
import asia.dreamdropsakura.reggie.service.SetmealDishService;
import asia.dreamdropsakura.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 套餐相关控制器类
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 分页获取套餐管理列表
     *
     * @param page     当前页数
     * @param pageSize 每页记录数
     * @param name     要查询的套餐名称
     * @return
     */
    @GetMapping("/page")
    public Result<IPage> getPaginationList(int page, int pageSize, String name) {
        if (page > 0 && pageSize > 0) {
            return Result.success(setmealService.paginationSetmeals(page, pageSize, name));
        }
        return Result.error("当前记录数与每页记录数不能小于1或不可为空");
    }

    /**
     * 获取所有套餐
     *
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> listAllSetmeal(@RequestParam Long categoryId, @RequestParam Integer status) {
        if (categoryId != null && status != null) {
            List<Setmeal> list = setmealService.list(new LambdaQueryWrapper<Setmeal>().eq(Setmeal::getCategoryId, categoryId).eq(Setmeal::getStatus, status));
            return Result.success(list);
        }

        return Result.error("无法查询指定的套餐信息，套餐id 或状态为空");
    }

    /**
     * 根据id 获取指定的套餐信息，用于修改界面的使用
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> getSetmealById(@PathVariable Long id) {
        if (id != null) {
            return Result.success(setmealService.selectSetmealById(id));
        }
        return Result.error("无法获取指定的套餐，id 不能为空");
    }

    /**
     * 更新套餐信息
     *
     * @param setmealDto
     * @return
     */
    @PutMapping
    public Result<String> modifySetmeal(@RequestBody SetmealDto setmealDto) {
        if (setmealDto != null) {
            if (setmealService.updateSetmeal(setmealDto)) {
                return Result.success("更新成功");
            }
            return Result.error("一个或多个信息更新失败，请重试");
        }
        return Result.error("无法更新套餐信息，内容为空");
    }

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Result<String> addSetmeal(@RequestBody SetmealDto setmealDto) {
        if (setmealDto != null ) {
            if (setmealService.addSetmeal(setmealDto)) {
                return Result.success("添加成功");
            }
            return Result.error("添加时出现问题，一个或多个添加操作未成功");
        }
        return Result.error("无法新增套餐，套餐内容为空");
    }

    /**
     * 删除一个或多个套餐, 启售中(status=1)的套餐不可被删除
     *
     * @return
     */
    @DeleteMapping
    @Transactional
    public Result<String> deleteSetmeals(@RequestParam List<Long> ids) {
        if (ids != null || ids.size() != 0) {
            if (setmealService.deleteByIds(ids)) {
                // 删除套餐对应的菜肴
                boolean remove = setmealDishService.remove(new LambdaQueryWrapper<SetmealDish>().in(SetmealDish::getSetmealId, ids));
                return Result.success("删除成功");
            }
            return Result.error("删除出错，套餐仍在售卖中或套餐已被删除");
        }
        return Result.error("请选择需要删除的菜肴");
    }

    /**
     * 更改一个或多个套餐的售卖状态，0为停售，1为启售
     *
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> modifySetmealStatusByIds(@PathVariable int status, @RequestParam List<Long> ids) {
        if (ids.size() > 0) {
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(status);
            // 该代码将会使mp 生成如下SQL 语句
            // UPDATE setmeal SET status=?, update_time=?, update_user=? WHERE is_deleted=0 AND (id IN (?,?,?,?,?,?,?,?,?,?))
            boolean update = setmealService.update(setmeal, new LambdaQueryWrapper<Setmeal>().in(Setmeal::getId, ids).select(Setmeal::getStatus));
            return Result.success("成功更改售卖状态");
        }
        return Result.error("请选择需要修改售卖状态的套餐");
    }

    /**
     * 根据指定菜肴的id 获取菜肴所属口味
     *
     * todo 该方法执行时共发起了4次数据库查询请求。
     *  可以优化
     *
     * @param id 菜肴id(dish表中的主键id)
     * @return SetmealDto 或DishDto
     */
    @GetMapping("/dish/{id}")
    public Result<Object> getDishFlavorsOrSetmealDishesById(@PathVariable Long id) {
        if (id != null) {
            // 先查套餐中是否存在指定主键id 的记录
            Setmeal setmealRecord = setmealService.getOne(new LambdaQueryWrapper<Setmeal>().eq(Setmeal::getId, id));
            // 再查菜肴表中有没有指定主键id 的菜品
            Dish dishRecord = dishService.getOne(new LambdaQueryWrapper<Dish>().eq(Dish::getId, id));

            SetmealDto setmealDto = new SetmealDto();
            DishDto dishDto = new DishDto();
            if (setmealRecord != null) {
                // 当该id 表示为一个套餐时，获取套餐所属所有菜肴
                setmealDto.setSetmealDishes(setmealDishService.list(new LambdaQueryWrapper<SetmealDish>().in(SetmealDish::getSetmealId, id)));
                return Result.success(setmealDto);
            } else if (dishRecord != null) {
                // 当该id 表示为一个菜肴时, 获取菜肴所属所有口味
                dishDto.setFlavors(dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().in(DishFlavor::getDishId, id)));
                return Result.success(dishDto);
            } else {
                // 如果都为空
                return Result.error("无法找到指定的菜肴或套餐的信息");
            }
        }

        return Result.error("无法获取菜肴的口味信息，菜肴id 不能为空");
    }
}
