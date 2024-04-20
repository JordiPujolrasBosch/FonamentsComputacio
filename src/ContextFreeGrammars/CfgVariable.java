package ContextFreeGrammars;

import java.util.Objects;

public class CfgVariable {
    private final char c;
    private final int n;

    public CfgVariable(char c, int n){
        this.c = c;
        this.n = n;
    }

    public static boolean check(String s){
        boolean isvar = s.length() >= 2;
        isvar = isvar && s.charAt(0) >= 'A' && s.charAt(0) <= 'Z';
        if(isvar){
            for(int i=1; i < s.length(); i++) isvar = isvar && s.charAt(i) >= '0' && s.charAt(i) <= '9';
        }
        return isvar;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CfgVariable that = (CfgVariable) o;
        return c == that.c && n == that.n;
    }
}
