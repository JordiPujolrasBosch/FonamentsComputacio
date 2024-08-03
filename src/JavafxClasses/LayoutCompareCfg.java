package JavafxClasses;

import Factory.Reader;
import Functionalities.Menu;
import Grammars.Cfg;
import Utils.Utility;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LayoutCompareCfg implements Layout{
    private final Label title;
    private final Label description;

    private final Button loadFileA;
    private final Button loadFileB;
    private final Button apply;

    private final Text textLeft;
    private final Text textRight;
    private final TextArea textAreaLeft;
    private final TextArea textAreaRight;

    private final Label under;
    private final TextField textFieldUnder;

    private CallMenuTwo call;

    public LayoutCompareCfg(){
        title = new Label();
        description = new Label();
        loadFileA = new Button();
        loadFileB = new Button();
        apply = new Button();
        textLeft = new Text();
        textRight = new Text();
        textAreaLeft = new TextArea();
        textAreaRight = new TextArea();
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
        buttonBox.getChildren().addAll(loadFileA, loadFileB, apply);

        GridPane grid = new GridPane();
        Properties.grid(grid);
        GridPane.setConstraints(textLeft,0,0);
        GridPane.setConstraints(textRight,1,0);
        GridPane.setConstraints(textAreaLeft,0,1);
        GridPane.setConstraints(textAreaRight,1,1);
        grid.getChildren().addAll(textLeft, textRight, textAreaLeft, textAreaRight);

        layout.getChildren().addAll(title,description,buttonBox,grid,under,textFieldUnder);
        return layout;
    }

    private void buildElements(){
        title.setText("Compare cfg & cfg");
        Properties.title(title);

        description.setText("Check if two context free grammars are equivalent.");
        Properties.description(description);

        Properties.textOfLoadFileAB(loadFileA, loadFileB);
        Properties.textOfApply(apply);

        textLeft.setText(Utility.cfgA);
        textRight.setText(Utility.cfgB);

        Properties.gridCompanion(textLeft);
        Properties.gridCompanion(textRight);
        Properties.textAreaEdit(textAreaLeft);
        Properties.textAreaEdit(textAreaRight);

        under.setText(Utility.result);
        Properties.textAreaCompanion(under);

        Properties.textFieldResult(textFieldUnder);
    }

    private void buildInteractions(){
        loadFileA.setOnAction(event -> Utility.loadFile(textAreaLeft));
        loadFileB.setOnAction(event -> Utility.loadFile(textAreaRight));
        apply.setOnAction(event -> textFieldUnder.setText(call.call(textAreaLeft.getText(),textAreaRight.getText())));
    }

    private void buildCall(){
        call = (a, b) -> {
            try{
                Cfg aa = Reader.readGrammarString(a);
                Cfg bb = Reader.readGrammarString(b);
                return Menu.equalCfgCfg(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }
}
