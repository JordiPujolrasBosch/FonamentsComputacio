package JavafxClasses;

import Factory.Printer;
import Utils.Utility;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Layout1x1Len {
    private final String title;
    private final String description;
    private final String up;
    private final String under;
    private final CallMenuOne call;

    private TextArea textAreaUp;
    private TextField textFieldUnder;

    public Layout1x1Len(String title, String description, String up, String under, CallMenuOne call){
        this.title = title;
        this.description = description;
        this.up = up;
        this.under = under;
        this.call = call;
    }

    public Pane build(){
        VBox layout = new VBox();
        Properties.centerVBox(layout);

        Label a = new Label(title);
        Properties.title(a);

        Label b = new Label(description);
        Properties.description(b);

        Pane c = buildButtons();

        Pane d = buildGrid();

        Label e = new Label(under);
        Properties.textAreaCompanion(e);

        textFieldUnder = new TextField();
        Properties.textFieldResult(textFieldUnder);

        layout.getChildren().addAll(a,b,c,d,e,textFieldUnder);
        return layout;
    }

    private HBox buildButtons(){
        HBox layout = new HBox();
        Properties.buttonBox(layout);

        Text len = new Text("Max length:");

        TextField inputNumber = new TextField("10");
        inputNumber.setEditable(true);
        inputNumber.setPrefWidth(80);

        Button load = new Button(Utility.loadFile);
        Properties.button(load);
        load.setOnAction(actionEvent -> Utility.loadFile(textAreaUp));

        Button apply = new Button(Utility.apply);
        Properties.button(apply);
        apply.setOnAction(event -> {
            if(!Utility.isNumber(inputNumber.getText())) textFieldUnder.setText(Printer.lengthOutOfRange());
            else{
                int n = Integer.parseInt(inputNumber.getText());
                if(n < 0 || n > 50) textFieldUnder.setText(Printer.lengthOutOfRange());
                else{
                    Utility.setInt(n);
                    textFieldUnder.setText(call.call(textAreaUp.getText()));
                }
            }
        });

        layout.getChildren().addAll(len, inputNumber, load, apply);
        return layout;
    }

    private GridPane buildGrid(){
        GridPane layout = new GridPane();
        Properties.grid(layout);

        Text text = new Text(up);
        Properties.gridCompanion(text);

        textAreaUp = new TextArea();
        Properties.textAreaEdit(textAreaUp);

        GridPane.setConstraints(text,0,0);
        GridPane.setConstraints(textAreaUp,0,1);

        layout.getChildren().addAll(text, textAreaUp);
        return layout;
    }
}
