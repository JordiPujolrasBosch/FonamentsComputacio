import AutomatonElements.AutomatonData;
import Automatons.DeterministicFiniteAutomaton;

public class Main {
    public static void main(String[] args){
        test2();
    }

    private static void test1(){
        try {
            Generator gen = new Generator();
            String path = "dfa1.txt";
            AutomatonData data = gen.readAutomatonFile(path);
            System.out.println(data.check());
            System.out.println(data.isDeterministic());
            DeterministicFiniteAutomaton dfa = data.getDfa();
            System.out.println(dfa.checkWord("110000"));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    private static void test2(){
        try {
            Generator gen = new Generator();
            String path = "dfa2.txt";
            AutomatonData data = gen.readAutomatonFile(path);
            System.out.println(data.check());
            System.out.println(data.isDeterministic());
            DeterministicFiniteAutomaton dfa = data.getDfa();
            System.out.println(dfa.checkWord(""));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }
}
