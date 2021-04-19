
/*Authors:Sourlantzis Dimitrios , AEM:9868, Phone number:6955757756 - 6948703383, e-mail:sourland@ece.auth.gr
 * 		  Sidiropoulos Evripidis, AEM:9679, Phone number:6971947855, e-mail:evripids@ece.auth.gr
 */

import java.util.*;


public class HeuristicPlayer extends Player{
	
	/*CLASS' VARIABLES
	 * ArrayList<Integer[]> path() contains player info
	 * Such as the move he chose, the amount of points given or taken by that specific move
	 * and info about his food, his weapons and the traps he might have fallen into in this particular move
	 */
		
	ArrayList<Integer[]> path = new ArrayList<Integer[]>();
	
	static int r;
	
	public HeuristicPlayer() {
		super();
		path = new ArrayList<Integer[]>(1000);
	}
	
	public HeuristicPlayer(int id, String name, Board board, int score, int x, int y, Weapon bow, Weapon pistol, Weapon sword, int radius) {
		super(id, name, board, score, x, y, bow, pistol, sword);
		this.path=null;	
		r = radius;
		path = new ArrayList<Integer[]>(1000);
		
	}
	
	//GETTERS
	
	public ArrayList<Integer[]> getPath() {
		return path;
	}
	
	public int getR() {
		return r;
	}
	
	//SETTERS
	
	public void setR(int radius) {
		r = radius;
	}
	
	public void setPath(ArrayList<Integer[]> path) {
		this.path = path;
	}
	
	//HELPERS
	
	public boolean isInRadiusItem(int x, int y) {
		if(this.x + x < r || this.x - x < r ||this.y - y < r ||this.y + y < r ) return true;
		else return false;
	}
	
	public boolean isInRadiusPlayer(Player p) {
		if(p.getX() + x< r || p.getX() - x < r ||p.getY() - y < r ||p.getY() + y < r ) return true;
		else return false;
	}
	
	public float playersDistance(Player p) {
		float distance = (float) Math.sqrt(Math.pow(x + p.getX(), 2.0) + Math.pow(y + p.getY(), 2.0));
		if(isInRadiusPlayer(p)) return distance;
		else return -1;
		
	}
	
	public static boolean kill(Player player1, Player player2, float d) {
		if(Math.sqrt(Math.pow(player1.getX() - player2.getX(), 2.0) + Math.pow(player1.getY() - player2.getY(), 2.0)) <=d 
				&& player1.getPistol() != null
				&& d < r)
			return true;
		else return false;
	}
	
	public double evaluate(int dice, Player p, float d) {
		
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
		
		double gainPoints = 0.11;
		double avoidTraps = 0.29;
		double forceKill = 0;;
		
		if(pistol != null) {
			forceKill = 10;
			getAGun = 0;
			 x+=moves[dice][0];
			 y+=moves[dice][1];
			if(playersDistance(p) < d && isInRadiusPlayer(p)) {
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
				
		for(int i = 0; i < board.getF() ; i++)
			if(x+moves[dice][0] == food[i].getX() && y+moves[dice][1] == food[i].getY() ) 
				return food[i].getPoints()*gainPoints;
		
		for(int i = 0; i < board.getT() ; i++)
			if(x+moves[dice][0] == traps[i].getX() && y+moves[dice][1] == traps[i].getY() ) 
				return -avoidTraps*traps[i].getPoints();
		
		return 0.00;
	}
	
	//method that evaluates the whole area that the player sees, and returns the right key
	
	public int getKey(Map<Integer, Double> map, Player p, float d){
		Map<Integer, Double> aroundMap = new HashMap<Integer, Double>();
		int key=0;
		double max = 0;
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
		
		for(int i = 0; i < 8 ; i++) {
			if(board.isPositionValid(x + moves[i][0],y + moves[i][0])) {
			
				if(map.get(i) > max) {
					max = map.get(i);
					key = i;
				}
			}
		}
	
		int normalX = x;
		int normalY = y;
		int blockCounter = 0;
		double blockValue = 0;
		
		if(max <= 0) {
			Vector<Double> vector =  new Vector<Double>(2, 6);
		
			//Scanning Algorithm: See Project Documentation
			
			for(int i = 0; i < 8 ; i++) {
				if(!board.isPositionValid(x + moves[i][0],y + moves[i][0])) continue;
				x += moves[i][0];
				y += moves[i][1];
				x = x==0 ? x + moves[i][0] : x;
				y = y==0 ? y + moves[i][1] : y;
			
				if((i + 1) % 2 != 0 ) {
					int ctr = 0;
				
					while(ctr < r) {
						ctr++;
					
						if(board.isPositionValid(((x + moves[i][0] == 0) ? x + 2*moves[i][0] : x + moves[i][0]),((y + moves[i][1] == 0) ? y + 2*moves[i][1] : y + moves[i][1]))) {
							if(evaluate(i, p ,d) > blockValue) blockValue = evaluate(i, p ,d);
							x += moves[i][0];
							y += moves[i][1];
							x = x==0 ? x + moves[i][0] : x;
							y = y==0 ? y + moves[i][1] : y;
							blockCounter++;
						}
					}
					
					for(int j = 0 ; j < blockCounter ; j++) {
						x -= moves[i][0];
						y -= moves[i][1];
						x = x==0 ? x + moves[i][0] : x;
						y = y==0 ? y + moves[i][1] : y;
					}
					
					vector.add(blockValue);
				}

				else if((i + 1) % 2 == 0){
					x += moves[i][0];
					y += moves[i][1];
					x = x==0 ? x + moves[i][0] : x;
					y = y==0 ? y + moves[i][1] : y;
					int n = i + 1;
					
					switch(n) {
					case 2:
						for(int a = 0 ; a < r -1 ; a++) {
							for(int b = 0 ; b < r -1 ; b++) {
								if(board.isPositionValid(((x + moves[i-1][0] == 0) ? x + 2*moves[i-1][0] : x + moves[i-1][0]),((y + moves[i-1][1] == 0) ? y + 2*moves[i-1][1] : y + moves[i-1][1]))){
									if(evaluate(i-1, p ,d) > blockValue) blockValue = evaluate(i, p ,d);
									x += moves[i-1][0];
									y += moves[i-1][1];
									x = x==0 ? x + moves[i-1][0] : x;
									y = y==0 ? y + moves[i-1][1] : y;
									blockCounter++;
								}
								for(int j = 0 ; j < blockCounter ; j++) {
									x -= moves[i-1][0];
									y -= moves[i-1][1];
									x = x==0 ? x + moves[i-1][0] : x;
									y = y==0 ? y + moves[i-1][1] : y;
									}
								if(board.isPositionValid(((x + moves[i+1][0] == 0) ? x + 2*moves[i+1][0] : x + moves[i+1][0]),((y + moves[i+1][1] == 0) ? y + 2*moves[i+1][1] : y + moves[i+1][1]))) {
									x += moves[i+1][0];
									y += moves[i+1][1];
									x = x==0 ? x + moves[i+1][0] : x;
									y = y==0 ? y + moves[i+1][1] : y;
								}
							}
						}
						vector.add(blockValue);
						break;
					case 4:
						for(int a = 0 ; a < r -1 ; a++) {
							for(int b = 0 ; b < r -1 ; b++) {
								if(board.isPositionValid(((x + moves[i-1][0] == 0) ? x + 2*moves[i-1][0] : x + moves[i-1][0]),((y + moves[i-1][1] == 0) ? y + 2*moves[i-1][1] : y + moves[i-1][1]))){
									if(evaluate(i-1, p ,d) > blockValue) blockValue = evaluate(i, p ,d);
									x += moves[i-1][0];
									y += moves[i-1][1];
									blockCounter++;
								}
								for(int j = 0 ; j < blockCounter ; j++) {
									x -= moves[i-1][0];
									y -= moves[i-1][1];
									x = x==0 ? x + moves[i-1][0] : x;
									y = y==0 ? y + moves[i-1][1] : y;
									}
								if(board.isPositionValid(((x + moves[i+1][0] == 0) ? x + 2*moves[i+1][0] : x + moves[i+1][0]),((y + moves[i+1][1] == 0) ? y + 2*moves[i+1][1] : y + moves[i+1][1]))) {
									x += moves[i+1][0];
									y += moves[i+1][1];
								}
							}
						}
						vector.add(blockValue);
						break;
					case 6:
						for(int a = 0 ; a < r -1 ; a++) {
							for(int b = 0 ; b < r -1 ; b++) {
								if(board.isPositionValid(((x + moves[i+1][0] == 0) ? x + 2*moves[i+1][0] : x + moves[i+1][0]),((y + moves[i+1][1] == 0) ? y + 2*moves[i+1][1] : y + moves[i+1][1]))){
									if(evaluate(i-1, p ,d) > blockValue) blockValue = evaluate(i, p ,d);
									x += moves[i+1][0];
									y += moves[i+1][1];
									blockCounter++;
								}
								for(int j = 0 ; j < blockCounter ; j++) {
									x -= moves[i+1][0];
									y -= moves[i+1][1];
									x = x==0 ? x + moves[i+1][0] : x;
									y = y==0 ? y + moves[i+1][1] : y;
									}
								if(board.isPositionValid(((x + moves[i-1][0] == 0) ? x + 2*moves[i-1][0] : x + moves[i-1][0]),((y + moves[i-1][1] == 0) ? y + 2*moves[i-1][1] : y + moves[i-1][1]))) {
									x += moves[i-1][0];
									y += moves[i-1][1];
								}
							}
						}
						vector.add(blockValue);
						break;
					case 8:
						for(int a = 0 ; a < r -1 ; a++) {
							for(int b = 0 ; b < r -1 ; b++) {
								if(board.isPositionValid(((x + moves[i-7][0] == 0) ? x + 2*moves[i-7][0] : x + moves[i-7][0]),((y + moves[i-7][1] == 0) ? y + 2*moves[i-7][1] : y + moves[i-7][1]))){
									if(evaluate(i-7, p ,d) > blockValue) blockValue = evaluate(i, p ,d);
									x += moves[i-7][0];
									y += moves[i-7][1];
									blockCounter++;
								}
								for(int j = 0 ; j < blockCounter ; j++) {
									x -= moves[i-7][0];
									y -= moves[i-7][1];
									x = x==0 ? x + moves[i-7][0] : x;
									y = y==0 ? y + moves[i-7][1] : y;
									}
								if(board.isPositionValid(((x + moves[i-1][0] == 0) ? x + 2*moves[i-1][0] : x + moves[i-1][0]),((y + moves[i-1][1] == 0) ? y + 2*moves[i-1][1] : y + moves[i-1][1]))) {
									x += moves[i-1][0];
									y += moves[i-1][1];
								}
							}
						}
						vector.add(blockValue);
						break;
					default: continue;
					}
					Collections.sort(vector);
					Collections.reverse(vector);
				}
				aroundMap.put(i, vector.get(0));
			}
			for(int i = 0; i < 8 ; i++) {
				if(!board.isPositionValid(x + moves[i][0],y + moves[i][0])) continue;
				
				if(aroundMap.get(i) > max) {
					max = aroundMap.get(i);
					 key = i;
				}
			}
		}
		x = normalX;
		y = normalY;
		if(max == 0) {
			double distance = Math.sqrt(x*x + y*y);
			for(int i = 0; i < 8; i++) {
				if(!board.isPositionValid(x + moves[i][0],y + moves[i][0])) continue;
				x += moves[i][0];
				y += moves[i][1];
				if(Math.sqrt(x*x + y*y) < distance) {
					distance = Math.sqrt(x*x + y*y);
					key = i;
				}
				x = normalX;
				y = normalY;
			}
		}
		return key;
	}
 	//dragons ahead...
	public int[] move(Player otherPlayer, float d, int round) {
		
		int[] move = new int[3];
		
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
		
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		for(int i=0; i < 8; i++) {
			if(board.isPositionValid(x + moves[i][0], y + moves[i][1]))
				 map.put(i, evaluate(i, otherPlayer, d)); 
			else map.put(i, (double)-50);
		}
		
		//we will search the 2 zones.
		
		int Key = getKey(map,otherPlayer , d);
		
		x+=moves[Key][0];
		y+=moves[Key][1];
		move[0] = x==0 ?  x+=moves[Key][0] : x;
		move[1] = y == 0 ? y+=moves[Key][0] : y;
		move[2] = Key;
		Integer[] newMove = new Integer[3];
		for(int i = 0 ; i < newMove.length ; i ++) {
			newMove[i] = move[i];
			newMove[i] = move[i];
			newMove[i] = move[i];
		}
		path.add(newMove);
		return move;
	}
	
	public void statistics(Player otherPlayer, int round) {
		Integer[] stats = new Integer[3];
		stats = path.get(round);
		System.out.println("Player " + name + " rolled a " + (stats[2] + 1));
		Weapon w = null;
		Food f = null;
		Trap t = null;
		for(int i=0; i<board.getWeapons().length; i++) {
			if(board.getWeapons()[i].getX() == x && 
			   board.getWeapons()[i].getY() == y &&
			   board.getWeapons()[i].getPlayerId() == id ) {
				w = board.getWeapons()[i];
				System.out.println("Found a weapon");
				if(w.getType() == "sword") {
					this.sword = w;
				} else if(w.getType() == "bow") {
					this.bow = w;
				} else if(w.getType() == "pistol") {
					this.pistol = w;
				}
				w.setX(0);
				w.setY(0);
			}
		}
		// Check if there is food on the new tile
		for(int i=0; i<board.getFood().length; i++) {
			if(board.getFood()[i].getX() == x && board.getFood()[i].getY() == y) {
				f = board.getFood()[i];
				System.out.println("Found food and got " + f.getPoints());
				this.score += f.getPoints();
				f.setX(0);
				f.setY(0);
			}
		}
		for(int i=0; i<board.getTraps().length; i++) {
			if(board.getTraps()[i].getX() == x && board.getTraps()[i].getY() == y) {
				t = board.getTraps()[i];
				boolean avoided = false;
				if(t.getType() == "ropes") {
					if(sword != null) {
						avoided = true;
					}
				} else if(t.getType() == "animals") {
					if(bow != null) {
						avoided = true;
					}
				}
				if(!avoided) {
					System.out.printf("Player %d (Trap) | prevscore %d, newscore %d\n", id, this.score, this.score-t.getPoints());
					this.score -= t.getPoints();
				} else {
					System.out.printf("Player %d (Trap) | successfully avoided", id);
				}
			}
		}
	}
}
	