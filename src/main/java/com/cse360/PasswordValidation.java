package com.cse360;

import java.util.regex.*;
// Password Validation class, work done by Caiden McGregor

public class PasswordValidation {

    public static boolean ValidatePassword(String password) {

        // Checks to make sure the password has at least one number
        String firstCheck = "(?=.*[0-9]).{7,18}";   

        // Checks to makes sure that the password has at least one lower-case letters
        String secondCheck = "(?=.*[a-z]).{7,18}";

        // Checks to make sre that the password has at least one upper-case letter
        String thirdCheck = "(?=.*[A-Z]).{7,18}";

        // Checks to make sure that the password contains some of the listed special charecters
        String fourthCheck = "(?=.*[!@#$%^&*<>?]).{7,18}";

        // Checks to make sure that there are no spaces in the password
        String fifthCheck = "(?=\\S+$).{7,18}";

        // Creates a list of all of the password checks
        String[] checkList = {firstCheck, secondCheck, thirdCheck, fourthCheck, fifthCheck}; 
        
        // Number of checks passed
        int count = 0;
        
        // Iterates through the checks
        for (int i = 0; i < 5; i++) {


            // Creates a pattern object to check the password with
            Pattern pattern = Pattern.compile(checkList[i]);

            // Checks if the password is empty
            if (password.equals("")) {

                // Password is empty, the password is not valid
                return false;
                
            }

            // Creates a match object to test if the password passes the checks
            Matcher match = pattern.matcher(password);

            // Increments count if the current check is passed
            if (match.matches()) {
                count++;
            }

        }

        // If all of the checks were passed, the password is valid
        if (count == 5) {
            return true;
        }

        // If not all of the checks were passed, the password is invalid
        return false;
    }

    // Main method, has no specific uses in this file.
    public static void main(String[] args) {
    }

}