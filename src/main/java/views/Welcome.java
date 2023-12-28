package views;

import dao.UserDAO;
import model.User;
import service.GenerateOTP;
import service.SendOTPService;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class Welcome {
    public void welcomeScreen() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the Application");
        System.out.println("Press 1 to Login");
        System.out.println("Press 2 to SignUp");
        System.out.println("Press 0 to exit");

        int choice = 0;
        try {
            choice = Integer.parseInt(br.readLine());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        switch (choice) {
            case 1:
                Login();
                break;
            case 2:
                SignUp();
                break;
            case 0:
                System.exit(0);
        }
    }

    private void Login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the email: 1");
        String email = sc.nextLine();
        try {
            if (UserDAO.isExists(email)) {
                String genOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email, genOTP);
                System.out.println("Enter the otp: ");
                String otp = sc.nextLine();

                if (otp.equals(genOTP)){
                    new UserView(email).home();
                }else {
                    System.out.println("Wrong OTP!!!");
                }

            }
            else {
                System.out.println("User not found");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void SignUp() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter name: ");
        String name = sc.nextLine();

        System.out.println("Enter email: ");
        String email = sc.nextLine();

        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email, genOTP);
        System.out.println("Enter the otp: ");
        String otp = sc.nextLine();

        if (otp.equals(genOTP)){
            User user = new User(name, email);
            int response = UserService.saveUser(user);
            switch (response){
                case 0:
                    System.out.println("User Registered");
                    break;
                case 1:
                    System.out.println("User Already Exist");
                    break;
            }
        }else {
            System.out.println("Wrong OTP!!!");
        }

    }
}
