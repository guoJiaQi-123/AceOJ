package com.tyut.ojcodebox;

import java.util.Arrays;

///**
// * @version v1.0
// * @author OldGj 2024/12/25
// * @apiNote TODO(一句话给出该类描述)
// */
//public class Main {
//    public static void main(String[] args) {
//        int a = Integer.parseInt(args[0]);
//        int b = Integer.parseInt(args[1]);
//        System.out.println("结果是：" + a + b);
//    }
//}
public class Main {
    public static void main(String[] args) {
        String s = String.valueOf(args[0]);
        int begin = 0;
        int[] map = new int[128];
        int res = 0;
        Arrays.fill(map, -1);
        for (int end = 0; end < s.length(); end++) {
            char ch = s.charAt(end);
            if (map[ch] != -1) {
                begin = Math.max(begin, map[ch] + 1);
                map[ch] = end;
            } else {
                map[ch] = end;
            }
            res = Math.max(res, end - begin + 1);
        }
        System.out.println(res);
    }
}