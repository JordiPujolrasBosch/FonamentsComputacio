package AutomatonElements;

import java.util.*;

public class Alphabet {
    private static final Map<String, Set<Character>> listOfElements = buildList();

    private final Set<Character> set;
    private boolean hasEmptyChar;

    //Constructor

    public Alphabet(){
        set = new HashSet<>();
        hasEmptyChar = false;
    }

    //Add and remove

    public void addChar(Character c) {
        if(getEmptyChar().equals(c)) hasEmptyChar = true;
        else set.add(c);
    }

    public void addAll(Alphabet alphabet) {
        set.addAll(alphabet.set);
        hasEmptyChar = hasEmptyChar || alphabet.hasEmptyChar;
    }

    public void addElement(String x){
        if(x.length() == 1) addChar(x.charAt(0));
        else if(x.equals("''")) hasEmptyChar = true;
        else if(listOfElements.containsKey(x)) set.addAll(listOfElements.get(x));
    }

    public void addEmptyChar() {
        hasEmptyChar = true;
    }

    public void removeEmptyChar() {
        hasEmptyChar = false;
    }

    //Consult

    public boolean contains(Character c) {
        if(getEmptyChar().equals(c)) return hasEmptyChar;
        else return set.contains(c);
    }

    public int size() {
        if(hasEmptyChar) return set.size() + 1;
        else return set.size();
    }

    public boolean equal(Alphabet b) {
        boolean eq = set.size() == b.set.size();
        eq = eq && ((hasEmptyChar && b.hasEmptyChar) || (!hasEmptyChar && !b.hasEmptyChar));

        Iterator<Character> it = set.iterator();
        while (eq && it.hasNext()){
            eq = b.set.contains(it.next());
        }

        return eq;
    }

    //Getter set

    public Set<Character> set(){
        return set;
    }

    //Empty char

    public static Character getEmptyChar() {
        return Character.MIN_VALUE;
    }

    //Transform string from rule in file

    public static Character transform(String s) throws Exception {
        if(s.length() == 1) return s.charAt(0);
        else if(s.equals("''")) return getEmptyChar();
        else if(s.equals("space")) return ' ';
        else throw new Exception();
    }

    //Elements

    public static boolean validElement(String x) {
        return x.length() == 1 || x.equals("''") || listOfElements.containsKey(x);
    }

    private static Map<String, Set<Character>> buildList(){
        Map<String, Set<Character>> mapper = new HashMap<>();

        Set<Character> lettersLower = new HashSet<>();
        for(char c = 'a'; c <= 'z'; c++) lettersLower.add(c);
        mapper.put("lettersLower", lettersLower);

        Set<Character> lettersUpper = new HashSet<>();
        for(char c = 'A'; c <= 'Z'; c++) lettersUpper.add(c);
        mapper.put("lettersUpper", lettersUpper);

        Set<Character> numbers = new HashSet<>();
        for(char c = '0'; c <= '9'; c++) numbers.add(c);
        mapper.put("numbers", numbers);

        Set<Character> space = new HashSet<>();
        space.add(' ');
        mapper.put("space", space);

        Set<Character> comma = new HashSet<>();
        comma.add(',');
        mapper.put("comma", comma);

        Set<Character> nothing = new HashSet<>();
        mapper.put("nothing",nothing);

        return mapper;
    }
}
