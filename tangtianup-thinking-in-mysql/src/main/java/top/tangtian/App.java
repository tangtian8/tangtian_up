package top.tangtian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        int[] nums = new int[]{-1,0,1,2,-1,-4};
        Arrays.sort(nums);
        List<List<Integer>> list = new ArrayList();
        for(int i = 0; i < nums.length - 2; i++){
            for(int j = i + 1; j < nums.length - 1; j ++){
                int c = nums.length - 1;
                while(c > j){
                    if(nums[i] + nums[j] == -1 * nums[c]){
                        List<Integer> listReuslt = new ArrayList();
                        listReuslt.add(nums[i]);
                        listReuslt.add(nums[j]);
                        listReuslt.add(nums[c]);
                        list.add(listReuslt);
                    }
                    c--;
                }

            }
        }
        System.out.println( list);
    }
}
