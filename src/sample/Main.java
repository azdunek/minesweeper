package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends Application {
    final int BOARD_SIZE = 10;
    final int FIELD_NUMBER =BOARD_SIZE*BOARD_SIZE;
    final int MINES_NUMBER = 10;
    private List<List<Field>> fields = new ArrayList<>();
    GridPane gridPane = new GridPane();
    Image mineImage = new Image(getClass().getResourceAsStream("mine.gif"), 30, 30, true, true);
    Image flagImage = new Image(getClass().getResourceAsStream("flag.png"), 30, 30, true, true);
    Alert gameOverAlert = new Alert(Alert.AlertType.CONFIRMATION, "Game over! Do you want to play again?", ButtonType.YES, ButtonType.NO);
    Alert winningAlert = new Alert(Alert.AlertType.CONFIRMATION, "Congratulations, you won!", ButtonType.YES, ButtonType.NO);

    Stage stage;
    private int flagsCounter = 0;
    
    // TODO: Play again ***
    // TODO: Change mine image
    // TODO: Refactoring *

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        initializeBoard();
        mineFields();
        setNeighbours();

        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(new Scene(gridPane, 510, 510));

        primaryStage.show();
    }

    // Lambda method
    public void handleClick(MouseEvent mouseEvent) {
        Field f = (Field) mouseEvent.getSource();

        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (f.isMined()) {
                gameover();
            } else {
                f.reveal();
            }
        } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            if (f.isFlagged()) {
                flagsCounter--;
                f.setGraphic(null);
                f.setFlagged(false);
            } else {
                flagsCounter++;
                f.setGraphic(new ImageView(flagImage));
                f.setFlagged(true);
            }
        }

        if (flagsCounter == MINES_NUMBER) {
            if (getNumberOfDisabledFields() == (FIELD_NUMBER - MINES_NUMBER)) {
                gameWin();
            }
        }
    }

    private int getNumberOfDisabledFields() {
        int numb = 0;
        for (List<Field> fieldList : fields) {
            for (Field field : fieldList) {
                if(field.isDisabled()){
                    numb++;
                }
            }
        }
        return numb;
    }

    private void gameWin() {
        winningAlert.showAndWait();
        if (winningAlert.getResult() == ButtonType.YES) {
            start(stage);
        } else {
            Platform.exit();
        }
    }


    public void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            List<Field> tmp = new ArrayList<>();
            for (int j = 0; j < BOARD_SIZE; j++) {
                Field field = new Field();
                tmp.add(field);

//                MinesweeperClickHandler handler = new MinesweeperClickHandler();
//                field.setOnAction(handler);

                field.setOnMouseClicked(this::handleClick);

                gridPane.add(field, i, j);
            }
            fields.add(tmp);
        }
    }

    private void gameover() {
        for (List<Field> l : fields) {
            for (Field f : l) {
                f.setDisable(true);
                if (f.isMined()) {
                    f.setGraphic(new ImageView(mineImage));
                }
            }
        }
        gameOverAlert.showAndWait();
        if (gameOverAlert.getResult() == ButtonType.YES) {
            start(stage);
        } else {
            Platform.exit();
        }
    }

    public void mineFields() {
        int x;
        int y;
        int minedFields = 0;
        Field randomlySelectedField;

        while (minedFields != MINES_NUMBER) {
            x = getRandomWithRange(0, BOARD_SIZE - 1);
            y = getRandomWithRange(0, BOARD_SIZE - 1);
            randomlySelectedField = fields.get(x).get(y);

            if (!randomlySelectedField.isMined()) {
                randomlySelectedField.setMined(true);
                minedFields++;
            }
        }
    }

    public void test() {
        for (List<Field> l : fields) {
            for (Field f : l) {
                if (f.isMined()) {
                    f.setText("B");
                } else {
                    f.setText(String.valueOf(f.getBombsAround()));
                }
            }
        }
    }

    public void setNeighbours() {
        List<Integer> numbers = Arrays.asList(-1, 0, 1);
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                Field selectedField = fields.get(x).get(y);
                for (Integer i : numbers) {
                    for (Integer j : numbers) {
                        int newX = x + i;
                        int newY = y + j;
                        if ((i != 0 || j != 0) && newX >= 0 && newX < BOARD_SIZE && newY >= 0 && newY < BOARD_SIZE) {
                            Field neighbour = fields.get(newX).get(newY);
                            selectedField.addNeighbour(neighbour);
                        }
                    }

                }
            }
        }


    }

    public int getRandomWithRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
