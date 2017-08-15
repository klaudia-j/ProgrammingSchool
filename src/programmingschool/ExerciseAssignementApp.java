package programmingschool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import programmingschool.model.Exercise;
import programmingschool.model.Group;
import programmingschool.model.Solution;
import programmingschool.model.User;

public class ExerciseAssignementApp {
	public static void main(String[] args) {
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false", "root", "");
			System.out.println("Witaj w module dodawania zadań użytkowniokm");
			Scanner scan = new Scanner(System.in);
			int action = 4;
			while (action != 0) {
				System.out.println();
				action = getAction(scan);
				switch(action) {
				case 1: addAssignement(scan, conn);
				break;
				case 2: viewSolutionsOfUser(scan, conn);
				break;
				default: break;
				}
				System.out.println();
			}
			System.out.println("Koniec pracy.");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void showMenu() {
		System.out.println("Wybierz jedną z opcji: ");
		System.out.println("1 - add - przypisanie zadania do użytkownika");
		System.out.println("2 - view - przeglądanie rozwiązań danego użytkownika");
		System.out.println("0 - quit - koniec pracy");	
	}

	private static int getAction(Scanner scan) {
		showMenu();
		while(! scan.hasNextInt()) {
			scan.next();
			showMenu();
		}
		int action = scan.nextInt();
		return action;
	}

	private static void showUserList(Connection conn) throws SQLException {
		System.out.println("Lista użytkowników");
		User[] users = User.loadAllUsers(conn);
		for (User u : users) {
			System.out.println(u.getId() + " | " + u.getUsername() + " | " + u.getEmail() + " | " + u.getPassword() + " | " + u.getGroupId());
		}
	}

	private static void showExerciseList(Connection conn) throws SQLException {
		System.out.println("Lista zadań");
		Exercise[] exercises = Exercise.loadAllExercises(conn);
		for (Exercise e : exercises) {
			System.out.println(e.getId() + " | " + e.getTitle() + " | " + e.getDescription());
		}
	}

	private static void addAssignement(Scanner scan, Connection conn) throws SQLException {
		showUserList(conn);
		System.out.println("Podaj id użytkownika: ");
		while(!scan.hasNextLong()) {
			scan.next();
			System.out.println("Podaj id użytkownika: ");
		}
		Long user_id = scan.nextLong();
		User user = User.loadById(conn, user_id);
		showExerciseList(conn);
		System.out.println("Podaj id zadania: ");
		while(!scan.hasNextInt()) {
			scan.next();
			System.out.println("Podaj id zadania: ");
		}
		int exercise_id = scan.nextInt();
		Exercise exercise = Exercise.loadById(conn, exercise_id);
		if ((! (user == null || exercise == null))) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			Solution solution = new Solution (dateFormat.format(date), null, null, exercise_id, user_id);
			solution.saveToDB(conn);
			System.out.println("Zakończono przypisywanie zadania.");
		} else {
			System.out.println("Podano nieprawidłowy id użytkownika lub nieprawidłowy id zadania.");
		}
	}

	private static void viewSolutionsOfUser(Scanner scan, Connection conn) throws SQLException {
		System.out.println("Lista użytkowników: ");
		showUserList(conn);
		while(!scan.hasNextLong()) {
			scan.next();
			System.out.println("Podaj id użytkownika, którego rozwiązania zadań chcesz zobaczyć: ");
		}
		Long user_id = scan.nextLong();
		User user = User.loadById(conn, user_id);
		if (! (user == null)) {
			Solution[] solutions = Solution.loadAllSolutionsByUserId(conn, user_id);
			for (Solution s : solutions) {
				System.out.println(s.getId() + " | " + s.getCreated() + " | " + s.getUpdated() + " | " + s.getDescription() + " | " + s.getExercise_id());
			}
		} else {
			System.out.println("Nie ma takiego użytkownika.");
		}
	}

}
