package com.se.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wtz
 * Date:2019/2/19
 */
public class Sum {

    public static void main(String[] args) {
        int[] intArray = new int[]{12, 3, 8, 56, 45, 98, 13, 26, 24, 76, 22, 31, 9, 45, 43, 27, 25, 24, 34, 19, 6, 28, 41};
        int[] twoSum = twoSum(intArray, 123);

        System.out.println(null == twoSum ? " 不存在" : twoSum[0] + " " + twoSum[1]);

//        System.out.println(new DecimalFormat("##.###").format(2.5625) );
    }

    /**
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
     * 给定 nums = [2, 7, 11, 15], target = 9 因为 nums[0] + nums[1] = 2 + 7 = 9 所以返回 [0, 1]
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>(nums.length);
        for (int i = 0; i < nums.length; i++) {
            int difference = target - nums[i];
            if (map.containsKey(difference)) {
                return new int[]{map.get(difference), i};
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }

}
