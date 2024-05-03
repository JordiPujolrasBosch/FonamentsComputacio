package Factory;

import Elements.Alphabet;
import Elements.Rule;
import Elements.State;

import java.util.*;

public class OutputMessages {
    public static String automatonCheck(String filename) {
        return "Incorrect format for the automaton in file " + filename;
    }

    public static String automatonNondeterministic(String filename) {
        return "The automaton of file " + filename + " is nondeterministic";
    }

    public static String equal(String fa, String fb) {
        return "EQUAL " + fa + " " + fb;
    }

    public static String nonequal(String fa, String fb) {
        return "NOT EQUAL " + fa + " " + fb;
    }

    public static String grammarCheck(String filename) {
        return "Incorrect format for the grammar file " + filename;
    }

}
