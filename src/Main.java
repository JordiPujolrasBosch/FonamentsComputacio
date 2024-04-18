import Automatons.AutomatonData;
import Factory.AutomatonFactory;
import Automatons.Dfa;
import Factory.Reader;

/*
* TODO
* Test all
* Read regular expression from file
* Implement CFG
* Chomsky reduce
* Pushdown automaton
* CFL to PDA
* PDA to CFL
* Turing Machine ?
* */

public class Main {
    public static void main(String[] args){
        test2();
    }

    private static void test1(){
        try {
            Reader gen = new Reader();
            String path = "dfa1.txt";
            AutomatonData data = gen.readAutomatonFile(path);
            System.out.println(data.check());
            System.out.println(data.isDeterministic());
            Dfa dfa = AutomatonFactory.dataToDfa(data);
            System.out.println(dfa.checkWord("110000"));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    private static void test2(){
        try {
            Reader gen = new Reader();
            String path = "dfa2.txt";
            AutomatonData data = gen.readAutomatonFile(path);
            System.out.println(data.check());
            System.out.println(data.isDeterministic());
            Dfa dfa = AutomatonFactory.dataToDfa(data);
            System.out.println(dfa.checkWord(""));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }
}
