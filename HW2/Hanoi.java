import java.util.*;

public class Hanoi {

    public static String convert (String[] hanoi, int src, int dest) {
        String result = "";        
        for (int i = 0; i < hanoi.length; i++) {
            if (i == src) {
                if (hanoi[i].length() == 1) result += "0";
                else result += hanoi[i].substring(1);                    
            } else if (i == dest) {
                if (hanoi[i].length() == 1 && hanoi[i].charAt(0) == '0') result += hanoi[src].charAt(0);
                else result += hanoi[src].charAt(0) + hanoi[i]; 
            } else { result += hanoi[i]; }
            result += " ";
        }      
        return result;
    }

    public static List<String> getSuccessor(String[] hanoi) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < hanoi.length; i ++) {
            if (hanoi[i].equals("0")) continue;  // no disk to move
            if (i - 1 >= 0)  // move disk to previous
                if (hanoi[i - 1].equals("0") || hanoi[i].charAt(0) < hanoi[i - 1].charAt(0))
                    result.add(convert(hanoi, i, i - 1));

            if (i + 1 <= hanoi.length - 1)  // move disk to the next
                if (hanoi[i + 1].equals("0") || hanoi[i].charAt(0) < hanoi[i + 1].charAt(0))
                    result.add(convert(hanoi, i, i + 1));
        }
        return result;    
    }

    public static void main(String[] args) {
        if (args.length < 3) return;
        List<String> sucessors = getSuccessor(args);
        for (int i = 0; i < sucessors.size(); i++) System.out.println(sucessors.get(i));
    }
}