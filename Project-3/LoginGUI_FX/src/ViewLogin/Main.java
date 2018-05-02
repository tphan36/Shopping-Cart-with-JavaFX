package ViewLogin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/ViewLogin/Login.fxml"));
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setHeight(570);
			primaryStage.setWidth(850);
			primaryStage.setResizable(true);
			primaryStage.show();
			System.out.println();
			// LoginController.LoginButton.setOnAction(e -> Window.setScene(scene));
			System.out.println("Tuan check commit");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void connectToDB() {
		Connection connection = null;
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:shoppingCartDb.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.
			String personTableQueryString = "create table if not exists personTable (" + "firstName string,"
					+ "lastName string, userName string primary key," + "email string, password string)";
			statement.executeUpdate(personTableQueryString);

			Statement statementBookTable = connection.createStatement();
			String bookTableQuery = "create table if not exists bookTable (" + "isbn int," + "book string,"
					+ "author string," + "edition int," + "year int," + "price float," + "soldBy string default 'Bookstore')";
			statementBookTable.executeUpdate(bookTableQuery);

			String cartTableQuery = "create table if not exists cartTable (" + "username string," + "isbn int,"
					+ "book string," + "author string," + "edition int," + "year int," + "price float," + "soldBy string,"
					+ "foreign key (username) references personTable(username))";
			statement.executeUpdate(cartTableQuery);
			
			String orderedQuery = "create table if not exists orderedTable (" + "username string," + "isbn int,"
					+ "book string," + "author string," + "edition int," + "year int," + "price float,"  + "soldBy string,"
					+ "foreign key (username) references personTable(username))";
			statement.executeUpdate(orderedQuery);

			Statement statementPaymentTable = connection.createStatement();
			String paymentTableQuery = "create table if not exists paymentTable (" + "username string," + "paymentType string,"
					+ "creditNumber string," + "expireDateMM int," + "expireDateYY int," + "firstname string," + "lastname string,"
					+ "billingAddress1 string," + "billingAddress2 string," + "city string," + "state string," + "zipcode int," + "phone string)";
			statementPaymentTable.executeUpdate(paymentTableQuery);


		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e);
			}
		}
	}

	public static void main(String[] args) {

		connectToDB();
		launch(args);
	}
}
