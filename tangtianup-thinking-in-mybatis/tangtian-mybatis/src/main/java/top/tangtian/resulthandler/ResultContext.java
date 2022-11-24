package top.tangtian.resulthandler;

public interface ResultContext<T> {

    T getResultObject();

    int getResultCount();
}
