package asia.dreamdropsakura.reggie.controller;

import asia.dreamdropsakura.reggie.common.Result;
import asia.dreamdropsakura.reggie.entity.Category;
import asia.dreamdropsakura.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜品与套餐分类管理相关控制器类
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
     * 分页获取所有套餐与菜品分类
     *
     * @param page 当前页数
     * @param pageSize 每页记录数
     * @return
     */
    @GetMapping("/page")
    public Result<IPage> getCategories(int page, int pageSize) {
        Page<Category> categoryPage = new Page<>(page, pageSize);
        Page<Category> page1 = categoryService.page(categoryPage);
        return Result.success(page1);
    }

    /**
     * 新增套餐、菜品分类的接口
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
     * 删除套餐、菜品分类的接口
     *
     */
    @DeleteMapping
    public Result<String> deleteCategory(@RequestParam long id) {
        boolean b = categoryService.removeById(id);
        if (!b) {
            return Result.error("无法删除指定的套餐或菜品的分类，可能该项已经被删除？");
        }
        return Result.success("删除成功");
    }

}



