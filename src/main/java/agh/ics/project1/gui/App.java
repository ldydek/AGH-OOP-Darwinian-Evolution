package agh.ics.project1.gui;

import agh.ics.project1.*;
import com.sun.javafx.scene.control.IntegerField;
import javafx.application.Application;
import javafx.scene.chart.Chart;
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
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class App extends Application implements IPositionChangeObserver {
    private GridPane gridPane;
    private Stage stage;
    private HBox hBox;
    private MapWithBorders map1;
    private MapWithoutBorders map2;
    private SimulationEngine engine;
    private int space;
    private int day = 0;
    private final VBox vBox1 = new VBox();
    private final VBox vBox2 = new VBox();
    private final HBox vBox1VBox2 = new HBox();
    private final VBox finalVBox = new VBox();
    private NumberAxis xAxis1 = new NumberAxis();
    private NumberAxis yAxis1 = new NumberAxis();
    private NumberAxis xAxis2 = new NumberAxis();
    private NumberAxis yAxis2 = new NumberAxis();
    private NumberAxis xAxis3 = new NumberAxis();
    private NumberAxis yAxis3 = new NumberAxis();
    private NumberAxis xAxis4 = new NumberAxis();
    private NumberAxis yAxis4 = new NumberAxis();
    private final LineChart lineChart1 = new LineChart(xAxis1, yAxis1);
    private final LineChart lineChart2 = new LineChart(xAxis2, yAxis2);
    private final LineChart lineChart3 = new LineChart(xAxis3, yAxis3);
    private final LineChart lineChart4 = new LineChart(xAxis4, yAxis4);

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
        startingEnergyInteger.setValue(20);
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
        Scene scene = new Scene(finalVBox, 1250, 800);
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
        space = map1.getMapHeight() + 5;
        stage.setScene(scene);
        stage.show();
        engine = new SimulationEngine(map1, map2);
        engine.addObserver(this);
        colorGridPane();
        addObjects();
        addStatistics();
        Thread thread = new Thread(engine);
        thread.start();
    }

    private void addStatistics() {
        vBox1.getChildren().clear();
        vBox2.getChildren().clear();
        vBox1.getChildren().addAll(animalQuantityLabel(map1), plantQuantityLabel(map1), averageEnergyLabel(map1),
                dominantGenome(map1), averageAgeOfDeadAnimals(map1), drawALineChart1(map1), drawALineChart3(map1));
        vBox2.getChildren().addAll(animalQuantityLabel(map2), plantQuantityLabel(map2), averageEnergyLabel(map2),
                dominantGenome(map2), averageAgeOfDeadAnimals(map2), drawALineChart2(map1), drawALineChart4(map2));
        vBox1VBox2.getChildren().clear();
        vBox1VBox2.getChildren().addAll(vBox1, vBox2);
        vBox1VBox2.setSpacing(space*10);
        finalVBox.getChildren().clear();
        finalVBox.getChildren().addAll(gridPane, vBox1VBox2);
        day++;
    }

    private Chart drawALineChart1(AbstractWorldMap map) {
        xAxis1.setLabel("Quantity");
        yAxis1.setLabel("Number of days");
        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.getData().add(new XYChart.Data(day, map.animalQuantity()));
        lineChart1.getData().add(dataSeries);
        return lineChart1;
    }

    private Chart drawALineChart2(AbstractWorldMap map) {
        xAxis2.setLabel("Quantity");
        yAxis2.setLabel("Number of days");
        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.getData().add(new XYChart.Data(day, map.animalQuantity()));
        lineChart2.getData().add(dataSeries);
        return lineChart2;
    }

    private Chart drawALineChart3(AbstractWorldMap map) {
        xAxis2.setLabel("Quantity");
        yAxis2.setLabel("Number of days");
        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.getData().add(new XYChart.Data(day, map.plantQuantity()));
        lineChart3.getData().add(dataSeries);
        return lineChart3;
    }

    private Chart drawALineChart4(AbstractWorldMap map) {
        xAxis2.setLabel("Quantity");
        yAxis2.setLabel("Number of days");
        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.getData().add(new XYChart.Data(day, map.plantQuantity()));
        lineChart4.getData().add(dataSeries);
        return lineChart4;
    }

    private void colorGridPane() {
        Vector2d lowerLeft = map1.getMapLowerLeft();
        Vector2d upperRight = map1.getMapUpperRight();
        for (int i = upperRight.getY(); i >= lowerLeft.getY(); i--) {
            for (int j = lowerLeft.getX(); j <= upperRight.getX(); j++) {
                Vector2d position = new Vector2d(j, i);
                StackPane pane = new StackPane();
                Rectangle rectangle = new Rectangle(30, 30);
                if (map1.jungleField(position)) {
                    rectangle.setFill(Color.DARKGREEN);
                }
                else {
                    rectangle.setFill(Color.GREEN);
                }
                pane.getChildren().add(rectangle);
                gridPane.add(pane, j, i);
            }
        }

        int a = map1.getMapHeight() + 1;
        for (int b = 0; b < 10; b++) {
            for (int i = upperRight.getY(); i >= lowerLeft.getY(); i--) {
                StackPane pane = new StackPane();
                Rectangle rectangle = new Rectangle(30, 30);
                rectangle.setFill(Color.WHITE);
                pane.getChildren().add(rectangle);
                gridPane.add(pane, a, i);
            }
            a++;
        }

        for (int i = upperRight.getY(); i >= lowerLeft.getY(); i--) {
            for (int j = lowerLeft.getX()+space; j <= upperRight.getX()+space; j++) {
                Vector2d position = new Vector2d(j-space, i);
                StackPane pane = new StackPane();
                Rectangle rectangle = new Rectangle(30, 30);
                if (map2.jungleField(position)) {
                    rectangle.setFill(Color.DARKGREEN);
                }
                else {
                    rectangle.setFill(Color.GREEN);
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
            addStatistics();
        });
    }

    private Label animalQuantityLabel(AbstractWorldMap map) {
        return new Label("Animal quantity: " + map.animalQuantity());
    }

    private Label plantQuantityLabel(AbstractWorldMap map) {
        return new Label("Plant quantity: " + map.plantQuantity());
    }

    private Label averageEnergyLabel(AbstractWorldMap map) {
        return new Label("Average energy: " + map.averageEnergy());
    }

    private Label dominantGenome(AbstractWorldMap map) {
        return new Label("Dominant genome: " + map.dominantGenome());
    }

    private Label averageAgeOfDeadAnimals(AbstractWorldMap map) {
        return new Label("Average age of dead animals: " + map.averageAgeOfDeadAnimals());
    }
}
