package game.kalaha.hbm;

import game.kalaha.exception.PitNotFoundException;

/**
 *	The game as stored by hibernate in the database <br/><br/>
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
public class Game {
	private Competitors competitors;

	private boolean turnP1;	
	private Integer store1;
	private Integer store2;
	private Integer pit[][] = new Integer[2][6];
	
	public Competitors getCompetitors() {
		return competitors;
	}
	public void setCompetitors(Competitors competitors) {
		this.competitors = competitors;
	}
	public Integer getPit1_1() {
		return pit[0][0];
	}
	public void setPit1_1(Integer pit1_1) {
		this.pit[0][0] = pit1_1;
	}
	public Integer getPit1_2() {
		return pit[0][1];
	}
	public void setPit1_2(Integer pit1_2) {
		this.pit[0][1] = pit1_2;
	}
	public Integer getPit1_3() {
		return pit[0][2];
	}
	public void setPit1_3(Integer pit1_3) {
		this.pit[0][2] = pit1_3;
	}
	public Integer getPit1_4() {
		return pit[0][3];
	}
	public void setPit1_4(Integer pit1_4) {
		this.pit[0][3] = pit1_4;
	}
	public Integer getPit1_5() {
		return pit[0][4];
	}
	public void setPit1_5(Integer pit1_5) {
		this.pit[0][4] = pit1_5;
	}
	public Integer getPit1_6() {
		return pit[0][5];
	}
	public void setPit1_6(Integer pit1_6) {
		this.pit[0][5] = pit1_6;
	}
	public Integer getStore1() {
		return store1;
	}
	public void setStore1(Integer store1) {
		this.store1 = store1;
	}
	public Integer getPit2_1() {
		return pit[1][0];
	}
	public void setPit2_1(Integer pit2_1) {
		this.pit[1][0] = pit2_1;
	}
	public Integer getPit2_2() {
		return pit[1][1];
	}
	public void setPit2_2(Integer pit2_2) {
		this.pit[1][1] = pit2_2;
	}
	public Integer getPit2_3() {
		return pit[1][2];
	}
	public void setPit2_3(Integer pit2_3) {
		this.pit[1][2] = pit2_3;
	}
	public Integer getPit2_4() {
		return pit[1][3];
	}
	public void setPit2_4(Integer pit2_4) {
		this.pit[1][3] = pit2_4;
	}
	public Integer getPit2_5() {
		return pit[1][4];
	}
	public void setPit2_5(Integer pit2_5) {
		this.pit[1][4] = pit2_5;
	}
	public Integer getPit2_6() {
		return pit[1][5];
	}
	public void setPit2_6(Integer pit2_6) {
		this.pit[1][5] = pit2_6;
	}
	public boolean isTurnP1() {
		return turnP1;
	}
	public void setTurnP1(boolean turnP1) {
		this.turnP1 = turnP1;
	}
	public Integer getStore2() {
		return store2;
	}
	public void setStore2(Integer store2) {
		this.store2 = store2;
	}
	
	/**
	 * Helper function to safely get pits from the matrix
	 * of pits
	 * 
	 * @param player
	 * @param pitIndex
	 * @return
	 * @throws PitNotFoundException
	 */
	public Integer getPit(int player, int pitIndex) throws PitNotFoundException {
		if(pit.length == 0 || player > pit.length ||
				pitIndex > pit[0].length)
			throw new PitNotFoundException("Player or pit index doesn't exist");
		
		return this.pit[player][pitIndex];
	}
	
	public void setPit(int player, int pitIndex, int val) throws PitNotFoundException {
		if(pit.length == 0 || player > pit.length ||
				pitIndex > pit[0].length)
			throw new PitNotFoundException("Player or pit index doesn't exist");
		
		this.pit[player][pitIndex] = val;
	}
}
