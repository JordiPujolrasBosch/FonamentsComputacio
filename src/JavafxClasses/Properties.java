package JavafxClasses;

import Utils.Utility;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Properties {
    public static void centerVBox(VBox vbox){
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(10,10,10,10));
    }

    public static void title(Label label){
        label.setFont(Font.font("Source Code Pro", FontWeight.BOLD, 30));
        label.setPadding(new Insets(20,20,20,20));
    }

    public static void description(Label label){
        label.setPadding(new Insets(0,20,20,20));
        label.setFont(Font.font("System Regular", 18));
        label.setTextAlignment(TextAlignment.CENTER);
    }

    public static void buttonBox(HBox hbox){
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(0,10,10,10));
        hbox.setSpacing(10);
    }

    public static void grid(GridPane gridPane){
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(0,0,10,0));
        gridPane.setAlignment(Pos.CENTER);
    }

    public static void gridCompanion(Text text){
        GridPane.setHalignment(text, HPos.CENTER);
        text.setUnderline(true);
    }

    public static void textAreaEdit(TextArea textArea){
        textArea.setEditable(true);
    }

    public static void textAreaCompanion(Label label){
        label.setUnderline(true);
        label.setPadding(new Insets(0,0,10,0));
    }

    public static void textFieldResult(TextField textField){
        textField.setEditable(false);
        textField.setMaxWidth(900);
    }

    public static void textAreaResult(TextArea textArea){
        textArea.setEditable(false);
    }

    public static void inputNumber(TextField field, int start){
        field.setText(String.valueOf(start));
        field.setEditable(true);
        field.setPrefWidth(80);
    }

    //Text

    public static void textOfLength(Text l){
        l.setText("Max length:");
    }

    public static void textOfLoadFile(Button b){
        b.setText(Utility.loadFile);
    }

    public static void textOfLoadFileAB(Button a, Button b){
        a.setText(Utility.loadFileA);
        b.setText(Utility.loadFileB);
    }

    public static void textOfApply(Button b){
        b.setText(Utility.apply);
    }

    //Main layout

    public static void verticalMenu(VBox vbox){
        vbox.setSpacing(3);
        vbox.setPadding(new Insets(10));
        vbox.setBackground(new Background(new BackgroundFill(Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY)));
    }
}
