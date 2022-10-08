package asia.dreamdropsakura.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>
 * 购物车
 * </p>
 *
 * @author 童话的爱
 * @since 2022-09-25
 */
@TableName("shopping_cart")
@Data
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     *
     */
    private Long id;

    /**
     * 名称
     *
     */
    private String name;

    /**
     * 图片
     *
     */
    private String image;

    /**
     * 用户id
     *
     */
    private Long userId;

    /**
     * 菜肴id
     *
     */
    private Long dishId;

    /**
     * 套餐id
     *
     */
    private Long setmealId;

    /**
     * 口味
     *
     */
    private String dishFlavor;

    /**
     * 数量
     *
     */
    private Integer number;

    /**
     * 金额
     *
     */
    private BigDecimal amount;

    /**
     * 创建时间
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.DEFAULT)
    private Long createUser;


    @TableField(fill = FieldFill.DEFAULT)
    private Long updateUser;

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
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }
    public Long getSetmealId() {
        return setmealId;
    }

    public void setSetmealId(Long setmealId) {
        this.setmealId = setmealId;
    }
    public String getDishFlavor() {
        return dishFlavor;
    }

    public void setDishFlavor(String dishFlavor) {
        this.dishFlavor = dishFlavor;
    }
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }



    @Override
    public String toString() {
        return "ShoppingCart{" +
            "id=" + id +
            ", name=" + name +
            ", image=" + image +
            ", userId=" + userId +
            ", dishId=" + dishId +
            ", setmealId=" + setmealId +
            ", dishFlavor=" + dishFlavor +
            ", number=" + number +
            ", amount=" + amount +
            ", createTime=" + createTime +
        "}";
    }
}
