package connection;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Question;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOTest {

    public static Question searchQuestion (String question_id) throws SQLException,
            ClassNotFoundException, IOException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM Questions WHERE id='"+question_id+"';";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsQue = DBConnect.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeFromResultSet method and get employee object
            Question que = new Question("1");
            while (rsQue.next()) {
                que = getQuestionFromResultSet(rsQue);
                que.setGoodAnswers(getGoodAnswersToQuestion(question_id));
                que.setBadAnswers(getBadAnswersToQuestion(question_id));
            }
            //Return employee object
            return que;
        } catch (SQLException e) {
            System.out.println("While searching an question with " + question_id + " question_id, an error occurred: " + e
                    + ". Method: searchMessage()");
            //Return exception
            throw e;
        }
    }

    public static String searchQuestionId (String question) throws SQLException,
            ClassNotFoundException, IOException {
        //Declare a SELECT statement
        String selectStmt = "SELECT id FROM Questions WHERE Question='"+question+"';";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsQue = DBConnect.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeFromResultSet method and get employee object
            String res="";
            while (rsQue.next()) {
                res = rsQue.getString(1);
            }
            //Return employee object
            return res;
        } catch (SQLException e) {
            System.out.println("While searching an question: " + question + ", an error occurred: " + e
                    + ". Method: searchQuestionId()");
            //Return exception
            throw e;
        }
    }

    public static String searchMaxAnswersId () throws SQLException,
            ClassNotFoundException, IOException {
        //Declare a SELECT statement
        String selectStmt = "SELECT MAX(id) FROM Answers;";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsQue = DBConnect.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeFromResultSet method and get employee object
            String res="";
            while (rsQue.next()) {
                res = rsQue.getString(1);
            }
            //Return employee object
            return res;
        } catch (SQLException e) {
            System.out.println("While searching an max answer id, an error occurred: " + e
                    + ". Method: searchMaxAnswersId()");
            //Return exception
            throw e;
        }
    }

    //сформировать экземпляр сообщения из вернувшегося ответа от БД
    private static Question getQuestionFromResultSet(ResultSet rs) throws SQLException, IOException {
        Question que = new Question("1");
        que.setQuestion(rs.getString(DBConnect.nameColQuestions.QUESTION));
        return que;
    }

    private static String getAnswerFromResultSet(ResultSet rs) throws SQLException, IOException {
        String que;
        que = rs.getString(DBConnect.nameColAnswers.ANSWER);
        return que;
    }

    public static ArrayList<StringProperty> getGoodAnswersToQuestion(String id_question) throws SQLException,ClassNotFoundException,IOException{
        ArrayList<StringProperty> answers = new ArrayList<>();
        String selectStmt = "SELECT * from Answers WHERE id_question='"+id_question+"' " +
                "AND Type='good';";
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsAns = DBConnect.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeFromResultSet method and get employee object
            while (rsAns.next()) {
                String ans = getAnswerFromResultSet(rsAns);
                answers.add(new SimpleStringProperty(ans));
            }
            //Return employee object
            return answers;
        } catch (Exception e) {
            System.out.println("While searching an question with " + id_question + " id_question, an error occurred: " + e
                    + ". Method: searchMessage()");
            //Return exception
            throw e;
        }
    }

    public static ArrayList<StringProperty> getBadAnswersToQuestion(String id_question) throws SQLException,ClassNotFoundException,IOException{
        ArrayList<StringProperty> answers = new ArrayList<>();
        String selectStmt = "SELECT * from Answers WHERE id_question='"+id_question+"' " +
                "AND Answers.Type='bad';";
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsAns = DBConnect.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeFromResultSet method and get employee object
            while (rsAns.next()) {
                String ans = getAnswerFromResultSet(rsAns);
                answers.add(new SimpleStringProperty(ans));
            }
            //Return employee object
            return answers;
        } catch (Exception e) {
            System.out.println("While searching an question with " + id_question + " id_question, an error occurred: " + e
                    + ". Method: searchMessage()");
            //Return exception
            throw e;
        }
    }

    public static void insertQuestion(Question ques) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        try {
            //вставляем вопрос
            String updateStmt = "INSERT INTO Questions (Question) VALUES ('"+ques.getQuestionProperty().getValue().toString()+"');";
            DBConnect.dbExecuteUpdate(updateStmt);
            System.out.println("DAO: вопрос добавлен");
            //вставляем все хорошие ответы в третью таблицу (id вопроса по вопросу, id ответа - последнее вставленное из списка)
            for (int i=0;i<ques.getGoodAnswers().size();i++) {
                updateStmt = "INSERT INTO Answers (id_question,Answer,Type) VALUES ('"+searchQuestionId(ques.getQuestionProperty().getValue().toString())+"','"+ques.getGoodAnswers().get(i).getValue().toString()+
                        "','good');";
                DBConnect.dbExecuteUpdate(updateStmt);
                System.out.println("DAO: Хороший ответ добавлен!");
            }
            //и плохие вопросы
            for (int i=0;i<ques.getBadAnswers().size();i++){
                updateStmt = "INSERT INTO Answers (id_question,Answer,Type) VALUES ('"+searchQuestionId(ques.getQuestionProperty().getValue().toString())+"','"+ques.getBadAnswers().get(i).getValue().toString()+
                        "','bad');";
                DBConnect.dbExecuteUpdate(updateStmt);
                System.out.println("DAO: Плохой ответ добавлен!");
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e + ". Method: insertMessage()");
            throw e;
        } catch (IOException e){
            System.out.println("Error occurred while INSERT Operation: " + e + ". Method: insertMessage()");
        }
    }

    public static void updateQuestion(Question ques, String ques_num) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        try {
            //вставляем вопрос
            String updateStmt = "UPDATE Questions SET Question='"+ques.getQuestionProperty().getValue().toString()+"' WHERE id='"+ques_num+"';";
            DBConnect.dbExecuteUpdate(updateStmt);
            System.out.println("DAO: вопрос под номером "+ques_num+" обновлен");
            updateStmt = "DELETE FROM Answers WHERE id_question='"+ques_num+"';";
            DBConnect.dbExecuteUpdate(updateStmt);
            //вставляем все хорошие ответы в третью таблицу (id вопроса по вопросу, id ответа - последнее вставленное из списка)
            for (int i=0;i<ques.getGoodAnswers().size();i++) {
                updateStmt = "INSERT INTO Answers (id_question,Answer,Type) VALUES ('"+ques_num+"','"+ques.getGoodAnswers().get(i).getValue().toString()+
                        "','good');";
                DBConnect.dbExecuteUpdate(updateStmt);
                System.out.println("DAO: Хороший ответ добавлен!");
            }
            //и плохие вопросы
            for (int i=0;i<ques.getBadAnswers().size();i++){
                updateStmt = "INSERT INTO Answers (id_question,Answer,Type) VALUES ('"+ques_num+"','"+ques.getBadAnswers().get(i).getValue().toString()+
                        "','bad');";
                DBConnect.dbExecuteUpdate(updateStmt);
                System.out.println("DAO: Плохой ответ добавлен!");
            }
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e + ". Method: insertMessage()");
            throw e;
        }
    }
}
