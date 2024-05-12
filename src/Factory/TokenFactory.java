package Factory;

import Elements.Alphabet;
import Factory.Builders.RegexOperator;
import Grammars.GramexChar;
import Grammars.GramexConcat;
import Grammars.GramexNonEmpty;
import RegularExpressions.RegexChar;
import RegularExpressions.RegexConcat;
import RegularExpressions.RegexUnion;
import RegularExpressions.RegularExpression;

import java.util.*;

public class TokenFactory {

    private final static Map<String, Set<Character>> gtokenGroup = buildGTokensGroup();

    public static Set<Character> gtokenGroupSet(String act) {
        return gtokenGroup.get(act);
    }

    private static Map<String, Set<Character>> buildGTokensGroup(){
        Map<String, Set<Character>> mapper = new HashMap<>();
        Set<Character> set = null;

        set = new HashSet<>();
        for(char c = 'a'; c <= 'z'; c++) set.add(c);
        mapper.put("$a", set);

        set = new HashSet<>();
        for(char c = 'A'; c <= 'Z'; c++) set.add(c);
        mapper.put("$A", set);

        set = new HashSet<>();
        for(char c = '0'; c <= '9'; c++) set.add(c);
        mapper.put("$0", set);

        return mapper;
    }




    public static char getSpecialChar(){return '$';}

    //A-TOKENS

    public static String getAEmptyChar(){return "$/";}
    public static String getANothing(){return "$w";}

    //A-CHAR
    /*
       $/ => EmptyChar
       $s => ' '
       $c => ','
    */

    private final static Map<String,Character> aChar = buildAChar();
    private static Map<String,Character> buildAChar(){
        Map<String,Character> mapper = new HashMap<>();
        mapper.put("$/", Alphabet.getEmptyChar());
        mapper.put("$s", ' ');
        mapper.put("$c", ',');
        return mapper;
    }

    public static boolean isAChar(String x){return aChar.containsKey(x);}
    public static char getAChar(String x){return aChar.get(x);}

    private final static Map<Character,String> aString = buildAString();
    private static Map<Character,String> buildAString(){
        Map<Character,String> mapper = new HashMap<>();
        mapper.put(Alphabet.getEmptyChar(), "$/");
        mapper.put(' ', "$s");
        mapper.put(',', "$c");
        return mapper;
    }

    public static boolean isAString(char x){return aString.containsKey(x);}
    public static String getAString(char x){return aString.get(x);}

    //A-GROUP
    /*
       $a => a..z
       $A => A..Z
       $0 => 0..9
    */

    private final static Map<String,Set<Character>> aGroup = buildAGroup();
    private static Map<String,Set<Character>> buildAGroup(){
        Map<String,Set<Character>> mapper = new HashMap<>();

        Set<Character> lower = new HashSet<>();
        Set<Character> upper = new HashSet<>();
        Set<Character> numbers = new HashSet<>();

        for(char c = 'a'; c <= 'z'; c++) lower.add(c);
        for(char c = 'A'; c <= 'Z'; c++) upper.add(c);
        for(char c = '0'; c <= '9'; c++) numbers.add(c);

        mapper.put("$a", lower);
        mapper.put("$A", upper);
        mapper.put("$0", numbers);

        return mapper;
    }

    public static boolean isAGroup(String x){return aGroup.containsKey(x);}
    public static Set<Character> getAGroup(String x){return aGroup.get(x);}



    //B-TOKENS

    //B-CHAR
    /*
       $/ => EmptyChar
       $s => ' '
    */

    private final static Map<String,Character> bChar = buildBChar();
    private static Map<String,Character> buildBChar(){
        Map<String,Character> mapper = new HashMap<>();
        mapper.put("$/", Alphabet.getEmptyChar());
        mapper.put("$s", ' ');
        return mapper;
    }

    public static boolean isBChar(String x){return bChar.containsKey(x);}
    public static char getBChar(String x){return bChar.get(x);}

    private final static Map<Character,String> bString = buildBString();
    private static Map<Character,String> buildBString(){
        Map<Character,String> mapper = new HashMap<>();
        mapper.put(Alphabet.getEmptyChar(), "$/");
        mapper.put(' ', "$s");
        return mapper;
    }

    public static boolean isBString(char x){return bString.containsKey(x);}
    public static String getBString(char x){return bString.get(x);}



    //R-TOKENS

    public static String getRVoid()      {return "#";}
    public static String getRUnion()     {return "|";}
    public static String getRStar()      {return "*";}
    public static String getREmptyChar() {return "/";}

    //R-OPERATOR
    /*
       ( => Open priority parenthesis
       ) => Close priority parenthesis
       | => Union
       * => Star
       + => Plus
       # => Void
       / => EmptyChar
    */

    private final static Map<Character,RegexOperator> rOperator = buildROperator();
    private static Map<Character,RegexOperator> buildROperator(){
        Map<Character,RegexOperator> mapper = new HashMap<>();
        mapper.put('(', RegexOperator.OPEN);
        mapper.put(')', RegexOperator.CLOSE);
        mapper.put('|', RegexOperator.UNION);
        mapper.put('*', RegexOperator.STAR);
        mapper.put('+', RegexOperator.PLUS);
        mapper.put('#', RegexOperator.VOID);
        mapper.put('/', RegexOperator.EMPTY);
        return mapper;
    }

    public static boolean isROperator(char x){return rOperator.containsKey(x);}
    public static RegexOperator getROperator(char x){return rOperator.get(x);}

    private final static Map<RegexOperator,Character> rOpchar = buildROpchar();
    private static Map<RegexOperator,Character> buildROpchar(){
        Map<RegexOperator,Character> mapper = new HashMap<>();
        mapper.put(RegexOperator.OPEN, '(');
        mapper.put(RegexOperator.CLOSE, ')');
        mapper.put(RegexOperator.UNION, '|');
        mapper.put(RegexOperator.STAR, '*');
        mapper.put(RegexOperator.PLUS, '+');
        mapper.put(RegexOperator.VOID, '#');
        mapper.put(RegexOperator.EMPTY, '/');
        return mapper;
    }

    public static boolean isROpchar(RegexOperator x){return rOpchar.containsKey(x);}
    public static char getROpchar(RegexOperator x){return rOpchar.get(x);}

    //R-CHAR
    /*
       $( => '('
       $) => ')'
       $| => '|'
       $* => '*'
       $+ => '+'
       $# => '#'
       $/ => '/'
       $s => ' '
       $$ => '$'
    */

    private final static Map<String,Character> rChar = buildRChar();
    private static Map<String,Character> buildRChar(){
        Map<String,Character> mapper = new HashMap<>();
        mapper.put("$(", '(');
        mapper.put("$)", ')');
        mapper.put("$|", '|');
        mapper.put("$*", '*');
        mapper.put("$+", '+');
        mapper.put("$#", '#');
        mapper.put("$/", '/');
        mapper.put("$s", ' ');
        mapper.put("$$", '$');
        return mapper;
    }

    public static boolean isRChar(String x){return rChar.containsKey(x);}
    public static char getRChar(String x){return rChar.get(x);}

    private final static Map<Character,String> rString = buildRString();
    private static Map<Character,String> buildRString(){
        Map<Character,String> mapper = new HashMap<>();
        mapper.put('(', "$(");
        mapper.put(')', "$)");
        mapper.put('|', "$|");
        mapper.put('*', "$*");
        mapper.put('+', "$+");
        mapper.put('#', "$#");
        mapper.put('/', "$/");
        mapper.put(' ', "$s");
        mapper.put('$', "$$");
        return mapper;
    }

    public static boolean isRString(char x){return rString.containsKey(x);}
    public static String getRString(char x){return rString.get(x);}

    //R-GROUP
    /*
       $a => a|..|z
       $A => A|..|Z
       $0 => 0|..|9
    */

    private final static Map<String,RegularExpression> rGroup = buildRGroup();
    private static Map<String,RegularExpression> buildRGroup(){
        Map<String,RegularExpression> mapper = new HashMap<>();

        RegularExpression lower = new RegexChar('a');
        RegularExpression upper = new RegexChar('A');
        RegularExpression numbers = new RegexChar('0');

        for(char c = 'b'; c <= 'z'; c++) lower = new RegexConcat(lower, new RegexChar(c));
        for(char c = 'B'; c <= 'Z'; c++) upper = new RegexConcat(upper, new RegexChar(c));
        for(char c = '1'; c <= '9'; c++) numbers = new RegexConcat(numbers, new RegexChar(c));

        mapper.put("$a", lower);
        mapper.put("$A", upper);
        mapper.put("$0", numbers);

        return mapper;
    }

    public static boolean isRGroup(String x){return rGroup.containsKey(x);}
    public static RegularExpression getRGroup(String x){return rGroup.get(x);}



    //G-TOKENS

    //G-OPERATOR
    /*
       |   => Union
       /   => EmptyChar
       ->  => Arrow
    */

    public static String getGUnion(){return "|";}
    public static String getGEmptyChar(){return "/";}
    public static String getGArrow(){return "->";}

    //G-CHAR
    /*
       $| => '|'
       $/ => '/'
       $s => ' '
    */

    private final static Map<String,Character> gChar = buildGChar();
    private static Map<String,Character> buildGChar(){
        Map<String,Character> mapper = new HashMap<>();
        mapper.put("$|", '|');
        mapper.put("$/", '/');
        mapper.put("$s", ' ');
        return mapper;
    }

    public static boolean isGChar(String x){return gChar.containsKey(x);}
    public static char getGChar(String x){return gChar.get(x);}

    private final static Map<Character,String> gString = buildGString();
    private static Map<Character,String> buildGString(){
        Map<Character,String> mapper = new HashMap<>();
        mapper.put('|', "$|");
        mapper.put('/', "$/");
        mapper.put(' ', "$s");
        return mapper;
    }

    public static boolean isGString(char x){return gString.containsKey(x);}
    public static String getGString(char x){return gString.get(x);}

    //G-GROUP
    /*
       $a => a|..|z
       $A => A|..|Z
       $0 => 0|..|9
    */

    private final static Map<String, GramexNonEmpty> gGroup = buildGGroup();
    private static Map<String, GramexNonEmpty> buildGGroup(){
        Map<String, GramexNonEmpty> mapper = new HashMap<>();

        GramexNonEmpty lower = new GramexChar('a');
        GramexNonEmpty upper = new GramexChar('A');
        GramexNonEmpty numbers = new GramexChar('0');

        for(char c = 'b'; c <= 'z'; c++) lower = new GramexConcat(lower, new GramexChar(c));
        for(char c = 'B'; c <= 'Z'; c++) upper = new GramexConcat(upper, new GramexChar(c));
        for(char c = '1'; c <= '9'; c++) numbers = new GramexConcat(numbers, new GramexChar(c));

        mapper.put("$a", lower);
        mapper.put("$A", upper);
        mapper.put("$0", numbers);

        return mapper;
    }

    public static boolean isGGroup(String x){return gGroup.containsKey(x);}
    public static GramexNonEmpty getGGroup(String x){return gGroup.get(x);}

}
