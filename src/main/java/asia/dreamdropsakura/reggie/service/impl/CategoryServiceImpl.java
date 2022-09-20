package asia.dreamdropsakura.reggie.service.impl;

import asia.dreamdropsakura.reggie.entity.Category;
import asia.dreamdropsakura.reggie.entity.Dish;
import asia.dreamdropsakura.reggie.entity.Setmeal;
import asia.dreamdropsakura.reggie.exception.CategoryException;
import asia.dreamdropsakura.reggie.mapper.CategoryMapper;
import asia.dreamdropsakura.reggie.service.CategoryService;
import asia.dreamdropsakura.reggie.service.DishService;
import asia.dreamdropsakura.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 菜系与套餐分类管理业务代码实现类
 *
 * @author 童话的爱
 * @since 2022-9-19
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setMealService;

    @Override
    public boolean removeById(Serializable id) {
        List<Dish> dishList = dishService.list(new LambdaQueryWrapper<Dish>().eq(Dish::getCategoryId, id));
        List<Setmeal> setmealList = setMealService.list(new LambdaQueryWrapper<Setmeal>().eq(Setmeal::getCategoryId, id));

        if (dishList.size() > 0 || setmealList.size() > 0) {
            throw new CategoryException("无法删除指定的套餐或菜系的分类，该套餐或菜系分类仍在被菜肴使用。请先删除该套餐或菜系分类下的所有菜肴然后再尝试删除");
        }
        return super.removeById(id);
    }
}
