package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MinesweeperClickHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent actionEvent) {
        Field f = (Field) actionEvent.getSource();
        f.reveal();
        }
    }



