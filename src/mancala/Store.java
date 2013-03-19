package mancala;

public class Store extends Piece {

	public Store(Player owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int takeSeedsFrom(Piece seedHolder, int seeds) {
		if (seedHolder.getOwner() == this.getOwner()) {
			return super.takeSeedsFrom(seedHolder, seeds);
		} else {
			return this.countSeeds();
		}
	}
}
