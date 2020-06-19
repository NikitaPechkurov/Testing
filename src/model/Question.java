package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.awt.image.ImagingOpException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import connection.DAOTest;

public class Question {
    StringProperty question;
    ArrayList<StringProperty> answergood;
    ArrayList<StringProperty> badanswer;
    Integer type;//категория вопроса
    int question_id = 1;

    public Question(String qw) {
        question = new SimpleStringProperty(qw);
        answergood = new ArrayList<>();
        badanswer = new ArrayList<>();
    }

    public int addTrue(String s) {
        answergood.add(new SimpleStringProperty(s));
        return answergood.size();
    }

    public int addFalse(String s) {
        badanswer.add(new SimpleStringProperty(s));
        return badanswer.size();
    }

    public ArrayList<StringProperty> getGoodAnswers() {
        return answergood;
    }

    public void setQuestion(String question) {
        this.question.set(question);
    }

    public ArrayList<StringProperty> getBadAnswers(){
        return badanswer;
    }

    public StringProperty getQuestionProperty(){
        return question;
    }

    public Question getTest() throws SQLException, ClassNotFoundException, IOException{
        question_id = getRandomNumber(1,7);
        question = DAOTest.searchQuestion(String.valueOf(question_id)).getQuestionProperty();
        answergood = DAOTest.getGoodAnswersToQuestion(String.valueOf(question_id));
        badanswer = DAOTest.getBadAnswersToQuestion(String.valueOf(question_id));
        return this;
    }

    public void setGoodAnswers(ArrayList<StringProperty> A){
        this.answergood = A;
    }

    public void setBadAnswers(ArrayList<StringProperty> B){
        this.badanswer = B;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public boolean addThisToDB(){
        try {
            DAOTest.insertQuestion(this);
            return true;
        } catch (SQLException e){
            System.out.println("SQLEx in addThisToDB: "+e);
            return false;
        } catch (ClassNotFoundException e){
            System.out.println("CLNotFndEx in addThisToDB: "+e);
            return false;
        }
    }

    public int getQuestion_id(){
        return question_id;
    }

    public boolean updateThisToDB(){
        try {
            DAOTest.updateQuestion(this,String.valueOf(getQuestion_id()));
            return true;
        } catch (SQLException e){
            System.out.println("SQLEx in updateThisToDB: "+e);
            return false;
        } catch (ClassNotFoundException e){
            System.out.println("CLNotFndEx in updateThisToDB: "+e);
            return false;
        }
    }
}