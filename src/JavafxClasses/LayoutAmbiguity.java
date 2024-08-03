package JavafxClasses;

import Factory.Printer;
import Factory.Reader;
import Functionalities.Menu;
import Grammars.Cfg;
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

public class LayoutAmbiguity implements Layout{
    private final Label title;
    private final Label description;

    private final Text length;
    private final TextField inputNumber;
    private final Button loadFile;
    private final Button apply;

    private final Text up;
    private final TextArea textAreaUp;

    private final Label under;
    private final TextField textFieldUnder;

    private CallMenuOne call;

    public LayoutAmbiguity(){
        title = new Label();
        description = new Label();
        length = new Text();
        inputNumber = new TextField();
        loadFile = new Button();
        apply = new Button();
        up = new Text();
        textAreaUp = new TextArea();
        under = new Label();
        textFieldUnder = new TextField();
    }

    public Pane build(){
        buildElements();
        buildInteractions();
        buildCall();

        VBox layout = new VBox();
        Properties.centerVBox(layout);

        HBox buttonBox = new HBox();
        Properties.buttonBox(buttonBox);
        buttonBox.getChildren().addAll(length, inputNumber, loadFile, apply);

        GridPane grid = new GridPane();
        Properties.grid(grid);
        GridPane.setConstraints(up,0,0);
        GridPane.setConstraints(textAreaUp,0,1);
        grid.getChildren().addAll(up, textAreaUp);

        layout.getChildren().addAll(title,description,buttonBox,grid,under,textFieldUnder);
        return layout;
    }

    private void buildElements(){
        title.setText("Ambiguity cfg");
        Properties.title(title);

        description.setText("Check if a context free grammar is ambiguous.\nA grammar is ambiguous if it can generate al least\none word in two or more different ways.");
        Properties.description(description);

        Properties.textOfLength(length);
        Properties.inputNumber(inputNumber, 10);
        Properties.textOfLoadFile(loadFile);
        Properties.textOfApply(apply);

        up.setText(Utility.cfg);
        Properties.gridCompanion(up);

        Properties.textAreaEdit(textAreaUp);

        under.setText(Utility.result);
        Properties.textAreaCompanion(under);

        Properties.textFieldResult(textFieldUnder);
    }

    private void buildInteractions(){
        loadFile.setOnAction(actionEvent -> Utility.loadFile(textAreaUp));

        apply.setOnAction(event -> {
            if(!Utility.isNumberInRange(inputNumber.getText(), 0, 50)) textFieldUnder.setText(Printer.lengthOutOfRange());
            else textFieldUnder.setText(call.call(textAreaUp.getText()));
        });
    }

    private void buildCall(){
        call = data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.checkAmbiguity(cfg, null, Integer.parseInt(inputNumber.getText()));
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }
}
