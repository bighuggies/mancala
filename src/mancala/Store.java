package mancala;

public class Store extends SeedHolder {

	public Store(Player owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int takeSeedsFrom(SeedHolder seedHolder, int seeds) {
		if (seedHolder.getOwner() == this.getOwner()) {
			return super.takeSeedsFrom(seedHolder, seeds);
		} else {
			return this.countSeeds();
		}
	}
}
