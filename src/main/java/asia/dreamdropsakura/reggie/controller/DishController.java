package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import asia.dreamdropsakura.reggie.dto.DishDto;
import asia.dreamdropsakura.reggie.entity.Dish;
import asia.dreamdropsakura.reggie.entity.DishFlavor;
import asia.dreamdropsakura.reggie.entity.Setmeal;
import asia.dreamdropsakura.reggie.exception.ApplicationException;
import asia.dreamdropsakura.reggie.service.DishFlavorService;
import asia.dreamdropsakura.reggie.service.DishService;
import asia.dreamdropsakura.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 菜肴相关控制器类
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 分页获取所有菜肴
     *
     * @param page     当前页数
     * @param pageSize 每页记录数
     * @return
     */
    @GetMapping("page")
    public Result<IPage> getDishes(int page, int pageSize, String name) {
        /*
        // 分页构造器
        Page<Dish> dishPage = new Page<>(page, pageSize);
        // 条件构造器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 根据菜肴所属菜系或套餐、或根据菜肴的添加日期、或根据菜肴名称排序。仅当前一位的排序相同时下一位才生效
        dishLambdaQueryWrapper.like(name != null , Dish::getName, name).orderByAsc(Dish::getCategoryId).orderByAsc(Dish::getCreateTime).orderByAsc(Dish::getName);

        // 执行分页查询
        Page<Dish> page1 = dishService.page(dishPage, dishLambdaQueryWrapper);
        */

        Page<DishDto> dishDtoPage = dishService.selectByPaginationWithDishAndCategoryTable(page, pageSize, name);

        // 由于封装结果集时会将结果放入Page 对象中，其中就有包含前端分页所需的所有数据，故直接返回该对象即可
        return Result.success(dishDtoPage);
    }

    /**
     * 新增菜肴
     * 注意由于是向两个表同时插入数据，故需要一起成功一起失败，即需要开启事务(已在saveWithFlavor 方法中开启)
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result<String> addDish(@RequestBody DishDto dishDto) {
        if (dishDto != null) {
            boolean b = dishService.saveWithFlavor(dishDto);
            return Result.success("添加成功");
        }
        return Result.error("菜肴数据为空，请检查值是否都已填写完毕");
    }

    /**
     * 修改菜肴信息，用于接收菜肴修改界面提交的数据
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result<String> modifyDish(@RequestBody DishDto dishDto) {
        if (dishDto != null) {
            boolean b = dishService.saveWithFlavorById(dishDto);
            return Result.success("修改成功");
        }
        return Result.error("菜肴数据为空，请检查值是否都已填写完毕");
    }

    /**
     * 根据id 获取菜肴信息，用于修改界面的使用
     *
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDto> getDishAndFlavorsById(@PathVariable long id) {
        if (id != 0) {
            DishDto dishDto = dishService.selectById(id);
            return Result.success(dishDto);
        }
        return Result.error("获取菜肴信息出错，菜肴的id 不能为空");
    }

    /**
     * 删除一个或多个菜肴, 售卖中的菜肴不可被删除
     *
     * @return
     */
    @DeleteMapping
    public Result<String> deleteDishes(@RequestParam List<Long> ids) {
        if (ids != null || ids.size() != 0) {
            boolean b = dishService.deleteByIds(ids);
            return Result.success("删除成功");
        }
        return Result.error("请选择需要删除的菜肴");
    }

    /**
     * 更改一个或多个菜肴的售卖状态，0为停售，1为启售
     *
     * todo 关于下面这些文档注释，格式应该怎么写？
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> modifyDishesStatusByIds(@PathVariable int status, @RequestParam List<Long> ids) {
        if (ids.size() > 0) {
            Dish dish = new Dish();
            dish.setStatus(status);
            // 该代码将会使mp 生成如下SQL 语句
            // UPDATE dish SET status=?, update_time=?, update_user=? WHERE is_deleted=0 AND (id IN (?,?,?,?,?,?,?,?,?,?))
            // todo 像上面的代码一样，业务层操作完成后的返回值是否需要接收？
            boolean update = dishService.update(dish, new LambdaQueryWrapper<Dish>().in(Dish::getId, ids).select(Dish::getStatus));
            return Result.success("成功更改售卖状态");
        }
        return Result.error("请选择需要修改售卖状态的菜肴");
    }

    /**
     * 获取菜肴表中指定categoryId 的所有菜肴, 另外一并获取单个菜肴所对应的口味，并将其封装到DishDto 对象中
     * todo 涉及到的数据库查询操作过多（查询请求在for 循环当中），速度较慢，需要优化
     *
     * @param categoryId 套餐或菜系id
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishDto>> getSetmealDishesOrDishesBycategoryId(@RequestParam Long categoryId) {
        if (categoryId != null) {
            // 由于在category 表中，有的分类记录为套餐（type 字段为2），有的是菜系（type为1），
            // 故这里如果传入一个是菜系的分类记录id 则获得的是该菜系下的所有菜肴
            // 如果传入一个是套餐的分类记录id 则获得的是该套餐下的所有菜肴
            // 在分类表中，里面的套餐与菜系是用于用户界面展示的分类用的，其都不代表某个具体的菜肴或套餐。（如某某菜系中包含有真正的菜肴，那些菜肴
            // 才是用户要吃的东西；而某某套餐并不直接是一个套餐，它只是一个分类，例如有儿童套餐分类，也有活动套餐分类，每个分类下才有各个不同儿童
            // 或不同活动所对应的套餐，这些套餐才是用户要点餐吃的东西。）

            // 先按指定id 获取所有的菜肴
            List<Dish> listDishesById = dishService.list(new LambdaQueryWrapper<Dish>().eq(Dish::getCategoryId, categoryId));

            // 然后使用stream 流，挨个看每个dish 菜肴对象的id，拿着id 去dishFlavor菜肴口味表找该id 的菜品所对应的口味
            Stream<Dish> stream = listDishesById.stream();
            List<DishDto> collect = stream.map((Function<Dish, DishDto>) dish -> {
                // 根据指定菜肴id 查找所属的口味
                Long dishId = dish.getId();
                List<DishFlavor> list = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dishId));
                // 将DishFlavor 菜肴口味与Dish 菜肴封装在一起
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(dish, dishDto);
                dishDto.setFlavors(list);
                // 然后返回封装好的DishDto 对象，这是前端需要的对象，前端遍历列表中的每一个dishDto ，然后获取数据
                return dishDto;
            }).collect(Collectors.toList());

            return Result.success(collect);
        }
        return Result.error("id参数不能为空");
    }
}
