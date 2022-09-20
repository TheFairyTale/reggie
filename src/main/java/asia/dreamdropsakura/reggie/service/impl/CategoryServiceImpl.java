package asia.dreamdropsakura.reggie.service.impl;

import asia.dreamdropsakura.reggie.entity.Category;
import asia.dreamdropsakura.reggie.mapper.CategoryMapper;
import asia.dreamdropsakura.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 菜品与套餐分类管理业务代码实现类
 *
 * @author 童话的爱
 * @since 2022-9-19
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
