package com.cse360;

public class EncryptionDecryption {
    
    
    public static String EncryptPassword(String password) {
        
        // A string containing the english alphabet
        String allLetters = "abcdefghijklmnopqrstuvwxyz";

        // An empty string that is to be popluated with the encrypted password
        String encryptedPassword = "";

        // Runs a simple ceaser cipher on the passwords
        for (int i = 0; i < password.length(); i++) {
            
            // Determines the index of the passwords letters in the allLetters string
            int index = allLetters.indexOf(password.charAt(i));
            
            // Shifts each letter by the number 50 and wraps around the alphabet
            int charShift = (50 + index) % 26;

            // Determines a new letter depending on the index that was just calculated
            char newValue = allLetters.charAt(charShift);

            // Adds the newly generated letter to the encryptedPassword string
            encryptedPassword += newValue;
        }

        // returns the fully encrypted password
        return encryptedPassword;
    }

    public static String DencryptPassword(String encryptedPassword) {
        
        // A string containing the english alphabet
        String allLetters = "abcdefghijklmnopqrstuvwxyz";

        // An empty string that is to be popluated with the decrypted password
        String password = "";

        // Deciphers the ceaser cipher on the passwords
        for (int i = 0; i < password.length(); i++) {

            // Determines the index of the passwords letters in the allLetters string
            int index = allLetters.indexOf(encryptedPassword.charAt(i));

            // Unshifts each letter by the number 50 and wraps around the alphabet
            int charShift = (index - 50) % 26;

            // Compensates for negative values
            if (charShift < 0) {
                charShift = allLetters.length() + charShift;
            }

            // Determines the original letter depending on the index that was just calculated
            char newValue = allLetters.charAt(charShift);

            // Adds the newly generated letter to the encryptedPassword string
            password += newValue;
        }

        //System.out.println(password);
        return password;
    }

    public void main(String[] args) {

    }
}