
package othello;
/**  * <tt>OthMove</tt> class represents a game move on the chess board.
  * For default 8x8 board, any legal move must have row position between
  * 0 and 7, and column position between 0 and 7.
 */
public final class OthMove
{
  /**
    * the row position of the move.
    */
  public int x;

  /**    * the column position of the move.
    */
  public int y;

  /**    * create a new move with row <tt>x</tt> and column <tt>y</tt>.
    */
  public OthMove(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  /**    * create a copy of move object
    */
  public OthMove Copy()
  {
    OthMove copy = new OthMove(this.x, this.y);
    return copy;
  }

  /**    * return the String representation of move object
    */
  public String toString()
  {
    return new String("["+x+","+y+"]");
  }
}

