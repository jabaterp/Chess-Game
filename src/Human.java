


public class Human {

	static char[] captured;
	static int points;
	final static int empty=0,friend=1,enemy=2;

	public static Board move(Board board,int[] loc,int[] move) throws InterruptedException{
		if (emptyOrEnemy(board,move)==enemy||emptyOrEnemy(board,move)==empty){
			if (canMovePiece(board,loc,move)){
				Board newBoard= board.makeChange(loc, move,Board.pieceColor.Black);
				newBoard.changed=true;
				return newBoard;
			}
			else 
				return board;

		}
		return board;
	}

	public static boolean canMovePiece(Board board,int[] loc, int[] move){
		if(loc[0]==move[0] && loc[1]==move[1])
			return false;
		if (loc[0]>=8 && loc[0]<0 && loc[1]>=8 && loc[1]<0)
			return false; 
		if(loc[0]==move[0] && loc[1]==move[1])
			return false;

		switch(board.getPiece(loc)){
		case Empty:
			return false;

		case bBishop:
			if (Math.abs(loc[0]-move[0])==Math.abs(loc[1]-move[1]) && !pieceBlocking(board,Board.piece.Empty,loc,move))
				return true;
			else return false;
		case bKing:
			if ((((Math.abs(loc[0]-move[0])==1) && (Math.abs(loc[1]-move[1])==1))||
					((Math.abs(loc[0]-move[0])==0) && (Math.abs(loc[1]-move[1])==1))||
					((Math.abs(loc[0]-move[0])==1) && (Math.abs(loc[1]-move[1])==0)))
					&& !pieceBlocking(board,Board.piece.Empty,loc, move))
				return true;
			else return false;
		case bKnight: 
			if ((Math.abs(loc[0]-move[0])==1 && Math.abs(loc[1]-move[1])==2 && 
			!pieceBlocking(board,Board.piece.Empty, loc,move))||
			(Math.abs(loc[1]-move[1])==1 && Math.abs(loc[0]-move[0])==2 &&
			!pieceBlocking(board,Board.piece.Empty, loc,move)))
				return true;
			else return false;
		case bPawn:
			if(loc[0]==1 && loc[1]==move[1]&& loc[0]-move[0]==-2 && !pieceBlocking(board,Board.piece.Empty,loc,move))
				return true;

			else if (loc[0]-move[0]==-1 && loc[1]==move[1]&& !pieceBlocking(board,Board.piece.Empty,loc,move))
				return true;
			else if (loc[0]-move[0]==-1 && Math.abs(loc[1]-move[1])==1 && emptyOrEnemy(board,move)==enemy)
				return true;
			else return false;
		case bQueen:
			if (    ((loc[0]-move[0]==0 && !pieceBlocking(board,Board.piece.Empty,loc,move))||
					(loc[1]-move[1]==0 && !pieceBlocking(board,Board.piece.Empty,loc,move)))
					||(Math.abs(loc[0]-move[0])==Math.abs(loc[1]-move[1]) && !pieceBlocking(board,Board.piece.Empty,loc,move)))
				return true;
			else return false;
		case bRook:
			if ((loc[0]-move[0]==0 && !pieceBlocking(board,Board.piece.Empty,loc,move))||
					(loc[1]-move[1]==0 && !pieceBlocking(board,Board.piece.Empty,loc,move)))
				return true;
			else return false;
		default:
			return false;

		}
	}

	public static boolean pieceBlocking(Board board,Board.piece piece, int[] loc, int[] move){

		Board.piece toCheck;
		if (piece==Board.piece.Empty)
			toCheck=board.getPiece(loc);
		else
			toCheck=piece;

		switch(toCheck){

		case bBishop:
			for(int i=1;i<=Math.abs(loc[0]-move[0]);i++){
				if (loc[0]-move[0]>0 && loc[1]-move[1]>0){
					if (Board.pieceColor.Empty==board.getPieceColor(new int[]{loc[0]-i,loc[1]-i})){
						if(loc[0]-i==move[0] &&loc[1]-i==move[1])
							return false;
					}else if((loc[0]-i==move[0] &&loc[1]-i==move[1]) &&
							Board.pieceColor.White==board.getPieceColor(move))
						return false;
					else return true;
				}		
				else if (loc[0]-move[0]>0 && loc[1]-move[1]<0){
					if (Board.pieceColor.Empty==board.getPieceColor(new int[]{loc[0]-i,loc[1]+i})){
						if(loc[0]-i==move[0] &&loc[1]+i==move[1])
							return false;
					}
					else if((loc[0]-i==move[0] &&loc[1]+i==move[1]) &&
							Board.pieceColor.White==board.getPieceColor(move))
						return false;
					else return true;
				}		

				else if (loc[0]-move[0]<0 && loc[1]-move[1]>0){
					if (Board.pieceColor.Empty==board.getPieceColor(new int[]{loc[0]+i,loc[1]-i})){
						if(loc[0]+i==move[0] &&loc[1]-i==move[1])
							return false;
					}
					else if((loc[0]+i==move[0] &&loc[1]-i==move[1]) &&
							Board.pieceColor.White==board.getPieceColor(move))
						return false;
					else return true;
				}

				else if (loc[0]-move[0]<0 && loc[1]-move[1]<0){
					if (Board.pieceColor.Empty==board.getPieceColor(new int[]{loc[0]+i,loc[1]+i})){
						if(loc[0]+i==move[0] &&loc[1]+i==move[1])
							return false;

					}
					else if((loc[0]+i==move[0] &&loc[1]+i==move[1]) &&
							Board.pieceColor.White==board.getPieceColor(move))
						return false;
					else return true;
				}	
			}
			return true;
		case bKing:
			if(board.getPieceColor(move)==Board.pieceColor.White ||
			board.getPieceColor(move)==Board.pieceColor.Empty)
				return false;
		case bKnight: if(board.getPieceColor(move)==Board.pieceColor.White ||
				board.getPieceColor(move)==Board.pieceColor.Empty)
			return false;
		case bPawn:

			if (loc[0]==1 && loc[0]-move[0]==-2 && board.getPieceColor(move)==Board.pieceColor.Empty){
				int[] check=new int[]{2,loc[1]};

				if (board.getPieceColor(check)==Board.pieceColor.Empty)
					return false;


			}else if(loc[0]-move[0]==-1 && loc[1]==move[1] && board.getPieceColor(move)==Board.pieceColor.Empty)
				return false;

			return true;
		case bQueen:
			if ((loc[0]-move[0]==0)||(loc[1]-move[1]==0))
				return pieceBlocking(board,Board.piece.bRook,loc,move);
			else return pieceBlocking(board,Board.piece.bBishop,loc,move);

		case bRook:
			if (loc[0]-move[0]==0 ){
				for (int i=1;i<=Math.abs(loc[1]-move[1]);i++){
					int[] check;
					if (loc[1]-move[1]>0)
						check=new int[]{loc[0],loc[1]-i};
					else 
						check=new int[]{loc[0],loc[1]+i};
					if (board.getPiece(check)==Board.piece.Empty ||
							(board.getPieceColor(check)==Board.pieceColor.White &&	check[1]==move[1]))
						continue;
					else
						return true;

				}
				return false;
			}else if(loc[1]-move[1]==0){

				for (int i=1;i<=Math.abs(loc[0]-move[0]);i++){
					int[] check;
					if (loc[0]-move[0]>0)
						check=new int[]{loc[0]-i,loc[1]};
					else 
						check=new int[]{loc[0]+i,loc[1]};
					if (board.getPiece(check)==Board.piece.Empty ||
							(board.getPieceColor(check)==Board.pieceColor.White &&	check[0]==move[0]))
						continue;
					else
						return true;

				}
				return false;
			}


		default:

			return true;

		}
	}


	public static int emptyOrEnemy(Board board, int[] loc){
		switch(board.getPiece(loc)){
		case Empty:
			return empty;
		case bBishop: 
			return friend;
		case bKing:
			return friend;
		case bKnight:
			return friend;
		case bPawn:
			return friend;
		case bQueen:
			return friend;
		case bRook:
			return friend;
		case wBishop:
			return enemy;
		case wKing:
			return enemy;
		case wKnight:
			return enemy;
		case wPawn:
			return enemy;
		case wQueen:
			return enemy;
		case wRook:
			return enemy;

		}
		return 10;
	}
}
