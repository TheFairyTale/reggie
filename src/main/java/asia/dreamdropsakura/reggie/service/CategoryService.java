package asia.dreamdropsakura.reggie.service;

import asia.dreamdropsakura.reggie.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;

/**
 * 菜系与套餐分类管理业务代码抽象类
 *
 * @author 童话的爱
 * @since 2022-9-19
 */
public interface CategoryService extends IService<Category> {

    /**
     * 根据id 删除指定记录. 需要确保要删除的套餐或菜品分类已不再被某个套餐与菜品所关联。
     *
     * @param id
     * @exception asia.dreamdropsakura.reggie.exception.CategoryException 如果有菜肴或套餐仍与SetMeal 套餐或Dish 菜肴有关联，则抛出自定义异常。
     * @return
     */
    @Override
    boolean removeById(Serializable id);
}
