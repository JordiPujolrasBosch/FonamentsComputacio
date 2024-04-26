import Exceptions.AutomatonReaderException;
import Exceptions.RegexReaderException;

import java.io.FileNotFoundException;

/*
* TODO
* Test all
* Implement CFG
* Chomsky reduce
* Pushdown automaton
* CFL to PDA
* PDA to CFL
* Turing Machine ?
* */

public class Main {
    public static void main(String[] args) throws FileNotFoundException, RegexReaderException, AutomatonReaderException {
        Menu.equalDfaNfa("Resources/x1_dfa.txt", "Resources/x1_nfa3.txt");
    }



}
