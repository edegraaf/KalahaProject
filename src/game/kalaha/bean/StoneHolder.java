package game.kalaha.bean;

/**
 *	Interface of Spring Bean. Both the store/kalaha and the other 
 *  pits hold the stones.<br/><br/>
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
 */
public interface StoneHolder {
	/** 
	 * @return The number of stones in the holder
	 */
	public int getStoneCount();
	/**
	 * @param count The number of stones to set
	 */
	public void setStoneCount(int count);
	/**
	 * The next holder stones will be placed
	 * in when pit selected.
	 * 
	 * @return The next holder, can also be a store
	 */
	public StoneHolder nextHolder();
	/**
	 * Empties the pit and takes the stones.
	 * 
	 * @return The stones in pit
	 */
	public int takeStones();
	/**
	 * Distributes the stones of the next pits
	 * 
	 * @param takenStones
	 */
	public void putStone(int takenStones);
	/**
	 * Does take and put in order
	 */
	public void takePut();
	/**
	 * Help function where origin is kept
	 * in this way we will only take opposite
	 * stones if the player owns the last empty
	 * pit
	 * 
	 * @param takenStones
	 * @param origin
	 */
	public void putStone(int takenStones, String origin);
	/**
	 * @return User string of the owner of the pit / store
	 */
	public String getOwner();
	public void setOwner(String owner);
	/**
	 * @return The store of the owner
	 */
	public StoneHolder getOwnerStore();
	public void setOwnerStore(StoneHolder store);
	public void setNextHolder(StoneHolder holder);
	/**
	 * Sets the pit opposite of this pit. This allows
	 * you to take the stones opposite when the rules
	 * allow for it.
	 * 
	 * @param holder The opposite holder / pit
	 */
	public void setOppositeHolder(StoneHolder holder);
	public StoneHolder getOppositeHolder();
	/**
	 * Gets the last stone count passing by the holder.
	 * If this is 1 for the players store then the player
	 * has another turn.
	 * 
	 * @return Last stone count passing by the holder
	 */
	public int getLastPastBy();
	public void setLastPastBy(int newVal);
}
