package asia.dreamdropsakura.reggie.service.impl;

import asia.dreamdropsakura.reggie.entity.Employee;
import asia.dreamdropsakura.reggie.mapper.EmployeeMapper;
import asia.dreamdropsakura.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 员工业务代码实现类
 *
 * @author 童话的爱
 * @since 2022-9-17
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
