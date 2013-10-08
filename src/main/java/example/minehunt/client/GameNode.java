package example.minehunt.client;

import eu.hansolo.enzo.clock.Clock;
import eu.hansolo.enzo.clock.ClockBuilder;
import example.minehunt.Grid;
import example.minehunt.MinehuntService;
import example.minehunt.client.flickr.FlickProvider;
import javafx.animation.Animation.Status;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.NotificationPane;

import java.util.concurrent.Executors;


/**
 *
 */
public final class GameNode extends Region {

    private static final String GAME_ID = "gameId";

    static GameNode getCurrentGame(final Node node) {
        return (GameNode) node.getScene().lookup("#" + GAME_ID);
    }

    private final NotificationPane notificationPane;
    private final Clock clock;
    private final GridNode gridNode;
    private final VBox leftSide;

    public GameNode(final MinehuntService minehuntService, final Image backgroundImage) {
        setId(GAME_ID);


        final Grid grid = minehuntService.createGrid(10, 10, 8);
        gridNode = new GridNode(grid, backgroundImage);


        final Image image = new Image("mine.png", 32, 32, true, true);
        final Label mineLabel = new Label(String.valueOf(gridNode.hiddenMinesCountProperty().get()), new ImageView(image));
        mineLabel.textProperty().bind(Bindings.convert(gridNode.hiddenMinesCountProperty()));


        clock = ClockBuilder.create()
                .prefSize(150, 150)
                .design(Clock.Design.DB)
                .nightMode(true)
                .build();
        Platform.runLater(clock::start);

        final Button newGameButton = new Button("New Game");
        newGameButton.getStyleClass().add("button");

        leftSide = new VBox();
        leftSide.setFillWidth(true);
        leftSide.getStyleClass().add("leftSide");
        leftSide.getChildren().addAll(mineLabel, clock, newGameButton);
        leftSide.setAlignment(Pos.CENTER);
        leftSide.setSpacing(20);
        leftSide.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(5), null)));

        newGameButton.setOnAction(event -> {
            final Image newBackgroundImage = FlickProvider.getInstance().nextImage();
            final Scene scene = getScene();

            final Window window = scene.getWindow();
            final double widthDiff = newBackgroundImage.getWidth() - backgroundImage.getWidth();
            final double heightDiff = newBackgroundImage.getHeight() - backgroundImage.getHeight();
            final FadeTransition offTransition = new FadeTransition(Duration.millis(500), gridNode);
            offTransition.setFromValue(1);
            offTransition.setToValue(0.5);
            setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(5), null)));
            offTransition.play();
            final FadeTransition leftOffTransition = new FadeTransition(Duration.millis(750), leftSide);
            leftOffTransition.setFromValue(1);
            leftOffTransition.setToValue(0);
            leftOffTransition.play();


            final GameNode newGame = new GameNode(minehuntService, newBackgroundImage);
            final FadeTransition inTransition = new FadeTransition(Duration.millis(1000), newGame.gridNode);
            newGame.gridNode.setOpacity(0);
            inTransition.setFromValue(0);
            inTransition.setToValue(1);

            final FadeTransition leftInTransition = new FadeTransition(Duration.millis(1000), newGame.leftSide);
            newGame.leftSide.setOpacity(0);
            leftInTransition.setFromValue(0);
            leftInTransition.setToValue(1);

            offTransition.setOnFinished(e -> {
                newGame.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(5), null)));
                scene.setRoot(newGame);

                if (widthDiff != 0 || heightDiff != 0) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        final double windowWidth = window.getWidth();
                        final double windowHeight = window.getHeight();
                        double width = widthDiff;
                        double height = heightDiff;
                        while (width != 0 || height != 0 || inTransition.getStatus() != Status.STOPPED) {
                            if (width != 0) {
                                width = width > 0 ? Math.max(0, width - 4) : Math.min(0, width + 4);
                                window.setWidth(windowWidth + widthDiff - width);
                            }
                            if (height != 0) {
                                height = height > 0 ? Math.max(0, height - 4) : Math.min(0, height + 4);
                                window.setHeight(windowHeight + heightDiff - height);
                            }
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException e2) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        Platform.runLater(() -> newGame.setBackground(null));
                    });
                } else {
                    inTransition.setOnFinished(e2 -> newGame.setBackground(null));
                }

                inTransition.play();
                leftInTransition.play();
            });


        });


        final BorderPane pane = new BorderPane();
        final StackPane leftPane = new StackPane();
        leftPane.getStyleClass().add("leftPane");
        leftPane.getChildren().add(leftSide);
        pane.setLeft(leftPane);
        pane.setCenter(gridNode);

        notificationPane = new NotificationPane();
        notificationPane.setContent(pane);
        getStyleClass().add(NotificationPane.STYLE_CLASS_DARK);
        notificationPane.setShowFromTop(false);
        getChildren().add(notificationPane);
    }

    void displayMessage(final String message) {
        notificationPane.setText(message);
        notificationPane.show();
    }

    long stopClock() {
        return clock.stop();
    }


}
