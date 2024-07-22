package JavafxClasses;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ThreeTextAreas {
    private final String title;
    private final String description;
    private final String left;
    private final String middle;
    private final String right;
    private final CallTwo call;

    private Button loada;
    private Button loadb;
    private Button apply;
    private TextArea textAreaLeft;
    private TextArea textAreaMiddle;
    private TextArea textAreaRight;

    public ThreeTextAreas(String title, String description, String left, String middle, String right, CallTwo call){
        this.title = title;
        this.description = description;
        this.left = left;
        this.middle = middle;
        this.right = right;
        this.call = call;
    }

    public Pane build(){
        VBox layout = new VBox();

        //Properties
        layout.setAlignment(Pos.TOP_CENTER);

        //Children
        Pane a = buildTitle();
        Pane b = buildDescription();
        Pane c = buildButtons();
        Pane d = buildGrid();

        layout.getChildren().addAll(a,b,c,d);
        return layout;
    }

    private HBox buildTitle(){
        HBox layout = new HBox();

        //Properties
        layout.setPadding(new Insets(20,20,20,20));
        layout.setAlignment(Pos.CENTER);

        //Children
        Text textTitle = new Text(title);
        textTitle.setFont(Font.font("Source Code Pro", FontWeight.BOLD, 30));

        layout.getChildren().add(textTitle);
        return layout;
    }

    private HBox buildDescription(){
        HBox layout = new HBox();

        //Properties
        layout.setPadding(new Insets(20,20,20,20));
        layout.setAlignment(Pos.CENTER);

        //Children
        Text textDescription = new Text(description);
        textDescription.setFont(Font.font("System Regular", 20));

        layout.getChildren().add(textDescription);
        return layout;
    }

    private HBox buildButtons(){
        HBox layout = new HBox();

        //Properties
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10,10,10,10));
        layout.setSpacing(10);

        //Children
        loada = new Button("LOAD FILE A");
        loadb = new Button("LOAD FILE B");
        apply = new Button("APPLY");

        //Interactions
        loada.setOnAction(actionEvent -> {
            String loaded = UtilFx.loadFile();
            if(loaded != null) textAreaLeft.setText(loaded);
        });

        loadb.setOnAction(actionEvent -> {
            String loaded = UtilFx.loadFile();
            if(loaded != null) textAreaMiddle.setText(loaded);
        });

        apply.setOnAction(actionEvent -> {
            textAreaRight.setText(call.call(textAreaLeft.getText(),textAreaMiddle.getText()));
        });

        layout.getChildren().addAll(loada, loadb, apply);
        return layout;
    }

    private GridPane buildGrid(){
        GridPane layout = new GridPane();

        //Properties
        layout.setVgap(10);
        layout.setHgap(10);
        layout.setPadding(new Insets(10,10,10,10));
        layout.setAlignment(Pos.CENTER);
        layout.setMaxWidth(Double.POSITIVE_INFINITY);

        //Children
        Text textLeft = new Text(left);
        GridPane.setHalignment(textLeft, HPos.CENTER);
        textLeft.setUnderline(true);

        Text textMiddle = new Text(middle);
        GridPane.setHalignment(textMiddle, HPos.CENTER);
        textLeft.setUnderline(true);

        Text textRight = new Text(right);
        GridPane.setHalignment(textRight, HPos.CENTER);
        textRight.setUnderline(true);

        textAreaLeft = new TextArea();
        textAreaLeft.setEditable(true);
        GridPane.setVgrow(textAreaLeft, Priority.ALWAYS);

        textAreaMiddle = new TextArea();
        textAreaMiddle.setEditable(true);
        GridPane.setVgrow(textAreaMiddle, Priority.ALWAYS);

        textAreaRight = new TextArea();
        textAreaRight.setEditable(false);
        GridPane.setVgrow(textAreaRight, Priority.ALWAYS);

        //Position
        GridPane.setConstraints(textLeft,0,0);
        GridPane.setConstraints(textMiddle,1,0);
        GridPane.setConstraints(textRight,2,0);
        GridPane.setConstraints(textAreaLeft,0,1);
        GridPane.setConstraints(textAreaMiddle,1,1);
        GridPane.setConstraints(textAreaRight,2,1);

        layout.getChildren().addAll(textLeft, textMiddle, textRight, textAreaLeft, textAreaMiddle, textAreaRight);
        return layout;
    }
}
