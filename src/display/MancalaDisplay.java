package display;

import mancala.Board;

public class MancalaDisplay {
	private Board _board;

	public MancalaDisplay(Board board) {
		_board = board;
	}

	public void display(MancalaFormatter formatter) {
		formatter.display(this._board);
	}
}
