package programmingschool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import programmingschool.model.Group;

public class GroupApp {
	public static void main(String[] args) {
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false", "root", "");
			System.out.println("Witaj w module Group");
			Scanner scan = new Scanner(System.in);
			int action = 4;
			while (action != 0) {
				showGroupList(conn);
				System.out.println();
				action = getAction(scan);
				switch(action) {
				case 1: addGroup(scan, conn);
				break;
				case 2: editGroup(scan, conn);
				break;
				case 3: deleteGroup(scan, conn);
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
		System.out.println("1 - add - dodanie grupy");
		System.out.println("2 - edit - edycja grupy");
		System.out.println("3 - delete - usunięcie grupy");
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

	private static void showGroupList(Connection conn) throws SQLException {
		System.out.println("Lista zadań");
		Group[] groups = Group.loadAllGroups(conn);
		for (Group g : groups) {
			System.out.println(g.getId() + " | " + g.getName());
		}
	}

	private static void addGroup(Scanner scan, Connection conn) throws SQLException {
		System.out.println("Podaj nazwę: ");
		String name = scan.next();
		Group group = new Group (name);
		group.saveToDB(conn);
		System.out.println("Zakończono dodawanie grupy.");
	}

	private static void editGroup(Scanner scan, Connection conn) throws SQLException {
		System.out.println("Podaj id grupy, która zostanie zmodyfikowana: ");
		while(!scan.hasNextInt()) {
			scan.next();
			System.out.println("Podaj id grupy: ");
		}
		int id = scan.nextInt();
		Group group = Group.loadById(conn, id);
		if (! (group == null)) {
			System.out.println("Podaj nową nazwę: ");
			String name = scan.next();
			group.setName(name);
			group.saveToDB(conn);
			System.out.println("Zakończono edytowanie grupy.");
		} else {
			System.out.println("Nie ma takiej grupy.");
		}
	}

	private static void deleteGroup(Scanner scan, Connection conn) throws SQLException {
		System.out.println("Podaj id grupy do usnięcia: ");
		while(!scan.hasNextInt()) {
			scan.next();
			System.out.println("Podaj id grupy: ");
		}
		int id = scan.nextInt();
		Group group = Group.loadById(conn, id);
		if (! (group == null)) {
			group.delete(conn);
			System.out.println("Zakończono usuwanie grupy.");
		} else {
			System.out.println("Nie ma takiej grupy.");
		}
	}
}
