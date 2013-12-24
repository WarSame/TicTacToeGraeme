import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JPanel;

public class TicTacToePanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int DIVISIONS = 3;
	private static final int DIMENSIONS_SIZE = 600;
	private static final int DIVISION_DIMENSION = DIMENSIONS_SIZE/DIVISIONS;
	private static final int NEITHER = 0;
	private static final int USER = 1;
	private static final int AI = 2;
	
	private int[][] gameArray;//Stores the current squares for each player.
	private int currentPlayer;//1 = user, 2 = AI. Stores the person who's turn it is
	private int winningPlayer;//Same as above. Stores the person who won the last game.
	private boolean gameWon;//Is the game won yet?
	private boolean gameTie;//Is the game a tie?
	private int xGuess;//The AI's x co-ord guess
	private int yGuess;//" y "
	private int playerWins;//Stats for how many games the player has won
	private int aiWins;//" AI "
	private int tieGames;//The number of ties
	
	public TicTacToePanel(){
		setSize(DIMENSIONS_SIZE, DIMENSIONS_SIZE);
		gameArray = new int[DIVISIONS][DIVISIONS];//Stores the value of the current game
		for (int i = 0; i<DIVISIONS; i++){
			for (int j = 0; j<DIVISIONS; j++){
				gameArray[i][j] = 0;
			}
		}
		gameWon = false;//Initialize the first game.
		gameTie = false;
		winningPlayer = NEITHER;
		currentPlayer = USER;
		addMouseListener(new MouseHandler(this));
		aiWins = 0;
		playerWins = 0;
		tieGames = 0;
	}
	public void reset() {
		//Clears the board and characteristics
		for (int i = 0; i<DIVISIONS; i++){
			for (int j = 0; j<DIVISIONS; j++){
				gameArray[i][j] = 0;
			}
		}
		gameWon = false;
		winningPlayer = NEITHER;
		gameTie = false;
		currentPlayer = USER;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		paintGame(g);
		paintGrid(g);
		paintWin(g);
		paintStats(g);
	}
	
	private void paintStats(Graphics g) {
		//Controls the stats panel.
		g.setColor(Color.GRAY);
		g.fillRect(600, 0, 900, 600);
		
		g.setColor(Color.BLACK);
		Font textFont = new Font("Times New Roman", Font.PLAIN, 20);
		g.setFont(textFont);
		g.drawString("Games Won: " + playerWins, 610, 200);
		g.drawString("Games Tied: " + tieGames, 610, 300);
		g.drawString("Games Lost: " + aiWins, 610, 400);
	}
	private void paintWin(Graphics g) {
		//Controls the winning conditions
		if (gameWon){
			Font textFont = new Font("Monotype Corsiva", Font.PLAIN, 30);
			g.setFont(textFont);
			if (winningPlayer == USER){
				//If player won
				g.drawString("You WON!", DIMENSIONS_SIZE/2 - 50, DIMENSIONS_SIZE/2);
				playerWins++;
			}
			if (winningPlayer == AI){
				//If player lost
				g.drawString("You LOST!", DIMENSIONS_SIZE/2 - 50, DIMENSIONS_SIZE/2);
				aiWins++;
			}
		}
		if (gameTie){
			//If it's a tie
			Font textFont = new Font("Monotype Corsiva", Font.PLAIN, 30);
			g.setFont(textFont);
			g.drawString("It's a tie!", DIMENSIONS_SIZE/2 - 50, DIMENSIONS_SIZE/2);
			tieGames++;
		}
	}
	private void paintGrid(Graphics g){
		//Draws the grid
		g.setColor(Color.GREEN);
		for (int i = 1; i<DIVISIONS; i++){
			g.drawLine(i*DIVISION_DIMENSION, 0, i*DIVISION_DIMENSION, DIMENSIONS_SIZE);
			g.drawLine(0, i*DIVISION_DIMENSION, DIMENSIONS_SIZE, i*DIVISION_DIMENSION);
		}
	}
	
	private void paintGame(Graphics g){
		//Draws the current board
		g.setColor(Color.RED);
		g.fillRect(0, 0, 600, 600);
		
		//Search through for the player/AI blocks, paint them blue/black.
		for (int i = 0; i<DIVISIONS; i++){
			for(int j = 0; j<DIVISIONS; j++){
				int initialXCoord = i*200;
				int initialYCoord = j*200;
				if (gameArray[i][j] == USER){
					g.setColor(Color.BLUE);
					g.fillRect(initialXCoord, initialYCoord,  DIVISION_DIMENSION, DIVISION_DIMENSION);
				}
				if (gameArray[i][j] == AI){
					g.setColor(Color.BLACK);
					g.fillRect(initialXCoord, initialYCoord,  DIVISION_DIMENSION,  DIVISION_DIMENSION);
				}
			}
		}
	}
	
	private void AISelect() {
		//Runs the AI's turn
		Random generator = new Random();
		currentPlayer = AI;
		int guessAttempts = 0;
		boolean paint = true;//If it remains true then the board will be repainted. Turns false when the AI can't move.
		
		if (!gameWon){
		do {
			//Logic for next AI Guess
			int playerBoxes = 0;
			int aiBoxes = 0;
			int nextGuessRow = 0;
			int nextGuessColumn = 0;
			int boxesLeft = 0;
			int xGuessWin = -1;
			int yGuessWin = -1;

			xGuess = generator.nextInt(DIVISIONS);
			yGuess = generator.nextInt(DIVISIONS);
			
			//Check whether any row has all but 1 box filled in all by either player. Moves at the empty space if so.
			
			for (int i = 0; i<DIVISIONS; i++){//Column
				for (int j = 0; j<DIVISIONS; j++){//Row
					if (gameArray[i][j] == USER){
						//Add up user's boxes
						playerBoxes++;
					}
					if (gameArray[i][j] == AI){
						//Add up AI's boxes
						aiBoxes++;
					}
					if (gameArray[i][j] == NEITHER){
						//Add up empty boxes and store the empty spot
						boxesLeft++;
						nextGuessRow = j;
					}
				}
				if (playerBoxes == DIVISIONS - 1 && boxesLeft >0){
					//Guess to block the user
					xGuess = i;
					yGuess = nextGuessRow;
				}
				if (aiBoxes == DIVISIONS - 1 && boxesLeft >0){
					//Guess to win the game
					xGuessWin = i;
					yGuessWin = nextGuessRow;
				}
				playerBoxes = 0;
				aiBoxes = 0;
				boxesLeft = 0;
			}
			
			
			//Check whether any column has all but 1 box filled in all by either player. Moves there if so.
			for (int j = 0; j<DIVISIONS; j++){//Row
				for (int i = 0; i<DIVISIONS; i++){//Column
					if (gameArray[i][j] == USER){
						//Add up user boxes in that column
						playerBoxes++;
					}
					if (gameArray[i][j] == AI){
						aiBoxes++;
					}
					if (gameArray[i][j] == NEITHER){
						boxesLeft++;
						nextGuessColumn = i;
					}
				}
				if (playerBoxes == DIVISIONS - 1 && boxesLeft >0){
					//Guess to block
					xGuess = nextGuessColumn;
					yGuess = j;
				}
				if (aiBoxes == DIVISIONS - 1 && boxesLeft >0){
					//Guess to win the game
					xGuessWin = nextGuessColumn;
					yGuessWin = j;
				}
				playerBoxes = 0;
				aiBoxes = 0;
				boxesLeft = 0;
			}
			
			//Check whether the top left to lower right diagonal has all but 1 box filled in
			for (int i = 0; i<DIVISIONS; i++){
				if (gameArray[i][i] == USER){
					playerBoxes++;
				}
				if (gameArray[i][2-i] == AI){
					aiBoxes++;
				}
				if (gameArray[i][i] == NEITHER){
					boxesLeft++;
					nextGuessRow = i;
				}
			}
			if (playerBoxes == DIVISIONS - 1 && boxesLeft >0){
				xGuess = nextGuessRow;
				yGuess = nextGuessRow;
			}
			if (aiBoxes == DIVISIONS - 1 && boxesLeft >0){
				xGuessWin = nextGuessRow;
				yGuessWin = nextGuessRow;
			}
			playerBoxes = 0;
			aiBoxes = 0;
			boxesLeft = 0;
			
			//Check whether the top left to lower right diagonal has all but 1 box filled in
			for (int i = 0; i<DIVISIONS; i++){
				if (gameArray[i][2-i] == USER){
					playerBoxes++;
				}
				if (gameArray[i][2-i] == AI){
					aiBoxes++;
				}
				if (gameArray[i][2-i] == NEITHER){
					boxesLeft++;
					nextGuessRow = i;
				}
			}
			if (playerBoxes == DIVISIONS - 1 && boxesLeft >0){
				xGuess = nextGuessRow;
				yGuess = 2-nextGuessRow;
			}
			if (aiBoxes == DIVISIONS - 1 && boxesLeft >0){
				xGuessWin = nextGuessRow;
				yGuessWin = 2-nextGuessRow;
			}
			playerBoxes = 0;
			aiBoxes = 0;
			boxesLeft = 0;
			
			if (yGuessWin != -1 && xGuessWin != -1){
				xGuess = xGuessWin;
				yGuess = yGuessWin;
			}
		
			guessAttempts++;//Iterate guess count. Acts as a failsafe to avoid perma-while looping. Results in a tie.
			if (guessAttempts >= DIVISIONS*DIVISIONS){
				paint = false;
				gameTie = true;
			}
			} while (gameArray[xGuess][yGuess] != 0  && (guessAttempts <= (DIVISIONS * DIVISIONS)));
			
			//When the AI makes a guess, update the array and repaint the game board.
			if (paint){
				gameArray[xGuess][yGuess] = 2;
			}
			guessAttempts = 0;
			}
		
			checkVictory();//Checks whether the AI won.
			repaint();
	}
	
	private void checkVictory() {
		//Looks at the current xGuess and yGuess, checks the vertical, horizontal and vertical lines.
		//If any set matches the current player the game is won.
		boolean currentWin = true;
		
		//Vertical win
		for (int i = 0; i<DIVISIONS; i++){
			if (gameArray[i][yGuess] != currentPlayer){
				currentWin = false;
			}
		}
		if (currentWin){
			gameWon = true;
		}
		
		//Horizontal win
		currentWin = true;
		for (int i = 0; i<DIVISIONS; i++){
			if (gameArray[xGuess][i] != currentPlayer){
				currentWin = false;
			}
		}
		if (currentWin){
			gameWon = true;
		}
		
		//Top left bottom right diagonal win
		currentWin = true;
		if (xGuess == yGuess){
		for (int i = 0; i<DIVISIONS; i++){
			if (gameArray[i][i] != currentPlayer){
				currentWin = false;
			}
		}
		if (currentWin){
			gameWon = true;
		}
		}
		
		//Bottom left top right diagonal win
		currentWin = true;
		if (yGuess + xGuess == 2){
			for (int i = 0; i<DIVISIONS; i++){
				if (gameArray[i][2-i] != currentPlayer){
					currentWin = false;
				}
			}
			if (currentWin){
				gameWon = true;
			}
		}
		
	}


private class MouseHandler implements MouseListener {
	//Handles user click input
	public MouseHandler(TicTacToePanel ticTacToePanel) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameWon){
			int xCoord = e.getX();
			int yCoord = e.getY();
		
			//If the click is within our playing board
			if (xCoord < DIMENSIONS_SIZE && yCoord < DIMENSIONS_SIZE){
				//Then our X/Y section is the click's X/Y co-ord integer divided by the width of each box
				int xSection = xCoord / DIVISION_DIMENSION;//=0,1,2...
				int ySection = yCoord / DIVISION_DIMENSION;
		
				if (gameArray[xSection][ySection] == NEITHER){
					//If the box is blank the user can play there. If not, make them pick again.
					gameArray[xSection][ySection] = USER;
					xGuess = xSection;
					yGuess = ySection;

					checkVictory();//Checks whether the player won
					if (gameWon){
						winningPlayer = USER;
					}
					repaint();//If the game is not won we keep playing, move on to the AI's turn.
					AISelect();
					if (gameWon && winningPlayer != USER){
						//Give the AI credit where credit's due! He won TTT while going second...
						winningPlayer = AI;
					}
			
				}
				currentPlayer = USER;
			}
		}
	}

	



	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {	
	}
	
	
}



}
