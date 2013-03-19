package mancala;

import java.util.Arrays;

public class Player {
	private String _name;
	private Piece[] _stores;

	public Player(String name, int numHolders) {
		this._name = name;
		this._stores = new Piece[numHolders];

		for (int i = 0; i < numHolders - 1; i++) {
			this._stores[i] = new House(this);
		}

		this._stores[numHolders] = new Store(this);
	}

	public String getName() {
		return this._name;
	}

	public Piece[] getHouses() {
		return (Piece[]) Arrays.asList(_stores)
				.subList(0, _stores.length - 2).toArray();
	}

	public Piece getStore() {
		return _stores[_stores.length - 1];
	}

	public Piece getHouse(int index) {
		return this._stores[index];
	}
}
