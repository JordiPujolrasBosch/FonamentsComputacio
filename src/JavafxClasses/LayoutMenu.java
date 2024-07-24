package JavafxClasses;

import Utils.Pair;
import Utils.Utility;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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
        list.add(new ItemMenu("Welcome to the app", infoPane(), true, false));
        for(Pair<String,List<GuiMenu.GuiMenuIn>> pair : GuiMenu.getList()){
            list.add(new ItemMenu(pair.getA(), null, false, false));
            for(GuiMenu.GuiMenuIn g : pair.getB()){
                list.add(new ItemMenu(g.getTitle(), g.getCallPane(), true, true));
            }
        }
        return list;
    }

    private static CallPane infoPane(){
        return () -> {
            LayoutStart start = new LayoutStart();
            return start.build();
        };
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
