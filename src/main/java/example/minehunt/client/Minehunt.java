package example.minehunt.client;

import example.minehunt.Grid;
import example.minehunt.MinehuntService;
import example.minehunt.SimpleMinehuntService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 */
public final class Minehunt extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Stage stage = new Stage();

        final MinehuntService minehuntService = new SimpleMinehuntService();
        final Grid grid = minehuntService.createGrid(10, 10, 8);
        final GridNode gridNode = new GridNode(grid);
        final ToolBar toolbar = new ToolBar();
        final Image image = new Image("mine.png", 32, 32, true, true);
        final Label mineLabel = new Label(gridNode.hiddenMinesCountProperty().get(), new ImageView(image));
        mineLabel.textProperty().bind(gridNode.hiddenMinesCountProperty());

        toolbar.getItems().add(mineLabel);
        final VBox vbox = new VBox();
        vbox.getChildren().add(toolbar);
        vbox.getChildren().add(gridNode);

        Scene scene = new Scene(vbox);
        stage.setScene(scene);

        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.setTitle("Minehunt");
        stage.show();
    }
}
