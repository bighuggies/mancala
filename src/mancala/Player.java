package mancala;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Player {
	private SeedHolder[] _stores;
	
	public Player(SeedHolder[] stores) {
		this._stores = stores;
	}
	
	public int distributeSeeds(int amount) {
		for(SeedHolder s : this._stores) {
			s.addSeeds(1);
			amount =- 1;
			
			if (amount == 0)
				break;
		}
		
		return amount;
	}
	
	public int distributeSeeds(int amount, int startingHouse) {
		throw new NotImplementedException();
	}
	
	public void transferSeeds(int amount, int depositHouse) {
	}
}
