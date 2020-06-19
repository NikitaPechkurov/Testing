package model;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.swing.*;

public class Viewer extends Colleague {

    public Viewer(Mediator mediator) {
        super(mediator);
    }

    //заредачить под просмотр теста
    public void notifyColleague(Question message) {
        VBox qwpane = new VBox();
        qwpane.setStyle("-fx-border-color: black; border-width: 10");
        qwpane.setPadding(new Insets(10, 50, 50, 50));
        qwpane.setSpacing(10);
        Label qwfield = new Label();
        qwfield.textProperty().bindBidirectional(message.getQuestionProperty());
        qwpane.getChildren().add(qwfield);
        Separator separator = new Separator();
        separator.setMaxWidth(240);
        separator.setMinHeight(20);
        qwpane.getChildren().add(separator);
        for (int i = 0; i < message.getGoodAnswers().size() ; i++) {
            TextField qwfieldi = new TextField();
            qwfieldi.setEditable(false);//без возможности редактирования поля
            qwfieldi.textProperty().bindBidirectional(message.getGoodAnswers().get(i));
            qwpane.getChildren().add(qwfieldi);
        }
        for (int i = 0; i < message.getBadAnswers().size() ; i++) {
            TextField qwfieldi = new TextField();
            qwfieldi.setEditable(false);
            qwfieldi.textProperty().bindBidirectional(message.getBadAnswers().get(i));
            qwpane.getChildren().add(qwfieldi);
        }
        mediator.setView(qwpane);
    }
}
