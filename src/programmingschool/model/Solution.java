package programmingschool.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Solution {
	
	private static final int UNSAVED_ID = 0;
	private static final String TABLE = "solution";
	private int id = UNSAVED_ID;
	private String created;
	private String updated;
	private String description;
	private int exercise_id;
	private Long users_id;

	public Solution() {

	}

	public Solution(String created, String updated, String description, int exercise_id, Long users_id) {
		super();
		this.created = created;
		this.updated = updated;
		this.description = description;
		this.exercise_id = exercise_id;
		this.users_id = users_id;
	}


	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExercise_id() {
		return exercise_id;
	}

	public void setExercise_id(int exercise_id) {
		this.exercise_id = exercise_id;
	}

	public Long getUsers_id() {
		return users_id;
	}

	public void setUsers_id(Long users_id) {
		this.users_id = users_id;
	}

	public int getId() {
		return id;
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == UNSAVED_ID) {
			final String sql = "INSERT INTO " + TABLE + " VALUES (default, ?, ?, ?, ?, ?);";
			final String[] generatedColumns = {"id"};
			PreparedStatement ps = conn.prepareStatement(sql, generatedColumns);
			ps.setString(1, this.created);
			ps.setString(2, this.updated);
			ps.setString(3, this.description);
			ps.setInt(4, this.exercise_id);
			ps.setLong(5, this.users_id);
			ps.executeUpdate();
			ResultSet generated = ps.getGeneratedKeys();
			if (generated.next()) {
				this.id = generated.getInt(1);
			} 
			generated.close();
			ps.close();
			System.out.println(id);
		} else {
			String sql = "UPDATE " + TABLE + " SET created=?, updated=?, description =?, exercise_id=?, users_id=? WHERE id = ?"; 
			PreparedStatement ps;
			ps = conn.prepareStatement(sql); 
			ps.setString(1, this.created);
			ps.setString(2, this.updated);
			ps.setString(3, this.description);
			ps.setInt(4, this.exercise_id);
			ps.setLong(5, this.users_id);
			ps.setInt(6, this.id); 
			ps.executeUpdate();
		}
	}

	public static Solution loadById(Connection conn, final int id) throws SQLException {
		Solution solution = null;
		final String sql = "SELECT created, updated, description, exercise_id, users_id FROM " + TABLE + " WHERE id = ?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			String created = rs.getString("created");
			String updated = rs.getString("updated");
			String description = rs.getString("description");
			int exercise_id = rs.getInt("exercise_id");
			Long users_id = rs.getLong("users_id");
			solution = new Solution(created, updated, description, exercise_id, users_id);
			solution.id = id;
		} 
		return solution;
	}

	public static Solution[] loadAllSolutions(Connection conn) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		String sql = "SELECT * FROM " + TABLE;
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql); 
		ResultSet resultSet = preparedStatement.executeQuery(); 
		while (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = resultSet.getInt("id"); 
			loadedSolution.created = resultSet.getString("created");
			loadedSolution.updated = resultSet.getString("updated");
			loadedSolution.description = resultSet.getString("description");
			loadedSolution.exercise_id = resultSet.getInt("exercise_id");
			loadedSolution.users_id = resultSet.getLong("users_id");
			solutions.add(loadedSolution);
		}
		Solution[] sArray = new Solution[solutions.size()]; 
		sArray = solutions.toArray(sArray);
		return sArray;
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
	
	public static Solution[] loadAllSolutionsByUserId(Connection conn, Long users_id) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		String sql = "SELECT * FROM " + TABLE + " WHERE users_id = ?;";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql); 
		preparedStatement.setLong(1, users_id);
		ResultSet resultSet = preparedStatement.executeQuery(); 
		while (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = resultSet.getInt("id"); 
			loadedSolution.created = resultSet.getString("created");
			loadedSolution.updated = resultSet.getString("updated");
			loadedSolution.description = resultSet.getString("description");
			loadedSolution.exercise_id = resultSet.getInt("exercise_id");
			solutions.add(loadedSolution);
		}
		Solution[] sArray = new Solution[solutions.size()]; 
		sArray = solutions.toArray(sArray);
		return sArray;
	}
	
	public static Solution[] loadAllSolutionsByExerciseId(Connection conn, int exercise_id) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		String sql = "SELECT * FROM " + TABLE + " WHERE exercise_id = ?;";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql); 
		preparedStatement.setLong(1, exercise_id);
		ResultSet resultSet = preparedStatement.executeQuery(); 
		while (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = resultSet.getInt("id"); 
			loadedSolution.created = resultSet.getString("created");
			loadedSolution.updated = resultSet.getString("updated");
			loadedSolution.description = resultSet.getString("description");
			loadedSolution.users_id = resultSet.getLong("users_id");
			solutions.add(loadedSolution);
		}
		Solution[] sArray = new Solution[solutions.size()]; 
		sArray = solutions.toArray(sArray);
		return sArray;
	}


}
