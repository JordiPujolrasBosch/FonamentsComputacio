package Readers;

import AutomatonElements.AutomatonData;
import Automatons.DeterministicFiniteAutomaton;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AutomatonReader {

    public DeterministicFiniteAutomaton readFile(String filename) throws FileNotFoundException, AutomatonReaderException {
        Scanner sc = new Scanner(new File(filename));
        AutomatonData data = new AutomatonData();

        try {
            while (!data.hasBasic()) {
                String x = sc.next();
                switch (x) {
                    case "states:"   -> data.setStates(sc.nextInt());
                    case "start:"    -> data.setStart(sc.nextInt());
                    case "final:"    -> data.setFinal(sc.nextLine().replace(" ", "").split(","));
                    case "alphabet:" -> data.setAlphabet(sc.nextLine().replace(" ", "").split(","));
                    default          -> throw new Exception();
                }
            }

            while (sc.hasNextLine()) {
                data.addRule(sc.nextInt(), sc.next(), sc.nextInt());
            }
        }
        catch (Exception ex){
            throw new AutomatonReaderException();
        }

        return data.getAutomaton();
    }

}
