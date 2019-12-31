package sample;

import javafx.scene.control.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Field extends Button {
    private boolean mined;
    private ArrayList<Field> neighbours = new ArrayList<>();


    public Field() {
        mined = false;
        this.getStylesheets().add("/sample/style.css");
        this.getStyleClass().add("field");
    }

    public ArrayList<Field> getNeighbours() {
        return neighbours;
    }

    public int neighboursNumber() {
        return neighbours.size();
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

        this.setText(String.valueOf(this.getBombsAround()));

        if (this.getBombsAround() == 0) {
            for (Field n : this.getNeighbours()) {
                if (!n.isDisabled()) {
                    n.reveal();
                }
            }
        }
    }
}

