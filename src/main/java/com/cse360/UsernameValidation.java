package com.cse360;


import java.util.regex.*;

// Password Validation class, work done by Caiden McGregor

public class UsernameValidation {

    public static boolean ValidateUsername(String username) {

        // Checks if the user is a developer
        //String DevCheck = "Developer"; 
        
        // Checks if the username contains a number
        String numCheck = "(?=.*[0-9]).{9,11}";   

        //String allCheck = "Developer" + "(?=.*[0-9])";

        // Checks if the user is a manager
        //String ManagerCheck = "(?=.*[a-z]).{7,18}";

        // Creates a list of all of the password checks
        String[] checkList = {numCheck}; 
        
        // Number of checks passed
        int count = 0;
        
        // Iterates through the checks
        for (int i = 0; i < checkList.length; i++) {


            // Creates a pattern object to check the password with
            Pattern pattern = Pattern.compile(checkList[i]);

            // Checks if the password is empty
            if (username.equals("")) {

                // Password is empty, the password is not valid
                return false;
                
            }

            // Creates a match object to test if the password passes the checks
            Matcher match = pattern.matcher(username);

            // Increments count if the current check is passed
            if (match.matches()) {
                count++;
            }

        }

        // If all of the checks were passed, the username is valid
        if (count == checkList.length) {
            return true;
        }

        // If not all of the checks were passed, the username is invalid
        return false;
    }

    // Main method, has no specific uses in this file.
    public static void main(String[] args) {
        
        // For testing, remove before submission.
        /* 
        String username = "David";              // Invalid
        String username1 = "Developer";         // Invalid
        String username2 = "Developer2";        // Vaild
        String username3 = "Developer 3";       // Valid
        String username4 = "Developer14";       // Valid
        String username5 = "Developer567879";   // Invalid 
        String username6 = "DeveloperYe";       // Invalid

        String[] users = {username, username1, username2, username3, username4, username5, username6};

        for (int i = 0; i < users.length; i++) {

            boolean valid = ValidateUsername(users[i]);
            System.out.println("Username " + i + ": " + valid);
        }
        */
    }

}