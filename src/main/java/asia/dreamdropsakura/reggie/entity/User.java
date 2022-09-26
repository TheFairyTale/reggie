package asia.dreamdropsakura.reggie.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author 童话的爱
 * @since 2022-09-25
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     *
     */
    private Long id;

    /**
     * 姓名
     *
     */
    private String name;

    /**
     * 手机号
     *
     */
    private String phone;

    /**
     * 性别
     *
     */
    private String sex;

    /**
     * 身份证号
     *
     */
    private String idNumber;

    /**
     * 头像
     *
     */
    private String avatar;

    /**
     * 状态 0:禁用，1:正常
     *
     */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", name=" + name +
            ", phone=" + phone +
            ", sex=" + sex +
            ", idNumber=" + idNumber +
            ", avatar=" + avatar +
            ", status=" + status +
        "}";
    }
}
