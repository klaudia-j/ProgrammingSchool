package programmingschool.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Exercise {
	private static final int UNSAVED_ID = 0;
	private static final String TABLE = "exercise";
	private int id = UNSAVED_ID;
	private String title;
	private String description;

	public Exercise() {

	}

	public Exercise(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == UNSAVED_ID) {
			final String sql = "INSERT INTO " + TABLE + " VALUES (default, ?, ?);";
			final String[] generatedColumns = {"id"};
			PreparedStatement ps = conn.prepareStatement(sql, generatedColumns);
			ps.setString(1, this.title);
			ps.setString(2, this.description);
			ps.executeUpdate();
			ResultSet generated = ps.getGeneratedKeys();
			if (generated.next()) {
				this.id = generated.getInt(1);
			} 
			generated.close();
			ps.close();
			System.out.println(id);
		} else {
			String sql = "UPDATE " + TABLE + " SET title=?, description =? WHERE id = ?"; 
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql); 
			preparedStatement.setString(1, this.title);
			preparedStatement.setString(2, this.description);
			preparedStatement.setInt(3, this.id); 
			preparedStatement.executeUpdate();
		}
	}

	public static Exercise loadById(Connection conn, final int id) throws SQLException {
		Exercise exercise = null;
		final String sql = "SELECT title, description FROM " + TABLE + " WHERE id = ?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			String title = rs.getString("title");
			String description = rs.getString("description");
			exercise = new Exercise(title, description);
			exercise.id = id;
		} 
		return exercise;
	}

	public static Exercise[] loadAllExercises(Connection conn) throws SQLException {
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		String sql = "SELECT * FROM " + TABLE;
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql); 
		ResultSet resultSet = preparedStatement.executeQuery(); 
		while (resultSet.next()) {
			Exercise loadedExercise = new Exercise();
			loadedExercise.id = resultSet.getInt("id"); 
			loadedExercise.title = resultSet.getString("title");
			loadedExercise.description = resultSet.getString("description");
			exercises.add(loadedExercise);
		}
		Exercise[] eArray = new Exercise[exercises.size()]; 
		eArray = exercises.toArray(eArray);
		return eArray;
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
