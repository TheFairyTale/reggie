package asia.dreamdropsakura.reggie.mapper;

import asia.dreamdropsakura.reggie.entity.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 地址簿数据库表映射类
 *
 * @author itheima
 * @since 2022-9-25
 */
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}
