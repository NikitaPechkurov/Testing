package connection;

import com.sun.rowset.CachedRowSetImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Question;

import java.io.IOException;
import java.sql.*;

public class DBConnect {
    private static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_URL = "jdbc:sqlserver://localhost\\SQLEXPRESS:1890;databaseName=tests;user=sa;password=12345";
    private static final String DB_USER = "sa";//sa
    private static final String DB_PASSWORD = "12345";//12345
    public static final String DBName = "tests";

    public static final class nameColTables{
        public static final String QUESTIONS = "Questions";
        public static final String ANSWERS = "Answers";
        public static final String QUESTIONS_ANSWERS= "Questions-Answers";
    }

    public static final class nameColQuestions {
        public static final String ID = "id";
        public static final String QUESTION = "Question";
    }

    public static final class nameColAnswers{
        public static final String ID = "id";
        public static final String ANSWER = "Answer";
        public static final String TYPE = "Type";
    }

    public static final class nameColQuestions_Answers{
        public static final String ID = "id";
        public static final String ID_QUESTION = "id_question";
        public static final String ID_ANSWER = "id_answer";
    }


    //создание соединения
    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("\r\nОшибка: " + e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database CONNECTED!");
            return dbConnection;
        } catch (SQLException e) {
            System.out.println("Ошибка: " + e.getMessage() + ". Method: getDBConnection()");
        }
        return dbConnection;
    }


    //createDbUserTable() - База Данных уже была создана


    //простой запрос к БД.  Вернет cachedResultSet
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        //Declare statement, resultSet and CachedResultSet as null
        Connection dbConnection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnection = getDBConnection();
            System.out.println("Select statement: " + queryStmt + "\n");
            //Create statement
            statement = dbConnection.createStatement();
            //Execute select (query) operation
            resultSet = statement.executeQuery(queryStmt);
            //CachedRowSet Implementation
            //In order to prevent "java.sql.SQLRecoverableException:Closed Connection: next" error
            //We are using CachedRowSet
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e + ". Method: dbExecuteQuery()");
            throw e;
        } finally { //закрыли соединения и данные
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (statement != null) {
                //Close Statement
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
        //Return CachedRowSet
        return crs;
    }

    //обновление БД. Ничего не вернет
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Connection dbConnection = null;
        Statement statement = null;
        try {
            //Connect to DB (Establish Oracle Connection)
            dbConnection = getDBConnection();
            //Create Statement
            statement = dbConnection.createStatement();
            //Run executeUpdate operation with given sql statement
            statement.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e + ". Method: dbExecuteUpdate()");
            throw e;
        } finally {
            if (statement != null) {
                //Close statement
                statement.close();
            }
            //Close connection
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    //данный метод заполняет массив вопросов с помощью переданного sql-запроса
    //переделать этот метод под сообщения в БД (кто отправил еще поле)
    public ObservableList<Question> createQuestions(String query) throws SQLException, IOException {

        ObservableList<Question> questions = FXCollections.observableArrayList();
        Connection dbConnection = null;
        Statement statement = null;
        // String selectTableSQL = "SELECT USER_ID, USERNAME from DBUSER";
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            // выбираем данные с БД
            ResultSet rs = statement.executeQuery(query);
            // И, если что то было получено, то цикл while сработает
            while (rs.next()) {
                Question question = new Question(rs.getString(2));
                //question.add
                questions.add(question);
            }
        } catch (SQLException e) {
            System.out.println("Ошиюка: " + e.getMessage() + ". Method: create()");
        }
        return questions;
    }

    //данный метод обновляет данные в БД. Использовать после каждого добавления нового сообщения в массив
    /*public void update(ObservableList<Message> messages) {

        Connection dbConnection = null;
        Statement statement = null;
        // String selectTableSQL = "SELECT USER_ID, USERNAME from DBUSER";
        if (messages.isEmpty()) return;
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    nameColTables.MESSAGES + "VALUES"+ messages.get(messages.size()-1).getId()+ messages.get(messages.size()-
                    1).getId_user()+messages.get(messages.size()-1).getId_slide()+messages.get(messages.size()-1).getMessage()
                    + messages.get(messages.size()- 1).getType());
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }*/


}
