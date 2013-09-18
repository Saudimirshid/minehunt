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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.NotificationPane;

/**
 *
 */
public final class Minehunt extends Application {

    static NotificationPane notificationPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Stage stage = new Stage(StageStyle.UTILITY);

        final MinehuntService minehuntService = new SimpleMinehuntService();
        final Image background = new Image("mer-surface.jpg");
        //final Image background = new Image("etoile-mer.jpg");
        final Grid grid = minehuntService.createGrid(10, 10, 8);
        final GridNode gridNode = new GridNode(grid, background);
        final ToolBar toolbar = new ToolBar();

        final Image image = new Image("mine.png", 32, 32, true, true);
        final Label mineLabel = new Label(gridNode.hiddenMinesCountProperty().get(), new ImageView(image));
        mineLabel.textProperty().bind(gridNode.hiddenMinesCountProperty());

        toolbar.getItems().add(mineLabel);

        final BorderPane pane = new BorderPane();
        pane.setTop(toolbar);
        pane.setCenter(gridNode);

        notificationPane = new NotificationPane(pane);
        notificationPane.getStyleClass().add(NotificationPane.STYLE_CLASS_DARK);
        notificationPane.setShowFromTop(false);


        Scene scene = new Scene(notificationPane);
        stage.setScene(scene);


        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.setTitle("Minehunt");
        stage.show();
    }
}
