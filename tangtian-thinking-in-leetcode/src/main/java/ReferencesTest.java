/**
 * @author tangtian
 * @description
 * @date 2023/6/19 18:41
 */
public class ReferencesTest {
    static class Student {
        private String name;
    }
    public static void main(String[] args) {
        Student studentA = new Student();
        studentA.name = "aaa";
        Student studentB = new Student();
        studentB.name = "bbb";
        setName(studentA.name, "ccc");
        setName(studentB, "ccc");
        System.out.println(studentA.name + ", " + studentB.name);
    }
    private static void setName(String name, String newName) {
        name = newName;
    }
    private static void setName(Student student, String newName) {
        student.name = newName;
    }
}
