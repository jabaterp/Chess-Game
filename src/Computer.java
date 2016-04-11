
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;




public class Computer { 

	final int pawn=1,rook=4,knight=3,bishop=3,queen=8,king=10;
	final static int empty=0,friend=1,enemy=2;

	static ArrayList<int[]> compLocs;
	static ArrayList<int[]> humanLocs;
	static int humans=0;
	static int comps=0;
	private final boolean compType=true;
	private final boolean humType=false;


	public Board makeMove(Board board) throws InterruptedException{
		getLocs(board,true);
		ArrayList<int[][]> danger=inDanger(board);
		if(danger.isEmpty()){

			//attack
			Move move= canAttackFree(board);
			if(move==null)
				move=canSacrifice(board);
			if(move==null)
				move=bestAttack(board,allMoves(board,compType));
			//Move move= randomMove(board);
			return board.makeChange(move.loc, move.mov, Board.pieceColor.White);
		}else{
			Move move=dangerAttackFree(board);
			if(move!=null){
				return board.makeChange(move.loc, move.mov, Board.pieceColor.White);
			}
			move=dangerCanKillHigher(board);

			if(move!=null){
				Board ans=board.makeChange(move.loc, move.mov, Board.pieceColor.White);
				return ans;
			}else{
				Move curr=defend(board);
				Board ans=board.makeChange(curr.loc, curr.mov, Board.pieceColor.White);
				return ans;
			}
		}
	}

	public class Move{
		Board.piece name;
		int compVal;
		int[] loc;
		Board.piece humName;
		int humVal;
		int[] mov;

		public Move(Board board,int[] loc, int[] mov){
			this.name=board.getPiece(loc); 
			this.humName=board.getPiece(mov);
			this.compVal=board.getVal(loc);
			if(board.getPieceColor(mov)==Board.pieceColor.Black)
				this.humVal=board.getVal(mov);
			else
				this.humVal=0;
			this.mov=mov;
			this.loc=loc;
		}

		public boolean compareMoves(Move move){
			if(move.loc[0]==this.loc[0] && move.loc[1]==this.loc[1])
				return true;
			else
				return false;
		}
	}

	public ArrayList<ArrayList<int[]>> getLocs(Board board, boolean setVars){
		ArrayList<ArrayList<int[]>> ans =new ArrayList<ArrayList<int[]>>();
		ArrayList<int[]> computerLocs=new ArrayList<int[]>();
		ArrayList<int[]> humLocs=new ArrayList<int[]>();
		int hums=0;
		int cs=0;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				if(board.getPieceColor(new int[]{i,j})==Board.pieceColor.Black){
					hums++;
					humLocs.add(new int[]{i,j});
				}else if(board.getPieceColor(new int[]{i,j})==Board.pieceColor.White){
					cs++;
					computerLocs.add(new int[]{i,j});
				}
			}
		}
		if(setVars){
			humans=hums;
			comps=cs;
			humanLocs=humLocs;
			compLocs=computerLocs;
		}
		ans.add(computerLocs);
		ans.add(humLocs);
		return ans;
	}

	public Move randomMove(Board board) throws InterruptedException{

		ArrayList<Move> moves=allMoves(board,compType);
		return bestAttack(board,moves);
	}

	public static boolean compCanMovePiece(Board board,int[] loc, int[] move){
		if (loc[0]>=8 && loc[0]<0 && loc[1]>=8 && loc[1]<0)
			return false; 
		if(loc[0]==move[0] && loc[1]==move[1])
			return false;

		switch(board.getPiece(loc)){
		case Empty:
			return false;

		case wBishop:
			if (Math.abs(loc[0]-move[0])==Math.abs(loc[1]-move[1]) && !compPieceBlocking(board,Board.piece.Empty,loc,move))
				return true;
			else return false;
		case wKing:
			if ((((Math.abs(loc[0]-move[0])==1) && (Math.abs(loc[1]-move[1])==1))||
					((Math.abs(loc[0]-move[0])==0) && (Math.abs(loc[1]-move[1])==1))||
					((Math.abs(loc[0]-move[0])==1) && (Math.abs(loc[1]-move[1])==0)))
					&& !compPieceBlocking(board,Board.piece.Empty,loc, move))
				return true;
			else return false;
		case wKnight: 
			if ((Math.abs(loc[0]-move[0])==1 && Math.abs(loc[1]-move[1])==2 && 
			!compPieceBlocking(board,Board.piece.Empty, loc,move))||
			(Math.abs(loc[1]-move[1])==1 && Math.abs(loc[0]-move[0])==2 &&
			!compPieceBlocking(board,Board.piece.Empty, loc,move)))
				return true;
			else return false;
		case wPawn:
			if(loc[0]==6 && loc[1]==move[1]&& loc[0]-move[0]==2 && !compPieceBlocking(board,Board.piece.Empty,loc,move))
				return true;

			else if (loc[0]-move[0]==1 && loc[1]==move[1]&& !compPieceBlocking(board,Board.piece.Empty,loc,move))
				return true;
			else if (loc[0]-move[0]==1 && Math.abs(loc[1]-move[1])==1 && compEmptyOrEnemy(board,move)==enemy)
				return true;
			else return false;
		case wQueen:
			if (    ((loc[0]-move[0]==0 && !compPieceBlocking(board,Board.piece.Empty,loc,move))||
					(loc[1]-move[1]==0 && !compPieceBlocking(board,Board.piece.Empty,loc,move)))
					||(Math.abs(loc[0]-move[0])==Math.abs(loc[1]-move[1]) && !compPieceBlocking(board,Board.piece.Empty,loc,move)))
				return true;
			else return false;
		case wRook:
			if ((loc[0]-move[0]==0 && !compPieceBlocking(board,Board.piece.Empty,loc,move))||
					(loc[1]-move[1]==0 && !compPieceBlocking(board,Board.piece.Empty,loc,move)))
				return true;
			else return false;
		default:
			return false;

		}
	}

	public static boolean compPieceBlocking(Board board,Board.piece piece, int[] loc, int[] move){

		Board.piece toCheck;
		if (piece==Board.piece.Empty)
			toCheck=board.getPiece(loc);
		else
			toCheck=piece;

		switch(toCheck){

		case wBishop:
			for(int i=1;i<=Math.abs(loc[0]-move[0]);i++){
				if (loc[0]-move[0]>0 && loc[1]-move[1]>0){
					if (Board.pieceColor.Empty==board.getPieceColor(new int[]{loc[0]-i,loc[1]-i})){
						if(loc[0]-i==move[0] &&loc[1]-i==move[1])
							return false;
					}else if((loc[0]-i==move[0] &&loc[1]-i==move[1]) &&
							Board.pieceColor.Black==board.getPieceColor(move))
						return false;
					else return true;
				}		
				else if (loc[0]-move[0]>0 && loc[1]-move[1]<0){
					if (Board.pieceColor.Empty==board.getPieceColor(new int[]{loc[0]-i,loc[1]+i})){
						if(loc[0]-i==move[0] &&loc[1]+i==move[1])
							return false;
					}
					else if((loc[0]-i==move[0] &&loc[1]+i==move[1]) &&
							Board.pieceColor.Black==board.getPieceColor(move))
						return false;
					else return true;
				}		

				else if (loc[0]-move[0]<0 && loc[1]-move[1]>0){
					if (Board.pieceColor.Empty==board.getPieceColor(new int[]{loc[0]+i,loc[1]-i})){
						if(loc[0]+i==move[0] &&loc[1]-i==move[1])
							return false;
					}
					else if((loc[0]+i==move[0] &&loc[1]-i==move[1]) &&
							Board.pieceColor.Black==board.getPieceColor(move))
						return false;
					else return true;
				}

				else if (loc[0]-move[0]<0 && loc[1]-move[1]<0){
					if (Board.pieceColor.Empty==board.getPieceColor(new int[]{loc[0]+i,loc[1]+i})){
						if(loc[0]+i==move[0] &&loc[1]+i==move[1])
							return false;

					}
					else if((loc[0]+i==move[0] &&loc[1]+i==move[1]) &&
							Board.pieceColor.Black==board.getPieceColor(move))
						return false;
					else return true;
				}	
			}
			return true;
		case wKing:
			if(board.getPieceColor(move)==Board.pieceColor.Black ||
			board.getPieceColor(move)==Board.pieceColor.Empty)
				return false;
		case wKnight: if(board.getPieceColor(move)==Board.pieceColor.Black ||
				board.getPieceColor(move)==Board.pieceColor.Empty)
			return false;
		case wPawn:

			if (loc[0]==6 && loc[0]-move[0]==2 && board.getPieceColor(move)==Board.pieceColor.Empty){
				int[] check=new int[]{5,loc[1]};

				if (board.getPieceColor(check)==Board.pieceColor.Empty)
					return false;


			}else if(loc[0]-move[0]==1 && loc[1]==move[1] && board.getPieceColor(move)==Board.pieceColor.Empty)
				return false;

			return true;
		case wQueen:
			if ((loc[0]-move[0]==0)||(loc[1]-move[1]==0))
				return compPieceBlocking(board,Board.piece.wRook,loc,move);
			else return compPieceBlocking(board,Board.piece.wBishop,loc,move);

		case wRook:
			if (loc[0]-move[0]==0 ){
				for (int i=1;i<=Math.abs(loc[1]-move[1]);i++){
					int[] check;
					if (loc[1]-move[1]>0)
						check=new int[]{loc[0],loc[1]-i};
					else 
						check=new int[]{loc[0],loc[1]+i};
					if (board.getPiece(check)==Board.piece.Empty ||
							(board.getPieceColor(check)==Board.pieceColor.Black &&	check[1]==move[1]))
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
							(board.getPieceColor(check)==Board.pieceColor.Black &&	check[0]==move[0]))
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


	public static int compEmptyOrEnemy(Board board, int[] loc){
		switch(board.getPiece(loc)){
		case Empty:
			return empty;
		case bBishop: 
			return enemy;
		case bKing:
			return enemy;
		case bKnight:
			return enemy;
		case bPawn:
			return enemy;
		case bQueen:
			return enemy;
		case bRook:
			return enemy;
		case wBishop:
			return friend;
		case wKing:
			return friend;
		case wKnight:
			return friend;
		case wPawn:
			return friend;
		case wQueen:
			return friend;
		case wRook:
			return friend;

		}
		return 10;
	}

	public ArrayList<Move> allMoves(Board board, Boolean player){
		ArrayList<Move> moves=new ArrayList<Move>();
		ArrayList<ArrayList<int[]>> locs=getLocs(board, false);
		ArrayList<int[]> allLocs;
		if(player==compType){
			allLocs=locs.get(0);
		}else{
			allLocs=locs.get(1);
		}
		for(int[] loc:allLocs){
			for(int i=0;i<8;i++){
				for(int j=0;j<8;j++){
					if(player==compType){
						if(compCanMovePiece(board,loc,new int[]{i,j}) && board.getVal(loc)!=0)
							moves.add(new Move(board,loc,new int[]{i,j}));
					}else{
						if(Human.canMovePiece(board,loc,new int[]{i,j}) && board.getVal(loc)!=0)
							moves.add(new Move(board,loc,new int[]{i,j}));						
					}
				}
			}
		}

		return moves;
	}

	public ArrayList<Move> takesMostSpace(Board board) throws InterruptedException{
		int humMoves;
		ArrayList<Move> ans=new ArrayList<Move>();
		int minMoves=100;
		ArrayList<Move> moves =allMoves(board,compType);
		Board fake;
		for(Move move:moves){
			fake=board.boardCopy();
			fake.makeChange(move.loc, move.mov, Board.pieceColor.White);
			humMoves = allMoves(fake,humType).size();
			if(humMoves < minMoves){
				minMoves = humMoves;
				ans=new ArrayList<Move>();
				ans.add(move);
			}else if(humMoves == minMoves){
				ans.add(move);
			}
		}

		return ans;
	}

	public int[] highestDanger(Board board, ArrayList<int[][]> ans){
		int[] high=new int[]{};
		int most=0;
		for(int[][] move:ans){

			if(most==0|| board.getVal(move[0])>most){
				high=move[0];
				most=board.getVal(move[0]);
			}

		}
		return high;
	}

	public ArrayList<Move> safestMoves(Board board,ArrayList<Move> moves) throws InterruptedException{
		Board newBoard;
		int min=20;
		ArrayList<Move> ans=new ArrayList<Move>();
		Move best=moves.get(0);
		for(Move move:moves){
			newBoard=board.boardCopy();
			newBoard=newBoard.makeChange(move.loc, move.mov, Board.pieceColor.White);
			int cost=dangerValue(newBoard,inDanger(newBoard));
			if(cost<min	){
				min=cost;
				best=move;
				if(cost==0)
					ans.add(move);
			}else if(cost==0){
				ans.add(move);
			}
		}
		if(ans.isEmpty())
			ans.add(best);
		return ans;
	}

	public boolean isDefended(Board board,int[] comp,int[] human){
		int dangerVal=board.getVal(comp);
		Board newBoard=board.boardCopy();
		newBoard.erasePiece(comp);
		newBoard.fakePiece(comp,humType);

		ArrayList<Move> moves=allMoves(newBoard,compType);

		for(Move jab:moves){
			if(human!=null){
				if(jab.mov[0]==comp[0] && jab.mov[1]==comp[1] && newBoard.getVal(human)>= dangerVal)
					return true;
			}else{
				if(jab.mov[0]==comp[0] && jab.mov[1]==comp[1])
					return true;
			}
		}

		return false;
	}
	public Move dangerCanAttackHigher(Board board) throws InterruptedException{
		Move ans=null;
		int high=board.getVal(highestDanger(board,inDanger(board)));

		for(Move move:allMoves(board,compType)){
			Board newBoard=board.boardCopy();
			newBoard=newBoard.makeChange(move.loc, move.mov, Board.pieceColor.White);
			Move retVal=dangerCanKillHigher(newBoard);
			if(retVal!=null){
				if(newBoard.getVal(retVal.mov)>high)
					ans=move;
				high=newBoard.getVal(retVal.mov);
			}				
		}

		return ans;		
	}

	public Move dangerCanKillHigher(Board board) throws InterruptedException{
		Move ans=null;
		int high=board.getVal(highestDanger(board,inDanger(board)));
		Board fakeBoard;
		for(Move move:allMoves(board,compType)){
			fakeBoard=board.boardCopy();
			fakeBoard.makeChange(move.loc,move.mov,Board.pieceColor.White);
			int[] fakeHigh=highestDanger(fakeBoard,inDanger(fakeBoard));
			if(move.humVal>high && fakeBoard.getVal(fakeHigh)<move.humVal){// ||(fakeHigh.length!=0 &&fakeBoard.getVal(fakeHigh)<high)){
				ans= move;
			}
		}
		return ans;
	}

	public Move dangerAttackFree(Board board) throws InterruptedException{

		ArrayList<int[][]> danger=inDanger(board);
		int count=0;
		while(count<danger.size()){
			int initial=count;
			int[] curr=danger.get(count)[1];
			Board fake=board.boardCopy();
			fake.fakePiece(curr,compType);
			for(int[] jab: humanLocs){
				int[] jim=new int[]{jab[0],jab[1]};
				int[] bob=new int[]{curr[0],curr[1]};
				if(Human.canMovePiece(fake, jim, bob)){
					return null;
				}
			}
			if(initial==count){
				count++;
			}
		}
		ArrayList<Move> moves=new ArrayList<Move>();
		for(int[][] dang:danger){
			for(Move move:allMoves(board,compType)){
				if(move.mov[0]==dang[1][0] && move.mov[1]==dang[1][1]){
					moves.add(move);
				}
			}
		}
		if(!moves.isEmpty())
			return bestAttack(board,moves);
		else 
			return null;
	}

	public Move canAttackFree(Board board) throws InterruptedException{
		Move ans=null;
		ArrayList<Move> allmoves=allMoves(board,compType);
		for(Move move: allmoves){
			if(move.humVal>0){
				Board brd=board.boardCopy();
				brd.makeChange(move.loc, move.mov, Board.pieceColor.White);
				if(inDanger(brd).isEmpty()){
					if(ans==null || move.humVal>ans.humVal)
						ans=move;
				}
			}

		}
		return ans;
	}

	public Move canSacrifice(Board board) throws InterruptedException{
		Move ans=null;
		for(Move move:allMoves(board,compType)){
			if(move.humVal>move.compVal && (ans==null || move.humVal>ans.humVal)){
				Board fake=board.boardCopy();
				fake.makeChange(move.loc, move.mov, Board.pieceColor.White);
				int [] high=highestDanger(fake,inDanger(fake));
				if(fake.getVal(high)<move.humVal)
					ans=move;
			}
		}
		return ans;
	}

	public Move bestAttack(Board board,ArrayList<Move> moves) throws InterruptedException{

		int high=0;
		Move ans=null;
		for(Move move1:moves){
			int count=0;
			Board brd=board.boardCopy();
			brd.makeChange(move1.loc, move1.mov, Board.pieceColor.White);
			ArrayList<Move> retMoves=allMoves(brd,compType);
			for(Move move2:retMoves){
				if(humanLocs.contains(new int[]{move2.mov[0],move2.mov[1]}) && inDanger(brd).size()==0)
					count++;
			}
			if(count>high){
				high=count;
				ans=move1;
			}
		}
		if(ans==null){
			Random rand=new Random();
			ArrayList<Move> safeMoves=safestMoves(board,allMoves(board,compType));
			ArrayList<Move> movesMostSpace=takesMostSpace(board);
			Move ret=safeAndSpaceMove(safeMoves,movesMostSpace);
			if(ret!=null)
				return ret;

			int n=rand.nextInt(safeMoves.size());		
			return safeMoves.get(n); 
		}else
			return ans;
	}

	public Move safeAndSpaceMove(ArrayList<Move> safeMoves, ArrayList<Move> mostSpaceMoves){
		Move ans=null;
		ArrayList<Move> goodMoves=new ArrayList<Move>();
		for(Move safeMove:safeMoves){
			for(Move mostMove:mostSpaceMoves){
				if(safeMove.compareMoves(mostMove)){
					goodMoves.add(safeMove);
				}
			}
		}

		if(goodMoves.size()!=0){
			Random rand=new Random();
			int n=rand.nextInt(goodMoves.size());	
			if(goodMoves.size()>0)
				ans=goodMoves.get(n);
		}
		return ans;
	}

	public ArrayList<int[][]> inDanger(Board board){
		ArrayList<ArrayList<int[]>> locs= getLocs(board,false);
		ArrayList<int[]> comps=locs.get(0);
		ArrayList<int[]> hums=locs.get(1);
		ArrayList<int[][]> ans=new ArrayList<int[][]>();
		for(int[] comp:comps){
			for(int[] human:hums){
				if(Human.canMovePiece(board, human, comp) && !isDefended(board,comp,human))
					ans.add(new int[][]{comp,human});
			}
		}
		return ans;
	}

	public int totalDangerValue(Board board, ArrayList<int[][]> danger){
		int totalCost=0;
		for(int[][] jab:danger){
			totalCost+=board.getVal(jab[0]);
		}
		return totalCost;
	}

	public int dangerValue(Board board,ArrayList<int[][]> danger){
		int cost=0;
		for(int[][] jab:danger){
			if(board.getVal(jab[0])>cost)
				cost=board.getVal(jab[0]);
		}
		return cost;
	}
	//If pieces are in danger then defend the most points of pieces
	public Move defend(Board board) throws InterruptedException{
		int min=20;
		int totalCost=200;
		int curr;
		ArrayList<int[][]> danger;
		int totalDanger=0;
		Move best=null;
		for(Move move:allMoves(board,compType)){
			Board fake=board.boardCopy();
			fake=fake.makeChange(move.loc, move.mov, Board.pieceColor.White);
			danger=(inDanger(fake));
			totalDanger=totalDangerValue(board,danger);
			if((curr=dangerValue(fake,danger))<min || (curr==min && totalDanger<totalCost)){
				min=curr;
				best=move;
				totalCost=totalDanger;
			}
		}
		return best;
	}
	//If in danger
	//if(can attack higher)
	//if(lower and can defend)
	//if (can move away)
	//if (can put in check a few times to move piece to safety)
	//else(attack a piece or ignore)

	//check if can attack pieces

	//try to do a plan

	//do random move





}

