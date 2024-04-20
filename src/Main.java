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
    public static void main(String[] args) throws FileNotFoundException, RegexReaderException {
        String s = "a     bcd  e";
        String[] ss = s.split(" ");
        System.out.println(ss);
    }



}
