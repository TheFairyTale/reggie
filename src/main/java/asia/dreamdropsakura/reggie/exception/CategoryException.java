package asia.dreamdropsakura.reggie.exception;

/**
 * 菜系与套餐分类自定义异常控制器
 * 在业务逻辑操作过程中，如果遇到一些业务参数、操作异常的情况下，我们直接抛出此异常即可。
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
public class CategoryException extends RuntimeException {
    public CategoryException(String msg) {
        super(msg);
    }
}
