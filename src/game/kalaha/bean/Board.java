package game.kalaha.bean;

import game.kalaha.exception.PitNotFoundException;
import game.kalaha.hbm.Competitors;
import game.kalaha.util.GameStatus;

/**
 *	POJO keeping information about the state of the board <br/><br/>
 *  Copyright (C) 2014  Edgar H. de Graaf
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Copyright (C) 2014  Edgar H. de Graaf, edgargithub&#64;outlook.com
 *  
 *  @author Edgar de Graaf
 */
public class Board {	
	private StoneHolder[][] board;
	private StoneHolder player1Store;
	private StoneHolder player2Store;
	private int playerNr;
	private int holderNr;
	private boolean turnP1;
	private boolean gameEnd;
	private GameStatus gameStatus = GameStatus.UNDECIDED;
	private Competitors competitors;
	
	/**
	 * The store or kalaha for player 1
	 * 
	 * @return The store
	 */
	public StoneHolder getPlayer1Store() {
		return player1Store;
	}
	
	/**
	 * Set the player 1 store / kalaha
	 * 
	 * @param player1Store
	 */
	public void setPlayer1Store(StoneHolder player1Store) {
		this.player1Store = player1Store;
	}
	
	/**
	 * The store or kalaha for player 2
	 * 
	 * @return The store
	 */
	public StoneHolder getPlayer2Store() {
		return player2Store;
	}
	
	/**
	 * Set the player 2 store / kalaha
	 * 
	 * @param player1Store
	 */
	public void setPlayer2Store(StoneHolder player2Store) {
		this.player2Store = player2Store;
	}
	
	public StoneHolder getPit(int player, int pit) throws PitNotFoundException {
		if(player > playerNr - 1 ||
				pit > holderNr)
			throw new PitNotFoundException("Player or pit index doesn't exist");
			
		return board[player][pit];
	}
	
	/**
	 * Sets the matrix of pits and also decides the
	 * dimensions.
	 * 
	 * @param board The new board matrix
	 */
	public void setBoard(StoneHolder[][] board) {
		this.board = board;
		playerNr = board.length;
		holderNr = board[0].length;
	}
	
	/**
	 * Gets the number of players playing on this board
	 * 
	 * @return Number of players
	 */
	public int getPlayerNr() {
		return playerNr;
	}
	
	/**
	 * Sets the number of players
	 * 
	 * @param playerNr Number of players
	 */
	public void setPlayerNr(int playerNr) {
		this.playerNr = playerNr;
	}
	
	/**
	 * Gets the number of pits on this board
	 * 
	 * @return The number of pits
	 */
	public int getHolderNr() {
		return holderNr;
	}
	
	/**
	 * Sets the number of pits
	 * 
	 * @param holderNr The number of pits
	 */
	public void setHolderNr(int holderNr) {
		this.holderNr = holderNr;
	}
	
	/**
	 * Decide if player 1 has the turn
	 * 
	 * @return
	 */
	public boolean isTurnP1() {
		if(gameEnd)
			return false;
		else
			return turnP1;
	}
	public void setTurnP1(boolean turnP1) {
		this.turnP1 = turnP1;
	}
	
	/**
	 * turn remains as it is if the last stone
	 * ends in the store.
	 */
	public void decideTurnP1(){
		if(turnP1) {
			turnP1 = player1Store.getLastPastBy() == 1;
			player1Store.setLastPastBy(0);
		} else {
			turnP1 = player2Store.getLastPastBy() != 1;
			player2Store.setLastPastBy(0);
		}
	}
	
	/**
	 * When one of the players is out of stones then
	 * the game has ended and all remaining stones are
	 * collected in the stores. The player with the most
	 * stones won the game.
	 */
	public void decideGameEnd(){
		gameEnd = false;
		gameStatus = GameStatus.UNDECIDED;
		boolean player1Empty = true;
		for(int i = 0;i < holderNr;i++){
			if(board[0][i].getStoneCount() > 0){
				player1Empty = false;
			}
		}
		if(player1Empty){
			gameEnd = true;
			allStonesInStore();
			return;
		}
		boolean player2Empty = true;
		for(int i = 0;i < holderNr;i++){
			if(board[1][i].getStoneCount() > 0){
				player2Empty = false;
			}
		}
		if(player2Empty){
			gameEnd = true;
			allStonesInStore();
			return;
		}
	}

	/**
	 * Get the players playing against each other
	 * 
	 * @return
	 */
	public Competitors getCompetitors() {
		return competitors;
	}
	
	/**
	 * Set the players playing each other
	 * 
	 * @param competitors
	 */
	public void setCompetitors(Competitors competitors) {
		this.competitors = competitors;
	}
	
	/**
	 * True if the game was decided as ended
	 * 
	 * @return
	 */
	public boolean isGameEnd() {
		return gameEnd;
	}
	
	/**
	 * Collect all stones and put them in the store of their
	 * owner. 
	 */
	private void allStonesInStore(){
		for(int p = 0;p < playerNr;p++){
			for(int i = 0;i < holderNr;i++){
				board[p][i].getOwnerStore().setStoneCount(
				board[p][i].getOwnerStore().getStoneCount() + board[p][i].getStoneCount());
				board[p][i].setStoneCount(0);
			}
		}
		
		if(player1Store.getStoneCount() > player2Store.getStoneCount()){
			gameStatus = GameStatus.PLAYER1;
		} else if(player1Store.getStoneCount() < player2Store.getStoneCount()){
			gameStatus = GameStatus.PLAYER2;
		} else {
			gameStatus = GameStatus.UNDECIDED;
		}
	}
	
	/**
	 * Will give the winning player if the game
	 * has been won.
	 * 
	 * @return Game status
	 */
	public GameStatus getGameStatus() {
		return gameStatus;
	}
}
