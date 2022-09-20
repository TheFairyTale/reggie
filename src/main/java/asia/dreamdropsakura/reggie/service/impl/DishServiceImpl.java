package asia.dreamdropsakura.reggie.service.impl;

import asia.dreamdropsakura.reggie.entity.Dish;
import asia.dreamdropsakura.reggie.mapper.DishMapper;
import asia.dreamdropsakura.reggie.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 菜肴业务代码实现类
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
