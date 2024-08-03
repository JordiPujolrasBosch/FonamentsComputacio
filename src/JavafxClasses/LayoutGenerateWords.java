package JavafxClasses;

import Automatons.Dfa;
import Automatons.Nfa;
import Factory.Printer;
import Factory.Reader;
import Functionalities.Menu;
import Grammars.Cfg;
import RegularExpressions.RegularExpression;
import Utils.Utility;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LayoutGenerateWords implements Layout {
    private final Label title;
    private final Label description;

    private final ChoiceBox<String> options;
    private final Text size;
    private final TextField inputNumber;
    private final Button loadFile;
    private final Button apply;

    private final Text textLeft;
    private final Text textRight;
    private final TextArea textAreaLeft;
    private final TextArea textAreaRight;

    private CallMenuOne call;

    public LayoutGenerateWords(){
        title = new Label();
        description = new Label();
        options = new ChoiceBox<>();
        size = new Text();
        inputNumber = new TextField();
        loadFile = new Button();
        apply = new Button();
        textLeft = new Text();
        textRight = new Text();
        textAreaLeft = new TextArea();
        textAreaRight = new TextArea();
    }

    public Pane build(){
        buildElements();
        buildInteractions();
        loadGenerateWordsDfa();

        VBox layout = new VBox();
        Properties.centerVBox(layout);

        HBox buttonBox = new HBox();
        Properties.buttonBox(buttonBox);
        buttonBox.getChildren().addAll(options, size, inputNumber ,loadFile, apply);

        GridPane grid = new GridPane();
        Properties.grid(grid);
        GridPane.setConstraints(textLeft,0,0);
        GridPane.setConstraints(textRight,1,0);
        GridPane.setConstraints(textAreaLeft,0,1);
        GridPane.setConstraints(textAreaRight,1,1);
        grid.getChildren().addAll(textLeft, textRight, textAreaLeft, textAreaRight);

        layout.getChildren().addAll(title,description,buttonBox,grid);
        return layout;
    }

    private void buildElements(){
        Properties.title(title);
        Properties.description(description);

        options.getItems().clear();
        options.getItems().addAll(Utility.dfa, Utility.nfa, Utility.regex, Utility.cfg);
        options.setValue(Utility.dfa);

        size.setText("Size:");
        Properties.inputNumber(inputNumber, 100);
        Properties.textOfLoadFile(loadFile);
        Properties.textOfApply(apply);

        Properties.gridCompanion(textLeft);
        Properties.gridCompanion(textRight);
        Properties.textAreaEdit(textAreaLeft);
        Properties.textAreaResult(textAreaRight);

        textRight.setText(Utility.result);
    }

    private void buildInteractions(){
        loadFile.setOnAction(event -> Utility.loadFile(textAreaLeft));

        options.setOnAction(event -> {
            if(options.getValue() != null){
                switch (options.getValue()){
                    case Utility.dfa -> loadGenerateWordsDfa();
                    case Utility.nfa -> loadGenerateWordsNfa();
                    case Utility.regex -> loadGenerateWordsRegex();
                    case Utility.cfg -> loadGenerateWordsCfg();
                }
            }
        });

        apply.setOnAction(event -> {
            if(!Utility.isNumberInRange(inputNumber.getText(), 0, 500)) textAreaRight.setText(Printer.sizeOutOfRange());
            else textAreaRight.setText(call.call(textAreaLeft.getText()));
        });
    }

    private void loadGenerateWordsDfa(){
        title.setText("Generate words dfa");
        description.setText("Generate a list of words that are accepted by a deterministic finite automaton.");
        textLeft.setText(Utility.dfa);
        call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.generateWordsDfa(dfa, Integer.parseInt(inputNumber.getText()));
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadGenerateWordsNfa(){
        title.setText("Generate words nfa");
        description.setText("Generate a list of words that are accepted by a nondeterministic finite automaton.");
        textLeft.setText(Utility.nfa);
        call = data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.generateWordsNfa(nfa, Integer.parseInt(inputNumber.getText()));
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadGenerateWordsRegex(){
        title.setText("Generate words regex");
        description.setText("Generate a list of words from a regular expression.");
        textLeft.setText(Utility.regex);
        call = data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.generateWordsRegex(regex, Integer.parseInt(inputNumber.getText()));
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadGenerateWordsCfg(){
        title.setText("Generate words cfg");
        description.setText("Generate a list of words from a context free grammar.");
        textLeft.setText(Utility.cfg);
        call = data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.generateWordsCfg(cfg, Integer.parseInt(inputNumber.getText()));
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }
}
