package Factory;

import AutomatonElements.Alphabet;
import RegularExpressions.RegexChar;
import RegularExpressions.RegexUnion;
import RegularExpressions.RegularExpression;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TokenFactory {
    private final static Map<String, Set<Character>> atokens = buildATokens();
    private final static Map<Character, String> atokensReverse = buildATokensReverse();
    private final static Map<String, Character> btokens = buildBTokens();
    private final static Map<Character, String> btokensReverse = buildBTokensReverse();
    private final static Map<String, RegularExpression> rtokens = buildRTokens();
    private final static Map<Character, String> rtokensReverse = buildRTokensReverse();

    //ATokens

    public static boolean atokensContains(String s) {
        return atokens.containsKey(s);
    }

    public static Set<Character> atokensGet(String s) {
        return atokens.get(s);
    }

    public static String atokensGetNothing() {
        return "$w";
    }

    //ATokensReverse

    public static String atokensReverseGet(Character c) {
        return atokensReverse.get(c);
    }

    public static boolean atokensReverseContains(Character c) {
        return atokensReverse.containsKey(c);
    }

    //BTokens

    public static boolean btokensContains(String s) {
        return btokens.containsKey(s);
    }

    public static Character btokensGet(String s) {
        return btokens.get(s);
    }

    //BTokensReverse

    public static boolean btokensReverseContains(Character character) {
        return btokensReverse.containsKey(character);
    }

    public static String btokensReverseGet(Character character) {
        return btokensReverse.get(character);
    }

    //Regex

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

    public static String grammarUnionString() {
        return "|";
    }

    //Build

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



}
