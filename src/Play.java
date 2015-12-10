import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JTextField;


public class Play {

	final static boolean human=true;
	final static boolean comp=false;
	private static boolean turn=human;
	private static boolean play=true;

	public static void main(String[] args) throws InterruptedException{
		Board newBoard, newBoard2;
		Board.board=new Board();
		Board.oldBoards.add(Board.board.boardCopy());
		Display.displayGui(Board.board); 

		while(play){
			if (turn==comp){
				Computer comp=new Computer();
				
				newBoard=comp.makeMove(Board.board);
				Board.oldBoards.add(newBoard.boardCopy());
				Display.makeChange(newBoard);
				Board.boardNum++;
				turn=human;
			}
			else{
				Board.board.changed=false;
				while(Display.first==null){

				}

				int[] loc= new int[2];
				int[] move=new int[2];
				loc[0]=Display.first[0];
				loc[1]=Display.first[1];
				while(Display.second==null){

				}
				move[0]=Display.second[0];
				move[1]=Display.second[1];
				Display.first=null;
				Display.second=null;

				
				newBoard2=Human.move(Board.board, loc, move);

				while (!newBoard2.changed){		

					System.out.println("That is not a valid move. Please enter a valid move.");
					Display.makeChange(newBoard2);


					while (Display.first==null){

					}
					loc[0]=Display.first[0];
					loc[1]=Display.first[1];
					while(Display.second==null){

					}
					move[0]=Display.second[0];
					move[1]=Display.second[1];
					Display.first=null;
					Display.second=null;
					newBoard2=Human.move(Board.board, loc, move);
				}
				newBoard2=Human.move(Board.board, loc, move);
				Board.board=newBoard2;
				Display.makeChange(newBoard2);
				Board.oldBoards.add(newBoard2.boardCopy());
				Board.boardNum++;
				turn=comp;
			}
		}
	}
}
