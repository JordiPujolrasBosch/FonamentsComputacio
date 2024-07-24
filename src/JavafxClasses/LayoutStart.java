package JavafxClasses;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class LayoutStart {

    public Pane build(){
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20,20,20,20));

        Label title = new Label("Regular Languages &\nContext Free Languages\nTools");
        title.setFont(Font.font("Source Code Pro", FontWeight.BOLD, 40));
        title.setPadding(new Insets(20,20,20,20));
        title.setTextAlignment(TextAlignment.CENTER);

        layout.getChildren().addAll(title);
        return layout;
    }
}
