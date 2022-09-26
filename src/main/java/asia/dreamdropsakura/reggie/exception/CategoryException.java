package asia.dreamdropsakura.reggie.exception;

/**
 * 菜系与套餐分类自定义异常控制器
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
public class CategoryException extends RuntimeException {
    public CategoryException(String msg) {
        super(msg);
    }
}
