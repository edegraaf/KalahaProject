package game.kalaha.hbm;

import java.io.Serializable;

/**
 *	Keeps the composite key of player ids competing in a game <br/><br/>
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
public class Competitors implements Serializable {
	private static final long serialVersionUID = -2506069004553926465L;
	private Integer player1Id;
	private Integer player2Id;
	
	public Integer getPlayer1Id() {
		return player1Id;
	}
	public void setPlayer1Id(Integer player1Id) {
		this.player1Id = player1Id;
	}
	public Integer getPlayer2Id() {
		return player2Id;
	}
	public void setPlayer2Id(Integer player2Id) {
		this.player2Id = player2Id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((player1Id == null) ? 0 : player1Id.hashCode());
		result = prime * result
				+ ((player2Id == null) ? 0 : player2Id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Competitors other = (Competitors) obj;
		if (player1Id == null) {
			if (other.player1Id != null)
				return false;
		} else if (!player1Id.equals(other.player1Id))
			return false;
		if (player2Id == null) {
			if (other.player2Id != null)
				return false;
		} else if (!player2Id.equals(other.player2Id))
			return false;
		return true;
	}
	
}
