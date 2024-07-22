package JavafxClasses;

import Factory.Printer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UtilFx {
    public static Stage stage;

    public static int number;

    public static void setStage(Stage s){
        stage = s;
    }

    public static Stage getStage(){
        return stage;
    }

    public static void setInteger(int n){
        number = n;
    }

    public static int getInteger(){
        return number;
    }

    public static String loadFile(){
        String out = null;
        FileChooser fileChooser = new FileChooser();

        File f = fileChooser.showOpenDialog(stage);
        if(f != null){
            try{
                Scanner scanner = new Scanner(f);
                List<String> list = new ArrayList<>();
                while(scanner.hasNextLine()) list.add(scanner.nextLine());
                out = Printer.stringOfWords(list);
            }
            catch (Exception ex){}
        }

        return out;
    }
}
