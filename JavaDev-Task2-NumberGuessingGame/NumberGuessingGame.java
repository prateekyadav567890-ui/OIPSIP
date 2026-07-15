import java.util.Scanner;
import java.util.Random;

public class NumberGuessingGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        boolean playAgain = true;
        int currentRound = 1;

        System.out.println("========================================");
        System.out.println("  Welcome to the Number Guessing Game!  ");
        System.out.println("========================================");

        while (playAgain) {
            System.out.println("\n--- Round " + currentRound + " ---");
            
            // Bonus: Difficulty Selection
            System.out.println("Select Difficulty Level:");
            System.out.println("1. Easy (1-50, 10 attempts)");
            System.out.println("2. Medium (1-100, 7 attempts)");
            System.out.println("3. Hard (1-200, 5 attempts)");
            System.out.print("Enter your choice (1, 2, or 3): ");
            
            int choice = scanner.nextInt();
            int maxRange = 100;
            int maxAttempts = 7;
            
            // Configuring settings based on difficulty
            if (choice == 1) {
                maxRange = 50;
                maxAttempts = 10;
            } else if (choice == 2) {
                maxRange = 100;
                maxAttempts = 7;
            } else if (choice == 3) {
                maxRange = 200;
                maxAttempts = 5;
            } else {
                System.out.println("Invalid choice! Defaulting to Medium Level.");
            }
            
            // Generating the random number for the round
            int targetNumber = random.nextInt(maxRange) + 1;
            int attemptsTaken = 0;
            boolean guessedCorrectly = false;
            
            System.out.println("\nI have picked a number between 1 and " + maxRange + ".");
            System.out.println("You have a maximum of " + maxAttempts + " attempts to guess it.");
            
            // Game Loop
            while (attemptsTaken < maxAttempts) {
                System.out.print("\nAttempt " + (attemptsTaken + 1) + "/" + maxAttempts + " - Enter your guess: ");
                int guess = scanner.nextInt();
                attemptsTaken++;
                
                if (guess == targetNumber) {
                    System.out.println("Correct! You guessed the right number.");
                    guessedCorrectly = true;
                    break;
                } else if (guess < targetNumber) {
                    System.out.println("Too Low!");
                } else {
                    System.out.println("Too High!");
                }
            }
            
            // End of Round Summary
            if (guessedCorrectly) {
                System.out.println("\n🎉 Round " + currentRound + " Summary: Guessed in " + attemptsTaken + " attempts. 🎉");
            } else {
                System.out.println("\nYou Lost! The correct number was " + targetNumber + ".");
            }
            
            // Play Again Prompt
            System.out.print("\nDo you want to play another round? (yes/no): ");
            String response = scanner.next().toLowerCase();
            playAgain = response.equals("yes") || response.equals("y");
            
            if (playAgain) {
                currentRound++;
            }
        }
        
        System.out.println("\nThanks for playing! You played a total of " + currentRound + " rounds. Goodbye!");
        scanner.close();
    }
}