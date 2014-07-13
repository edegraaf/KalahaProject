package game.kalaha.service;

import game.kalaha.bean.Board;
import game.kalaha.bean.BoardBuilder;
import game.kalaha.config.ApplicationContextLoader;
import game.kalaha.exception.PitNotFoundException;
import game.kalaha.hbm.Competitors;
import game.kalaha.hbm.Game;
import game.kalaha.hbm.User;
import game.kalaha.util.HibernateUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 *	Model gets the Spring bean with results and makes it available to the
 *  controller <br/><br/>
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
@Service
public class GameService {
    private Map<Competitors, Board> gamesPlayed = new HashMap<Competitors, Board>();
    private Map<Integer, Competitors> competitors = new HashMap<Integer, Competitors>();

    public GameService() {
    	initService();
    }

    public void initService() {
    	SessionFactory factory = HibernateUtil.getSessionFactory();
    	Session session = factory.openSession();
    	List list = session.createQuery( "from Game" ).list();
		List<Game> result = (List<Game>)list;
		session.close();
		ApplicationContext appContext = ApplicationContextLoader.getInstance().getAppContext();
		BoardBuilder builder = (BoardBuilder)appContext.getBean("boardBuilder");

		for ( Game game :  result ) {
			Board boardPojo = builder.initBoard();
			gamesPlayed.put(game.getCompetitors(), boardPojo);
			/*
			 * Update the board with the stored values
			 */
			for(int p = 0;p < boardPojo.getPlayerNr();p++){
				for(int h = 0;h < boardPojo.getHolderNr();h++){
					try {
						boardPojo.getPit(p, h).setStoneCount(game.getPit(p, h));
					} catch (PitNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			boardPojo.getPlayer1Store().setStoneCount(game.getStore1());
			boardPojo.getPlayer2Store().setStoneCount(game.getStore2());
			boardPojo.setTurnP1(game.isTurnP1());
			boardPojo.setCompetitors(game.getCompetitors());
			competitors.put(game.getCompetitors().getPlayer1Id(), game.getCompetitors());
			competitors.put(game.getCompetitors().getPlayer2Id(), game.getCompetitors());
		}
    }
    
    public Board getBoard(Integer playerId){
    	return gamesPlayed.get(competitors.get(playerId));
    }

	public Map<Competitors, Board> getGamesPlayed() {
		return gamesPlayed;
	}

	public Map<Integer, Competitors> getCompetitors() {
		return competitors;
	}

	public User retrieveUser(String username){
		SessionFactory factory = HibernateUtil.getSessionFactory();
    	Session session = factory.openSession();
		Query query = session.createQuery("from User where username = :username");
	    query.setString("username",username);
	    User user = (User)query.uniqueResult();
	    session.close();
	    return user;
	}
	
	public void storeBoard(Integer playerId){
		Board board = this.getBoard(playerId);
		Game game = new Game();
		game.setCompetitors(getCompetitors().get(playerId));

		SessionFactory factory = HibernateUtil.getSessionFactory();
    	Session session = factory.openSession();
		Transaction transaction = session.beginTransaction();
		for(int p = 0;p < board.getPlayerNr();p++){
			for(int b = 0;b < board.getHolderNr();b++){
				try {
					game.setPit(p, b, board.getPit(p, b).getStoneCount());
				} catch (PitNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		game.setStore1(board.getPlayer1Store().getStoneCount());
		game.setStore2(board.getPlayer2Store().getStoneCount());
		game.setTurnP1(board.isTurnP1());
		session.update(game);
		transaction.commit();
		session.close();
	}
}

