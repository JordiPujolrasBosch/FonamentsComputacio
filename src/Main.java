import Automatons.Pda;
import Factory.Reader;
import Grammars.Cfg;

/*
 * TODO
 * Test all
 * */


public class Main {
    public static void main(String[] args) {
        try{
            Pda pda = Reader.readGrammarFile("Resources/cfg1.txt").toPda();
            pda.checkWord("abbbbbb");
            pda.checkWord("aaaaaabbbbbb");
            pda.checkWord("aaabbb");
            pda.checkWord("b");
            pda.checkWord("abbbbaa");
            pda.checkWord("aaaaaaa");
            pda.checkWord("abb");
        }
        catch (Exception ex){
            System.out.println(ex);
        }

    }

}
