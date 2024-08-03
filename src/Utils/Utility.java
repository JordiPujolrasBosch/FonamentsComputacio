package Utils;

import Elements.State;
import Factory.Printer;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Iterator;

public class Utility {

    //Conjunction of set

    public static boolean conjunctionIsEmpty(Set<State> a, Set<State> b) {
        boolean found = false;
        Iterator<State> it = b.iterator();
        while(it.hasNext() && !found){
            found = a.contains(it.next());
        }
        return !found;
    }

    //String is number

    public static boolean isNumber(String x){
        if(x == null || x.isEmpty()) return false;
        int i=0;
        boolean isNumber = true;
        while(i<x.length() && isNumber){
            char c = x.charAt(i);
            isNumber = c >= '0' && c <= '9';
            i++;
        }
        return isNumber;
    }

    public static boolean isNegativeNumber(String x){
        if(x == null || x.length() < 2) return false;
        return x.charAt(0) == '-' && isNumber(x.substring(1));
    }

    public static boolean isNumberInRange(String x, int a, int b){
        if(!isNumber(x) && !isNegativeNumber(x)) return false;
        return Integer.parseInt(x) >= a && Integer.parseInt(x) <= b;
    }

    //Stage

    private static Stage stage;

    public static void setStage(Stage s){
        stage = s;
    }

    //Border pane

    private static BorderPane borderPane;

    public static void setBorderPane(BorderPane layout) {
        borderPane = layout;
    }

    public static BorderPane getBorderPane() {
        return borderPane;
    }

    //Load file

    public static void loadFile(TextArea textArea){
        FileChooser fileChooser = new FileChooser();
        File f = fileChooser.showOpenDialog(stage);
        if(f != null){
            try{
                Scanner scanner = new Scanner(f);
                List<String> list = new ArrayList<>();
                while(scanner.hasNextLine()) list.add(scanner.nextLine());
                textArea.setText(Printer.stringOfWords(list));
            } catch (Exception ignored){}
        }
    }

    //Mini strings

    public static final String dfa    = "DFA";
    public static final String dfaA   = "DFA A";
    public static final String dfaB   = "DFA B";
    public static final String nfa    = "NFA";
    public static final String nfaA   = "NFA A";
    public static final String nfaB   = "NFA B";
    public static final String regex  = "REGEX";
    public static final String regexA = "REGEX A";
    public static final String regexB = "REGEX B";
    public static final String cfg    = "CFG";
    public static final String cfgA   = "CFG A";
    public static final String cfgB   = "CFG B";
    public static final String result = "RESULT";
    public static final String words  = "WORDS";

    public static final String loadFile  = "LOAD FILE";
    public static final String loadFileA = "LOAD FILE A";
    public static final String loadFileB = "LOAD FILE B";
    public static final String apply = "APPLY";
    public static final String defaultString = "DEFAULT";
    public static final String retYes = "ACCEPTED WORDS";
    public static final String retNo = "REJECTED WORDS";
}
