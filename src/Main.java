

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
        Menu.findCounterExampleCfgs("Resources/gc1.txt", "Resources/gc2.txt");
        Menu.checkAmbiguity("Resources/gc2.txt");
    }

}
