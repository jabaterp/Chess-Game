package PlayChess;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import PlayChess.Board.piece;


public class Play {

	final static boolean human=true;
	final static boolean comp=false;
	private static boolean turn=human;
	private static boolean play=true;
	private static Scanner sc=new Scanner(System.in);

	public static void main(String[] args) throws InterruptedException{
		play();
	}
	
	public static void newGame() throws InterruptedException {
		turn=human;
		Computer.newGame();
		Human.newGame();
		Board.newGame();
		play();
	}

	public static void play() throws InterruptedException{
		Board newBoard, newBoard2;
		Board.board=new Board();
		Board.oldBoards.add(Board.board.boardCopy());

		Display.displayGui(Board.board); 

		while(play){
			if (turn==comp){
				Computer comp=new Computer();

				newBoard=comp.makeMove(Board.board);
				int i =Board.oldBoards.size()-1;
				while(i>Board.boardNum){
					Board.oldBoards.remove(i);
					i--;
				}
				Board.oldBoards.add(newBoard.boardCopy());
				Display.makeChange(newBoard);
				Board.boardNum++;
				if(checkWin(turn)) {
					System.out.println();
					System.out.println("I'm sorry you lost. Would you like to play again? (yes/no)");
					String playAgain=sc.nextLine();
					if(playAgain.trim().equals("yes")) {
						System.out.println("Alright here we go!");
						newGame();
					}else {
						play=false;						
					}
				}
				turn=human;
			}
			else{
				Board.board.changed=false;
				while(Display.first==null) {
					TimeUnit.MICROSECONDS.sleep(500);					
				}

				int[] loc= new int[2];
				int[] move=new int[2];
				loc[0]=Display.first[0];
				loc[1]=Display.first[1];

				while(Display.second==null) {
					TimeUnit.MICROSECONDS.sleep(500);					
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
						TimeUnit.MICROSECONDS.sleep(500);
					}
					loc[0]=Display.first[0];
					loc[1]=Display.first[1];
					while(Display.second==null){
						TimeUnit.MICROSECONDS.sleep(500);
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
				int i=Board.oldBoards.size()-1;
				while(i>Board.boardNum){
					Board.oldBoards.remove(i);
					i--;
				}
				copyBoardList();
				Board.oldBoards.add(newBoard2.boardCopy());
				Board.boardNum++;
				if(checkWin(turn)) {
					System.out.println();
					System.out.println("Congratulations! You won! Would you like to play again? (yes/no)");
					String playAgain=sc.nextLine();
					if(playAgain.trim().equals("yes")) {
						System.out.println("Alright here we go!");
						newGame();
					}else {
						play=false;						
					}
				}
				turn=comp;
			}
		}
	}

	public static boolean checkWin(boolean turn) {
		if(turn==comp) {
			if(Computer.captured.contains(piece.bKing)) {
				return true;
			}
		}else {
			if(Human.captured.contains(piece.wKing)) {
				return true;
			}
		}
		return false;
	}

	public static void copyBoardList(){
		ArrayList<Board> boardCopy=new ArrayList<Board>();
		for(int i=0; i<=Board.boardNum; i++)
			boardCopy.add(Board.oldBoards.get(i));
		Board.oldBoards=boardCopy;
	}
}
