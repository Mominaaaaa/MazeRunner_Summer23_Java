import java.util.*;
public class Assignment1 {
    
    public static final int rows=7;
    public static final int columns=7;
    public static char maze[][]=new char[rows][columns];
    public static int playerX;
    public static int playerY;
    public static int exitX;
    public static int exitY;
    public static int steps=0;
    public static int score=0;
    public static int highScore=0;
    public static boolean gameOver=false;
    public static final int timeLimitSeconds=80; // 1 minute 20 seconds time limit

    public static void main(String[] args) {
        MainMenu();
    }

    public static void MainMenu() {
    	Scanner sc = new Scanner(System.in);
        while (!gameOver) {
            System.out.println("Welcome! Choose an option: \n"
            					+ "a. Play Game\n"
            					+ "b. Instructions\n"
            					+ "c. Credits\n"
            					+ "d. High Score\n"
            					+ "e. Exit");
            
            char choice=sc.nextLine().charAt(0);

            switch (choice) {
                case 'a':
                	System.out.println("You have 1 minute 20 seconds to complete the game! Good Luck!\n");
                    StartNewGame();
                    break;
                case 'b':
                    ShowInstructions();
                    break;
                case 'c':
                    ShowCredits();
                    break;
                case 'd':
                    ShowHighScore();
                    break;
                case 'e':
                    ExitGame();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void StartNewGame() {  
        InitializeMaze();
        // Reset player position and game variables
        playerX=1;
        playerY=1;
        score=0;
        
        PlayGame();
    }

    public static void InitializeMaze() {
    	// Initialize the maze with the layout
        char initialMaze[][]={
                {'#', '#', '#', '#', '#', '#', '#'},
                {'#', 'P', '.', '.', '.', '#', '#'},
                {'#', '#', '#', '#', '.', '#', '#'},
                {'#', '#', '.', '.', '.', '#', '#'},
                {'#', '#', '#', '.', '#', '#', '#'},
                {'#', '#', '.', '.', '.', 'E', '#'},
                {'#', '#', '#', '#', '#', '#', '#'}
        };

        // Copy the maze layout to the maze variable
        for (int i=0;i<rows;i++) {
            for (int j=0;j<columns;j++) {
                maze[i][j]=initialMaze[i][j];
                if (maze[i][j]=='P') {
                    playerX=i;
                    playerY=j;
                } else if (maze[i][j]=='E') {
                    exitX=i;
                    exitY=j;
                }
            }
        }
    }

    public static void PrintMaze() {
    	System.out.println("MAZE:\n");
    	//Print the maze
        for (int i=0;i<rows;i++) {
            for (int j=0;j<columns;j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("In this layout: \n"
        		+ "# represents walls that are impassable.\n"
        		+ ". represents open paths that you can move through.\n"
        		+ "P represents the starting position of the player.\n"
        		+ "E represents the exit point that the player needs to reach.\n ");
    }

    public static void PlayGame() {
    	long startTime=System.currentTimeMillis(); // Start the timer
        // Start the time monitor thread
        Thread timeThread=new Thread(() -> {
            try {
                Thread.sleep(timeLimitSeconds*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!gameOver) {
                System.out.println("Time's up! Game Over!(LOSER)");
                DisplayResult(startTime);
                gameOver = true;
            }
        });
        timeThread.start();

        while (!gameOver) {
            PrintMaze();

            System.out.println("Enter your move (W/A/S/D):");
            Scanner sc=new Scanner(System.in);
            char move=sc.nextLine().toUpperCase().charAt(0);

            MovePlayer(move);

            // Check if the player has won or lost
            if (HasPlayerWon()) {
                System.out.println("Congratulations! You reached the exit and WON!!!");
                DisplayResult(startTime);
                gameOver=true;
            } else if (IsGameLost()) {
                System.out.println("Game Over! You LOST! (HA loser)");
                DisplayResult(startTime);
                gameOver=true;
            }
        }
    }

    public static boolean IsValidMove(int newX, int newY) {
        // Check if the new position is within the maze boundaries and not a wall
        return newX>=0 && newX<rows && newY>=0 && newY<columns && maze[newX][newY]!='#';
    }

    
    public static void MovePlayer(char direction) {
    	// Move the player based on the input direction (W/A/S/D)
        int newX=playerX;
        int newY=playerY;
        int prevX=playerX;
        int prevY=playerY;

        switch (direction) {
            case 'W':
                newX--;
                break;
            case 'A':
                newY--;
                break;
            case 'S':
                newX++;
                break;
            case 'D':
                newY++;
                break;
            default:
                System.out.println("Invalid move. Try again.");
                return;
        }

        // Check if the move is valid and update the player's position
        if (IsValidMove(newX, newY)) {
            steps++;
            playerX=newX;
            playerY=newY;
            maze[prevX][prevY]='.'; // Leave a dot in the previous position
            maze[playerX][playerY]='P'; // Move 'P' to the new position
            score=steps*2;
            System.out.println("Steps: "+ steps);
            System.out.println("Score: "+ score);
        } else {
            System.out.println("Invalid move. Try again.");
        }
    }

    public static boolean HasPlayerWon() {
    	// Check if the player has reached the exit
        return playerX==exitX && playerY==exitY;
    }
  
    public static boolean IsGameLost() {
    	 // Check if the player hit a wall or went out of bounds
        return maze[playerX][playerY]=='#';
    }
    
    public static void ShowHighScore() {
    	// Show highest score achieved so far
    	 // and Update the high score
        if (score>highScore) {
            highScore=score;
            System.out.println("Your high score is: " + highScore);
        }   
    }

    public static void DisplayResult(long startTime) {
        System.out.println("Number of steps taken: " + steps);
        System.out.println("Your scored " + CalculateScore(startTime) + " points");
     // Calculate and display the elapsed time
        long timeTaken=System.currentTimeMillis()-startTime;
        int secs=(int) (timeTaken/1000);
        int mins=secs/60;
        secs=secs%60;
        System.out.println("Time Taken: " + mins + " minutes " + secs + " seconds");     
    }
    
    public static int CalculateScore(long startTime) {
    	  // Calculate the elapsed time
        long timeTaken=(System.currentTimeMillis()-startTime)/1000;
        return (int) (timeLimitSeconds-timeTaken);
    }
    
    public static void ShowInstructions() {
        // Display the game rules and instructions
    	System.out.println("MOVEMENTS: \n"
				+ "Press A to move left \n"
				+ "Press W to move up \n"
				+ "press S to move down \n"
				+ "Press D to move right \n"
				+ "AVOID deadends and walls\n"
				+ "Find your way to the exit point. "
				+ "GOOD LUCK!!");
    }
    
    public static void ShowCredits() {
        // Display game development credits
    	System.out.println("Developer Name: "
				+ "Momina Binte Asad\n"
				+ "Roll Number: "
				+ 251694202);
    }
    
    public static void ExitGame() {
    	// Exit the game
        gameOver=true;
        System.out.println("Thank you for playing! Exiting the game...");
        System.exit(0);
    }

}
