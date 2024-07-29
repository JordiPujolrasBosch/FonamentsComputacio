package Utils;

import Factory.Printer;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utility {

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

    //Integer

    private static int x;

    public static void setInt(int n){
        x = n;
    }

    public static int getInt(){
        return x;
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

    public static String dfa    = "DFA";
    public static String dfaA   = "DFA A";
    public static String dfaB   = "DFA B";
    public static String nfa    = "NFA";
    public static String nfaA   = "NFA A";
    public static String nfaB   = "NFA B";
    public static String regex  = "REGEX";
    public static String regexA = "REGEX A";
    public static String regexB = "REGEX B";
    public static String cfg    = "CFG";
    public static String cfgA   = "CFG A";
    public static String cfgB   = "CFG B";
    public static String result = "RESULT";
    public static String words  = "WORDS";

    public static String loadFile  = "LOAD FILE";
    public static String loadFileA = "LOAD FILE A";
    public static String loadFileB = "LOAD FILE B";
    public static String apply = "APPLY";
    public static String defaultString = "DEFAULT";
    public static String retYes = "ACCEPTED WORDS";
    public static String retNo = "REJECTED WORDS";
}
