package asia.dreamdropsakura.reggie;

import asia.dreamdropsakura.reggie.dto.DishDto;
import asia.dreamdropsakura.reggie.entity.DishFlavor;
import asia.dreamdropsakura.reggie.mapper.DishFlavorMapper;
import asia.dreamdropsakura.reggie.mapper.DishMapper;
import asia.dreamdropsakura.reggie.service.DishFlavorService;
import asia.dreamdropsakura.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;

@SpringBootTest
class ReggieTakeoutApplicationTests {

	@Autowired
	private DishService dishService;

	@Autowired
	private DishMapper dishMapper;

	@Autowired
	private DishFlavorService dishFlavorService;

	@Autowired
	private DishFlavorMapper dishFlavorMapper;

	@Test
	void contextLoads() {
	}

	/**
	 * 根据指定条件分页查询所有菜肴
	 *
	 */
	@Test
	public void testDishPaginationQueryWithCondition() {
		Page<DishDto> result = dishService.selectByPaginationWithDishAndCategoryTable(1, 5, null);
		for (DishDto dto : result.getRecords()) {
			System.out.println("Name: " + dto.getCategoryName() + " _ " + dto.getName() + " _ img: " + dto.getImage());
		}
	}

	/**
	 * 根据指定条件分页查询所有菜肴
	 *
	 */
	@Test
	public void testDishPaginationQueryWithConditionAndName() {
		Page<DishDto> result = dishService.selectByPaginationWithDishAndCategoryTable(1, 5, "麻辣");
		for (DishDto dto : result.getRecords()) {
			System.out.println("Name: " + dto.getCategoryName() + " _ " + dto.getName() + " _ img: " + dto.getImage());
		}
	}

	@Test
	public void testGetAllRecordsCount() {
		System.out.println(dishMapper.selectCount(null));
	}

	@Test
	public void testRemoveDishAndFlavors() {
		ArrayList<Long> ids = new ArrayList<>();
		ids.add(1413385247889891330L);
		// 移除菜肴及菜肴所属的所有口味
		int i = dishMapper.deleteBatchIds(ids);
		boolean remove = dishFlavorService.remove(new LambdaQueryWrapper<DishFlavor>().in(DishFlavor::getDishId, ids));

	}
}
