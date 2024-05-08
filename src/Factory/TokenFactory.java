package Factory;

import Elements.Alphabet;
import Factory.Builders.RegexOperator;
import Grammars.RightChar;
import Grammars.RightConcat;
import Grammars.RightNonEmpty;
import RegularExpressions.RegexChar;
import RegularExpressions.RegexConcat;
import RegularExpressions.RegexUnion;
import RegularExpressions.RegularExpression;

import java.util.*;

public class TokenFactory {
    private final static Map<String, Set<Character>> atokens = buildATokens();
    private final static Map<Character, String> atokensReverse = buildATokensReverse();
    private final static Map<String, Character> btokens = buildBTokens();
    private final static Map<Character, String> btokensReverse = buildBTokensReverse();
    private final static Map<String, RegularExpression> rtokens = buildRTokens();
    private final static Map<Character, String> rtokensReverse = buildRTokensReverse();
    private final static Map<String, Character> gtokensChar = buildGTokensChar();
    private final static Map<String, Set<Character>> gtokenGroup = buildGTokensGroup();

    // A-TOKENS

    /*
       $/  => EmptyChar
       $s  => ' '
       $c  => ','
       $w  => nothing
       $a  => a..z
       $A  => A..Z
       $0  => 0..9
    */

    private static Map<String, Set<Character>> buildATokens(){
        Map<String, Set<Character>> mapper = new HashMap<>();
        Set<Character> set = null;

        set = new HashSet<>();
        set.add(Alphabet.getEmptyChar());
        mapper.put("$/", set);

        set = new HashSet<>();
        for(char c = 'a'; c <= 'z'; c++) set.add(c);
        mapper.put("$a", set);

        set = new HashSet<>();
        for(char c = 'A'; c <= 'Z'; c++) set.add(c);
        mapper.put("$A", set);

        set = new HashSet<>();
        for(char c = '0'; c <= '9'; c++) set.add(c);
        mapper.put("$0", set);

        set = new HashSet<>();
        set.add(' ');
        mapper.put("$s", set);

        set = new HashSet<>();
        set.add(',');
        mapper.put("$c", set);

        set = new HashSet<>();
        mapper.put("$w", set);

        return mapper;
    }

    private static Map<Character, String> buildATokensReverse(){
        Map<Character, String> mapper = new HashMap<>();

        mapper.put(Alphabet.getEmptyChar(), "$/");
        mapper.put(' ', "$s");
        mapper.put(',', "$c");

        return mapper;
    }

    public static boolean atokensContains(String s) {
        return atokens.containsKey(s);
    }

    public static boolean atokensReverseContains(Character c) {
        return atokensReverse.containsKey(c);
    }

    public static Set<Character> atokensGet(String s) {
        return atokens.get(s);
    }

    public static String atokensReverseGet(Character c) {
        return atokensReverse.get(c);
    }

    public static String atokensGetNothing() {
        return "$w";
    }

    public static String atokensGetEmptyChar() {
        return "$/";
    }

    // B-TOKENS

    /*
       $/  => EmptyChar
       $s  => ' '
    */

    private static Map<String, Character> buildBTokens(){
        Map<String, Character> mapper = new HashMap<>();

        mapper.put("$/", Alphabet.getEmptyChar());
        mapper.put("$s", ' ');

        return mapper;
    }

    private static Map<Character, String> buildBTokensReverse(){
        Map<Character, String> mapper = new HashMap<>();

        mapper.put(Alphabet.getEmptyChar(), "$/");
        mapper.put(' ', "$s");

        return mapper;
    }

    public static boolean btokensContains(String s) {
        return btokens.containsKey(s);
    }

    public static boolean btokensReverseContains(Character character) {
        return btokensReverse.containsKey(character);
    }

    public static Character btokensGet(String s) {
        return btokens.get(s);
    }

    public static String btokensReverseGet(Character character) {
        return btokensReverse.get(character);
    }

    // REGEX

    public static String regexVoidString() {
        return "#";
    }

    public static String regexUnionString() {
        return "|";
    }

    public static String regexStarString() {
        return "*";
    }

    public static String regexEmptyCharString() {
        return "/";
    }

    public static boolean rtokensReverseContains(Character c){
        return rtokensReverse.containsKey(c);
    }

    public static String rtokensReverseGet(Character c){
        return rtokensReverse.get(c);
    }

    //Grammar

    public static String getGrammarUnion() {
        return "|";
    }

    public static String getGrammarEmpty() {
        return "/";
    }

    public static boolean gtokenCharContains(String act) {
        return gtokensChar.containsKey(act);
    }

    public static Character gtokenCharGet(String act) {
        return gtokensChar.get(act);
    }

    public static boolean gtokenGroupContains(String act) {
        return gtokenGroup.containsKey(act);
    }

    public static Set<Character> gtokenGroupSet(String act) {
        return gtokenGroup.get(act);
    }

    //Build









    private static Map<String, RegularExpression> buildRTokens(){
        Map<String, RegularExpression> mapper = new HashMap<>();
        RegularExpression regex = null;

        regex = new RegexChar('(');
        mapper.put("$(", regex);

        regex = new RegexChar(')');
        mapper.put("$)", regex);

        regex = new RegexChar('|');
        mapper.put("$|", regex);

        regex = new RegexChar('*');
        mapper.put("$*", regex);

        regex = new RegexChar('+');
        mapper.put("$+", regex);

        regex = new RegexChar('#');
        mapper.put("$#", regex);

        regex = new RegexChar('/');
        mapper.put("$/", regex);

        regex = new RegexChar(' ');
        mapper.put("$s", regex);

        regex = new RegexChar('$');
        mapper.put("$$", regex);

        regex = new RegexChar('0');
        for(char c = '1'; c <= '9'; c++) regex = new RegexUnion(new RegexChar(c), regex);
        mapper.put("$0", regex);

        regex = new RegexChar('a');
        for(char c = 'b'; c <= 'z'; c++) regex = new RegexUnion(new RegexChar(c), regex);
        mapper.put("$a", regex);

        regex = new RegexChar('A');
        for(char c = 'B'; c <= 'Z'; c++) regex = new RegexUnion(new RegexChar(c), regex);
        mapper.put("$A", regex);

        return mapper;
    }

    private static Map<Character, String> buildRTokensReverse(){
        Map<Character, String> mapper = new HashMap<>();

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

    private static Map<String, Character> buildGTokensChar(){
        Map<String, Character> mapper = new HashMap<>();

        mapper.put("$/", '/');
        mapper.put("$|", '|');
        mapper.put("$s", ' ');

        return mapper;
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

    public static char getGUnion(){return '|';}
    public static char getGEmptyChar(){return '/';}
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

    private final static Map<String,RightNonEmpty> gGroup = buildGGroup();
    private static Map<String,RightNonEmpty> buildGGroup(){
        Map<String,RightNonEmpty> mapper = new HashMap<>();

        RightNonEmpty lower = new RightChar('a');
        RightNonEmpty upper = new RightChar('A');
        RightNonEmpty numbers = new RightChar('0');

        for(char c = 'b'; c <= 'z'; c++) lower = new RightConcat(lower, new RightChar(c));
        for(char c = 'B'; c <= 'Z'; c++) upper = new RightConcat(upper, new RightChar(c));
        for(char c = '1'; c <= '9'; c++) numbers = new RightConcat(numbers, new RightChar(c));

        mapper.put("$a", lower);
        mapper.put("$A", upper);
        mapper.put("$0", numbers);

        return mapper;
    }

    public static boolean isGGroup(String x){return gGroup.containsKey(x);}
    public static RightNonEmpty getGGroup(String x){return gGroup.get(x);}

}
