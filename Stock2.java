import java.util.*;

class InvalidDateException extends Exception {
    public InvalidDateException(String message) {
        super(message);
    }
}

class InvalidMonthException extends Exception {
    public InvalidMonthException(String message) {
        super(message);
    }
}

class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class InvalidSharesException extends Exception {
    public InvalidSharesException(String message) {
        super(message);
    }
}

class Trade {
    Scanner sc = new Scanner(System.in);
    String email, password, name;
    int BirthDate, BirthMonth, BirthYear;
    static double currentPrize, currentPrize1, todayOpen;
    static boolean validYear=true;

    void signup() throws InvalidDateException, 
                         InvalidMonthException, InvalidPasswordException {
   
        boolean validName;
        System.out.print("Enter your name: ");
        do {
            name = sc.nextLine();
            validName = true;
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
                    validName = false;
                    break;
                }
            }
            if (!validName) {
                System.out.println("Please! Enter valid name.");
            }
        } while (!validName);
		
        System.out.print("Enter your birth date: ");
		while (true) {
			try {
				BirthDate = sc.nextInt();
				if (BirthDate <= 0 || BirthDate > 31) {
					throw new InvalidDateException("Date should be between 1 and 31");
				}
				break;
			} catch (InputMismatchException e) {
				sc.nextLine(); 
				System.out.println("Date should be a valid number");

			} catch (InvalidDateException e) {
				System.out.println(e.getMessage());
			}
		}
        System.out.print("Enter your birth month: ");
		while(true){
            try {
                BirthMonth = sc.nextInt();
                if (BirthMonth <= 0 || BirthMonth > 12) {
                    throw new InvalidMonthException("Month should be between 1 and 12");
                }
                break;
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Month should be a valid number");    
            }
            catch (InvalidMonthException e) {
				System.out.println(e.getMessage());
			}
        }
        System.out.print("Enter your birth year: ");
        while(true){
            try {
                BirthYear = sc.nextInt();
                if (BirthYear > 2007 || (BirthYear == 2007 && BirthMonth >= 2)) {
                    System.out.println("Sorry! You Are not Eligible for trading.");
                    return;
                }
                break;  
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("Year should be a valid number");
            }             
            }
        System.out.println("Fill below details");
        boolean b = true;
        int i = 1;
        System.out.print("Enter your mobile number: ");
        sc.nextLine();
        do {
            String num = sc.nextLine();
            for (int j = 0; j < num.length(); j++) {
                if (!(num.charAt(j) >= '0' && num.charAt(j) <= '9' && num.length() == 10 &&
                        num.charAt(0) >= '6' && num.charAt(0) <= '9')) {
                    b = true;
                    System.out.println("Invalid! Try again.");
                    if (i == 1) {
                        System.out.println("You have 2 tries");
                    } else if (i == 2) {
                        System.out.println("You have 1 try.");
                    } else {
                        System.out.println("Try after 24 hours!");
                        return;
                    }
                    i++;
                    break;
                } else {
                    b = false;
                }
            }
        } while (b && i <= 3);

        boolean isValid;
        System.out.print("Enter your E-mail id: ");
        do {
            email = sc.nextLine();
            isValid = true;
            if (email.length() > 10 && email.endsWith("@gmail.com")) {
                for (int j = 0; j < email.length() - 10; j++) {
                    char e = email.charAt(j);
                    if (!((e >= 'a' && e <= 'z') || (e >= 'A' && e <= 'Z') || (e >= '0' && e <= '9') || e == '.'
                            || e == '_')) {
                        isValid = false;
                        break;
                    }
                }
            } else {
                isValid = false;
            }

            if (!isValid) {
                System.out.println("Invalid email! Enter a correct email.");
                System.out.print("Enter your E-mail id: ");
            }
        } while (!isValid);

        System.out.println("Enter a 4-digit password:");
        boolean valid = false;
        do {
            try{
            password = sc.nextLine();
            valid = true;
            if (password.length() == 4) {
                for (int j = 0; j < password.length(); j++) {
                    char ch = password.charAt(j);
                    if (!(ch >= '0' && ch <= '9')) {
                        valid = false;
                        break;
                    }
                }
            } else {
                valid = false;
            }
            if (!valid)
                throw new InvalidPasswordException("Enter Valid 4 digit password");
        }catch (InvalidPasswordException e){
                System.out.println(e.getMessage());
        }
        } while (!valid);
    }

    void login(String email, String password) throws InvalidPasswordException{
        if(Trade.validYear==false)
            return;
            while (true) {
                System.out.println("Enter E-mail for login:");
                String inputEmail = sc.next();
                if (!inputEmail.equals(email)) {
                    System.out.println("Enter correct Email");
                } 
                else
                break;
            }

        System.out.println("Enter Password:");
        for (int i = 1; i <= 3; i++) {
            String inputPassword = sc.next();
            if (inputPassword.equals(password)) {
                return;
            }
            if (i < 3) {
                System.out.println("Incorrect password. You have " + (3 - i) + " tries left.");
            } else {
                throw new InvalidPasswordException("Too many failed attempts. Try after 24 hours!");
            }
        }
    }

    void stockData(String name, double close) {
        StockMarket.print("         You selected: " + name + "              ");
        todayOpen = (close - 20) + (Math.random() * 40.0);
        todayOpen = (int) (todayOpen * 100.0) / 100.0;
        System.out.println("|       Previous day's close price = " + (int) (close * 100.0) / 100.0 + "      |");
        System.out.println("|       Today's Open price = " + (int) (todayOpen * 100.0) / 100.0 + "              |");

        currentPrize = (close - 50) + (Math.random() * 100.0);
        currentPrize = (int) (currentPrize * 100.0) / 100.0;
        System.out.println("|       Current price = " + (int) (currentPrize * 100.0) / 100.0 + "                   |");
        System.out.println("+------------------------------------------------+");
    }
}

class Invest extends Trade {
    int share, shareAdd;
    double CompanyInvest;
    static int[] sharesHeld = new int[10];
    static double[] buyingPrice = new double[10];
    double buyingAmount;
    double sellPrice;

    void buy(String companyName, int i) throws InsufficientFundsException, InvalidSharesException {
        if (StockMarket.Amount < currentPrize) {
            throw new InsufficientFundsException("Not Enough Balance For buy this share! Add more Balance.");
        }

        int count = (int) (StockMarket.Amount / currentPrize);
        System.out.println("you can buy Maximum " + count + " Shares.");

        try {
            share = sc.nextInt();
            if (share <= 0) {
                throw new InvalidSharesException("Number of shares must be positive");
            }
            if (share > count) {
                throw new InvalidSharesException("Sorry! you can buy Maximum " + count + " Shares.");
            }
        } catch (InputMismatchException e) {
            sc.nextLine();
            throw new InvalidSharesException("Please enter a valid number of shares");
        }

        StockMarket.Amount -= share * currentPrize;
        System.out.println("--------------------------------------------------");
        System.out.println("Your balance: " + (int) (StockMarket.Amount * 100.0) / 100.0 + "Rs. ");
        CompanyInvest = (int) ((share * currentPrize * 100.0) / 100.0);
        StockMarket.TotalAmount1[i] += CompanyInvest;
        sharesHeld[i] += share;
        buyingPrice[i] = currentPrize;
        System.out.println("You invested " + (int) (CompanyInvest * 100.0) / 100.0 + " Rs. in " + companyName);
        System.out.println("--------------------------------------------------");
        System.out.println();
        currentPrize1 = currentPrize;

        if (StockMarket.Amount >= currentPrize1) {
            System.out.println("you can buy more " + (count - share) + " shares:");
            boolean b = true;
            while (b) {
                System.out.println("if you want to buy more shares press 1 or press 2 for exit");
                try {
                    int addshare = sc.nextInt();
                    if (addshare == 2) {
                        b = false;
                        return;
                    } else if (addshare != 1) {
                        System.out.println("Enter valid choice");
                        continue;
                    }

                    currentPrize1 = currentPrize - 5 + (Math.random() * 10.0);
                    currentPrize1 = (int) (currentPrize1 * 100.0) / 100.0;
                    StockMarket.print("new Current price = " + (int) (currentPrize1 * 100.0) / 100.0);
                    System.out.println("How much shares you want to Add?");
                    System.out.println("you can buy maximum " + (int) (StockMarket.Amount / currentPrize1) + " Shares now.");

                    shareAdd = sc.nextInt();
                    if (shareAdd <= 0) {
                        throw new InvalidSharesException("Number of shares must be positive");
                    }
                    if (shareAdd > (int) (StockMarket.Amount / currentPrize1)) {
                        throw new InvalidSharesException("Not enough funds for " + shareAdd + " shares");
                    }

                    StockMarket.Amount -= shareAdd * currentPrize1;
                    StockMarket.print("Your balance: " + (int) (StockMarket.Amount * 100.0) / 100.0 + "Rs. ");
                    CompanyInvest = (int) ((shareAdd * currentPrize1 * 100.0) / 100.0);
                    StockMarket.TotalAmount1[i] += CompanyInvest;
                    sharesHeld[i] += shareAdd;
                    buyingPrice[i] = StockMarket.TotalAmount1[i] / sharesHeld[i];
                    StockMarket.print("You invested " + (CompanyInvest * 100.0) / 100.0 + " Rs. in " + companyName);
                    System.out.println();

                    if (StockMarket.Amount < currentPrize1) {
                        b = false;
                    }
                } catch (InputMismatchException e) {
                    sc.nextLine();
                    System.out.println("Please enter a valid number");
                }
            }
        }
    }

    void sell(String companyName, int i) throws InvalidSharesException {
        if (sharesHeld[i] == 0) {
            System.out.println("You don't own any shares of " + companyName);
            return;
        }

        System.out.println("You currently hold " + sharesHeld[i] + " shares of " + companyName);
        System.out.println("+------------------------------+");
        sellPrice = (int) (buyingPrice[i] * (0.95 + (Math.random() * 0.1)) * 100.0) / 100.0;
        System.out.println("now current price is = " + sellPrice);
        System.out.println("your buying price is = " + buyingPrice[i]);
        System.out.println("+------------------------------+");

        System.out.println("if you want to sell press 1 & press 2 for Exit");
        try {
            int choice = sc.nextInt();
            if (choice == 1) {
                System.out.println("Enter the number of shares you want to sell:\nYou can sell Maximum " + sharesHeld[i] + " Shares: ");
                int sharesToSell = sc.nextInt();
                
                if (sharesToSell <= 0) {
                    throw new InvalidSharesException("Number of shares must be positive");
                }
                if (sharesToSell > sharesHeld[i]) {
                    throw new InvalidSharesException("You don't have enough shares to sell");
                }

                double sellAmount = sharesToSell * sellPrice;
                StockMarket.Amount += sellAmount;
                sharesHeld[i] -= sharesToSell;
                StockMarket.TotalAmount1[i] -= (sharesToSell * buyingPrice[i]);
                System.out.println("You sold " + sharesToSell + " shares of " + companyName + " at " + sellPrice + " Rs. per share.");
                System.out.println("Total amount received: " + (int) (sellAmount * 100.0) / 100.0 + " Rs.");

                buyingAmount = sharesToSell * buyingPrice[i];
                double profitOrLoss = sellAmount - buyingAmount;
                System.out.println("+-----------------------------------------+");
                if (profitOrLoss > 0) {
                    System.out.println("You made a profit of " + (int) (profitOrLoss * 100.0) / 100.0 + " Rs.");
                } else if (profitOrLoss < 0) {
                    System.out.println("You made a loss of " + ((-1) * ((int) (profitOrLoss * 100.0) / 100.0)) + " Rs.");
                } else {
                    System.out.println("You broke even (no profit or loss).");
                }
                System.out.println("Your new balance is " + (int) (StockMarket.Amount * 100.0) / 100.0 + " Rs.");
                System.out.println("+-----------------------------------------+");
            } else if (choice != 2) {
                System.out.println("Invalid choice, returning to menu");
            }
        } catch (InputMismatchException e) {
            sc.nextLine();
            System.out.println("Invalid input, please enter numbers only");
        }
    }
}

class StockMarket {
    static double Amount = 0, AddAmount;
    static char yn;
    static String[] companyName = new String[10];
    static double[] TotalAmount1 = new double[10];
    static Scanner sc = new Scanner(System.in);

    static void info(String[] topIndianCompanies, int i) {
        Invest I = new Invest();

        System.out.println("Do you want to invest in " + topIndianCompanies[i] + "?");
        System.out.println("Press Y/y for Yes and N/n for No");
        try {
            yn = sc.next().charAt(0);
        } catch (Exception e) {
            System.out.println("Invalid input, please try again");
            sc.nextLine();
            return;
        }

        if (yn == 'Y' || yn == 'y') {
            companyName[i] = topIndianCompanies[i];
            try {
                I.buy(topIndianCompanies[i], i);
            } catch (InsufficientFundsException | InvalidSharesException e) {
                System.out.println("Error: " + e.getMessage());
                return;
            }

            boolean b = true;
            while (b) {
                System.out.println("You want to add Balance? ");
                System.out.println("(Y/y) for Yes | (N/n) for No");
                try {
                    char choice1 = sc.next().charAt(0);
                    if (choice1 == 'Y' || choice1 == 'y') {
                        System.out.println("Add Balance");
                        try {
                            AddAmount = sc.nextDouble();
                            if (AddAmount <= 0) {
                                throw new InputMismatchException("Amount must be positive");
                            }
                            StockMarket.Amount += AddAmount;
                            StockMarket.print("Your balance: " + (int) (StockMarket.Amount * 100.0) / 100.0 + "Rs. ");
                            b = false;
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid amount");
                            sc.nextLine();
                        }
                    } else if (choice1 == 'N' || choice1 == 'n') {
                        b = false;
                        System.out.println();
                    } else {
                        System.out.println("Enter valid choice! ");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input, please try again");
                    }
                }
            }else if (yn == 'N' || yn == 'n') {
            System.out.println("Returning to Portfolio");
        } else {
            System.out.println("Invalid input.");
        }
        } 
    

    static boolean AllInvestment() {
        boolean hasInvestments = false;
        for (int i = 0; i < companyName.length; i++) {
            if (TotalAmount1[i] > 0) {
                hasInvestments = true;
                break;
            }
        }
        if (!hasInvestments) {
            System.out.println();
            StockMarket.print("Not Invested Anything");
            System.out.println();
            return false;
        }
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < companyName.length; i++) {
            if (TotalAmount1[i] > 0) {
                System.out.println("You invested " + (int) (TotalAmount1[i] * 100.0) / 100.0 + " Rs. in (" + (i + 1)
                        + ") " + companyName[i] + " (" + Invest.sharesHeld[i] + " shares)");
            }
        }
        System.out.println("--------------------------------------------------");
        System.out.println();
        return true;
    }

    static void sellShares(String[] topIndianCompanies) {
        if (!AllInvestment()) {
            return;
        }
        System.out.println("Enter the company number (1-10) to sell shares:");
        try {
            int choice = sc.nextInt() - 1;
            if (choice < 0 || choice >= topIndianCompanies.length) {
                System.out.println("Invalid choice! Please enter a number between 1 and 10.");
                return;
            }
            if (StockMarket.TotalAmount1[choice] == 0) {
                StockMarket.print("You don't own any shares of " + topIndianCompanies[choice]);
                return;
            }
            Invest I = new Invest();
            try {
                I.sell(topIndianCompanies[choice], choice);
            } catch (InvalidSharesException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number");
            sc.nextLine();
        }
    }

    static void print(String s) {
        System.out.print("+");
        for (int i = 0; i < s.length() + 4; i++) {
            System.out.print("-");
        }
        System.out.print("+");
        System.out.println("");
        System.out.println("|  " + s + "  |");
        System.out.print("+");
        for (int i = 0; i < s.length() + 4; i++) {
            System.out.print("-");
        }
        System.out.print("+");
        System.out.println();
    }

    static void PortfolioSummary() {
        System.out.println("----------------- Portfolio Summary -----------------");
        System.out.println("Your current balance: " + (int) (Amount * 100.0) / 100.0 + " Rs.");
        System.out.println("Your investments:");
        for (int i = 0; i < companyName.length; i++) {
            if (TotalAmount1[i] > 0) {
                System.out.println(companyName[i] + ": " + Invest.sharesHeld[i] + " shares, Invested: "
                        + (int) (TotalAmount1[i] * 100.0) / 100.0 + " Rs.");
            }
        }
        System.out.println("-----------------------------------------------------");
    }

    public static void main(String[] args) {
        Trade T = new Trade();
        StockMarket.print("-------- WELCOME --------");
        System.out.println("-------- Sign up --------");
        try {
            T.signup();
        } catch (InvalidDateException | 
                 InvalidMonthException | InvalidPasswordException e) {
            System.out.println( e.getMessage());
            return;
        }
        if (T.BirthYear > 2007 || (T.BirthYear == 2007 && T.BirthMonth > 2))
            return;
        System.out.println();
        System.out.println("--------Login Section--------");
        System.out.println();
        
        try {
            T.login(T.email, T.password);
        } catch (InvalidPasswordException  e) {
            System.out.println("Login failed: " + e.getMessage());
            return;
        }

        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        System.out.println("|       Welcome " + T.name + "! your Demate Account is ready.    |");
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        System.out.println();
        
        boolean b = true;
        System.out.println("Add Funds in your Account for trading.");
        do {
            try {
                Amount = sc.nextDouble();
                if (Amount <= 0) {
                    System.out.println("Amount must be positive! Please enter valid amount.");
                    b = true;
                } else {
                    b = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number");
                sc.nextLine();
                b = true;
            }
        } while (b);

        String[] topIndianCompanies = {
                "Reliance Industries Limited",
                "Tata Consultancy Services (TCS)",
                "HDFC Bank",
                "ICICI Bank",
                "Infosys Limited",
                "Hindustan Unilever Limited (HUL)",
                "State Bank of India (SBI)",
                "Bharti Airtel",
                "Adani Enterprises",
                "Wipro Limited"
        };
        System.out.println();
        StockMarket.print(" Company information ");

        for (int i = 0; i < topIndianCompanies.length; i++) {
            System.out.println((i + 1) + ". " + topIndianCompanies[i]);
        }
        System.out.println();
        
        while (true) {
            System.out.println("Your choice (Enter 1-10 for Company Information) \n(11 to see your holdings, 12 to sell shares, 13 to Add funds, or 0 to exit): ");
            try {
                int choice = sc.nextInt();
                if (choice == 0) {
                    PortfolioSummary();
                    System.out.println("Exiting... Thank you!");
                    break;
                } else if (choice == 11) {
                    StockMarket.AllInvestment();
                } else if (choice == 12) {
                    sellShares(topIndianCompanies);
                } else if (choice == 13) {
                    System.out.println("Add fund:");
                    try {
                        double amount = sc.nextDouble();
                        if (amount <= 0) {
                            System.out.println("Amount must be positive");
                            continue;
                        }
                        StockMarket.Amount += amount;
                        StockMarket.print("Your Balance : " + ((int) (StockMarket.Amount * 100)) / 100);
                        System.out.println();
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter a valid amount");
                        sc.nextLine();
                    }
                } else if (choice >= 1 && choice <= 10) {
                    switch (choice) {
                        case 1:
                            T.stockData("Reliance", 1263.65);
                            info(topIndianCompanies, 0);
                            break;
                        case 2:
                            T.stockData("TCS", 4145.45);
                            info(topIndianCompanies, 1);
                            break;
                        case 3:
                            T.stockData("HDFC", 1664.90);
                            info(topIndianCompanies, 2);
                            break;
                        case 4:
                            T.stockData("ICICI", 1201.75);
                            info(topIndianCompanies, 3);
                            break;
                        case 5:
                            T.stockData("Infosys", 1865.40);
                            info(topIndianCompanies, 4);
                            break;
                        case 6:
                            T.stockData("HUL", 2321.70);
                            info(topIndianCompanies, 5);
                            break;
                        case 7:
                            T.stockData("SBI", 745.90);
                            info(topIndianCompanies, 6);
                            break;
                        case 8:
                            T.stockData("Bharti Airtel", 1636.0);
                            info(topIndianCompanies, 7);
                            break;
                        case 9:
                            T.stockData("Adani", 2385.0);
                            info(topIndianCompanies, 8);
                            break;
                        case 10:
                            T.stockData("Wipro", 317.70);
                            info(topIndianCompanies, 9);
                            break;
                    }
                } else {
                    System.out.println("Invalid choice! Please enter a number between 0 and 13.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number");
                sc.nextLine();
            }
        }
    }
}
