package asia.dreamdropsakura.reggie.service.impl;

import asia.dreamdropsakura.reggie.entity.User;
import asia.dreamdropsakura.reggie.mapper.UserMapper;
import asia.dreamdropsakura.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author 童话的爱
 * @since 2022-09-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
