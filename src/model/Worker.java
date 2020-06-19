package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Worker extends Colleague {

    boolean flagRight;
    public Worker(Mediator mediator) {
        super(mediator);
        flagRight = false;
    }

    public void notifyColleague(Question message) {
        ArrayList<CheckBox> fieldsGoodAns = new ArrayList<>();
        ArrayList<CheckBox> fieldsBadAns = new ArrayList<>();
        VBox qwpane = new VBox();
        qwpane.setPadding(new Insets(0, 50, 0, 10));
        qwpane.setSpacing(10);
        Label qwfield = new Label();
        qwfield.textProperty().bindBidirectional(message.getQuestionProperty());
        qwpane.getChildren().add(qwfield);
        Separator separator = new Separator();
        separator.setMaxWidth(240);
        separator.setMinHeight(20);
        qwpane.getChildren().add(separator);
        for (int i = 0; i < message.getGoodAnswers().size() ; i++) {
            CheckBox qwfieldi = new CheckBox();
            qwfieldi.textProperty().bindBidirectional(message.getGoodAnswers().get(i));
            qwpane.getChildren().add(qwfieldi);
            fieldsGoodAns.add(qwfieldi);
        }
        for (int i = 0; i < message.getBadAnswers().size() ; i++) {
            CheckBox qwfieldi = new CheckBox();
            qwfieldi.textProperty().bindBidirectional(message.getBadAnswers().get(i));
            qwpane.getChildren().add(qwfieldi);
            fieldsBadAns.add(qwfieldi);
        }
        Separator separator2 = new Separator();
        separator2.setMaxWidth(240);
        separator2.setMinHeight(20);
        qwpane.getChildren().add(separator2);
        Button add = new Button();
        add.setText("Подтвердить результат");
        add.setMinWidth(120);
        add.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for (int i=0;i<fieldsGoodAns.size();i++){
                    if (fieldsGoodAns.get(i).isSelected()){
                        flagRight = true;
                    }
                }
                for (int j=0;j<fieldsBadAns.size();j++){
                    if (fieldsBadAns.get(j).isSelected()){
                        flagRight = false;
                    }
                }
                VBox qw2 = new VBox();
                TextField t = new TextField();
                if(flagRight) {
                    t.setText("ВЫ ПРАВИЛЬНО ОТВЕТИЛИ НА ВОПРОС");
                } else {t.setText("!ОШИБКА!");}
                qw2.getChildren().add(t);
                mediator.setView(qw2);//добавляем представление в view
            }
        });
        qwpane.getChildren().add(add);
        mediator.setView(qwpane);
    }
}
