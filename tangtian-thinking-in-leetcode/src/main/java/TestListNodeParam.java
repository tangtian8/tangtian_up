import org.openjdk.jol.vm.VM;
import pojo.ListNode;

/**
 * @author tangtian
 * @description
 * @date 2023/7/11 7:32
 */
public class TestListNodeParam {
    public static void main(String[] args) {
        ListNode l1 = new ListNode(0);
        ListNode l2 = new ListNode(1,l1);
        ListNode l3 = new ListNode(2,l2);
        print(l3,"1");
        change(l3);
//        ListNode temp = l3;
        print(l3,"2");
        while (l3 != null){
            System.out.println(l3.val);
            l3 = l3.next;

        }
        System.out.println(l3);

    }

    private static void change(ListNode l3) {
        print(l3,"change");
        while (l3 != null){
            System.out.println(l3.val);
            l3 = l3.next;
        }
    }

    private static void print(Object obj,String preFix){
        System.out.println(preFix + "Memory address: " + VM.current().addressOf(obj));
//        System.out.println(preFix + "toString: " + obj);
//        System.out.println(preFix + "hashCode: " + obj.hashCode());
//        System.out.println(preFix + "hashCode: " + System.identityHashCode(obj));
    }
}
