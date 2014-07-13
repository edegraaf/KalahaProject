package game.kalaha.test;

import static org.junit.Assert.*;
import game.kalaha.bean.Board;
import game.kalaha.controller.GameController;
import game.kalaha.controller.UserInfo;
import game.kalaha.exception.PitNotFoundException;
import game.kalaha.hbm.Game;
import game.kalaha.hbm.User;
import game.kalaha.service.GameService;
import game.kalaha.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *	Tests the model-view-control mechanism used in the GUI<br/><br/>
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
public class ModelControlTest {
	GameService service;
	GameController controller;

	@Before
	public void setUp() throws Exception {
		service = new GameService();
		controller = new GameController(service);
		User user = new User();
		user.setUserid(0);
		user.setUsername("player1");
		user.setPassword("player1");
		controller.setUser(user);
		resetBoard();
	}

	@After
	public void tearDown() throws Exception {
		resetBoard();
	}

	@Test
	public void testServiceCreation() {
		assertFalse("No games being played", service.getGamesPlayed().isEmpty());
		assertFalse("No competitors set", service.getCompetitors().isEmpty());
	}
	
	@Test
	public void testPlayerTurn() {
		ModelMap model = new ModelMap();
		String functionReturn = controller.playerTurn(model);
		assertFalse("Wrong capitalization hasturn", model.containsKey("hasTurn"));
		assertTrue("No hasturn in the model", model.containsKey("hasturn"));
		assertEquals("Function return unexpected", "turn_status", functionReturn);
	}
	
	@Test
	public void testRetrieveBox() {
		ModelMap model = new ModelMap();
		String functionReturn = controller.retrieveBox(0, 0, model);
		assertEquals("Function return unexpected", "board_status", functionReturn);
		assertTrue("No stonecount in the model", model.containsKey("stonecount"));		
	}
	
	@Test
	public void testRetrieveStore() {
		ModelMap model = new ModelMap();
		String functionReturn = controller.retrieveStore(0, model);
		assertEquals("Function return unexpected", "store_status", functionReturn);
		assertTrue("No stonecount in the model", model.containsKey("stonecount"));		
	}
	
	@Test
	public void testLogin(){
		ModelMap model = new ModelMap();
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername("player1");
		
		User dbuser = service.retrieveUser(userInfo.getUsername());
		userInfo.setPassword(dbuser.getPassword());
		String functionReturn = controller.login(userInfo, model);
		assertEquals("Valid password failt", "redirect:board.do", functionReturn);
		userInfo.setPassword("invalidpwd");
		functionReturn = controller.login(userInfo, model);
		assertEquals("Invalid password was accepted", "redirect:login.do", functionReturn);
	}
	
	@Test
	public void testBoardGui(){
		ModelMap model = new ModelMap();
		String functionReturn = controller.board(model);
		assertNotNull("Board image is NULL", model.get("boardimg"));
		assertNotEquals("Board image empty", "", model.get("boardimg"));
		assertEquals("View unexpected", "board", functionReturn);
	}
	
	@Test
	public void testPlayBox(){
		ModelMap model = new ModelMap();
		String functionReturn = controller.playBox(0, 5, model);
		assertEquals("View playBox unexpected", "click_status", functionReturn);
		functionReturn = controller.retrieveBox(5, 0, model);
		assertEquals("View retrieveBox unexpected", "board_status", functionReturn);
		assertEquals("Value pit 0, 5 unexpected", 0, ((Integer)model.get("stonecount")).intValue());
		functionReturn = controller.retrieveBox(4, 0, model);
		assertEquals("Value pit 0, 4 unexpected", 7, ((Integer)model.get("stonecount")).intValue());
	}
	
	@Test
	public void testRetrievePlayers(){
		ModelMap model = new ModelMap();
		controller.retrievePlayers(model);
		assertEquals("Player id for player1 unexpected", 0, model.get("player1"));
		assertEquals("Player id for player2 unexpected", 1, model.get("player2"));
	}
	
	private void resetBoard(){
		Board board = service.getBoard(0);
		Game game = new Game();
		game.setCompetitors(service.getCompetitors().get(0));

		SessionFactory factory = HibernateUtil.getSessionFactory();
    	Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		for(int p = 0;p < board.getPlayerNr();p++){
			for(int b = 0;b < board.getHolderNr();b++){
				try {
					game.setPit(p, b, 6);
				} catch (PitNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		game.setStore1(0);
		game.setStore2(0);
		game.setTurnP1(true);
		session.update(game);
		transaction.commit();
		session.close();
	}
}
