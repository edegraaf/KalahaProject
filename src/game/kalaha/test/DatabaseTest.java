package game.kalaha.test;

import static org.junit.Assert.*;

import java.util.List;

import game.kalaha.hbm.Competitors;
import game.kalaha.hbm.Game;
import game.kalaha.hbm.User;
import game.kalaha.util.HibernateUtil;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *	Testing loading and storing from / in the database by Hibernate<br/><br/>
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
public class DatabaseTest {	
	private SessionFactory factory;
	private Session session;
	
	@Before
	public void setUp() throws Exception {
		factory = HibernateUtil.getSessionFactory();
		session = factory.openSession();
	}

	@After
	public void tearDown() throws Exception {
		session.close();
	}

	@Test
	public void testSession() {
		assertNotNull("Hibernate session is NULL", session);
	}
	
	@Test
	public void testUserData() {
		List list = session.createQuery( "from User" ).list();
		List<User> result = (List<User>)list;
		for ( User user :  result ) {
			assertNotNull("User id is NULL", user.getUserid());
			assertNotNull("Username is NULL", user.getUserid());
			assertFalse("Username is empty string", user.getUsername().equals(""));
		}
	}
	
	@Test
	public void testGameData() {
		List list = session.createQuery( "from Game" ).list();
		List<Game> result = (List<Game>)list;
		for ( Game game :  result ) {
			assertNotNull("Composite key not correct", game.getCompetitors());
			assertNotNull("Player 1 id not correct", game.getCompetitors().getPlayer1Id());
			assertNotNull("Player 2 id not correct", game.getCompetitors().getPlayer2Id());
		}
	}
	
	@Test
	public void testGameDataOnId() {
		Query query = session.createQuery("from Game where competitors.player1Id = :p1Id and "
				+ "competitors.player2Id = :p2Id ");
        query.setInteger("p1Id", 0);
        query.setInteger("p2Id", 1);
        assertTrue("Game Player1 vs. Player2 doesn't exist", query.iterate().hasNext());
	}
	
	@Test
	public void testUpdateGame() {
		Query query = session.createQuery("from Game where competitors.player1Id = :p1Id and "
				+ "competitors.player2Id = :p2Id ");
        query.setInteger("p1Id", 0);
        query.setInteger("p2Id", 1);
        Game game = (Game)query.uniqueResult();
        
		Transaction transaction = session.beginTransaction();
		game.setPit1_5(3);
		game.setStore1(2);
		session.update(game);
		transaction.commit();
				
		query = session.createQuery("from Game where competitors.player1Id = :p1Id and "
				+ "competitors.player2Id = :p2Id ");
        query.setInteger("p1Id", 0);
        query.setInteger("p2Id", 1);
        game = (Game)query.iterate().next();
        
        assertEquals("Pit 1,5 was not updated", 3, game.getPit1_5().intValue());
        assertEquals("Store 1 was not updated", 2, game.getStore1().intValue());
        

        transaction = session.beginTransaction();
		game.setPit1_5(6);
		game.setStore1(0);
		session.update(game);
		transaction.commit();
		
		query = session.createQuery("from Game where competitors.player1Id = :p1Id and "
				+ "competitors.player2Id = :p2Id ");
        query.setInteger("p1Id", 0);
        query.setInteger("p2Id", 1);
        game = (Game)query.iterate().next();
        
        assertEquals("Pit 1,5 was not updated back", 6, game.getPit1_5().intValue());
        assertEquals("Store 1 was not updated back", 0, game.getStore1().intValue());
	}
}
