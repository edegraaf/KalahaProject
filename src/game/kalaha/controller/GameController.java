package game.kalaha.controller;

import javax.servlet.http.HttpServletRequest;

import game.kalaha.bean.Board;
import game.kalaha.bean.BoardBuilder;
import game.kalaha.bean.StoneHolder;
import game.kalaha.config.ApplicationContextLoader;
import game.kalaha.exception.PitNotFoundException;
import game.kalaha.hbm.User;
import game.kalaha.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *	The controller handles UI events and gives results to the Velocity view<br/><br/>
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
@Controller
public class GameController {
    private GameService gameService;
    
    @Autowired
    private User user;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * The first login view
     * 
     * @param model
     * @return
     */
    @RequestMapping("/login.do")
    public String loginView(ModelMap model) {
    	return doLogin(model, "");
    }    
    
    /**
     * Checks the password and logs in.
     * 
     * @param userInfo Given username and password
     * @param model
     * @return Next step in process
     */
    @RequestMapping("/processlogin.do")
    public String login(@ModelAttribute("userinfo") UserInfo userInfo, ModelMap model) {
    	//model.put("errormsg", userInfo.getUsername());
    	User dbuser = gameService.retrieveUser(userInfo.getUsername());
    	if(dbuser.getPassword().equals(userInfo.getPassword())){
    		user.setUsername(dbuser.getUsername());
    		user.setPassword(dbuser.getPassword());
    		user.setUserid(dbuser.getUserid());
    		return "redirect:board.do";
    	} else {
    		return "redirect:login.do";
    	}
    }
    
    /**
     * Invalidates the session and thus the
     * session scoped user bean is also reset
     * via autowire.
     * 
     * @param request
     * @return
     */
    @RequestMapping("/logout.do")
    public String logout(HttpServletRequest request){
    	request.getSession().invalidate();
    	return "redirect:login.do";
    }
    
    /**
     * The first screen after login displays the board.
     * 
     * @param model
     * @return
     */
    @RequestMapping("/board.do")
    public String board(ModelMap model) {
    	if(user.getUsername() == null || user.getUsername().equals("")){
    		return "redirect:login.do";
    	}
        
    	ApplicationContext appContext = ApplicationContextLoader.getInstance().getAppContext();
    	BoardBuilder builder = (BoardBuilder)appContext.getBean("boardBuilder");
    	model.put("boardimg", builder.getBoardImg());
    	model.put("username", user.getUsername());
        return "board";
    }

    /**
     * When the user clicks a pit then we play the stones
     * and distribute them according to the rules.
     * 
     * @param boxId Pit number according to the player
     * @param playerId The player playing
     * @param model
     * @return
     */
    @RequestMapping("/player{pid}/box{id}/click.do")
    public String playBox(@PathVariable(value = "pid") Integer playerId,
    		@PathVariable(value = "id") Integer boxId, ModelMap model) {
    	if(user.getUsername() == null || user.getUsername().equals("")){
    		return "redirect:login.do";
    	}
    	
    	Board board = gameService.getBoard(user.getUserid());
    	// StoneHolder boardField[][] = board.getPits();
    	int playerIndex = board.getCompetitors().getPlayer1Id() == user.getUserid() ? 0 : 1;
    	boolean hasTurn = (board.isTurnP1() && playerIndex == 0) ||
        		(!board.isTurnP1() && playerIndex == 1);
    	try {
	    	if(hasTurn && playerIndex == playerId) {
	    		board.getPit(playerIndex, boxId).takePut();
	    		board.decideTurnP1();
	    		board.decideGameEnd();
	    		gameService.storeBoard(playerId);
	    	}
	    
	        hasTurn = (board.isTurnP1() && playerIndex == 0) ||
	        		(!board.isTurnP1() && playerIndex == 1);
	    	model.put("hasturn", hasTurn);
	    	model.put("stonecount", board.getPit(playerIndex, boxId).getStoneCount());
    	} catch(PitNotFoundException ex){
    		ex.printStackTrace();
    	}
        return "click_status";
    }
    
    /**
     * Retrieves the board without playing a box
     * 
     * @param boxId
     * @param playerId
     * @param model
     * @return
     */
    @RequestMapping("/player{pid}/box{id}/retrieve.do")
    public String retrieveBox(@PathVariable(value = "id") Integer boxId, 
    		@PathVariable(value = "pid") Integer playerId, ModelMap model) {
    	if(user.getUsername() == null || user.getUsername().equals("")){
    		return "redirect:login.do";
    	}
    	
        Board board = gameService.getBoard(user.getUserid());
    	// StoneHolder boardField[][] = board.getPits(); 	
    	
    	try {
			model.put("stonecount", board.getPit(playerId, boxId).getStoneCount());
		} catch (PitNotFoundException e) {
			e.printStackTrace();
		}
        return "board_status";
    }
    
    /**
     * Retrieves the stone count for the stones in the store
     * 
     * @param storeId 0 for player 1 and 1 for player 2
     * @param model
     * @return
     */
    @RequestMapping("/store{id}/retrieve.do")
    public String retrieveStore(@PathVariable(value = "id") Integer storeId, ModelMap model) {
    	if(user.getUsername() == null || user.getUsername().equals("")){
    		return "redirect:login.do";
    	}
    	
        Board board = gameService.getBoard(user.getUserid());
    	StoneHolder playerStore = board.getPlayer1Store();
    	if(storeId == 1){
    		playerStore = board.getPlayer2Store();
    	}
    	
    	model.put("stonecount", playerStore.getStoneCount());
        return "store_status";
    }
    
    /**
     * Retrieves the players opposing each other.
     * 
     * @param model
     * @return
     */
    @RequestMapping("/players/retrieve.do")
    public String retrievePlayers(ModelMap model) {
    	if(user.getUsername() == null || user.getUsername().equals("")){
    		return "redirect:login.do";
    	}
    	
        Board board = gameService.getBoard(user.getUserid());
    	
        model.put("player1", board.getCompetitors().getPlayer1Id());
    	model.put("player2", board.getCompetitors().getPlayer2Id());
        return "player_data";
    }
    
    /**
     * Based on the logged in player the board is retrieved,
     * player turn is decided and returned
     * 
     * @param model
     * @return true if the current player has his/her turn
     */
    @RequestMapping("/player/hasturn.do")
    public String playerTurn(ModelMap model) {
    	if(user.getUsername() == null || user.getUsername().equals("")){
    		return "redirect:login.do";
    	}
    	
        Board board = gameService.getBoard(user.getUserid());
    	
        int playerIndex = board.getCompetitors().getPlayer1Id() == user.getUserid() ? 0 : 1;
        boolean hasTurn = (board.isTurnP1() && playerIndex == 0) ||
        		(!board.isTurnP1() && playerIndex == 1);
    	model.put("hasturn", hasTurn);
    	model.put("gamestatus", board.getGameStatus().toString());
        return "turn_status";
    }
    
    private String doLogin(ModelMap model, String msg){
    	model.put("errormsg", msg);
    	return "login";
    }

    /**
     * A method that makes unit testing possible.
     * Usually user is set via autowire
     * 
     * @param user
     */
	public void setUser(User user) {
		this.user = user;
	}
}

