package stocktrading.service;

import stocktrading.model.User;
import stocktrading.exception.*;

import java.util.*;
import java.util.InputMismatchException;

public class UserService {
    private Scanner sc = new Scanner(System.in);

    // And modify signUp() to store the user:
    public User signUp() throws InvalidDateException, InvalidMonthException, InvalidPasswordException {
        String name = validateName();
        int[] birthDetails = validateBirthDate();
        String mobile = validateMobile();
        String email = validateEmail();
        String password = validatePassword();

        User newUser = new User(name, email, password, birthDetails[0], birthDetails[1],
                birthDetails[2], mobile);
        registeredUsers.add(newUser);
        return newUser;
    }

    private String validateName() {
        boolean validName;
        String name;
        System.out.println("Enter your name: ");
        do {
            name = sc.nextLine();
            validName = true;
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == ' ')) {
                    validName = false;
                    break;
                }
            }
            if(name.isEmpty()){
                validName = false;
            }
            if (!validName) {
                System.out.println("Please! Enter valid name (letters and spaces only).");
            }
        } while (!validName);
        return name;
    }

    private int[] validateBirthDate() throws InvalidDateException, InvalidMonthException {
        int birthDate = validateDate();
        int birthMonth = validateMonth();
        int birthYear = validateYear(birthMonth);

        return new int[]{birthDate, birthMonth, birthYear};
    }

    private int validateDate() throws InvalidDateException {
        System.out.print("Enter your birth date: ");
        while (true) {
            try {
                int date = sc.nextInt();
                if (date <= 0 || date > 31) {
                    throw new InvalidDateException("Date should be between 1 and 31");
                }
                return date;
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Date should be a valid number");
            }
        }
    }

    private int validateMonth() throws InvalidMonthException {
        System.out.print("Enter your birth month: ");
        while(true) {
            try {
                int month = sc.nextInt();
                if (month <= 0 || month > 12) {
                    throw new InvalidMonthException("Month should be between 1 and 12");
                }
                return month;
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Month should be a valid number");
            }
        }
    }

    private int validateYear(int birthMonth) {
        System.out.print("Enter your birth year: ");
        while(true) {
            try {
                int year = sc.nextInt();
                if (year > 2007 || (year == 2007 && birthMonth >= 2)) {
                    System.out.println("Sorry! You Are not Eligible for trading.");
                    System.exit(0);
                }
                return year;
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Year should be a valid number");
            }
        }
    }

    private String validateMobile() {
        boolean isValid = false;
        String mobile = "";
        int mobTry = 1;

        System.out.print("Enter your mobile number: ");
        sc.nextLine();

        do {
            mobile = sc.nextLine();
            isValid = true;

            if (mobile.length() != 10) {
                isValid = false;
            } else {
                for (int j = 0; j < mobile.length(); j++) {
                    char c = mobile.charAt(j);
                    if (!(c >= '0' && c <= '9')) {
                        isValid = false;
                        break;
                    }
                }

                // Check if first digit is 6-9
                if (isValid && !(mobile.charAt(0) >= '6' && mobile.charAt(0) <= '9')) {
                    isValid = false;
                }
            }

            if (!isValid) {
                System.out.println("Invalid! Try again.");
                if (mobTry == 1) {
                    System.out.println("You have 2 tries left");
                } else if (mobTry == 2) {
                    System.out.println("You have 1 try left");
                } else {
                    System.out.println("Try after 24 hours!");
                    System.exit(0);
                }
                mobTry++;
            }
        } while (!isValid);

        return mobile;
    }

    public String validateEmail() {
        boolean isValid;
        String email;

        System.out.print("Enter your E-mail id: ");
        do {
            email = sc.nextLine();
            isValid = true;

            if (email.length() <= 10 || !email.endsWith("@gmail.com")) {
                isValid = false;
            } else {
                for (int j = 0; j < email.length() - 10; j++) {
                    char e = email.charAt(j);
                    if (!((e >= 'a' && e <= 'z') || (e >= 'A' && e <= 'Z') ||
                            (e >= '0' && e <= '9') || e == '.' || e == '_')) {
                        isValid = false;
                        break;
                    }
                }
            }

            if (!isValid) {
                System.out.println("Invalid email! Enter a correct email.");
                System.out.print("Enter your E-mail id: ");
            }
        } while (!isValid);

        return email;
    }

    public String validatePassword() throws InvalidPasswordException {
        System.out.println("Enter a 4-digit password:");
        String password;
        boolean valid;

        do {
            password = sc.nextLine();
            valid = true;

            if (password.length() != 4) {
                valid = false;
            } else {
                for (int j = 0; j < password.length(); j++) {
                    char ch = password.charAt(j);
                    if (!(ch >= '0' && ch <= '9')) {
                        valid = false;
                        break;
                    }
                }
            }

            if (!valid) {
                throw new InvalidPasswordException("Enter Valid 4 digit password");
            }
        } while (!valid);

        return password;
    }

    public User login(String email, String password) throws InvalidPasswordException {
        System.out.println("Enter E-mail for login:");
        while (true) {
            String inputEmail = sc.next();
            if (!inputEmail.equals(email)) {
                System.out.println("Enter correct Email");
            } else {
                break;
            }
        }

        System.out.println("Enter Password:");
        for (int i = 1; i <= 3; i++) {
            String inputPassword = sc.next();
            if (inputPassword.equals(password)) {
                return null;
            }
            if (i < 3) {
                System.out.println("Incorrect password. You have " + (3 - i) + " tries left.");
            } else {
                throw new InvalidPasswordException("Too many failed attempts. Try after 24 hours!");
            }
        }
        return null;
    }
    private List<User> registeredUsers = new ArrayList<>();

    public User getUserByEmail(String email) throws UserNotFoundException {
        return registeredUsers.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    public boolean hasRegisteredUsers() {
        return !registeredUsers.isEmpty();
    }
}