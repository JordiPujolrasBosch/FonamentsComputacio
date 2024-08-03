package JavafxClasses;

import Automatons.Dfa;
import Automatons.Nfa;
import Factory.Reader;
import Functionalities.Menu;
import RegularExpressions.RegularExpression;
import Utils.Utility;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LayoutConversion implements Layout{
    private final Label title;
    private final Label description;

    private final ChoiceBox<String> optionsIn;
    private final ChoiceBox<String> optionsOut;
    private final CheckBox minimize;
    private final Button loadFile;
    private final Button apply;

    private final Text textLeft;
    private final Text textRight;
    private final TextArea textAreaLeft;
    private final TextArea textAreaRight;

    private CallMenuOne call;
    private boolean done;

    public LayoutConversion(){
        title = new Label();
        description = new Label();
        optionsIn = new ChoiceBox<>();
        optionsOut = new ChoiceBox<>();
        minimize = new CheckBox();
        loadFile = new Button();
        apply = new Button();
        textLeft = new Text();
        textRight = new Text();
        textAreaLeft = new TextArea();
        textAreaRight = new TextArea();
        done = false;
    }

    public Pane build(){
        if(!done){
            buildElements();
            buildInteractions();
            makeDfa();
            loadConversionDfaNfa();
        }

        VBox layout = new VBox();
        Properties.centerVBox(layout);

        HBox buttonBox = new HBox();
        Properties.buttonBox(buttonBox);
        buttonBox.getChildren().addAll(optionsIn, optionsOut, minimize, loadFile, apply);

        GridPane grid = new GridPane();
        Properties.grid(grid);
        GridPane.setConstraints(textLeft,0,0);
        GridPane.setConstraints(textRight,1,0);
        GridPane.setConstraints(textAreaLeft,0,1);
        GridPane.setConstraints(textAreaRight,1,1);
        grid.getChildren().addAll(textLeft, textRight, textAreaLeft, textAreaRight);

        layout.getChildren().addAll(title,description,buttonBox,grid);
        done = true;
        return layout;
    }

    private void buildElements(){
        Properties.title(title);
        Properties.description(description);

        optionsIn.getItems().clear();
        optionsIn.getItems().addAll(Utility.dfa, Utility.nfa, Utility.regex);
        optionsIn.setValue(Utility.dfa);

        minimize.setText("Minimize");
        minimize.setSelected(false);

        Properties.textOfLoadFile(loadFile);
        Properties.textOfApply(apply);

        Properties.gridCompanion(textLeft);
        Properties.gridCompanion(textRight);
        Properties.textAreaEdit(textAreaLeft);
        Properties.textAreaResult(textAreaRight);
    }

    private void buildInteractions(){
        loadFile.setOnAction(event -> Utility.loadFile(textAreaLeft));
        apply.setOnAction(event -> textAreaRight.setText(call.call(textAreaLeft.getText())));

        optionsIn.setOnAction(event -> {
            if(optionsIn.getValue() != null){
                switch (optionsIn.getValue()){
                    case Utility.dfa -> makeDfa();
                    case Utility.nfa -> makeNfa();
                    case Utility.regex -> makeRegex();
                }
            }
        });

        optionsOut.setOnAction(event -> {
            if(optionsIn.getValue() != null && optionsOut.getValue() != null){
                switch (optionsIn.getValue()){
                    case Utility.dfa -> {switch (optionsOut.getValue()) {
                        case Utility.nfa -> makeDfaNfa();
                        case Utility.regex -> makeDfaRegex();
                        case Utility.cfg -> makeDfaCfg();
                    }}
                    case Utility.nfa -> {switch (optionsOut.getValue()){
                        case Utility.dfa -> makeNfaDfa();
                        case Utility.regex -> makeNfaRegex();
                        case Utility.cfg -> makeNfaCfg();
                    }}
                    case Utility.regex -> {switch (optionsOut.getValue()){
                        case Utility.dfa -> makeRegexDfa();
                        case Utility.nfa -> makeRegexNfa();
                        case Utility.cfg -> makeRegexCfg();
                    }}
                }
            }
        });

        minimize.setOnAction(event -> {
            if(optionsIn.getValue() != null && optionsOut.getValue() != null){
                boolean sel = minimize.isSelected();
                boolean nd = Utility.nfa.equals(optionsIn.getValue())   && Utility.dfa.equals(optionsOut.getValue());
                boolean rd = Utility.regex.equals(optionsIn.getValue()) && Utility.dfa.equals(optionsOut.getValue());
                boolean rn = Utility.regex.equals(optionsIn.getValue()) && Utility.nfa.equals(optionsOut.getValue());

                if(sel && nd)       loadConversionNfaDfaMinim();
                else if(!sel && nd) loadConversionNfaDfa();
                else if(sel && rd)  loadConversionRegexDfaMinim();
                else if(!sel && rd) loadConversionRegexDfa();
                else if(sel && rn)  loadConversionRegexNfaMinim();
                else if(!sel && rn) loadConversionRegexNfa();
            }
        });
    }

    //Make

    private void makeDfa(){
        optionsOut.getItems().clear();
        optionsOut.getItems().addAll(Utility.nfa, Utility.regex, Utility.cfg);
        optionsOut.setValue(Utility.nfa);
        makeDfaNfa();
    }

    private void makeNfa(){
        optionsOut.getItems().clear();
        optionsOut.getItems().addAll(Utility.dfa, Utility.regex, Utility.cfg);
        optionsOut.setValue(Utility.dfa);
        makeNfaDfa();
    }

    private void makeRegex(){
        optionsOut.getItems().clear();
        optionsOut.getItems().addAll(Utility.dfa, Utility.nfa, Utility.cfg);
        optionsOut.setValue(Utility.dfa);
        makeRegexDfa();
    }

    private void makeDfaNfa(){
        minimize.setVisible(false);
        loadConversionDfaNfa();
    }

    private void makeDfaRegex(){
        minimize.setVisible(false);
        loadConversionDfaRegex();
    }

    private void makeDfaCfg(){
        minimize.setVisible(false);
        loadConversionDfaCfg();
    }

    private void makeNfaDfa(){
        minimize.setVisible(true);
        if(minimize.isSelected()) loadConversionNfaDfaMinim();
        else loadConversionNfaDfa();
    }

    private void makeNfaRegex(){
        minimize.setVisible(false);
        loadConversionNfaRegex();
    }

    private void makeNfaCfg(){
        minimize.setVisible(false);
        loadConversionNfaCfg();
    }

    private void makeRegexDfa(){
        minimize.setVisible(true);
        if(minimize.isSelected()) loadConversionRegexDfaMinim();
        else loadConversionRegexDfa();
    }

    private void makeRegexNfa(){
        minimize.setVisible(true);
        if(minimize.isSelected()) loadConversionRegexNfaMinim();
        else loadConversionRegexNfa();
    }

    private void makeRegexCfg(){
        minimize.setVisible(false);
        loadConversionRegexCfg();
    }

    //Load

    private void loadConversionDfaNfa(){
        title.setText("Transform dfa to nfa");
        description.setText("Transform a deterministic finite automaton to a nondeterministic finite automaton.");
        textLeft.setText(Utility.dfa);
        textRight.setText(Utility.nfa);
        call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.transformDfaNfa(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadConversionDfaRegex(){
        title.setText("Transform dfa to regex");
        description.setText("Transform a deterministic finite automaton to a regular expression.");
        textLeft.setText(Utility.dfa);
        textRight.setText(Utility.regex);
        call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.transformDfaRegex(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadConversionNfaDfa(){
        title.setText("Transform nfa to dfa");
        description.setText("Transform a nondeterministic finite automaton to a deterministic finite automaton.");
        textLeft.setText(Utility.nfa);
        textRight.setText(Utility.dfa);
        call = data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.transformNfaDfa(nfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadConversionNfaDfaMinim(){
        title.setText("Transform nfa to dfa minimized");
        description.setText("Transform a nondeterministic finite automaton to a deterministic finite automaton minimized.");
        textLeft.setText(Utility.nfa);
        textRight.setText(Utility.dfa);
        call = data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.transformNfaDfaMinim(nfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadConversionNfaRegex(){
        title.setText("Transform nfa to regex");
        description.setText("Transform a nondeterministic finite automaton to a regular expression.");
        textLeft.setText(Utility.nfa);
        textRight.setText(Utility.regex);
        call = data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.transformNfaRegex(nfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadConversionRegexDfa(){
        title.setText("Transform regex to dfa");
        description.setText("Transform a regular expression to a deterministic finite automaton.");
        textLeft.setText(Utility.regex);
        textRight.setText(Utility.dfa);
        call = data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexDfa(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadConversionRegexDfaMinim(){
        title.setText("Transform regex to dfa minimized");
        description.setText("Transform a regular expression to a deterministic finite automaton minimized.");
        textLeft.setText(Utility.regex);
        textRight.setText(Utility.dfa);
        call = data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexDfaMinim(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadConversionRegexNfa(){
        title.setText("Transform regex to nfa");
        description.setText("Transform a regular expression to a nondeterministic finite automaton.");
        textLeft.setText(Utility.regex);
        textRight.setText(Utility.nfa);
        call = data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexNfa(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadConversionRegexNfaMinim(){
        title.setText("Transform regex to nfa minimized");
        description.setText("Transform a regular expression to a nondeterministic finite automaton minimized.");
        textLeft.setText(Utility.regex);
        textRight.setText(Utility.nfa);
        call = data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexNfaMinim(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadConversionDfaCfg(){
        title.setText("Transform dfa to cfg");
        description.setText("Transform a deterministic finite automaton to a context free grammar.");
        textLeft.setText(Utility.dfa);
        textRight.setText(Utility.cfg);
        call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.transformDfaCfg(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadConversionNfaCfg(){
        title.setText("Transform nfa to cfg");
        description.setText("Transform a nondeterministic finite automaton to a context free grammar.");
        textLeft.setText(Utility.nfa);
        textRight.setText(Utility.cfg);
        call = data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.transformNfaCfg(nfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadConversionRegexCfg(){
        title.setText("Transform regex to cfg");
        description.setText("Transform a regular expression to a context free grammar.");
        textLeft.setText(Utility.regex);
        textRight.setText(Utility.cfg);
        call = data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexCfg(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }
}
