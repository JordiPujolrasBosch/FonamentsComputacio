package Elements.Grammars;

import java.util.Objects;

/**
 * Grammar variable. A non-terminal used in context free grammars. Is defined by an upper letter and a positive number.
 */

public class Gvar {
    private final char c;
    private final int n;

    public Gvar(char c, int n){
        this.c = c;
        this.n = n;
    }

    //Getters

    /**
     * @return The character of this gvar.
     */
    public char getC(){
        return c;
    }

    /**
     * @return The number of this gvar.
     */
    public int getN(){
        return n;
    }

    //String and equals

    /**
     * @return A string that represents this gvar in the defined format.
     */
    @Override
    public String toString() {
        return Character.toString(c) + n;
    }

    /**
     * Compares if two objects are equal.
     * @param o The object to compare.
     * @return True if this and o are equal. False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gvar that = (Gvar) o;
        return c == that.c && n == that.n;
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(c, n);
    }
}
