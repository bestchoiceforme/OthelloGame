
package othello;
/**  * OthPlayer serves as the abstract base class of any
  * Othello game player class. Any concrete player class must
  * implement the <tt>NextMove</tt> method.
  */
public abstract class OthPlayer
{
  /**
    * specify black player
    */
  public static final int BLACK=0;

  /**    * specify white player
    */

  public static final int WHITE=1;
  /**    * specify the chess board cell status as not occupied by either black
    * or white
    */
   public static final int EMPTY=2;

  /**    * specify the color of player instance
    */
   protected int _color;  // player color.

  /**
    * Computes the next move based on the current board status.
    * @param <tt>board_status</tt> is a 2D array with size of each
    *  dimension being 8. Any valid board_status[x][y] has only 3
    * possible values: OthPlayer.BLACK, OthPlayer.WHITE, OthPlayer.EMPTY,
    * indicating the board cell at row x and column y is occupied by
    * the black or white player or just empty.
    * @return A OthMove object to specify the row and column position
    * of the next move for current player.
    */
   public abstract OthMove NextMove(int[][] board_status);
}


