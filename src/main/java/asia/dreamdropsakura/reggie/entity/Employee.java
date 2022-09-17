package asia.dreamdropsakura.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体
 *
 * @author 童话的爱
 * @since 2022-9-17
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    /**
     * 员工密码，设置为返回结果集时不包含
     *
     */
    @TableField(select = false)
    private String password;

    private String phone;

    private String sex;

    /**
     * 身份证号码
     *
     */
    private String idNumber;

    /**
     * 用户账号状态
     *
     * 0禁用 1正常
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
