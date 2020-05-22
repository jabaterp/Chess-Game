package PlayChess;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.ImageIcon;

public class Board {

	enum piece{Empty,bPawn,wPawn,bRook,wRook,bKnight,wKnight,bBishop,wBishop,bKing,wKing,bQueen,wQueen};
	enum pieceColor{White,Black,Empty}
	enum spaceColor{Blue,Brown,Empty}
	boolean changed=false;
	public static Board board;
	public static ArrayList<Board> oldBoards=new ArrayList<Board>();
	public static int boardNum=0;
	Space[][] brd;

	public static void newGame() {
		board=new Board();
		oldBoards=new ArrayList<Board>();
		boardNum=0;
	}

	public class Space{	
		int[] location;
		piece piece;
		pieceColor pieceColor;
		int pieceVal;
		spaceColor spaceColor; 

		public Space(spaceColor spaceColor,pieceColor pieceColor, int[] location, piece piece, int val){
			this.pieceColor=pieceColor;
			this.location=location;
			this.piece=piece;
			this.spaceColor=spaceColor;
			pieceVal=val;
		}
	}


	public Board boardCopy(){
		Board newGuy=new Board();
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				newGuy.brd[i][j]=new Space(this.brd[i][j].spaceColor,this.brd[i][j].pieceColor,
						this.brd[i][j].location,this.brd[i][j].piece,this.brd[i][j].pieceVal);
			}
		}
		return newGuy;
	}

	public Board(){
		brd=new Space[8][8];
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				if(i==6){
					if (j%2==1){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Brown,pieceColor.White,loc,piece.wPawn,1);
					}
					else{
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Blue,pieceColor.White,loc,piece.wPawn,1);
					}
				}
				else if(i==1) {
					if (j%2==0){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Brown,pieceColor.Black,loc,piece.bPawn,1);
					}
					else{
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Blue,pieceColor.Black,loc,piece.bPawn,1);
					}
				}
				else if(i==7) {
					if (j==0){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Brown,pieceColor.White,loc,piece.wRook,4);
					}
					else if(j==1){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Blue,pieceColor.White,loc,piece.wKnight,3);
					}
					else if(j==2){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Brown,pieceColor.White,loc,piece.wBishop,3);
					}
					else if(j==3){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Blue,pieceColor.White,loc,piece.wQueen,8);
					}
					else if(j==4){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Brown,pieceColor.White,loc,piece.wKing,10);
					}
					if (j==5){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Blue,pieceColor.White,loc,piece.wBishop,3);
					}
					else if(j==6){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Brown,pieceColor.White,loc,piece.wKnight,3);
					}
					else if(j==7){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Blue,pieceColor.White,loc,piece.wRook,4);
					}
				}
				else if (i==0){
					if (j==0){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Blue,pieceColor.Black,loc,piece.bRook,4);
					}
					else if(j==1){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Brown,pieceColor.Black,loc,piece.bKnight,3);
					}
					else if(j==2){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Blue,pieceColor.Black,loc,piece.bBishop,3);
					}
					else if(j==3){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Brown,pieceColor.Black,loc,piece.bQueen,8);
					}
					else if(j==4){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Blue,pieceColor.Black,loc,piece.bKing,10);
					}
					else if(j==5){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Brown,pieceColor.Black,loc,piece.bBishop,3);
					}
					else if(j==6){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Blue,pieceColor.Black,loc,piece.bKnight,3);
					}
					else if(j==7){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Brown,pieceColor.Black,loc,piece.bRook,4);
					}
				}
				else{
					if ((i%2==0 && j%2==1)||(i%2==1 && j%2==0)){
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Brown,pieceColor.Empty,loc,piece.Empty,0);
					}
					else{
						int[] loc=new int[]{i,j};
						brd[i][j]=new Space(spaceColor.Blue,pieceColor.Empty,loc,piece.Empty,0);
					}
				}
			}
		}

	}

	public piece getPiece(int[] loc){
		return brd[loc[0]][loc[1]].piece;
	}

	public Board erasePiece(int[] loc){
		brd[loc[0]][loc[1]].piece=piece.Empty;
		brd[loc[0]][loc[1]].pieceColor=pieceColor.Empty;
		return this;
	}
	public Board fakePiece(int[] loc,boolean color){
		if(color){
			brd[loc[0]][loc[1]].piece=piece.wBishop;
			brd[loc[0]][loc[1]].pieceColor=pieceColor.White;
		}else{
			brd[loc[0]][loc[1]].piece=piece.bBishop;
			brd[loc[0]][loc[1]].pieceColor=pieceColor.Black;			
		}
		return this;
	}

	public pieceColor getPieceColor(int[] loc){
		return brd[loc[0]][loc[1]].pieceColor;
	}
	public spaceColor getSpaceColor(int[] loc){
		return brd[loc[0]][loc[1]].spaceColor;
	}

	public int getVal(int[] loc){
		return brd[loc[0]][loc[1]].pieceVal;
	}

	public Board makeChange(int[] loc, int[] move,pieceColor color) throws InterruptedException{
		piece piece=brd[loc[0]][loc[1]].piece;
		brd[move[0]][move[1]].pieceVal= getVal(loc);
		brd[loc[0]][loc[1]].piece=piece.Empty;
		brd[loc[0]][loc[1]].pieceColor=pieceColor.Empty;
		brd[move[0]][move[1]].piece=piece;
		brd[move[0]][move[1]].pieceColor=color;
		brd[loc[0]][loc[1]].pieceVal=0;
		changed=true;
		if(piece==Board.piece.wPawn && move[0]==0) {
			brd[move[0]][move[1]].piece=Board.piece.wQueen;
		}else if (piece ==Board.piece.bPawn && move[0]==7){
			brd[move[0]][move[1]].piece=Board.piece.bQueen;
		}

		return this;
	}


}

