
/*Authors:Sourlantzis Dimitrios , AEM:9868, Phone number:6955757756 - 6948703383, e-mail:sourland@ece.auth.gr
 * 		  Sidiropoulos Evripidis, AEM:9679, Phone number:6971947855, e-mail:evripids@ece.auth.gr
 */

public class Game {
	
	private int round;

	public Game(int round) {
		this.round = round;
	}

	public Game() {
		round = 0;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public static void main(String[] args) {
		Game game = new Game();
		// Initialise board
		int N = 20;
		Board board = new Board(N, N, 6, 10, 8);
		// Initialise board limits
		int[][] wal = {
				{-2,-2},
				{2,-2},
				{2,2},
				{-2,2},
			};
		int[][] fal = {
				{-3,-3},
				{3,-3},
				{3,3},
				{-3,3},
		};
		int[][] tal = {
				{-4,-4},
				{4,-4},
				{4,4},
				{-4,4},
			};
		board.setWeaponAreaLimits(wal);
		board.setFoodAreaLimits(fal);
		board.setTrapAreaLimits(tal);
		// Generate random board
		board.createBoard();
		int radius = 3;
		// Initialise players
		Player player = new Player(0, "Bot", board, 0, N/2, N/2, null, null, null);
		HeuristicPlayer Smart = new HeuristicPlayer(1, "Smart-Bot", board, 0, -N/2, -N/2, null, null, null, radius);


		// Game loop
		int maxRounds = 120; // Just for safety
		while(maxRounds-- >= 0) {
			System.out.println("======= Round " + game.round + "===========");
			
			// Make each player move
			player.move();
			Smart.move(player, 2, game.getRound());
			if (HeuristicPlayer.kill(Smart, player,2) ) {
				System.out.println(Smart.getName() + " killed " + player.getName());
				break;
			}
			Smart.statistics(player, game.getRound());
			// Resize board
			if(game.round != 0 && game.round%3 == 0) {
				board.resizeBoard(player, Smart);
			}
			
			// Increment round counter
			game.round += 1;
			
			// Get board as string array to print
			String ss[][] = board.getStringRepresentation();
			
			// Add player positions on the board
			ss[board.x2i(player.getX())][board.y2j(player.getY())] = "  P0";
			ss[board.x2i(Smart.getX())][board.y2j(Smart.getY())] = "  P1";			
			
			// Print board
			for(int i=0; i<ss.length; i++) {
				for(int j=0; j<ss[0].length; j++) {
					System.out.print(ss[i][j]);
					System.out.print("|");
				}
				System.out.print("\n");
			}
			
			// Check end game conditions
			if(board.getM() == 4 && board.getN() == 4) {
				// End game
				break;
			}
		}
		
		// Find winner
		Player winner = null;
		if(HeuristicPlayer.kill(Smart, player,2) ) {
			winner = Smart;
		}
		else if(player.getScore() > Smart.getScore()) {
			winner = player;
		} else if(player.getScore() < Smart.getScore()) {
			winner = Smart;
		}
	
		// Print game stats
		System.out.printf("Game finished: total rounds %d, scores %d %d, winner is player %s", (game.round-1),player.getScore(), Smart.getScore(), (winner!=null ? winner.getName() : "...actually, it was a tie"));
	}

}
