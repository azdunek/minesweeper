package sample;

import javafx.scene.control.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Field extends Button {
    private boolean mined;
    private ArrayList<Field> neighbours = new ArrayList<>();
    private List<String> colors = Arrays.asList("blue", "green", "red", "purple", "#800000", "#40E0D0", "black", "gray");
    private boolean flagged;


    public Field() {
        mined = false;
        this.getStylesheets().add("/sample/style.css");
        this.getStyleClass().add("field");

    }

    public ArrayList<Field> getNeighbours() {
        return neighbours;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public boolean isMined() {
        return mined;
    }

    public void setMined(boolean mined) {
        this.mined = mined;
    }


    public void addNeighbour(Field neighbour) {
        neighbours.add(neighbour);
    }

    public int getBombsAround() {
        int bombsCounter = 0;

        for (Field f : neighbours) {
            if (f.isMined()) {
                bombsCounter++;
            }
        }
        return bombsCounter;
    }

    public void reveal() {
        this.setDisable(true);


        if (this.getBombsAround() != 0) {
            this.setStyle("-fx-text-fill:" + colors.get(this.getBombsAround() - 1) + ";");
            this.setText(String.valueOf(this.getBombsAround()));
        } else {
                for (Field n : this.getNeighbours()) {
                    if (!n.isDisabled()) {
                        n.reveal();
                    }
                }
            }
        }
    }


