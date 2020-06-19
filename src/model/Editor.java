package model;

import connection.DAOTest;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Editor extends Colleague {//редактор (им является лектор)

    public Editor(Mediator mediator) {
        super(mediator);
    }

    public void notifyColleague(Question message) {//переделать размеры, добавить применение редаченья
        ArrayList<TextField> fieldsGoodAns = new ArrayList<>();
        ArrayList<TextField> fieldsBadAns = new ArrayList<>();
        VBox qwpane = new VBox();
        TextField qwfield = new TextField();
        qwfield.textProperty().bindBidirectional(message.getQuestionProperty());
        qwpane.getChildren().add(qwfield);
        Separator separator = new Separator();
        separator.setMaxWidth(240);
        separator.setMinHeight(20);
        qwpane.getChildren().add(separator);
        for (int i = 0; i < message.getGoodAnswers().size() ; i++) {
            TextField qwfieldi = new TextField();
            qwfieldi.textProperty().bindBidirectional(message.getGoodAnswers().get(i));
            qwpane.getChildren().add(qwfieldi);
            fieldsGoodAns.add(qwfieldi);
        }
        for (int i = 0; i < message.getBadAnswers().size() ; i++) {
            TextField qwfieldi = new TextField();
            qwfieldi.textProperty().bindBidirectional(message.getBadAnswers().get(i));
            qwpane.getChildren().add(qwfieldi);
            fieldsBadAns.add(qwfieldi);
        }
        Separator separator2 = new Separator();
        separator2.setMaxWidth(240);
        separator2.setMinHeight(20);
        qwpane.getChildren().add(separator2);
        Button add = new Button();
        add.setText("Подтвердить редактирование");
        add.setMinWidth(120);
        add.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                message.setQuestion(qwfield.getText());
                ArrayList<StringProperty> ga = new ArrayList<>();
                for (int i=0;i<fieldsGoodAns.size();i++){
                    ga.add(new SimpleStringProperty(fieldsGoodAns.get(i).getText()));
                }
                ArrayList<StringProperty> ba = new ArrayList<>();
                for (int j=0;j<fieldsBadAns.size();j++){
                    ba.add(new SimpleStringProperty(fieldsBadAns.get(j).getText()));
                }
                message.setGoodAnswers(ga);message.setBadAnswers(ba);
                if(message.updateThisToDB()) {
                    System.out.println("Успешное обновление вопроса (с ответами)!");
                } else System.out.println("Вопрос не обновлен :(");
            }
        });
        qwpane.getChildren().add(add);
        mediator.setView(qwpane);
    }
}
