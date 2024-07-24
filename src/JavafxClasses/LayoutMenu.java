package JavafxClasses;

import Utils.Pair;
import Utils.Utility;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class LayoutMenu {
    private static final List<ItemMenu> menu = buildMenu();
    private static int pos = 1;

    public static Pane build(){
        BorderPane layout = new BorderPane();
        Utility.setBorderPane(layout);
        layout.setLeft(verticalMenu());
        layout.setCenter(menu.get(pos).action.call());
        return layout;
    }

    private static ScrollPane verticalMenu(){
        VBox layout = new VBox();
        layout.setSpacing(3);
        layout.setPadding(new Insets(10));
        layout.setBackground(new Background(new BackgroundFill(Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY)));

        for(ItemMenu i : menu){
            Node node;
            if(!i.space) node = new Text(i.text);
            else if(!i.active){
                Hyperlink t = new Hyperlink(i.text);
                t.setStyle("-fx-text-fill: black; -fx-underline: false;");
                node = t;
            }
            else{
                Hyperlink h = new Hyperlink(i.text);
                h.setOnAction(actionEvent -> move(i));
                node = h;
            }
            if(i.space) VBox.setMargin(node, new Insets(0,0,0,10));
            else VBox.setMargin(node, new Insets(0,0,0,4));
            layout.getChildren().add(node);
        }

        ScrollPane scroll = new ScrollPane(layout);
        scroll.setBackground(new Background(new BackgroundFill(Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY)));
        return scroll;
    }

    private static void move(ItemMenu item){
        item.active = false;
        menu.get(pos).active = true;
        pos = menu.indexOf(item);
        Utility.getBorderPane().setLeft(verticalMenu());
        Utility.getBorderPane().setCenter(item.action.call());
    }

    private static List<ItemMenu> buildMenu(){
        List<ItemMenu> list = new ArrayList<>();
        list.add(new ItemMenu("Information", null, false, false));
        list.add(new ItemMenu("Welcome to the app", startPane(), true, false));
        list.add(new ItemMenu("Manuals", infoPane(), true, true));
        for(Pair<String,List<GuiMenu.GuiMenuIn>> pair : GuiMenu.getList()){
            list.add(new ItemMenu(pair.getA(), null, false, false));
            for(GuiMenu.GuiMenuIn g : pair.getB()){
                list.add(new ItemMenu(g.getTitle(), g.getCallPane(), true, true));
            }
        }
        return list;
    }

    private static CallPane startPane(){
        return () -> {
            VBox layout = new VBox();
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(20,20,20,20));

            Label title = new Label("Regular Languages &\nContext Free Languages\nTools");
            title.setFont(Font.font("Source Code Pro", FontWeight.BOLD, 40));
            title.setPadding(new Insets(20,20,20,20));
            title.setTextAlignment(TextAlignment.CENTER);

            layout.getChildren().addAll(title);
            return layout;
        };
    }

    private static CallPane infoPane(){
        return () -> {
            VBox layout = new VBox();
            layout.setAlignment(Pos.TOP_LEFT);
            layout.setPadding(new Insets(20,20,20,20));

            Label titleManual1 = new Label("Command line manual");
            infoTitleProperties(titleManual1);

            TextField manual1 = new TextField("https://github.com/JordiPujolrasBosch/FonamentsComputacio/blob/master/CommandLineManual.md");
            infoManualProperties(manual1);

            Label titleManual2 = new Label("Format of dfa, nfa, regex and cfg");
            infoTitleProperties(titleManual2);

            TextField manual2 = new TextField("https://github.com/JordiPujolrasBosch/FonamentsComputacio/blob/master/ReaderManual.md");
            infoManualProperties(manual2);

            Label title3 = new Label("Author");
            infoTitleProperties(title3);

            Label author = new Label("Made by: Jordi Pujolras Bosch");
            author.setPadding(new Insets(0,0,0,10));

            layout.getChildren().addAll(titleManual1, manual1, titleManual2, manual2, title3, author);
            return layout;
        };
    }

    private static void infoTitleProperties(Label label){
        label.setFont(Font.font("Source Code Pro", FontWeight.BOLD, 20));
        label.setPadding(new Insets(20,0,20,10));
    }

    private static void infoManualProperties(TextField textField){
        textField.setPadding(new Insets(0,0,0,10));
        textField.setEditable(false);
        textField.setMaxWidth(600);
        textField.setPrefHeight(30);
    }

    private static class ItemMenu{
        public String text;
        public boolean space;
        public boolean active;
        public CallPane action;
        public ItemMenu(String text, CallPane action, boolean space, boolean active){
            this.text = text;
            this.action = action;
            this.active = active;
            this.space = space;
        }
    }

}
