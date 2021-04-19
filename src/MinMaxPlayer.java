
/*Authors:Sourlantzis Dimitrios , AEM:9868, Phone number:6955757756 - 6948703383, e-mail:sourland@ece.auth.gr
 * 		  Sidiropoulos Evripidis, AEM:9679, Phone number:6971947855, e-mail:evripids@ece.auth.gr
 */

import java.util.*;

public class MinMaxPlayer extends Player {
	
	ArrayList<Integer[]> path;
	
	public MinMaxPlayer() {
		super();
		path = new ArrayList<Integer[]>();
		
	}
	
	public MinMaxPlayer(int id, String name, Board board, int score, int x, int y, Weapon bow, Weapon pistol, Weapon sword, ArrayList<Integer[]> path) {
		super(id, name, board, score, x, y, bow, pistol, sword);
		this.path = path;
	}
	
	public float playersDistance(Player p) {
		float distance = (float) Math.sqrt(Math.pow(x + p.getX(), 2.0) + Math.pow(y + p.getY(), 2.0));
		return distance;
		
	}
	
	public static boolean kill(Player player1, Player player2, float d) {
		if(Math.sqrt(Math.pow(player1.getX() - player2.getX(), 2.0) + Math.pow(player1.getY() - player2.getY(), 2.0)) <=d 
				&& player1.getPistol() != null)
			return true;
		else return false;
	}
	
	/*
	 * @param dice the dice that gets evaluated as a move
	 * @param x, y the  pos of the player;
	 * @param opponent the opponent player that is used help to the evaluation calculation
	 * @param d the distance between the two players
	 */
	
	double evaluate(int dice, int x, int y, Player opponent, int d) {
		
		Weapon[] weapons = new Weapon[board.getW()];
		weapons = board.getWeapons();
		
		Food[] food = new Food[board.getF()];
		food = board.getFood();
		
		Trap[] traps = new Trap[board.getT()];
		traps = board.getTraps();
		
		int moves[][] = {
				{0,-1},
				{1,-1},
				{1,0},
				{1,1},
				{0,1},
				{-1,1},
				{-1,0},
				{-1,-1},
		};
		//Set the need factors
		

		double getABow;
		double getASword;
		double getAGun;
		
		double gainPoints = 0.2;
		double avoidTraps = 0.2;
		double forceKill = 0;;
		
		if(pistol != null) {
			forceKill = 10;
			getAGun = 0;
			 x+=moves[dice][0];
			 y+=moves[dice][1];
			if(playersDistance(opponent) < d) {
				x-=moves[dice][0];
				y-=moves[dice][1];
				return forceKill;
			}		
		}
		else { 
			forceKill = -10;
			getAGun = 5;
		}
		
		if(sword != null) getASword = 0;
		else  getASword = 4;
		
		if(bow != null) getABow = 0;
		else  getABow = 4;
		
		for(int i = 0; i < board.getW() ; i++) {
			if(x+moves[dice][0] == weapons[i].getX() && y+moves[dice][1] == weapons[i].getY() ) {
				if(weapons[i].getType() == "pistol") return getAGun;
				if(weapons[i].getType() == "sword") return getABow;
				if(weapons[i].getType() == "bow") return getASword;
			}
		}
				
		for(int i = 0; i < board.getF() ; i++) {
			if(x+moves[dice][0] == food[i].getX() && y+moves[dice][1] == food[i].getY() ) 
				return food[i].getPoints()*gainPoints;
		}
		for(int i = 0; i < board.getT() ; i++) {
			if(x+moves[dice][0] == traps[i].getX() && y+moves[dice][1] == traps[i].getY() ) 
				return -avoidTraps*traps[i].getPoints();
		}
		return 0.02 / Math.sqrt(x*x + y*y);
	}
	
	public int findAvailableMoves() {
		int availableMoves = 8;
		boolean isXEdge = x == -board.getN()/2 || x == board.getN()/2;
		boolean isYEdge = y == -board.getM()/2 || y == board.getM()/2;
		if(isXEdge && isYEdge) {
			availableMoves = 3;
		} else if(isXEdge || isYEdge) {
			availableMoves = 5;
		}
		
		return availableMoves;
	}
	
	public <TEMP> void swap(TEMP A, TEMP B) {
		TEMP temp;
		temp = A;
		A = B;
		B = temp;
	}
	
	int chooseMinMaxMove(Node root) {
		int[] bestMove = new int[3];
		
		return bestMove[3];
	}
	
	public void createMySubTree(Node root, int depth, int xCurrentPos, int yCurrentPos, int xOpponentsCurrentPos, int yOpponentsCurrentPos) {
		
	}
	
	//public int[] getNextMove(int xCurrentPos,int yCurrentPos, int xOpponentsCurrentPos, int yOpponentsCurrentPos) {
		
	//}
	
	public void createOpponentSubTree(Node parent, int depth, int xCurrentPos, int yCurrentPos, int xOpponentsCurrentPos, int yOpponentsCurrentPos, int d) {
		int availableMoves = 8;
		boolean isXEdge = xCurrentPos == -board.getN()/2 || x == board.getN()/2;
		boolean isYEdge = yCurrentPos == -board.getM()/2 || y == board.getM()/2;
		
		if(isXEdge && isYEdge) availableMoves = 3;
		else if(isXEdge || isYEdge) availableMoves = 5;
		
		int moves[][] = {
				{0,-1},
				{1,-1},
				{1,0},
				{1,1},
				{0,1},
				{-1,1},
				{-1,0},
				{-1,-1},
		};
		
		List<Integer> list = new ArrayList<Integer>(availableMoves);
		for(int i = 0; i < 8 ; i++) {
			if(board.isPositionValid(x + moves[i][0], y + moves[i][1]))
				list.add(i);
		}
		
		Board cloneBoard = parent.getNodeBoard();
		Player clone = new Player();
		
		clone.setX(xOpponentsCurrentPos);
		clone.setY(yOpponentsCurrentPos);
		
		for(int i = 0 ; i < availableMoves ; i++){
		
			cloneBoard = parent.getNodeBoard();
			xCurrentPos += moves[list.get(i)][0];
			yCurrentPos += moves[list.get(i)][1];
			Node child = new Node();
			child.setNodeEvaluation(parent.getNodeEvaluation() - evaluate(list.get(i), xCurrentPos, yCurrentPos, clone, d));
			parent.getChildren().ensureCapacity(availableMoves);
			parent.getChildren().add(child);
		
		}
	}
}
