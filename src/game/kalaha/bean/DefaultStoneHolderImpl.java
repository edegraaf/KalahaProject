package game.kalaha.bean;

/**
 *	One implementation of the StoneHolder interface. Implements pit functionality.<br/><br/>
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
 *  @see game.kalaha.bean.StoneHolder
 */
public class DefaultStoneHolderImpl implements StoneHolder {
	private int stoneCount;
	private StoneHolder nextHolder;
	private StoneHolder oppositeHolder;
	private StoneHolder ownerStore;
	private String owner = "";
	private int lastPastBy;

	@Override
	public StoneHolder nextHolder() {
		return nextHolder;
	}

	@Override
	public int takeStones() {
		if(ownerStore == null)
			return 0;
		int temp = stoneCount;
		stoneCount = 0;
		return temp;
	}

	@Override
	public void putStone(int takenStones) {
		putStone(takenStones, owner);
	}
	
	public void putStone(int takenStones, String origin) {
		if(takenStones == 0)
			return;
		else if(takenStones == 1 && 
				stoneCount == 0 && 
				origin.equals(owner) &&
				oppositeHolder != null) { // Last pit was empty => take opposite
			ownerStore.setStoneCount(ownerStore.getStoneCount() + oppositeHolder.takeStones() + 1);
		} else {
			lastPastBy = takenStones; // In order to know turn
			takenStones--;
			stoneCount++;
			nextHolder.putStone(takenStones, origin);
		}
	}

	@Override
	public String getOwner() {
		return owner;
	}

	@Override
	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public int getStoneCount() {
		return stoneCount;
	}
	
	@Override
	public void setStoneCount(int count) {
		stoneCount = count;
	}

	@Override
	public StoneHolder getOwnerStore() {
		return ownerStore;
	}

	@Override
	public void setOwnerStore(StoneHolder store) {
		ownerStore = store;		
	}

	@Override
	public void setNextHolder(StoneHolder holder) {
		nextHolder = holder;	
	}

	@Override
	public void setOppositeHolder(StoneHolder holder) {
		oppositeHolder = holder;
	}

	@Override
	public StoneHolder getOppositeHolder() {
		return oppositeHolder;
	}

	@Override
	public int getLastPastBy() {
		return lastPastBy;
	}

	@Override
	public void takePut() {
		int takenStones = takeStones();
		this.nextHolder().putStone(takenStones);
	}

	@Override
	public void setLastPastBy(int newVal) {
		lastPastBy = newVal;		
	}

}
