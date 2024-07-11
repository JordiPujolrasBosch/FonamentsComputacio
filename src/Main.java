import Automatons.Pda;
import Elements.Grammars.Gvar;
import Factory.Reader;
import Grammars.Cfg;
import Grammars.Gramex;
import Grammars.GramexNonEmpty;
import Grammars.GramexVar;

import java.util.*;

/*
 * TODO
 * Test all
 *
 * Algorithms
 * Article CFG==CFG
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
        Menu.equalCfgCfgArticle("Resources/g0a.txt", "Resources/g0b.txt");
    }

}
