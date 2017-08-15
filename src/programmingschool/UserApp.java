package programmingschool;

import programmingschool.model.User;

public class UserApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Witaj w module User");
		System.out.println("Lista użytkowników");
		//			showUserList();
		//			showMenu();
		//			int input = UserInput.getInt();
		//			switch(input) {
		//			case 1:
		//				addUser();
		//				break;
		//			case 2:
		//				modifyUser();
		//				break;
	}

	private static void modifyUser() {
		// get input from terminal
		Long id = null; //scanner


	}

	private static void addUser() {
		// TODO Auto-generated method stub

	}

	private static void showMenu() {
		System.out.println("1 - add");
		System.out.println("2 - modify");
		System.out.println("0 - quit");	
	}

	private static void showUserList() {
		User[] users = User.loadAllUsers();
		for (User u : users) {
			System.out.println(u.getEmail());
		}
	}
}

