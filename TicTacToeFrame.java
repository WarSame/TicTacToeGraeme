import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class TicTacToeFrame extends JFrame {
	//Creator: Graeme Cliffe
	//Description: A tic tac toe game. Includes stats, an AI and a reset/new game feature.
	//Consists of a JFrame containing 3 JPanels.
	private static final long serialVersionUID = 1L;
	public TicTacToeFrame(){
		//Create initial frame
		setTitle("TicTacToe - Graeme Cliffe");
		setSize(750, 662);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//Create the game's TTTPanel
		final TicTacToePanel ticTacToe = new TicTacToePanel();
		add(ticTacToe, BorderLayout.CENTER);
		ticTacToe.requestFocus();
		
		//Create the reset button's panel
		JPanel resetPanel = new JPanel();
		resetPanel.setBackground(Color.GRAY);
		JButton resetButton = new JButton("Reset/New Game");
		resetPanel.add(resetButton);
		add(resetPanel, BorderLayout.SOUTH);
		
		//Reset logic. Calls reset() in TTTPanel
		resetButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ticTacToe.reset();
				ticTacToe.repaint();
			}
			
		});
		setVisible(true);
	}
	public static void main(String[] args) {
		new TicTacToeFrame();
	}

	
}
