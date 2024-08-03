package JavafxClasses;

import Automatons.Dfa;
import Automatons.Nfa;
import Factory.Reader;
import Functionalities.Menu;
import Grammars.Cfg;
import RegularExpressions.RegularExpression;
import Utils.Utility;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class LayoutCheckWords implements Layout{
    private final Label title;
    private final Label description;

    private final ChoiceBox<String> options;
    private final ChoiceBox<String> ret;
    private final Button loadFileA;
    private final Button loadFileB;
    private final Button apply;

    private final Text textLeft;
    private final Text textRight;
    private final TextArea textAreaLeft;
    private final TextArea textAreaRight;

    private final Text under;
    private final TextArea textAreaUnder;

    private CallMenuTwo call;
    private boolean done;

    public LayoutCheckWords(){
        title = new Label();
        description = new Label();
        options = new ChoiceBox<>();
        ret = new ChoiceBox<>();
        loadFileA = new Button();
        loadFileB = new Button();
        apply = new Button();
        textLeft = new Text();
        textRight = new Text();
        textAreaLeft = new TextArea();
        textAreaRight = new TextArea();
        under = new Text();
        textAreaUnder = new TextArea();
        done = false;
    }

    public Pane build(){
        if(!done){
            buildElements();
            buildInteractions();
            loadCheckWordsDfa();
        }

        VBox layout = new VBox();
        Properties.centerVBox(layout);

        HBox buttonBox = new HBox();
        Properties.buttonBox(buttonBox);
        buttonBox.getChildren().addAll(options, ret, loadFileA, loadFileB, apply);

        GridPane gridUp = new GridPane();
        Properties.grid(gridUp);
        GridPane.setConstraints(textLeft,0,0);
        GridPane.setConstraints(textRight,1,0);
        GridPane.setConstraints(textAreaLeft,0,1);
        GridPane.setConstraints(textAreaRight,1,1);
        gridUp.getChildren().addAll(textLeft, textRight, textAreaLeft, textAreaRight);

        GridPane gridDown = new GridPane();
        Properties.grid(gridDown);
        GridPane.setConstraints(under,0,0);
        GridPane.setConstraints(textAreaUnder,0,1);
        gridDown.getChildren().addAll(under, textAreaUnder);

        layout.getChildren().addAll(title,description,buttonBox,gridUp,gridDown);
        done = true;
        return layout;
    }

    private void buildElements(){
        Properties.title(title);
        Properties.description(description);

        options.getItems().clear();
        options.getItems().addAll(Utility.dfa, Utility.nfa, Utility.regex, Utility.cfg);
        options.setValue(Utility.dfa);

        ret.getItems().clear();
        ret.getItems().addAll(Utility.defaultString, Utility.retYes, Utility.retNo);
        ret.setValue(Utility.defaultString);

        Properties.textOfLoadFileAB(loadFileA, loadFileB);
        Properties.textOfApply(apply);

        textRight.setText(Utility.words);

        Properties.gridCompanion(textLeft);
        Properties.gridCompanion(textRight);
        Properties.textAreaEdit(textAreaLeft);
        Properties.textAreaEdit(textAreaRight);

        under.setText(Utility.result);

        Properties.gridCompanion(under);
        Properties.textAreaResult(textAreaUnder);
    }

    private void buildInteractions(){
        options.setOnAction(event -> {
            if(options.getValue() != null){
                switch (options.getValue()){
                    case Utility.dfa   -> loadCheckWordsDfa();
                    case Utility.nfa   -> loadCheckWordsNfa();
                    case Utility.cfg   -> loadCheckWordsCfg();
                    case Utility.regex -> loadCheckWordsRegex();
                }
            }
        });

        loadFileA.setOnAction(event -> Utility.loadFile(textAreaLeft));
        loadFileB.setOnAction(event -> Utility.loadFile(textAreaRight));

        apply.setOnAction(event -> textAreaUnder.setText(call.call(textAreaLeft.getText(), textAreaRight.getText())));
    }

    //Load

    private void loadCheckWordsDfa(){
        title.setText("Check words dfa");
        description.setText("Test if a list of words are accepted by a deterministic finite automaton.");
        textLeft.setText(Utility.dfa);
        call = (a, b) -> {
            try{
                Dfa aa = Reader.readAutomatonString(a).toDfa();
                List<String> bb = Reader.readWordsString(b);
                if(ret.getValue().equals(Utility.defaultString)) return Menu.checkWordsDfa(aa,bb);
                if(ret.getValue().equals(Utility.retYes)) return Menu.checkWordsDfaYes(aa,bb);
                return Menu.checkWordsDfaNo(aa,bb);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadCheckWordsNfa(){
        title.setText("Check words nfa");
        description.setText("Test if a list of words are accepted by a nondeterministic finite automaton.");
        textLeft.setText(Utility.nfa);
        call = (a, b) -> {
            try{
                Nfa aa = Reader.readAutomatonString(a).toNfa();
                List<String> bb = Reader.readWordsString(b);
                if(ret.getValue().equals(Utility.defaultString)) return Menu.checkWordsNfa(aa,bb);
                if(ret.getValue().equals(Utility.retYes)) return Menu.checkWordsNfaYes(aa,bb);
                return Menu.checkWordsNfaNo(aa,bb);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadCheckWordsRegex(){
        title.setText("Check words regex");
        description.setText("Test if a list of words can be generated by a regular expression.");
        textLeft.setText(Utility.regex);
        call = (a, b) -> {
            try{
                RegularExpression aa = Reader.readRegularExpressionString(a);
                List<String> bb = Reader.readWordsString(b);
                if(ret.getValue().equals(Utility.defaultString)) return Menu.checkWordsRegex(aa,bb);
                if(ret.getValue().equals(Utility.retYes)) return Menu.checkWordsRegexYes(aa,bb);
                return Menu.checkWordsRegexNo(aa,bb);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadCheckWordsCfg(){
        title.setText("Check words cfg");
        description.setText("Test if a list of words can be generated by a context free grammar.");
        textLeft.setText(Utility.cfg);
        call = (a, b) -> {
            try{
                Cfg aa = Reader.readGrammarString(a);
                List<String> bb = Reader.readWordsString(b);
                if(ret.getValue().equals(Utility.defaultString)) return Menu.checkWordsCfg(aa,bb);
                if(ret.getValue().equals(Utility.retYes)) return Menu.checkWordsCfgYes(aa,bb);
                return Menu.checkWordsCfgNo(aa,bb);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }
}
