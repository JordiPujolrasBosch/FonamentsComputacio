import Automatons.Pda;
import Factory.Reader;
import Grammars.Cfg;

import java.util.ArrayList;
import java.util.List;

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
        try{
            String s = Reader.readRegularExpressionFile("Resources/x1_regex.txt").toString();
            System.out.println(s);
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

}
