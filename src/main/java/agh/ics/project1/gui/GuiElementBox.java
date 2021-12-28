package agh.ics.project1.gui;

import agh.ics.project1.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GuiElementBox {
    private final VBox vBox;

    public GuiElementBox(IMapElement element) {
        String path = element.getPath();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(fileInputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        vBox = new VBox();
        vBox.getChildren().add(imageView);
        vBox.setAlignment(Pos.CENTER);
    }

    public VBox getVBox() {
        return vBox;
    }
}
