import pojo.Person;

/**
 * @author tangtian
 * @description
 * @date 2023/7/10 18:52
 */
//问题：发现当传递的变量变为引用变量时，方法中形参的修改导致实参也发生了修改
//原因：Java当中，对象存放在堆中, 其引用存放在栈中。当实参创建副本传递给形参时，它们实际操作的都是堆内存中的同一个对象
public class TestObjectParam {
    public static void main(String[] args) {
        Person person = new Person(18, "luosanliang");
        System.out.println("对象修改前:" + person);
        changePerson(person);
        System.out.println("对象修改后:" + person);
    }

    public static void changePerson(Person person){
        person.setAge(20);
        person.setName("java");
    }
}
