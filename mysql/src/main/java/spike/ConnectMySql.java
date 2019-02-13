package spike;

import java.sql.*;

public class ConnectMySql {
    public static void main(String[] args) throws Exception {
        ConnectMySql dao = new ConnectMySql();
        dao.readDataBase();
    }

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://172.17.0.2:3306/firstdb";

    static final String USER = "user";
    static final String PASS = "pass123";


    public void readDataBase() throws Exception {
        readDataBase("SELECT person_id, first_name, last_name, address, city FROM persons");
    }

    private void readDataBase(String sql) throws ClassNotFoundException, SQLException {
        // This will load the MySQL driver, each DB has its own driver
        Class.forName(JDBC_DRIVER);

        // Setup the connection with the DB
        System.out.println("Connecting to database...");
        try (Connection connect = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // Statements allow to issue SQL queries to the database
            try (Statement statement = connect.createStatement()) {
                // Result set get the result of the SQL query
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    writeMetaData(resultSet);
                    writeResultSet(resultSet);
                }
            }
        }
    }

    private void writeMetaData(ResultSet resultSet) throws SQLException {
        //  Now get some metadata from the database
        // Result set get the result of the SQL query

        System.out.println("The columns in the table are: ");

        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
        }
    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);
            displayFields(resultSet, "person_id", "first_name", "last_name", "address", "city");

        }
    }

    private void displayFields(ResultSet resultSet, String... fields) throws SQLException {
        for (String field : fields) {
            displayField(field, resultSet);
        }
    }

    private void displayField(String field, ResultSet resultSet) throws SQLException {
        System.out.println(field + ":" + resultSet.getString(field));
    }

}
