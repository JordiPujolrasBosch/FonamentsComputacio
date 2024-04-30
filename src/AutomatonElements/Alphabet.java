package AutomatonElements;

import java.util.*;

public class Alphabet {
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

    public void addAll(Set<Character> s) {
        for(Character c : s) addChar(c);
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

}
