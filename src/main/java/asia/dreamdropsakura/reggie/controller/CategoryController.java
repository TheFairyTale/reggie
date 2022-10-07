package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import asia.dreamdropsakura.reggie.entity.Category;
import asia.dreamdropsakura.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 分页获取所有套餐与菜系分类并按1排序规则排序、2分类排序、3名称依次排序
     *
     * @param page     当前页数
     * @param pageSize 每页记录数
     * @return
     */
    @GetMapping({"/page"})
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
     * 返回添加菜品(菜肴)页面所需的菜系选择下拉框所需的列表
     *
     * @return
     */
    @GetMapping({"/list"})
    // get 请求的参数如果希望封装为一个实体类对象则不需要加注解
    public Result<List<Category>> getCategoryList(Category category) {
        List<Category> list = null;

        log.warn("检查缓存中...");
        // 判断缓存中是否已有指定id 对应的所有已启售菜肴
        Object category1 = redisTemplate.opsForValue().get("category");
        if (category1 == null) {
            // 条件构造器, 用于实现依据sort 字段进行数据的正序倒序排序
            LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            // 添加排序条件，根据sort 字段按正序排序
            categoryLambdaQueryWrapper.eq(category.getType() != null, Category::getType, category.getType()).orderByAsc(Category::getSort).orderByAsc(Category::getName);
            // 列出所有内容
            list = categoryService.list(categoryLambdaQueryWrapper);
            redisTemplate.opsForValue().set("category", list);
        } else {
            list = (List<Category>) category1;
        }

        /*log.warn("sql result: ");
        log.warn(list.toString());*/

        /*List<Category> categories = new ArrayList<>();
        Category studentCategory = new Category();
        studentCategory.setName("套餐");
        studentCategory.setId(1573617588406702081L);
        categories.add(studentCategory);*/
        return Result.success(list);
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
            // todo (redisTemplate)每个新增、删除、修改 的方法都需要在相同逻辑后加相同的清除代码，AOP可以实现吗
            Boolean hasDeleted = redisTemplate.delete("category");
            log.info(hasDeleted != null ? hasDeleted ? "[addCategory]已删除缓存数据" : "删除时出现问题" : "删除时出现问题");
            return Result.success("新增成功");
        }

        return Result.error("新增失败, 内容为空, 请重试");
    }

    /**
     * 删除套餐、菜系分类的接口. 需要确保要删除的套餐或菜品分类已不再被某个套餐与菜品所关联。
     * <p>
     * todo 如果不清楚需要构建什么查询条件，可以先在mysql 写原生sql 语句去实现功能，然后再使用java 代码实现功能
     */
    @DeleteMapping
    public Result<String> deleteCategory(@RequestParam long id) {
        boolean b = categoryService.removeById(id);
        // todo (redisTemplate)每个新增、删除、修改 的方法都需要在相同逻辑后加相同的清除代码，AOP可以实现吗
        Boolean hasDeleted = redisTemplate.delete("category");
        log.info(hasDeleted != null ? hasDeleted ? "[deleteCategory]已删除缓存数据" : "删除时出现问题" : "删除时出现问题");

        return b ? Result.error("无法删除指定的套餐或菜系的分类，可能该项已经被删除？") : Result.success("删除成功");
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

        // todo (redisTemplate)每个新增、删除、修改 的方法都需要在相同逻辑后加相同的清除代码，AOP可以实现吗
        Boolean hasDeleted = redisTemplate.delete("category");
        log.info(hasDeleted != null ? hasDeleted ? "[modifyCategory]已删除缓存数据" : "删除时出现问题" : "删除时出现问题");

        return id ? Result.success("修改成功") : Result.error("修改失败");
    }
}



