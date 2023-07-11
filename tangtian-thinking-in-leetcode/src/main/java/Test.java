/**
 * @author tangtian
 * @description
 * @date 2023/6/19 18:40
 */
public class Test {
    public static void main(String [] args) {
        String projectA = "1122";
        String subProject1 = "11";
        String subProject2 = "22";
        String projectB = "11" + "22";
        String projectC = String.valueOf(1122);
        String projectD = subProject1 + subProject2;
        System.out.println(projectA == projectB);
        System.out.println(projectB == projectC);
        System.out.println(projectC == projectD);
        System.out.println(projectC.equals(projectA));
    }
}
