import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;



public class Display{
	private static JLabel[][] boardLabels;
	private static JPanel gridPanel;
	static int[] first,second;


	public static void displayGui(Board board)  throws InterruptedException
	{

		JLabel yourPiecesLabel;
		JLabel yourScoreLabel;
		JLabel compPiecesLabel;
		JLabel compScoreLabel;

		int numRows = 8;
		int numCols = 8;
		JFrame frame = new JFrame("Chess Board");
		JPanel mainPanel = new JPanel(new BorderLayout());
		gridPanel = new JPanel(new GridLayout(numRows,numCols));



		JPanel runPanel = new JPanel(new GridLayout(1,6));
		boardLabels = new JLabel[numRows][numCols];



		gridPanel.setBorder(BorderFactory.createTitledBorder("Chess Board"));

		yourScoreLabel = new JLabel("0",JLabel.CENTER);
		yourScoreLabel.setBorder(BorderFactory.createTitledBorder("Your Score: "));
		compScoreLabel = new JLabel("0",JLabel.CENTER);
		compScoreLabel.setBorder(BorderFactory.createTitledBorder("Computer Score: "));

		JButton forward=new JButton("Forward");
		forward.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(Board.boardNum==Board.oldBoards.size()-1)
					return;
				else{
					Board.boardNum+=2;
					Display.makeChange(Board.oldBoards.get(Board.boardNum));
				}

			}


		});
		JButton back=new JButton("Back");
		back.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(Board.boardNum==0)
					return;
				else{
						Board.boardNum-=2;
						Board.board=Board.oldBoards.get(Board.boardNum);
						Display.makeChange(Board.oldBoards.get(Board.boardNum));
					
				}

			}


		});
		runPanel.add(yourScoreLabel);
		runPanel.add(compScoreLabel);

		runPanel.add(forward);
		runPanel.add(back);

		for(int i=7;i>=0;i--)
		{
			for(int j=7;j>=0;j--)
			{
				JLabel imageLabel = new JLabel(); 
				boardLabels[i][j]=imageLabel;
				gridPanel.add(imageLabel);
			}
		}
		start(board);

		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				boardLabels[i][j].addMouseListener(new MouseAdapter()  
				{  
					public void mouseReleased(MouseEvent e)  
					{  
						for(int i=0;i<8;i++){
							for(int j=0;j<8;j++){
								if(e.getSource()==boardLabels[i][j]){
									if(first==null){
										first= new int[]{i,j};
										Display.selected(first);
									}
									else{
										second= new int[]{i,j};
									}
								}
							}
						}

					}  
				});  
			}
		}

		mainPanel.add(gridPanel,BorderLayout.CENTER);
		mainPanel.add(runPanel,BorderLayout.SOUTH);
		frame.add(mainPanel);
		frame.setSize(550,650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void selected(int[] loc){
		switch(Board.board.getPiece(loc)){
		case Empty:
			break;
		case bBishop:
			if(Board.board.getSpaceColor(loc)==Board.spaceColor.Blue)
				boardLabels[loc[0]][loc[1]].setIcon(new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackbishopblueselected.jpg"));
			else
				boardLabels[loc[0]][loc[1]].setIcon(new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackbishopbrownselected.jpg"));

			break;
		case bKing:
			if(Board.board.getSpaceColor(loc)==Board.spaceColor.Blue)
				boardLabels[loc[0]][loc[1]].setIcon(new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackkingblueselected.jpg"));
			else
				boardLabels[loc[0]][loc[1]].setIcon(new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackkingbrownselected.jpg"));

			break;
		case bKnight:
			if(Board.board.getSpaceColor(loc)==Board.spaceColor.Blue)
				boardLabels[loc[0]][loc[1]].setIcon(new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackknightblueselected.jpg"));
			else
				boardLabels[loc[0]][loc[1]].setIcon(new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackknightbrownselected.jpg"));

			break;
		case bPawn:
			if(Board.board.getSpaceColor(loc)==Board.spaceColor.Blue)
				boardLabels[loc[0]][loc[1]].setIcon(new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackpawnblueselected.jpg"));
			else
				boardLabels[loc[0]][loc[1]].setIcon(new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackpawnbrownselected.jpg"));

			break;
		case bQueen:
			if(Board.board.getSpaceColor(loc)==Board.spaceColor.Blue)
				boardLabels[loc[0]][loc[1]].setIcon(new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackqueenblueselected.jpg"));
			else
				boardLabels[loc[0]][loc[1]].setIcon(new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackqueenbrownselected.jpg"));

			break;
		case bRook:
			if(Board.board.getSpaceColor(loc)==Board.spaceColor.Blue)
				boardLabels[loc[0]][loc[1]].setIcon(new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackrookblueselected.jpg"));
			else
				boardLabels[loc[0]][loc[1]].setIcon(new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackrookbrownselected.jpg"));

		default:
			break;

		}
	}

	public static void start(Board board){
		for(int i=7;i>=0;i--)
		{
			for(int j=7;j>=0;j--)
			{
				int[] loc=new int[]{i,j};
				ImageIcon image;
				if(board.getPiece(loc)==Board.piece.Empty){
					if ((i%2==0 && j%2==1)||(i%2==1 && j%2==0))
						image = new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/brown.gif");
					else
						image = new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blue.jpg");
				}
				else{
					if (board.getPiece(loc)==Board.piece.bPawn)
						if(board.getSpaceColor(loc)==Board.spaceColor.Brown)
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackpawnbrown.jpg");
						else 
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackpawnblue.jpg");

					else if(board.getPiece(loc)==Board.piece.wPawn)
						if(board.getSpaceColor(loc)==Board.spaceColor.Brown)
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/whitepawnbrown.jpg");
						else 
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/whitepawnblue.jpg");

					else if(board.getPiece(loc)==Board.piece.wRook)
						if(board.getSpaceColor(loc)==Board.spaceColor.Brown)
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/whiterookbrown.jpg");
						else 
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/whiterookblue.jpg");

					else if(board.getPiece(loc)==Board.piece.bRook)
						if(board.getSpaceColor(loc)==Board.spaceColor.Brown)
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackrookbrown.jpg");
						else 
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackrookblue.jpg");

					else if(board.getPiece(loc)==Board.piece.wKnight)
						if(board.getSpaceColor(loc)==Board.spaceColor.Brown)
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/whiteknightbrown.jpg");
						else 
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/whiteknightblue.jpg");

					else if(board.getPiece(loc)==Board.piece.bKnight)
						if(board.getSpaceColor(loc)==Board.spaceColor.Brown)
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackknightbrown.jpg");
						else 
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackknightblue.jpg");

					else if(board.getPiece(loc)==Board.piece.wBishop)
						if(board.getSpaceColor(loc)==Board.spaceColor.Brown)
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/whitebishopbrown.jpg");
						else 
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/whitebishopblue.jpg");


					else if(board.getPiece(loc)==Board.piece.bBishop)
						if(board.getSpaceColor(loc)==Board.spaceColor.Brown)
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackbishopbrown.jpg");
						else 
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackbishopblue.jpg");

					else if(board.getPiece(loc)==Board.piece.wKing)
						if(board.getSpaceColor(loc)==Board.spaceColor.Brown)
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/whitekingbrown.jpg");
						else 
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/whitekingblue.jpg");

					else if(board.getPiece(loc)==Board.piece.bKing)
						if(board.getSpaceColor(loc)==Board.spaceColor.Brown)
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackkingbrown.jpg");
						else 
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackkingblue.jpg");

					else if(board.getPiece(loc)==Board.piece.wQueen)
						if(board.getSpaceColor(loc)==Board.spaceColor.Brown)
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/whitequeenbrown.jpg");
						else 
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/whitequeenblue.jpg");

					else
						if(board.getSpaceColor(loc)==Board.spaceColor.Brown)
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackqueenbrown.jpg");
						else 
							image=new ImageIcon("C:/Users/Jaren/workspace/Chess Game/resources/blackqueenblue.jpg");

				}


				boardLabels[i][j].setIcon(image);
			}
		}
	}

	public static void makeChange(Board board){
		start(board);
	}
}
