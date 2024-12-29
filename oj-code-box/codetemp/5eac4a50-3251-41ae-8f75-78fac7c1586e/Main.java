public class Main {
    public static void main(String[] args) {
        String s = Integer.parseInt(args[0]);
        int begin = 0;
        int[] map = new int[128];
        int res = 0;
        Arrays.fill(map,-1);
        for(int end = 0;end <s.length();end++){
            char ch = s.charAt(end);
            if(map[ch]!=-1){
                begin = Math.max(begin,map[ch]+1);
                map[ch] = end;
            }else{
                map[ch] = end;
            }
            res = Math.max(res,end-begin+1);
        }
        System.out.println(res);
    }
}