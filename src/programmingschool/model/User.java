package programmingschool.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import programmingschool.org.mindrot.jbcrypt.BCrypt;

public class User {
	private static final int UNSAVED_ID = 0;
	private static final String TABLE = "users";
	private Long id = new Long(UNSAVED_ID);
	private String username;
	private String email;
	private String password;
	private Integer groupId = new Integer(0);

	public User() {

	}

	public User(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.setPassword(password);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Long getId() {
		return id;
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == UNSAVED_ID) {
			final String sql = "INSERT INTO " + TABLE + " VALUES (default, ?, ?, ?);";
			final String[] generatedColumns = {"id"};
			PreparedStatement ps = conn.prepareStatement(sql, generatedColumns);
			ps.setString(1, this.username);
			ps.setString(2, this.email);
			ps.setString(3, this.password);
			ps.executeUpdate();
			ResultSet generated = ps.getGeneratedKeys();
			if (generated.next()) {
				this.id = generated.getLong(1);
			} 
			generated.close();
			ps.close();
			System.out.println(id);
		} else {
			String sql = "UPDATE Users SET username=?, email=?, password=? where id = ?"; 
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql); 
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, this.email); 
			preparedStatement.setString(3, this.password); 
			preparedStatement.setLong(4, this.id); 
			preparedStatement.executeUpdate();
		}
	}

	public static User loadById(Connection conn, final Long id) throws SQLException {
		User user = null;
		final String sql = "SELECT username, email, password FROM " 
				+ TABLE + " WHERE id = ?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			String username = rs.getString("username");
			String email = rs.getString("email");
			String password = rs.getString("password");
			user = new User(username, email, password);
			user.id = id;
		} 
		return user;
	}

	public static User[] loadAllUsers(Connection conn) throws SQLException {
		ArrayList<User> users = new ArrayList<User>();
		String sql = "SELECT * FROM Users";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql); 
		ResultSet resultSet = preparedStatement.executeQuery(); 
		while (resultSet.next()) {
			User loadedUser = new User();
			loadedUser.id = resultSet.getLong("id"); 
			loadedUser.username = resultSet.getString("username"); 
			loadedUser.password = resultSet.getString("password"); 
			loadedUser.email = resultSet.getString("email"); 
			users.add(loadedUser);
		}
		User[] uArray = new User[users.size()]; 
		uArray = users.toArray(uArray);
		return uArray;
	}

	public void delete(Connection conn) throws SQLException { 
		if (this.id != UNSAVED_ID) {
			String sql = "DELETE FROM Users WHERE id= ?"; 
			PreparedStatement preparedStatement; 
			preparedStatement = conn.prepareStatement(sql); 
			preparedStatement.setLong(1, this.id); 
			preparedStatement.executeUpdate();
			this.id = (long) UNSAVED_ID; 
		}
	}
}
