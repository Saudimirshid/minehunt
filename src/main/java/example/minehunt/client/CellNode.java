package example.minehunt.client;

import example.minehunt.Cell;
import example.minehunt.CellActionResult;
import example.minehunt.CellActionResult.Outcome;
import example.minehunt.Position;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.lang.String.valueOf;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static javafx.util.Duration.millis;

/* new 2013-09-05 : begin */
/* new 2013-09-05 : end */

/**
 *
 */
public final class CellNode extends Parent {

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(10);

    private final GridNode gridNode;
    private final Shape strokeShape;
    private final ImageView imageView;

    public CellNode(final GridNode grid, final Position position, final Image gridImage) {
        gridNode = grid;
        final double width = grid.getCellWidth();
        final double height = grid.getCellHeight();
        setTranslateX(position.getI() * width);
        setTranslateY(position.getJ() * height);

        final Rectangle imageClip = createShape(width, height);
        imageClip.setX(position.getI() * width);
        imageClip.setY(position.getJ() * height);
        imageView = new ImageView();
        imageView.setImage(gridImage);
        imageView.setClip(imageClip);
        imageView.setLayoutX(-position.getI() * width);
        imageView.setLayoutY(-position.getJ() * height);

        strokeShape = createShape(width, height);
        strokeShape.setFill(null);
        strokeShape.setStroke(Color.BLACK);

        getChildren().addAll(imageView, strokeShape);


        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                final Cell selectedCell = grid.getGrid().getCell(position);
                final CellActionResult result = selectedCell.clearAround();
                if (result.getOutcome() == Outcome.EXPLOSION) {
                    Minehunt.notificationPane.setText("You lose, Buddy!!");
                    Minehunt.notificationPane.show();

                } else {
                    long millis = 0;
                    for (Cell cell : result.getAffectedCells()) {
                        //the selected cell should be displayed immediately
                        if (cell.getPosition().equals(position)) {
                            showCell(cell, cell.getMinesNearby());
                        } else {
                            //display other cell in 10 ms interval
                            millis += 10;
                            SCHEDULED_EXECUTOR_SERVICE.schedule(
                                    () -> {
                                        Platform.runLater(() -> showCell(cell, cell.getMinesNearby()));
                                    }
                                    , millis, MILLISECONDS);

                        }

                    }
                    if (grid.getGrid().isGameWon()) {
                        Minehunt.notificationPane.setText("You win, Buddy!!");
                        Minehunt.notificationPane.show();
                    }

                }
            } else {
                /* toggle the flag on this cell */
                Cell selectedCell = grid.getGrid().getCell(position);
                final Cell.State state = selectedCell.getState();
                if (state == Cell.State.UNVISITED) {
                    if (selectedCell.setFlag()) { //should always be true
                        final ImageView view = new ImageView(new Image("flag-icon-128.png"));
                        view.setId("flag");
                        view.setPreserveRatio(true);
                        view.setSmooth(true);
                        view.setFitHeight(grid.getCellHeight() / 2);
                        view.setLayoutX((gridNode.getCellWidth() - view.getBoundsInLocal().getWidth()) / 2);
                        view.setLayoutY((gridNode.getCellHeight() - 3 * view.getBoundsInLocal().getHeight() / 4) / 2);
                        view.setOpacity(0);
                        getChildren().add(view);
                        final FadeTransition fadeTransition = new FadeTransition(millis(250), view);
                        fadeTransition.setFromValue(0);
                        fadeTransition.setToValue(1);
                        fadeTransition.play();
                        gridNode.refreshHiddenMinesCount();
                    }
                } else if (state == Cell.State.FLAGGED) {
                    if (selectedCell.unsetFlag()) { //should always be true
                        final Node imageFlag = lookup("#flag");
                        final FadeTransition fadeTransition = new FadeTransition(millis(250), imageFlag);
                        fadeTransition.setFromValue(1);
                        fadeTransition.setToValue(0);
                        fadeTransition.setOnFinished(e -> getChildren().remove(imageFlag));
                        fadeTransition.play();
                        gridNode.refreshHiddenMinesCount();
                    }
                }
            }
        });
    }

    private Rectangle createShape(final double width, final double height) {
        final Rectangle rectangle = new Rectangle();
        rectangle.setHeight(height);
        rectangle.setWidth(width);
        return rectangle;
    }

    private Paint getMinesNearbyPaint(int minesNearby) {
        switch (minesNearby) {
            case 0:
                return Color.GREEN;
            case 1:
                return Color.ORANGE;
            case 2:
                return Color.DARKORANGE;
            case 3:
                return Color.ORANGERED;
            case 4:
                return Color.INDIANRED;
            case 5:
                return Color.RED;
            case 6:
                return Color.DARKRED;
            case 7:
                return Color.VIOLET;
            default:
                return Color.DARKVIOLET;
        }
    }

    private void showCell(final Cell cell, final int nearby) {
        final CellNode node = gridNode.getCell(cell.getPosition());
        final FadeTransition imageTransition = new FadeTransition(millis(250), node.imageView);
        imageTransition.setFromValue(1.0);
        imageTransition.setToValue(0.4);
        imageTransition.play();
        //node.rectangle.setFill(Color.GREEN);

        final Text text = new Text();
        text.setText(valueOf(nearby));
        text.setFill(getMinesNearbyPaint(nearby));
        text.setFontSmoothingType(FontSmoothingType.LCD);
        text.setFont(new Font(20));
        text.setOpacity(0);

        text.setLayoutX((gridNode.getCellWidth() - text.getBoundsInLocal().getWidth()) / 2);
        text.setLayoutY((gridNode.getCellHeight() + text.getBoundsInLocal().getHeight() / 2) / 2);
        node.getChildren().add(text);

        final FadeTransition numberFadeTransition = new FadeTransition(millis(250), text);
        numberFadeTransition.setFromValue(0);
        numberFadeTransition.setToValue(1);
        final RotateTransition numberRotateTransition = new RotateTransition(millis(400), text);
        numberRotateTransition.setByAngle(360);
        final ParallelTransition numberTransition = new ParallelTransition(numberFadeTransition, numberRotateTransition);

        numberTransition.play();
    }
}

