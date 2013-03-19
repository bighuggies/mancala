package mancala;

public abstract class Piece {
	private int _seeds = 0;
	private Player _owner;
	
	public Piece(Player owner) {
		this._owner = owner;
	}
	
	public int takeSeedsFrom(Piece piece) {
		return this.takeSeedsFrom(piece, piece.countSeeds());
	}
	
	public int takeSeedsFrom(Piece piece, int seeds) {
		return this.putSeeds(piece.takeSeeds(seeds));
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
