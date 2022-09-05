package com.tangtian.lesson.jvm.memory;

/**
 * @author tangtian
 * @description
 * 是否有递归调用
 * 是否有大量循环或死循环
 * @date 2022/9/3 9:02
 */
public class MemoryStackDemo {
    static long count=0;

    public static void main(String[] args) {
        count++;
        System.out.println(count);
        main(args);
    }
}
