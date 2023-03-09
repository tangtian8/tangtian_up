import java.util.Stack;

/**
 * @author tangtian
 * @description
 * @date 2022/12/26 8:21
 */
public class MinStack {

    private Stack<Integer> stackA;
    private Stack<Integer> stackB;
    /** initialize your data structure here. */
    public MinStack() {
        stackA = new Stack<>();
        stackB = new Stack<>();
    }

    public void push(int x) {
       stackA.push(x);
       if (stackB.empty() || stackB.peek() >= x){
           stackB.push(x);
       }
    }

    public void pop() {
        if (stackA.pop().equals(stackB.peek())){
            stackB.pop();
        }
    }

    public int top() {
        return stackA.peek();
    }

    public int min() {
        return stackB.peek();
    }
}
