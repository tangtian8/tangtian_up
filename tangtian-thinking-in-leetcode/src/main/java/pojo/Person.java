package pojo;

/**
 * @author tangtian
 * @description
 * @date 2023/7/10 18:53
 */
public class Person {
        private int age;
        private String name;

        public Person(){}

        public Person(int age, String name) {
            this.age = age;
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }
}
