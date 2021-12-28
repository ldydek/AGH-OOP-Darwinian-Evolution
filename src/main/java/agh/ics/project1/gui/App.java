package agh.ics.project1.gui;

import agh.ics.project1.*;
import com.sun.javafx.scene.control.IntegerField;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.geometry.*;

import java.util.ArrayList;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.Node;

public class App extends Application implements IPositionChangeObserver {
    private GridPane gridPane;
    private Stage stage;
    private HBox hBox;
    private MapWithBorders map1;
    private MapWithoutBorders map2;
    private SimulationEngine engine;
    private int space;


    public void init() {
        gridPane = new GridPane();
        hBox = new HBox();
    }

    public void start(Stage primaryStage) {
        stage = primaryStage;
        VBox vBox = new VBox();
        Label label = new Label("ZACZYNAMY!");

        HBox mapWidthBox = new HBox();
        Label mapWidthLabel = new Label("Map width: ");
        IntegerField mapWidthInteger = new IntegerField();
        mapWidthInteger.setEditable(true);
        mapWidthInteger.setValue(15);
        mapWidthBox.getChildren().addAll(mapWidthLabel, mapWidthInteger);
        mapWidthBox.setAlignment(Pos.BASELINE_LEFT);

        HBox mapHeightBox = new HBox();
        Label mapHeightLabel = new Label("Map height: ");
        IntegerField mapHeightInteger = new IntegerField();
        mapHeightInteger.setEditable(true);
        mapHeightInteger.setValue(15);
        mapHeightBox.getChildren().addAll(mapHeightLabel, mapHeightInteger);
        mapHeightBox.setAlignment(Pos.BASELINE_LEFT);

        HBox startingEnergyBox = new HBox();
        Label startingEnergyLabel = new Label("Starting energy: ");
        IntegerField startingEnergyInteger = new IntegerField();
        startingEnergyInteger.setEditable(true);
        startingEnergyInteger.setValue(10);
        startingEnergyBox.getChildren().addAll(startingEnergyLabel, startingEnergyInteger);
        mapHeightBox.setAlignment(Pos.BASELINE_LEFT);

        HBox moveEnergyBox = new HBox();
        Label moveEnergyLabel = new Label("Move energy: ");
        IntegerField moveEnergyInteger = new IntegerField();
        moveEnergyInteger.setEditable(true);
        moveEnergyInteger.setValue(1);
        moveEnergyBox.getChildren().addAll(moveEnergyLabel, moveEnergyInteger);
        moveEnergyBox.setAlignment(Pos.BASELINE_LEFT);

        HBox plantEnergyBox = new HBox();
        Label plantEnergyLabel = new Label("Plant energy: ");
        IntegerField plantEnergyInteger = new IntegerField();
        plantEnergyInteger.setEditable(true);
        plantEnergyInteger.setValue(10);
        plantEnergyBox.getChildren().addAll(plantEnergyLabel, plantEnergyInteger);
        plantEnergyBox.setAlignment(Pos.BASELINE_LEFT);

        HBox jungleMapRatioBox = new HBox();
        Label jungleMapRatioLabel = new Label("Ratio between jungle and map (in percentage): ");
        IntegerField jungleMapRatioInteger = new IntegerField();
        jungleMapRatioInteger.setEditable(true);
        jungleMapRatioInteger.setValue(30);
        jungleMapRatioBox.getChildren().addAll(jungleMapRatioLabel, jungleMapRatioInteger);
        jungleMapRatioBox.setAlignment(Pos.BASELINE_LEFT);

        HBox animalQuantityBox = new HBox();
        Label animalQuantityLabel = new Label("Animal quantity: ");
        IntegerField animalQuantityInteger = new IntegerField();
        animalQuantityInteger.setEditable(true);
        animalQuantityInteger.setValue(10);
        animalQuantityBox.getChildren().addAll(animalQuantityLabel, animalQuantityInteger);
        animalQuantityBox.setAlignment(Pos.BASELINE_LEFT);

        Button button = new Button("START");
        vBox.getChildren().addAll(label, mapWidthBox, mapHeightBox, startingEnergyBox, moveEnergyBox, plantEnergyBox,
                jungleMapRatioBox, animalQuantityBox, button);
        vBox.setSpacing(10);

        Scene startScene = new Scene(vBox, 800, 800);
        Scene scene = new Scene(gridPane, 1250, 650);
        button.setOnAction(e -> switchScenes(scene, mapWidthBox, mapHeightBox, startingEnergyBox, moveEnergyBox,
                plantEnergyBox, jungleMapRatioBox, animalQuantityBox));
        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    private void switchScenes(Scene scene, HBox mapWidthBox, HBox mapHeightBox, HBox startingEnergyBox, HBox moveEnergyBox,
                              HBox plantEnergyBox, HBox jungleMapRatioBox, HBox animalQuantityBox) {
        int mapWidth = ((IntegerField) mapWidthBox.getChildren().get(1)).getValue();
        int mapHeight = ((IntegerField) mapHeightBox.getChildren().get(1)).getValue();
        int startingEnergy = ((IntegerField) startingEnergyBox.getChildren().get(1)).getValue();
        int moveEnergy = ((IntegerField) moveEnergyBox.getChildren().get(1)).getValue();
        int plantEnergy = ((IntegerField) plantEnergyBox.getChildren().get(1)).getValue();
        int jungleMapRatio = ((IntegerField) jungleMapRatioBox.getChildren().get(1)).getValue();
        int animalQuantity = ((IntegerField) animalQuantityBox.getChildren().get(1)).getValue();
        Animal.setStartingEnergy(startingEnergy);
        Animal.setMoveEnergy(moveEnergy);
        Plant.setPlantEnergy(plantEnergy);
        map1 = new MapWithBorders(mapHeight, mapWidth, jungleMapRatio, animalQuantity);
        map2 = new MapWithoutBorders(mapHeight, mapWidth, jungleMapRatio, animalQuantity);
        space = map1.getMapHeight() + 1;
        stage.setScene(scene);
        stage.show();
        engine = new SimulationEngine(map1, map2);
        engine.addObserver(this);
//        makeGridPane();
        colorGridPane();
        addObjects();
        Thread thread = new Thread(engine);
        thread.start();
    }

    private void makeGridPane() {
        int ctr = 1;
        Vector2d lowerLeft = map1.getMapLowerLeft();
        Vector2d upperRight = map1.getMapUpperRight();

        for (int i = upperRight.getY(); i >= lowerLeft.getY(); i--) {
            Label label = new Label(Integer.toString(i));
            GridPane.setHalignment(label, HPos.CENTER);
            gridPane.getRowConstraints().add(new RowConstraints(40));
            ctr++;
        }

        ctr = 1;
        for (int i = lowerLeft.getX(); i <= upperRight.getX(); i++) {
            Label label = new Label(Integer.toString(i));
            GridPane.setHalignment(label, HPos.CENTER);
            gridPane.getColumnConstraints().add(new ColumnConstraints(40));
            ctr++;
        }

        ctr = 1;
        for (int i = lowerLeft.getX() + space; i <= upperRight.getX() + space + 1; i++) {
            Label label = new Label(Integer.toString(i));
            GridPane.setHalignment(label, HPos.CENTER);
            gridPane.getColumnConstraints().add(new ColumnConstraints(40));
            ctr++;
        }

        ctr = 1;
        for (int i = upperRight.getY(); i >= lowerLeft.getY(); i--) {
            Label label = new Label(Integer.toString(i));
            GridPane.setHalignment(label, HPos.CENTER);
            gridPane.getRowConstraints().add(new RowConstraints(40));
            ctr++;
        }
    }

    private void colorGridPane() {
        Vector2d lowerLeft = map1.getMapLowerLeft();
        Vector2d upperRight = map1.getMapUpperRight();
        for (int i = upperRight.getY(); i >= lowerLeft.getY(); i--) {
            for (int j = lowerLeft.getX(); j <= upperRight.getX(); j++) {
                Vector2d position = new Vector2d(j, i);
                StackPane pane = new StackPane();
                Rectangle rectangle = new Rectangle(20, 20);
                if (map1.jungleField(position)) {
                    rectangle.setFill(Color.DARKGREEN);
//                    pane.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                else {
                    rectangle.setFill(Color.GREEN);
//                    pane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                pane.getChildren().add(rectangle);
                gridPane.add(pane, j, i);
            }
        }

        for (int i = upperRight.getY(); i >= lowerLeft.getY(); i--) {
            for (int j = lowerLeft.getX()+space; j <= upperRight.getX()+space; j++) {
                Vector2d position = new Vector2d(j-space, i);
                StackPane pane = new StackPane();
                Rectangle rectangle = new Rectangle(20, 20);
                if (map2.jungleField(position)) {
                    rectangle.setFill(Color.DARKGREEN);
//                    pane.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                else {
                    rectangle.setFill(Color.GREEN);
//                    pane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                }
                pane.getChildren().add(rectangle);
                gridPane.add(pane, j, i);
            }
        }
    }

    private void addObjects() {
        Vector2d lowerLeft = map1.getMapLowerLeft();
        Vector2d upperRight = map1.getMapUpperRight();
        Map<Vector2d, MapField> hashMap1 = map1.getHashMap();
        Map<Vector2d, MapField> hashMap2 = map2.getHashMap();
        ArrayList<IMapElement> mapElements1 = new ArrayList<>();
        ArrayList<IMapElement> mapElements2 = new ArrayList<>();
        for (Vector2d position: hashMap1.keySet()) {
            mapElements1.addAll(hashMap1.get(position).getSortedSet());
        }
        for (Vector2d position: hashMap2.keySet()) {
            mapElements2.addAll(hashMap2.get(position).getSortedSet());
        }
        for (IMapElement element: mapElements1) {
            GuiElementBox guiElementBox = new GuiElementBox(element);
            gridPane.add(guiElementBox.getVBox(), element.getPosition().getX()-lowerLeft.getX(),
                    upperRight.getY()-element.getPosition().getY());
        }
        for (IMapElement element: mapElements2) {
            GuiElementBox guiElementBox = new GuiElementBox(element);
            gridPane.add(guiElementBox.getVBox(), element.getPosition().getX()-lowerLeft.getX() + space,
                    upperRight.getY()-element.getPosition().getY());
        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element) {
        Platform.runLater(() -> {
            Node node = gridPane.getChildren().get(0);
            gridPane.getChildren().clear();
            this.gridPane.getChildren().add(node);
            colorGridPane();
            addObjects();
        });
    }
}
