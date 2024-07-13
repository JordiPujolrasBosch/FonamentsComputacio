import Automatons.Pda;
import Elements.Grammars.Gvar;
import Factory.Reader;
import Grammars.*;

import java.util.*;

/*
 * TODO
 * Test all
 *
 * Algorithms
 * Article CFG==CFG
 *
 * Generate words CFG, Regex
 * Regex to CFG
 * Find counter-example
 * Enumerator fixed length words
 * CFG termination checks
 * */

/*
 * DONE
 * Automatons
 * Elements
 * Exceptions
 * Grammars
 * RegularExpressions
 * Utils
 * */

public class Main {
    public static void main(String[] args) {
        Menu.generateWordsRegex("Resources/x1_regex.txt", 500);
    }

}
