package players;
import othello.*;
/**
* Sample skeleton for a smart player that knows how to make the next move based
* on some board evaluation strategy.
*/
public class SmartPlayer extends OthPlayer
{
	public SmartPlayer(int color)
	{
      _color = color;
	}

	/**
	 * @param boardStatus 
	 * @return
	 */
	private int evalBoard(int[][] boardStatus)
	{
		return (0); // Student to write the code.
	}

	/**
	 * @param boardStatus 
	 * @param depth 
	 * @param playerColor 
	 * @return 
	 */
	private int eval(int[][] boardStatus, int depth, int playerColor)
	{
		return (0); // Student to write the code.
	}
   
	/**
	 * Computes the next move based on the current board status.
	 * @param board_status 
	 * @return 
	 */
	public OthMove NextMove(int[][] board_status)
	{
		return (null); // Student to write the code.
	}

}

