import Automatons.Pda;
import Factory.Reader;
import Grammars.Cfg;

/*
 * TODO
 * Test all
 *
 * Regex builder
 * Right
 * Chomsky
 * Algorithms
 * TokenFactory
 *
 * Article CFG==CFG
 * */


public class Main {
    public static void main(String[] args) {
        try{
            Menu.checkWordsCfg("Resources/cfg1.txt", "Resources/words1.txt");
        }
        catch (Exception ex){
            System.out.println(ex);
        }

    }

}
