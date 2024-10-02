package Utils;

import java.util.Objects;

public class Pair<A,B> {
    private final A a;
    private final B b;

    public Pair(A a, B b){
        this.a = a;
        this.b = b;
    }

    public A getA(){
        return a;
    }

    public B getB(){
        return b;
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
        Pair<?,?> pair = (Pair<?,?>) o;
        return a.equals(pair.a) && b.equals(pair.b);
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
