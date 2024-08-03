package JavafxClasses;

import Automatons.Dfa;
import Automatons.Nfa;
import Factory.Reader;
import Functionalities.Menu;
import RegularExpressions.RegularExpression;
import Utils.Utility;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LayoutComparison implements Layout{
    private final Label title;
    private final Label description;

    private final ChoiceBox<String> optionsA;
    private final ChoiceBox<String> optionsB;
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

    public LayoutComparison(){
        title = new Label();
        description = new Label();
        optionsA = new ChoiceBox<>();
        optionsB = new ChoiceBox<>();
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
        loadEqualDfaDfa();

        VBox layout = new VBox();
        Properties.centerVBox(layout);

        HBox buttonBox = new HBox();
        Properties.buttonBox(buttonBox);
        buttonBox.getChildren().addAll(optionsA, optionsB, loadFileA, loadFileB, apply);

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
        Properties.title(title);
        Properties.description(description);

        optionsA.getItems().clear();
        optionsA.getItems().addAll(Utility.dfa, Utility.nfa, Utility.regex);
        optionsA.setValue(Utility.dfa);

        optionsB.getItems().clear();
        optionsB.getItems().addAll(Utility.dfa, Utility.nfa, Utility.regex);
        optionsB.setValue(Utility.dfa);

        Properties.textOfLoadFileAB(loadFileA, loadFileB);
        Properties.textOfApply(apply);

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

        optionsA.setOnAction(event -> {
            if(optionsA.getValue() != null && optionsB.getValue() != null){
                switch (optionsA.getValue()){
                    case Utility.dfa -> {switch (optionsB.getValue()){
                        case Utility.dfa -> loadEqualDfaDfa();
                        case Utility.nfa -> loadEqualDfaNfa();
                        case Utility.regex -> loadEqualDfaRegex();
                    }}
                    case Utility.nfa -> {switch (optionsB.getValue()){
                        case Utility.dfa -> loadEqualNfaDfa();
                        case Utility.nfa -> loadEqualNfaNfa();
                        case Utility.regex -> loadEqualNfaRegex();
                    }}
                    case Utility.regex -> {switch (optionsB.getValue()){
                        case Utility.dfa -> loadEqualRegexDfa();
                        case Utility.nfa -> loadEqualRegexNfa();
                        case Utility.regex -> loadEqualRegexRegex();
                    }}
                }
            }
        });

        optionsB.setOnAction(event -> {
            if(optionsA.getValue() != null && optionsB.getValue() != null){
                switch (optionsA.getValue()){
                    case Utility.dfa -> {switch (optionsB.getValue()){
                        case Utility.dfa -> loadEqualDfaDfa();
                        case Utility.nfa -> loadEqualDfaNfa();
                        case Utility.regex -> loadEqualDfaRegex();
                    }}
                    case Utility.nfa -> {switch (optionsB.getValue()){
                        case Utility.dfa -> loadEqualNfaDfa();
                        case Utility.nfa -> loadEqualNfaNfa();
                        case Utility.regex -> loadEqualNfaRegex();
                    }}
                    case Utility.regex -> {switch (optionsB.getValue()){
                        case Utility.dfa -> loadEqualRegexDfa();
                        case Utility.nfa -> loadEqualRegexNfa();
                        case Utility.regex -> loadEqualRegexRegex();
                    }}
                }
            }
        });
    }

    private void loadEqualDfaDfa(){
        title.setText("Compare dfa & dfa");
        description.setText("Check if two deterministic finite automatons are equivalent.");
        textLeft.setText(Utility.dfaA);
        textRight.setText(Utility.dfaB);
        call = (a, b) -> {
            try{
                Dfa aa = Reader.readAutomatonString(a).toDfa();
                Dfa bb = Reader.readAutomatonString(b).toDfa();
                return Menu.equalDfaDfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadEqualDfaNfa(){
        title.setText("Compare dfa & nfa");
        description.setText("Check if a deterministic finite automaton and a nondeterministic finite automaton are equivalent.");
        textLeft.setText(Utility.dfa);
        textRight.setText(Utility.nfa);
        call = (a, b) -> {
            try{
                Dfa aa = Reader.readAutomatonString(a).toDfa();
                Nfa bb = Reader.readAutomatonString(b).toNfa();
                return Menu.equalDfaNfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadEqualDfaRegex(){
        title.setText("Compare dfa & regex");
        description.setText("Check if a deterministic finite automaton and a regular expression are equivalent.");
        textLeft.setText(Utility.dfa);
        textRight.setText(Utility.regex);
        call = (a, b) -> {
            try{
                Dfa aa = Reader.readAutomatonString(a).toDfa();
                RegularExpression bb = Reader.readRegularExpressionString(b);
                return Menu.equalDfaRegex(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadEqualNfaDfa(){
        title.setText("Compare nfa & dfa");
        description.setText("Check if a nondeterministic finite automaton and a deterministic finite automaton are equivalent.");
        textLeft.setText(Utility.nfa);
        textRight.setText(Utility.dfa);
        call = (a, b) -> {
            try{
                Nfa aa = Reader.readAutomatonString(a).toNfa();
                Dfa bb = Reader.readAutomatonString(b).toDfa();
                return Menu.equalNfaDfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadEqualNfaNfa(){
        title.setText("Compare nfa & nfa");
        description.setText("Check if two nondeterministic finite automatons are equivalent.");
        textLeft.setText(Utility.nfaA);
        textLeft.setText(Utility.nfaB);
        call = (a, b) -> {
            try{
                Nfa aa = Reader.readAutomatonString(a).toNfa();
                Nfa bb = Reader.readAutomatonString(b).toNfa();
                return Menu.equalNfaNfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadEqualNfaRegex(){
        title.setText("Compare nfa & regex");
        description.setText("Check if a nondeterministic finite automaton and a regular expression are equivalent.");
        textLeft.setText(Utility.nfa);
        textLeft.setText(Utility.regex);
        call = (a, b) -> {
            try{
                Nfa aa = Reader.readAutomatonString(a).toNfa();
                RegularExpression bb = Reader.readRegularExpressionString(b);
                return Menu.equalNfaRegex(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadEqualRegexDfa(){
        title.setText("Compare regex & dfa");
        description.setText("Check if a regular expression and a deterministic finite automaton are equivalent.");
        textLeft.setText(Utility.regex);
        textRight.setText(Utility.dfa);
        call = (a, b) -> {
            try{
                RegularExpression aa = Reader.readRegularExpressionString(a);
                Dfa bb = Reader.readAutomatonString(b).toDfa();
                return Menu.equalRegexDfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadEqualRegexNfa(){
        title.setText("Compare regex & nfa");
        description.setText("Check if a regular expression and a nondeterministic finite automaton are equivalent.");
        textLeft.setText(Utility.regex);
        textRight.setText(Utility.nfa);
        call = (a, b) -> {
            try{
                RegularExpression aa = Reader.readRegularExpressionString(a);
                Nfa bb = Reader.readAutomatonString(b).toNfa();
                return Menu.equalRegexNfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadEqualRegexRegex(){
        title.setText("Compare regex & regex");
        description.setText("Check if two regular expressions are equivalent.");
        textLeft.setText(Utility.regexA);
        textRight.setText(Utility.regexB);
        call = (a, b) -> {
            try{
                RegularExpression aa = Reader.readRegularExpressionString(a);
                RegularExpression bb = Reader.readRegularExpressionString(b);
                return Menu.equalRegexRegex(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }
}
