package mancala;

public abstract class SeedHolder {
	private int _seeds = 0;
	private Player _owner;
	
	public SeedHolder(Player owner) {
		this._owner = owner;
	}
	
	public int takeSeedsFrom(SeedHolder seedHolder) {
		return this.takeSeedsFrom(seedHolder, seedHolder.countSeeds());
	}
	
	public int takeSeedsFrom(SeedHolder seedHolder, int seeds) {
		return this.putSeeds(seedHolder.takeSeeds(seeds));
	}

	public int takeSeeds() {
		return this.takeSeeds(this._seeds);
	}

	public int takeSeeds(int seeds) {
		if (this._seeds - seeds < 0)
			throw new IllegalArgumentException();

		this._seeds -= seeds;

		return this._seeds;
	}

	public int putSeeds(int seeds) {
		this._seeds += seeds;
		return this._seeds;
	}
	
	public int countSeeds() {
		return this._seeds;
	}
	
	public Player getOwner() {
		return _owner;
	}
}
