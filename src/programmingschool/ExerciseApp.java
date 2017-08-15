package programmingschool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import programmingschool.model.Exercise;

public class ExerciseApp {

	public static void main(String[] args) {
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false", "root", "");
			System.out.println("Witaj w module Exercise");
			Scanner scan = new Scanner(System.in);
			int action = 4;
			while (action != 0) {
				showExerciseList(conn);
				System.out.println();
				action = getAction(scan);
				switch(action) {
				case 1: addExercise(scan, conn);
				break;
				case 2: editExercise(scan, conn);
				break;
				case 3: deleteExercise(scan, conn);
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
		System.out.println("1 - add - dodanie zadania");
		System.out.println("2 - edit - edycja zadania");
		System.out.println("3 - delete - usunięcie zadania");
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

	private static void showExerciseList(Connection conn) throws SQLException {
		System.out.println("Lista zadań");
		Exercise[] exercises = Exercise.loadAllExercises(conn);
		for (Exercise e : exercises) {
			System.out.println(e.getId() + " | " + e.getTitle() + " | " + e.getDescription());
		}
	}

	private static void addExercise(Scanner scan, Connection conn) throws SQLException {
		System.out.println("Podaj tytuł: ");
		String title = scan.next();
		System.out.println("Podaj opis: ");
		String description = scan.next();
		Exercise exercise = new Exercise (title, description);
		exercise.saveToDB(conn);
		System.out.println("Zakończono dodawanie zadania.");
	}

	private static void editExercise(Scanner scan, Connection conn) throws SQLException {
		System.out.println("Podaj id zadania, które zostanie zmodyfikowany: ");
		while(!scan.hasNextInt()) {
			scan.next();
			System.out.println("Podaj id zadania: ");
		}
		int id = scan.nextInt();
		Exercise exercise = Exercise.loadById(conn, id);
		if (! (exercise == null)) {
			System.out.println("Podaj nowy tytuł: ");
			String title = scan.next();
			exercise.setTitle(title);
			System.out.println("Podaj nowy opis: ");
			String description = scan.next();
			exercise.setDescription(description);
			exercise.saveToDB(conn);
			System.out.println("Zakończono edytowanie zadania.");
		} else {
			System.out.println("Nie ma takiego zadania.");
		}
	}

	private static void deleteExercise(Scanner scan, Connection conn) throws SQLException {
		System.out.println("Podaj id zadania do usnięcia: ");
		while(!scan.hasNextInt()) {
			scan.next();
			System.out.println("Podaj id zadania: ");
		}
		int id = scan.nextInt();
		Exercise exercise = Exercise.loadById(conn, id);
		if (! (exercise == null)) {
			exercise.delete(conn);
			System.out.println("Zakończono usuwanie zadania.");
		} else {
			System.out.println("Nie ma takiego zadania.");
		}
	}

}
