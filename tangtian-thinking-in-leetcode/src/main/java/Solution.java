import java.util.*;

/**
 * @author tangtian
 * @description
 * @date 2022/12/27 7:37
 */
public class Solution {
    public int[] reversePrint(ListNode head) {
        Stack<ListNode> stack = new Stack<ListNode>();
        ListNode temp = head;
        while (temp != null) {
            stack.push(temp);
            temp = temp.next;
        }
        int size = stack.size();
        int[] print = new int[size];
        for (int i = 0; i < size; i++) {
            print[i] = stack.pop().val;
        }
        return print;
    }

    public ListNode reverseList(ListNode head) {
        // pre记录cur的前一个节点，cur是当前遍历到的节点
        ListNode pre = null, cur = head;

        // 遍历链表一般用while循环
        while(cur != null){
            // 保存cur的下一个节点
            ListNode next = cur.next;
            // 修改cur指针，指向它前一个节点
            cur.next = pre;
            // 更新pre、cur, 同时后移一位
            pre = cur;
            cur = next;
        }

        // 此时cur为null, pre指向最后一个节点
        return pre;
    }


    public Node copyRandomList(Node head) {
        Node cur = head;

        Node dum = new Node(0);
        Node pre = dum;
        while(cur != null) {
            Node node = new Node(cur.val); // 复制节点 cur
            pre.next = node;               // 新链表的 前驱节点 -> 当前节点
            // pre.random = "???";         // 新链表的 「 前驱节点 -> 当前节点 」 无法确定
            cur = cur.next;                // 遍历下一节点
            pre = node;                    // 保存当前新节点
        }
        return dum.next;
    }

    public String replaceSpace(String s) {
        if (s == null){
            return null;
        }
        char[] chars = s.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (Character c : chars){
            if (c.equals(' ')){
                builder.append("%20");
            }else {
                builder.append(c);
            }
        }
        return builder.toString();
    }


    public String reverseLeftWords(String s, int n) {
        if (s == null || s.length() <= n){
            return s;
        }
        char[] chars = s.toCharArray();
        StringBuilder left = new StringBuilder();
        StringBuilder right = new StringBuilder();
        for (int i = 0; i< chars.length; i ++){
           if (i<=n){
               right.append(chars[i]);
           }else {
               left.append(chars[i]);
           }
        }
        left.append(right);
        return left.toString();
    }


    public int findRepeatNumber(int[] nums) {
        Map<Integer,Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++){
            if (map.get(nums[i]) != null){
               return nums[i];
            }else {
                map.put(nums[i],1);
            }
        }
        return 0;
    }

    public int search(int[] nums, int target) {
        int i = 0;
        for (int a = 0; a < nums.length; a++){
            if (target == nums[a]){
                i++;
            }
        }
        return i;
    }

    public int missingNumber(int[] nums) {
        int[] temp = new int[nums[nums.length - 1]];
        for (int i = 0; i < temp.length; i++){
            temp[nums[i]] = 1;
        }
        for (int i = 0;i<temp.length;i++){
            if (temp[i] == 0){
                return i;
            }
        }
        return 0;
    }

//[
//  [1,   4,  7, 11, 15],
//  [2,   5,  8, 12, 19],
//  [3,   6,  9, 16, 22],
//  [10, 13, 14, 17, 24],
//  [18, 21, 23, 26, 30]
//]
// target = 5
//

    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        for (int i = 0;i < matrix.length; i++){
            for (int j = 0; j < matrix[i].length; j++){
                if (target == matrix[i][j]){
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    public int minArray(int[] numbers) {
        if (numbers.length == 1){
            return numbers[0];
        }
        int a = numbers[0],b = numbers[1];
        for (int i = 0; i < numbers.length - 1; i++){
            a = numbers[i];
            b = numbers[i + 1];
            if (b<=a){
                return b;
            }
        }
        return numbers[0];
    }

//"abaccdeff"
    public char firstUniqChar(String s) {
        char[] chars = s.toCharArray();
        int[] ints = new int[(int)'z'];
        for (int i = 0;i < s.length() ; i++){

        }

        for (int i = 0;i < s.length(); i++){
            if (chars[i] != 'A'){
                return chars[i];
            }
        }
        return ' ';
    }

    public int[] levelOrder(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        Deque<TreeNode> d = new ArrayDeque<>();
        if (root != null){
            d.addLast(root);
        }
        while (!d.isEmpty()){
            TreeNode t = d.pollFirst();
            list.add(t.val);
            if (t.left != null){
                d.addLast(t.left);
            }
            if (t.right != null){
                d.addLast(t.right);
            }
        }
        int n = list.size();
        int[] ams = new int[n];
        for (int i = 0; i < n; i++){
            ams[i] = list.get(i);
        }
        return ams;
    }


    public List<List<Integer>> levelOrder1(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        Deque<TreeNode> d = new ArrayDeque<>();
        if (root != null){
            d.addLast(root);
        }
        while (!d.isEmpty()){
            List<Integer> tmp = new ArrayList<>();
            for (int i = d.size(); i > 0; i--){
                TreeNode mode = d.pollFirst();
                tmp.add(mode.val);
                if (mode.left != null){
                    d.addLast(mode.left);
                }
                if (mode.right != null){
                    d.addLast(mode.right);
                }
            }
            result.add(tmp);
        }
        return result;
    }


    public List<List<Integer>> levelOrder2(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        Queue<TreeNode> d = new LinkedList<>();
        if (root != null){
            d.add(root);
        }
        while (!d.isEmpty()){
            LinkedList<Integer> tmp = new LinkedList<>();
            for (int i = d.size(); i > 0; i--){
                TreeNode mode = d.poll();
                if (result.size()%2 == 0){
                  tmp.addFirst(mode.val);
                }else { tmp.addLast(mode.val);
                }
                if (mode.right != null){
                    d.add(mode.right);
                }
                if (mode.left != null){
                    d.add(mode.left);
                }

            }
            result.add(tmp);
        }
        return result;
    }

    public boolean isSubStructure(TreeNode A, TreeNode B) {
        if ((A != null && B != null) && (recur(A,B) || isSubStructure(A.left,B) || isSubStructure(A.right,B))){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private boolean  recur(TreeNode a, TreeNode b) {
        if (b == null){
            return true;
        }
        if (a == null || a.val != b.val){
            return false;
        }
        return recur(a.left,b.left) && recur(a.right,b.right);
    }

    public TreeNode mirrorTree(TreeNode root) {
        if(root == null){
            return null;
        }
        TreeNode temp = root.left;
        root.left = mirrorTree(root.right);
        root.right = mirrorTree(temp);
        return  root;
    }


    public boolean isSymmetric(TreeNode root){
        return root == null ? true : recur1(root.left,root.right);
    }

    private boolean recur1(TreeNode left,TreeNode right){
        if (left == null && right == null){
            return true;
        }
        if (left == null || right == null || left.val != right.val){
            return false;
        }
        return recur1(left.left,right.right) && recur1(left.right,right.left);
    }

    public int fib(int n) {
        final  int MOD = 1000000007;
        if (n < 2){
            return n;
        }
        int  p = 0, q = 0, r = 1;

        for (int i = 2 ;i <=n; i++){
            p = q;
            q = r;
            r = (p + q) % MOD;
        }
        return  r;
    }

    public int numWays(int n) {

       int[] res = new int[n + 1];
       for (int i = 0; i< n; i++){
           if (i == 0 || i == 1){
               res[i] = 1;
           }else {
               res[i] = (res[i - 1] + res[i - 2])%1000000007;
           }
       }
       return res[n];
    }

    public int fib1(int n) {
        // final  int MOD = 1000000007;
        // if (n < 2){
        //     return n;
        // }
        // int  p = 0, q = 0, r = 1;

        // for (int i = 2 ;i <=n; i++){
        //     p = q;
        //     q = r;
        //     r = (p + q) % MOD;
        // }
        // return  r;
        int[] result = new int[n + 1];
        if (n == 0){
            result[0] = 0;
        }
        if (n == 1){
            result[1] = 1;
        }
        for (int i = 2; i<= n; i++){
            result[i] = result[i - 1] + result[i - 2];
        }
        return result[n];
    }

    public int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        for(int i = 0; i < prices.length; i++){
            minPrice = Math.min(minPrice, prices[i]);
            maxProfit = Math.max(maxProfit, prices[i] - maxProfit);
        }
        return maxProfit;
    }
// [-2,1,-3,4,-1,2,1,-5,4]
    public int maxSubArray(int[] nums) {
        int pre = 0, maxAns = nums[0];
        for (int i = 0; i < nums.length; i++){
            pre = Math.max(pre+nums[i],nums[i]);
            maxAns = Math.max(maxAns,pre);
        }
        return maxAns;
    }

    public int maxValue(int[][] grid) {
        if (grid == null || grid.length == 0){
            return 0;
        }
        int m = grid.length;
        int n = grid[0].length;
        int[][] dp = new int[m+1][n+1];
        for (int i = 0;i < m; i++){
            for (int j = 0; j< n; j++){
                dp[i+ 1][j+ 1] = Math.max(dp[i+1][j],dp[i][j+1]) + grid[i][j];
            }
        }
        return dp[m][n];
    }

//给定一个数字，我们按照如下规则把它翻译为字符串：0 翻译成 “a” ，1 翻译成 “b”，……，11 翻译成 “l”，……，25 翻译成 “z”。一个数字可能有多个翻译。请编程实现一个函数，用来计算一个数字有多少种不同的翻译方法
//    爬楼梯这道题：爬楼梯时每次可以爬一层或两层，求有多少种不同的方法到达楼顶。而这道题换成了，每次可以选择一个数字或两个数字，用来合并成一个字符，求可以合成多少种字符串。
    public int translateNum(int num) {
        String sNum =String.valueOf(num);
        return 0;

    }
//请从字符串中找出一个最长的不包含重复字符的子字符串，计算该最长子字符串的长度。
//输入: "abcabcbb"
//输出: 3
//解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
    public int lengthOfLongestSubstring(String s) {
        return 0;
    }


    public static String longestCommonPrefix(String[] strs) {
        int len = strs.length;
        int minStrLen=strs[0].length();
        for(int i = 0; i < len; i++){
            minStrLen = Math.min(minStrLen,strs[i].length());
        }
        int minIndex = 0;
        for(int i = 0; i < len; i++){
            if(minStrLen == strs[i].length()){
                minIndex = i;
            }
        }
        String minStr =  strs[minIndex];

        for(int i = 0;i < len; i ++){
            for(int j = 0;j < minIndex; j ++){
                String str = strs[i];
                if(str.charAt(j) != minStr.charAt(j)){
                    return minStr.substring(0,j);
                }
            }
        }
        return "";
    }

    public static int minSubArrayLen(int target, int[] nums) {
        int len = nums.length;
        int minLen =  Integer.MAX_VALUE;
        for(int i = 0; i < len - 1 ; i++){
            int tempTarget = nums[i];
            for(int j = i + 1; j < len;j++){
                tempTarget = tempTarget + nums[j];
                if(tempTarget >= target){
                    int tempLen = j - i;
                    minLen = Math.min(minLen,tempLen);
                    break;
                }
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }


    public static void main(String[] args) {
        System.out.println(summaryRanges((new int[]{0,2,3,4,6,8,9})));
    }


    public static boolean containsNearbyDuplicate(int[] nums, int k) {
        // for(int i = 0;i < nums.length;i++){
        //     int jIndex = i+1;
        //     while(jIndex < nums.length){
        //         if(nums[i] == nums[jIndex] && Math.abs(i - jIndex)<=k){
        //             return true;
        //         }
        //         jIndex++;
        //     }
        // }
        // return false;

        Map<Integer,Integer> numsMap = new HashMap<>();

        for(int i = 0; i < nums.length; i++){
            Integer value = numsMap.get(nums[i]);
            if(numsMap.containsKey(nums[i]) && value != null){
                if(Math.abs(i - value)<=k){
                    return true;
                }
            }else{
                numsMap.put(nums[i],i);
            }
        }
        return false;

    }
//[0,2,3,4,6,8,9]
    public static List<String> summaryRanges(int[] nums) {
        int left = 0;int i = 0;
        List<String> list = new ArrayList<>();
        while(i < nums.length - 1){
            if(nums[i + 1] - 1 != nums[i]){
                if(nums[left] == nums[i]){
                    list.add(nums[left] + "");
                }else{
                    list.add(nums[left] + "->" + nums[i]);
                }
                left = i + 1;
            }
            i++;
        }
        if (nums[left] == nums[nums.length -1]){
            list.add(nums[left] + "");
        }else {
            list.add(nums[left] + "->" + nums[nums.length -1]);
        }
        return list;
    }

}
