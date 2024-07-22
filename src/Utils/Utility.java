package Utils;

public class Utility {
    public static boolean isNumber(String x){
        if(x.isEmpty()) return false;
        int i=0;
        boolean isNumber = true;
        while(i<x.length() && isNumber){
            char c = x.charAt(i);
            isNumber = c >= '0' && c <= '9';
            i++;
        }
        return isNumber;
    }

    public static boolean isNegativeNumber(String x){
        if(x.length() < 2) return false;
        return x.charAt(0) == '-' && isNumber(x.substring(1));
    }
}
