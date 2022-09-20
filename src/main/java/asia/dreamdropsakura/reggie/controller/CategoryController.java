package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import asia.dreamdropsakura.reggie.entity.Category;
import asia.dreamdropsakura.reggie.service.CategoryService;
import asia.dreamdropsakura.reggie.service.DishService;
import asia.dreamdropsakura.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜系与套餐分类管理相关控制器类
 *
 * @author 童话的爱
 * @since 2022-9-19
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页获取所有套餐与菜系分类并按1排序规则排序、2分类排序、3名称依次排序
     *
     * @param page 当前页数
     * @param pageSize 每页记录数
     * @return
     */
    @GetMapping("/page")
    public Result<IPage> getCategories(int page, int pageSize) {
        // 分页构造器
        Page<Category> categoryPage = new Page<>(page, pageSize);
        // 条件构造器, 用于实现依据sort 字段进行数据的正序倒序排序
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加排序条件，根据sort 字段按正序排序
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getType).orderByAsc(Category::getName);
        // 分页查询
        Page<Category> page1 = categoryService.page(categoryPage, categoryLambdaQueryWrapper);
        return Result.success(categoryPage);
    }

    /**
     * 新增套餐、菜系分类的接口
     *
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> addCategory(@RequestBody Category category) {
        if (category != null) {
            categoryService.save(category);
            return Result.success("新增成功");
        }
        return Result.error("新增失败, 内容为空, 请重试");
    }

    /**
     * 删除套餐、菜系分类的接口. 需要确保要删除的套餐或菜品分类已不再被某个套餐与菜品所关联。
     *
     * todo 如果不清楚需要构建什么查询条件，可以先在mysql 写原生sql 语句去实现功能，然后再使用java 代码实现功能
     *
     */
    @DeleteMapping
    public Result<String> deleteCategory(@RequestParam long id) {
        boolean b = categoryService.removeById(id);
        if (!b) {
            return Result.error("无法删除指定的套餐或菜系的分类，可能该项已经被删除？");
        }
        return Result.success("删除成功");
    }

    /**
     * 修改套餐、菜系分类的接口
     *
     * @param category
     * @return
     */
    @PutMapping
    public Result<String> modifyCategory(@RequestBody Category category) {
        if (category == null) {
            return Result.error("修改失败，传入参数为空。");
        }
        boolean id = categoryService.update(category, new QueryWrapper<Category>().eq("id", category.getId()));
        if (id) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }
}



