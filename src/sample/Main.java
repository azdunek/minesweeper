package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends Application {
    final int BOARD_SIZE = 10;
    final int MINES_NUMBER = 10;
    private List<List<Field>> fields = new ArrayList<>();
    GridPane gridPane = new GridPane();
    Image image = new Image(getClass().getResourceAsStream("mine.gif"), 30, 30, true, true);

    // TODO: Pop up window "Game over"
    // TODO: Coloured numbers
    // TODO: Refactoring
    // TODO: git push in the bush

    @Override
    public void start(Stage primaryStage) {

        initializeBoard();
        mineFields();
        setNeighbours();

        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(new Scene(gridPane, 510, 510));
        primaryStage.show();
    }


    public void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            List<Field> tmp = new ArrayList<>();
            for (int j = 0; j < BOARD_SIZE; j++) {
                Field field = new Field();
                tmp.add(field);

//                MinesweeperClickHandler handler = new MinesweeperClickHandler();
//                field.setOnAction(handler);

                field.setOnAction(this::handleClick);

                gridPane.add(field, i, j);
            }
            fields.add(tmp);
        }
    }

    // Lambda method
    public void handleClick(ActionEvent actionEvent) {
        Field f = (Field) actionEvent.getSource();
        if (f.isMined()) {
            gameover();
        } else {
            f.reveal();
        }

    }

    private void gameover() {
        for (List<Field> l : fields) {
            for (Field f : l) {
                f.setDisable(true);
                if (f.isMined()) {
                    f.setGraphic(new ImageView(image));
                }
            }
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
