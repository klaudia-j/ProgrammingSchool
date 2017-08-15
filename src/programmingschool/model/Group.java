package programmingschool.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Group {
	private static final int UNSAVED_ID = 0;
	private static final String TABLE = "user_group";
	private int id = UNSAVED_ID;
	private String name;
	
	public Group() {
		
	}
	
	public Group(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}
	
	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == UNSAVED_ID) {
			final String sql = "INSERT INTO " + TABLE + " VALUES (default, ?);";
			final String[] generatedColumns = {"id"};
			PreparedStatement ps = conn.prepareStatement(sql, generatedColumns);
			ps.setString(1, this.name);
			ps.executeUpdate();
			ResultSet generated = ps.getGeneratedKeys();
			if (generated.next()) {
				this.id = generated.getInt(1);
			} 
			generated.close();
			ps.close();
			System.out.println(id);
		} else {
			String sql = "UPDATE " + TABLE + " SET name=? where id = ?"; 
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql); 
			preparedStatement.setString(1, this.name);
			preparedStatement.setInt(2, this.id); 
			preparedStatement.executeUpdate();
		}
	}
	
	public static Group loadById(Connection conn, final int id) throws SQLException {
		Group group = null;
		final String sql = "SELECT name FROM " + TABLE + " WHERE id = ?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			String name = rs.getString("name");
			group = new Group(name);
			group.id = id;
		} 
		return group;
	}
	
	public static Group[] loadAllGroups(Connection conn) throws SQLException {
		ArrayList<Group> groups = new ArrayList<Group>();
		String sql = "SELECT * FROM " + TABLE;
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql); 
		ResultSet resultSet = preparedStatement.executeQuery(); 
		while (resultSet.next()) {
			Group loadedGroup = new Group();
			loadedGroup.id = resultSet.getInt("id"); 
			loadedGroup.name = resultSet.getString("name"); 
			groups.add(loadedGroup);
		}
		Group[] gArray = new Group[groups.size()]; 
		gArray = groups.toArray(gArray);
		return gArray;
	}
	
	public void delete(Connection conn) throws SQLException { 
		if (this.id != UNSAVED_ID) {
			String sql = "DELETE FROM " + TABLE + " WHERE id= ?"; 
			PreparedStatement preparedStatement; 
			preparedStatement = conn.prepareStatement(sql); 
			preparedStatement.setInt(1, this.id); 
			preparedStatement.executeUpdate();
			this.id = UNSAVED_ID; 
		}
	}
	
}
