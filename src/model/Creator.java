package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Creator extends Colleague {//редактор (им является лектор)

    public Creator(Mediator mediator) {
        super(mediator);
    }

    public void notifyColleague(Question message) {//переделать размеры, добавить применение редаченья
        ArrayList<TextField> fieldsGoodAns = new ArrayList<>();
        ArrayList<TextField> fieldsBadAns = new ArrayList<>();
        VBox qwpane = new VBox();
        Label quest = new Label();
        quest.setText("Напишите вопрос:");
        qwpane.getChildren().add(quest);
        TextField qwfield = new TextField();
        qwfield.textProperty().bindBidirectional(new SimpleStringProperty());
        qwpane.getChildren().add(qwfield);
        Separator separator = new Separator();
        separator.setMinWidth(240);
        separator.setMinHeight(20);
        qwpane.getChildren().add(separator);
        for (int i = 0; i < message.getGoodAnswers().size() ; i++) {
            TextField qwfieldi = new TextField();
            qwfieldi.textProperty().bindBidirectional(new SimpleStringProperty());
            qwpane.getChildren().add(qwfieldi);
            fieldsGoodAns.add(qwfieldi);
        }
        Separator separator2 = new Separator();
        separator2.setMinWidth(240);
        separator2.setMinHeight(20);
        qwpane.getChildren().add(separator2);
        for (int i = 0; i < message.getBadAnswers().size() ; i++) {
            TextField qwfieldi = new TextField();
            qwfieldi.textProperty().bindBidirectional(new SimpleStringProperty());
            qwpane.getChildren().add(qwfieldi);
            fieldsBadAns.add(qwfieldi);
        }
        Separator separator3 = new Separator();
        separator3.setMinWidth(240);
        separator3.setMinHeight(20);
        qwpane.getChildren().add(separator3);
        Button add = new Button();
        add.setMinWidth(120);
        add.setText("Добавить вопрос");
        add.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Question toAdd = new Question(qwfield.getText());
                for (int i=0;i<fieldsGoodAns.size();i++){
                    toAdd.getGoodAnswers().add(new SimpleStringProperty(fieldsGoodAns.get(i).getText()));
                }
                for (int j=0;j<fieldsBadAns.size();j++){
                    toAdd.getBadAnswers().add(new SimpleStringProperty(fieldsBadAns.get(j).getText()));
                }
                if(toAdd.addThisToDB()) {
                    System.out.println("Успешное полное добавление вопроса!");
                } else System.out.println("Не добавлено :(");
            }
        });
        qwpane.getChildren().add(add);
        mediator.setView(qwpane);
    }
}
