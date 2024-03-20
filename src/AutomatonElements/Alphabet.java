package AutomatonElements;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Alphabet {
    static Map<String, Set<Character>> listOfElements = buildList();

    private final Set<Character> set;
    private boolean hasEmptyChar;

    public Alphabet(){
        set = new HashSet<>();
        hasEmptyChar = false;
    }

    // ADD and REMOVE

    public void addChar(char c) {
        set.add(c);
    }

    public void addEmptyChar() {
        hasEmptyChar = true;
    }

    public void addAll(Alphabet alphabet) {
        set.addAll(alphabet.set);
        hasEmptyChar = hasEmptyChar && alphabet.hasEmptyChar;
    }

    public void addElement(String x){
        if(x.length() == 1) addChar(x.charAt(0));
        else if(x.equals("''")) hasEmptyChar = true;
        else if(listOfElements.containsKey(x)) set.addAll(listOfElements.get(x));
    }

    public void removeEmptyChar() {
        hasEmptyChar = false;
    }

    //CONSULT

    public boolean contains(Character c) {
        if(c.equals(Character.MIN_VALUE)) return hasEmptyChar;
        else return set.contains(c);
    }

    public int size() {
        if(hasEmptyChar) return set.size() + 1;
        else return set.size();
    }

    //GETTER

    public Set<Character> set(){
        return set;
    }

    // STATIC

    public static Character getEmptyChar() {
        return Character.MIN_VALUE;
    }

    public static Character transform(String s) throws Exception {
        if(s.length() == 1) return s.charAt(0);
        else if(s.equals("''")) return Alphabet.getEmptyChar();
        else if(s.equals("space")) return ' ';
        else throw new Exception();
    }

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

        return mapper;
    }


}
