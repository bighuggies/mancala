package mancala;

public abstract class SeedHolder {	
	private int _seeds = 0;
	
	public void addSeeds(int amount) {
		_seeds += amount;
	}
	public void takeSeeds(int amount) {
		if (_seeds - amount < 0) {
			throw new IllegalArgumentException("Not enough seeds to take " + amount + ".");
		}
		
		_seeds -= amount;
	}
}
