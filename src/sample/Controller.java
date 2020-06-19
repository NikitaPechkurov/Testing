package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.Mediator;
import model.*;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Controller implements Initializable, Mediator {
    public Pane viewPane;
    public TextField login;
    private HashMap<String, Colleague> id = new HashMap<>();
    private Colleague currentColleague;
    private Question baseTest = new Question("");

    public void onStart(ActionEvent actionEvent) {
        try {
            currentColleague = id.get(login.getText());
            if (currentColleague == null) currentColleague = id.get("1");
            currentColleague.receive(baseTest.getTest());
            currentColleague.notifyColleague(currentColleague.getReceivedMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.put("admin", new Creator(this));//создатель вопросов
        id.put("student", new Worker(this));//проходящий тест
        id.put("lector", new Editor(this));//редактор вопросов
        id.put("1", new Viewer(this));//просматривающий тест
    }

    public void setView(Node control) {
        viewPane.getChildren().add(control);
    }
}
