package Elements;

import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

public class Alphabet {
    private final Set<Character> set;
    private boolean hasEmptyChar;

    //Constructor

    public Alphabet(){
        set = new HashSet<>();
        hasEmptyChar = false;
    }

    //Add and remove

    public void addChar(char c) {
        if(getEmptyChar() == c) hasEmptyChar = true;
        else set.add(c);
    }

    public void addAll(Alphabet alphabet) {
        set.addAll(alphabet.set);
        hasEmptyChar = hasEmptyChar || alphabet.hasEmptyChar;
    }

    public void addAll(Set<Character> s) {
        for(char c : s) addChar(c);
    }

    public void addEmptyChar() {
        hasEmptyChar = true;
    }

    public void removeChar(char c){
        if(getEmptyChar() == c) hasEmptyChar = false;
        else set.remove(c);
    }

    public void removeEmptyChar() {
        hasEmptyChar = false;
    }

    //Consult

    public boolean contains(char c) {
        if(getEmptyChar() == c) return hasEmptyChar;
        else return set.contains(c);
    }

    public boolean containsEmptyChar(){
        return hasEmptyChar;
    }

    public int size() {
        if(hasEmptyChar) return set.size() + 1;
        else return set.size();
    }

    //Getter set

    public Set<Character> getSet(){
        return set;
    }

    //Empty char and stack char

    public static char getEmptyChar() {
        return Character.MIN_VALUE;
    }

    public static char getStackChar(){
        return Character.MIN_VALUE + 1;
    }

    //Equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alphabet alphabet = (Alphabet) o;
        return hasEmptyChar == alphabet.hasEmptyChar && Objects.equals(set, alphabet.set);
    }

    @Override
    public int hashCode() {
        return Objects.hash(set, hasEmptyChar);
    }
}
