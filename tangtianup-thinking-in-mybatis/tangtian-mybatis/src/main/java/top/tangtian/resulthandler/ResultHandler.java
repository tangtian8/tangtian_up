package top.tangtian.resulthandler;

/**
 * @author tangtian
 * @description
 * @date 2022/11/23 8:34
 */
public interface ResultHandler<T>  {
    void handleResult(ResultContext<? extends T> resultContext);
}
