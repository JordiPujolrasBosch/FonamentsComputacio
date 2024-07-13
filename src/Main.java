import Automatons.Pda;
import Elements.Grammars.Gvar;
import Factory.Reader;
import Grammars.*;

import java.util.*;

/*
 * TODO
 * Test all
 *
 * Enumerator fixed length words
 * Ambiguity
 * GUI
 *
 * */

/*
 * DONE
 * Automatons
 * Elements
 * Exceptions
 * Grammars
 * RegularExpressions
 * Utils
 * Algorithms
 *
 * Article CFG==CFG
 * CFG termination checks
 * Find counter-example
 * */

public class Main {
    public static void main(String[] args) {
        Menu.generateWordsRegex("Resources/x1_regex.txt", 500);
    }

}
