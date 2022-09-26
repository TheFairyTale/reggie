package asia.dreamdropsakura.reggie.service.impl;

import asia.dreamdropsakura.reggie.common.Result;
import asia.dreamdropsakura.reggie.dto.SetmealDto;
import asia.dreamdropsakura.reggie.entity.Dish;
import asia.dreamdropsakura.reggie.entity.DishFlavor;
import asia.dreamdropsakura.reggie.entity.Setmeal;
import asia.dreamdropsakura.reggie.entity.SetmealDish;
import asia.dreamdropsakura.reggie.exception.ApplicationException;
import asia.dreamdropsakura.reggie.mapper.SetmealDishMapper;
import asia.dreamdropsakura.reggie.mapper.SetmealMapper;
import asia.dreamdropsakura.reggie.service.SetmealDishService;
import asia.dreamdropsakura.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 套餐业务代码实现类
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private SetmealDishService setmealDishService;


    @Override
    public IPage<SetmealDto> paginationSetmeals(int page, int pageSize, String name) {
        if (name != null) {
            name = "%" + name + "%";
        }

        // 分页公式:
        // limit 起始索引, 查询条目数(或者叫每页显示条数)
        // 起始索引：(当前页码 - 1) * 每页显示条数
        List<SetmealDto> setmeals = setmealMapper.selectWith((page - 1) * pageSize, pageSize, name);
        Long aLong = setmealMapper.selectCount(null);
        return new Page<SetmealDto>().setCurrent(page).setTotal(aLong).setRecords(setmeals);
    }

    @Override
    public SetmealDto selectSetmealById(Long id) {
        // 由于一个套餐中不仅有套餐本身的信息，还包含套餐内所带的菜肴，故我们需要查两次
        // 一次查套餐信息，第二次拿着套餐id 去套餐所属菜肴表里查套餐id 等于该套餐id 的记录，并把套餐和菜肴装在Dto 对象中，再返回Dto 对象
        // 查询指定id 的套餐
        Setmeal setmeal = setmealMapper.selectById(id);

        // 查询指定套餐id 的菜肴
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(new LambdaQueryWrapper<SetmealDish>().eq(SetmealDish::getSetmealId, id));

        // 将setmeal 拷贝为setmealDish
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        // 设置setmealDto 中的菜肴
        setmealDto.setSetmealDishes(setmealDishes);

        return setmealDto;
    }

    @Override
    @Transactional
    public boolean updateSetmeal(SetmealDto setmealDto) {
        int update = setmealMapper.update(setmealDto, new LambdaQueryWrapper<Setmeal>().eq(Setmeal::getId, setmealDto.getId()));
        // 先删除，再添加
        int delete = setmealDishMapper.delete(new LambdaQueryWrapper<SetmealDish>().eq(SetmealDish::getSetmealId, setmealDto.getId()));
        // 由于SetmealDao 中的套餐内的菜肴不带有套餐id ，故向setmealDish 表加入时需要先为每个setmealDish 对象设置setmeal_id 才能正常插入数据表
        // 利用stream 流中的map 方法进行数据的设置
        List<SetmealDish> collect = setmealDto.getSetmealDishes().stream().map(new Function<SetmealDish, SetmealDish>() {
            @Override
            public SetmealDish apply(SetmealDish setmealDish) {
                setmealDish.setSetmealId(setmealDto.getId());
                return setmealDish;
            }
        }).collect(Collectors.toList());

        boolean b = setmealDishService.saveBatch(collect);
        return (update > 0) && b;
    }

    @Override
    @Transactional
    public boolean addSetmeal(SetmealDto setmealDto) {
        int insert = setmealMapper.insert(setmealDto);

        // 由于SetmealDao 中的套餐内的菜肴不带有套餐id ，故向setmealDish 表加入时需要先为每个setmealDish 对象设置setmeal_id 才能正常插入数据表
        // 利用stream 流中的map 方法进行数据的设置
        List<SetmealDish> collect = setmealDto.getSetmealDishes().stream().map(new Function<SetmealDish, SetmealDish>() {
            @Override
            public SetmealDish apply(SetmealDish setmealDish) {
                setmealDish.setSetmealId(setmealDto.getId());
                return setmealDish;
            }
        }).collect(Collectors.toList());

        boolean b = setmealDishService.saveBatch(collect);

        return insert > 0 && b;
    }

    @Override
    @Transactional
    @Deprecated
    public boolean deleteByIds(Collection<Long> ids) {
        // 查找id 为ids 中的所有id，且status为启售的记录。
        List<Setmeal> setmealList = setmealMapper.selectList(new LambdaQueryWrapper<Setmeal>().in(Setmeal::getId, ids).eq(Setmeal::getStatus, 1));

        // 只有当启售的记录为空时才可删除
        if (setmealList.size() > 0) {
            // 不能删除则抛出业务异常
            throw new ApplicationException("无法删除套餐，有套餐仍在售卖中。");
        }

        // 开始删除
        // 移除套餐及套餐所属的所有口味
        int i = setmealMapper.deleteBatchIds(ids);
        // 注意当此处删除时没有符合规则的目标对象而不是删除失败的，也会返回false. 最终导致整条SQL语句事务回滚
        boolean remove = setmealDishService.remove(new LambdaQueryWrapper<SetmealDish>().in(SetmealDish::getDishId, ids));
        if (i <= 0) {
            throw new ApplicationException("删除套餐失败，可能套餐已经被删除");
        }

        return true;
    }
}
