import java.util.Stack;

/**
 * @author tangtian
 * @description
 * @date 2022/12/26 8:01
 */
public class CQueue {
    public CQueue() {
        s1 = new Stack<>();
        s2 = new Stack<>();
    }

    private Stack<Integer> s1;
    private Stack<Integer> s2;

    public void appendTail(int value) {
        Integer push = s1.push(value);
    }

    public int deleteHead() {
        if (s1.empty() && s2.empty()){
            return  -1;
        }
        if (s2.empty()){
            while (!s1.empty()){
                s2.push(s1.pop());
            }
        }
        Integer temp = s2.peek();
        s2.pop();
        return temp;
    }
}
