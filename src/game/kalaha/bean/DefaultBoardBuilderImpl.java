package game.kalaha.bean;

import org.springframework.context.ApplicationContext;

import game.kalaha.config.ApplicationContextLoader;
import game.kalaha.hbm.Game;

/**
 *	One implementation of the BoardBuilder interface. Generate a 6 pit, 6 stone, counter-clockwise
 *  board.<br/><br/>
 *
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
 *  @author Edgar H. de Graaf
 *  @see game.kalaha.bean.BoardBuilder
 */
public class DefaultBoardBuilderImpl implements BoardBuilder {
	private String boardImg = "board1.png";
	
	/**
	 * Initializes the board and laying all the needed connections
	 * between the different stone holders.
	 */
	@Override
	public Board initBoard() {
		int nrPits = 6;
		ApplicationContext appContext = ApplicationContextLoader.getInstance().getAppContext();
		
		Board boardPojo = new Board();
		StoneHolder[][] board = new StoneHolder[2][nrPits];
		StoneHolder player2Store = (StoneHolder)appContext.getBean("player2Store");
		StoneHolder player1Store = (StoneHolder)appContext.getBean("player1Store");
		boardPojo.setBoard(board);
		boardPojo.setPlayer1Store(player1Store);
		boardPojo.setPlayer2Store(player2Store);
				
		player1Store.setStoneCount(0); // Reset count
		player2Store.setStoneCount(0);
		StoneHolder previous1 = player2Store; // In order to set next
		StoneHolder previous2 = player1Store;
		for(int p = 0;p < nrPits;p++){
			StoneHolder player1Pit = (StoneHolder)appContext.getBean("player1Pit");
			StoneHolder player2Pit = (StoneHolder)appContext.getBean("player2Pit");
			player1Pit.setOwnerStore(player1Store);
			player2Pit.setOwnerStore(player2Store);
			previous1.setNextHolder(player1Pit);
			previous1 = player1Pit;
			previous2.setNextHolder(player2Pit);
			previous2 = player2Pit;
			
			board[1][p] = player2Pit;
			board[0][nrPits - p - 1] = player1Pit;
		}
		board[0][0].setNextHolder(player1Store);
		board[1][nrPits - 1].setNextHolder(player2Store);
		for(int p = 0;p < nrPits;p++){
			board[0][p].setOppositeHolder(board[1][p]);
			board[1][p].setOppositeHolder(board[0][p]);
		}
		
		return boardPojo;
	}

	/**
	 * Gives image (PNG, JPG, etc.) of a board
	 */
	@Override
	public String getBoardImg() {
		return boardImg;
	}
}
