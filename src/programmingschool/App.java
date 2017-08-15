package programmingschool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import programmingschool.model.User;

public class App {

	public static void main(String[] args) {
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false", "root", "");
			createUser(conn);
//			getUser(conn);
//			loadUsers(conn);
//			updateUser(conn);
//			deleteUser(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getUser(Connection conn) throws SQLException {
		User user = User.loadById(conn, 2l);
		System.out.println(user.getId());
		System.out.println(user.getUsername());
		System.out.println(user.getEmail());
		System.out.println(user.getPassword());
	}

	public static void createUser(Connection conn) throws SQLException {
		User user = new User ("user1", "email1@g.com", "qwerty");
		System.out.println(user.getId());
		System.out.println(user.getUsername());
		System.out.println(user.getEmail());
		System.out.println(user.getPassword());
		user.saveToDB(conn);
	}
	
	public static void loadUsers(Connection conn) throws SQLException {
		for (User user : User.loadAllUsers(conn)) {
			System.out.println(user.getId() + " | " + user.getUsername() + " | " + user.getEmail() + " | " + user.getPassword());
		}
	}
	
	public static void updateUser(Connection conn) throws SQLException {
		User user = User.loadById(conn, 1l);
		user.setUsername("Filip");
		user.saveToDB(conn);
	}
	
	public static void deleteUser(Connection conn) throws SQLException {
		User user = User.loadById(conn, 1l);
		user.delete(conn);
	}

}

