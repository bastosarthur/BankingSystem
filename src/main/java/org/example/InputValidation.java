package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.math.BigDecimal;
import org.mindrot.jbcrypt.BCrypt;

public class InputValidation {

    public String getCpf(Scanner input) {
        while (true) {
            String cpf = input.nextLine();
            if (cpf.matches("^[0-9]{11}$")) {
                return cpf;
            }
            System.out.print("Please type a 11-digit cpf number: ");
        }
    }

    public String getPassword(Scanner input) {
        while (true) {
            String password = input.nextLine();
            if (password.matches("^[0-9]{8}$")) {
                return BCrypt.hashpw(password, BCrypt.gensalt());
            }
            System.out.print("Please type a 8-digit numeric password: ");
        }
    }

    public String tryPassword(Scanner input) {
        while (true) {
            String tryPassword = input.nextLine();
            if (tryPassword.matches("^[0-9]{8}$")) {
                return tryPassword;
            }
            System.out.print("Please type a 8-digit numeric password: ");
        }
    }

    public int getAccountNumber(Scanner input) {
        while (true) {
            try {
                int accountNumber = input.nextInt();
                input.nextLine();
                if (accountNumber >= 100000 && accountNumber <= 999999) {
                    return accountNumber;
                }
                System.out.print("Please type a 6-digit account number: ");
            } catch (InputMismatchException e) {
                System.out.print("Please type a 6-digit account number: ");
                input.nextLine();
            }
        }
    }

    public BigDecimal getAmount(Scanner input) {
        while (true) {
            try {
                BigDecimal deposit = input.nextBigDecimal();
                input.nextLine();
                if (deposit.compareTo(BigDecimal.ZERO) > 0) {
                    return deposit;
                }
                System.out.print("Type a positive amount: ");
            } catch (InputMismatchException e) {
                System.out.print("Type a positive amount: ");
                input.nextLine();
            }
        }
    }

    public int getTransactionId(Scanner input) {
        while (true) {
            try {
                int transactionId = input.nextInt();
                input.nextLine();
                if (transactionId >= 100 && transactionId <= 999) {
                    return transactionId;
                }
                System.out.print("Please digit a 3-digit numeric transaction id: ");
            } catch (InputMismatchException e) {
                System.out.print("Please digit a 3-digit numeric transaction id: ");
                input.nextLine();
            }
        }
    }

    public LocalDate getDate(Scanner input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        while (true) {
            String dateInput = input.nextLine();
            try {
                return LocalDate.parse(dateInput, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Enter MM/dd/yyyy:");
            }
        }
    }

    public int getOption(Scanner input) {
        while (true) {
            try {
                int option = input.nextInt();
                input.nextLine();
                return option;
            } catch (InputMismatchException e) {
                System.out.print("Please enter a number: ");
                input.nextLine();
            }
        }
    }
}
