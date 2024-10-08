package Elements;

import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

/**
 * The alphabet of a language. The characters that are used to build the words of a language. The alphabet
 * can or cannot contain the special character: empty.
 */

public class Alphabet {
    private final Set<Character> set;
    private boolean hasEmptyChar;

    //Constructor

    public Alphabet(){
        set = new HashSet<>();
        hasEmptyChar = false;
    }

    //Add and remove

    /**
     * Adds a character.
     * @param c Character to add.
     */
    public void addChar(char c) {
        if(getEmptyChar() == c) hasEmptyChar = true;
        else set.add(c);
    }

    /**
     * Adds all the characters of another alphabet.
     * @param alphabet Alphabet to add.
     */
    public void addAll(Alphabet alphabet) {
        set.addAll(alphabet.set);
        hasEmptyChar = hasEmptyChar || alphabet.hasEmptyChar;
    }

    /**
     * Adds all the characters of a set.
     * @param s Set of characters to add.
     */
    public void addAll(Set<Character> s) {
        for(char c : s) addChar(c);
    }

    /**
     * Adds the empty character.
     */
    public void addEmptyChar() {
        hasEmptyChar = true;
    }

    /**
     * Removes a character.
     * @param c Character to remove.
     */
    public void removeChar(char c){
        if(getEmptyChar() == c) hasEmptyChar = false;
        else set.remove(c);
    }

    /**
     * Removes the empty character.
     */
    public void removeEmptyChar() {
        hasEmptyChar = false;
    }

    //Consult

    /**
     * Checks if this contains a character.
     * @param c Character to check.
     * @return True if this contains c. False otherwise.
     */
    public boolean contains(char c) {
        if(getEmptyChar() == c) return hasEmptyChar;
        else return set.contains(c);
    }

    /**
     * Checks if this contains the empty character.
     * @return True if this contains the empty character. False otherwise.
     */
    public boolean containsEmptyChar(){
        return hasEmptyChar;
    }

    /**
     * Counts the characters.
     * @return The number of characters.
     */
    public int size() {
        if(hasEmptyChar) return set.size() + 1;
        else return set.size();
    }

    //Getter set

    /**
     * @return The characters of the alphabet (without empty character).
     */
    public Set<Character> getSet(){
        return set;
    }

    //Empty char and stack char

    /**
     * @return The special character: empty character.
     */
    public static char getEmptyChar() {
        return Character.MIN_VALUE;
    }

    /**
     * @return The special character: stack character.
     */
    public static char getStackChar(){
        return Character.MIN_VALUE + 1;
    }

    //Equals

    /**
     * Compares if two objects are equal.
     * @param o The object to compare.
     * @return True if this and o are equal. False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alphabet that = (Alphabet) o;
        return hasEmptyChar == that.hasEmptyChar && set.equals(that.set);
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(set, hasEmptyChar);
    }
}
