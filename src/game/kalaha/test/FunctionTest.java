package game.kalaha.test;

import static org.junit.Assert.*;
import game.kalaha.bean.Board;
import game.kalaha.bean.BoardBuilder;
import game.kalaha.bean.StoneHolder;
import game.kalaha.config.ApplicationContextLoader;
import game.kalaha.exception.PitNotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 *	Testing the functionality of the kalada mechanism <br/><br/>
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
 *  @author Edgar de Graaf
 */
public class FunctionTest {
	private ApplicationContext appContext;
	private BoardBuilder builder;
	private Board board;
	private StoneHolder player1Store;
	private StoneHolder player2Store;
	
	@Before
	public void setUp() throws Exception {
		appContext = ApplicationContextLoader.getInstance().getAppContext();
		builder = (BoardBuilder)appContext.getBean("boardBuilder");
	
		board = builder.initBoard();

		player1Store = board.getPlayer1Store();
		player2Store = board.getPlayer2Store();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNextHolder() {
		try {
			StoneHolder player2First = board.getPit(1, 0);
			StoneHolder nextHolder = player2First;
			StoneHolder player1First = board.getPit(0, 5);
			
			
			for(int i = 0;i < 14;i++) {
				StoneHolder temp = nextHolder.nextHolder();
				assertNotSame("Singleton pits where prototype expected", nextHolder, temp);
				nextHolder = temp;
			}
			assertTrue("Cycle not ending at the beginning", nextHolder == player2First);
			assertTrue("Unexpected nextHolder player1", player1First.nextHolder() == board.getPit(0, 4) );
			assertTrue("Unexpected store player1", board.getPit(0, 0).nextHolder() == player1First.getOwnerStore() );
			assertTrue("Unexpected next store player1", player1First.getOwnerStore().nextHolder() == player2First );
		} catch(PitNotFoundException ex){
			ex.printStackTrace();
		}
	}

	@Test
	public void testOpposite() {
		try {
			for(int i = 0;i < board.getHolderNr();i++){
				assertSame("Opposite player 1 not correct", board.getPit(0, i).getOppositeHolder(), 
						board.getPit(1, i));
				assertSame("Opposite player 2 not correct", board.getPit(1, i).getOppositeHolder(), 
						board.getPit(0, i));
			}
		} catch(PitNotFoundException ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testOwner() {
		//StoneHolder player1Store = (StoneHolder)appContext.getBean("player1Store");
		//StoneHolder player2Store = (StoneHolder)appContext.getBean("player2Store");
		try {
			for(int i = 0;i < board.getHolderNr();i++){
				assertTrue("Owner player 1 not correct", board.getPit(0, i).getOwner().equals("Player1"));
				assertTrue("Owner player 2 not correct", board.getPit(1, i).getOwner().equals("Player2"));
				assertSame("Owner stone holder player 1 not correct", player1Store, board.getPit(0, i).getOwnerStore());
				assertSame("Owner stone holder player 2 not correct", player2Store, board.getPit(1, i).getOwnerStore());
			}
		} catch(PitNotFoundException ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testStoneCount() {
		try {
			assertEquals("Player 1 store stone count not 0", player1Store.getStoneCount(), 0);
			assertEquals("Player 2 store stone count not 0", player2Store.getStoneCount(), 0);
			for(int i = 0;i < board.getHolderNr();i++){
				assertEquals("Player 1 pit stone count not 6", board.getPit(0, i).getStoneCount(), 6);
				assertEquals("Player 2 pit stone count not 6", board.getPit(1, i).getStoneCount(), 6);
			}
		} catch(PitNotFoundException ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testTakePut() {
		try {
			board.getPit(0, 5).takePut();
			for(int i = 4;i >= 0;i--){
				assertEquals("Player 1 stone count not correct for pits", board.getPit(0, i).getStoneCount(), 7);
			}
			assertEquals("Player 1 origin pit not 0", board.getPit(0, 5).getStoneCount(), 0);
			assertEquals("Player 1 store last past by is not 1", player1Store.getLastPastBy(), 1);
					
			board = builder.initBoard();
	
			player1Store = board.getPlayer1Store();
			player2Store = board.getPlayer2Store();
		} catch(PitNotFoundException ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testTakeOpposite() {
		try {
			int takenStones = board.getPit(0, 5).takeStones();		
			board.getPit(0, 5).nextHolder().putStone(takenStones);
			takenStones = board.getPit(1, 5).takeStones();		
			board.getPit(1, 5).nextHolder().putStone(takenStones);
			takenStones = board.getPit(0, 4).takeStones();		
			board.getPit(0, 4).nextHolder().putStone(takenStones);
			takenStones = board.getPit(1, 5).takeStones();		
			board.getPit(1, 5).nextHolder().putStone(takenStones);
			int stoneCountOpp = board.getPit(1, 4).getStoneCount();
			int stoneCountStore = player1Store.getStoneCount();
			assertEquals("Stone count board (1,4) not expected", 6, board.getPit(1, 4).getStoneCount());
			assertEquals("Stone count board (0,5) not expected", 0, board.getPit(0, 4).getStoneCount());
			
			takenStones = board.getPit(0, 5).takeStones();		
			board.getPit(0, 5).nextHolder().putStone(takenStones);
			
			assertEquals("Stone count store not expected", 
					stoneCountStore + stoneCountOpp + 1, player1Store.getStoneCount());
		} catch(PitNotFoundException ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testBoardDimensions() {
		Board boardPojo = builder.initBoard();
		assertEquals("Number players incorrect", 2, boardPojo.getPlayerNr());
		assertEquals("Number pits incorrect", 6, boardPojo.getHolderNr());
	}
}
