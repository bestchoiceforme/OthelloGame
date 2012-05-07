
package players;
import othello.*;
import java.util.*;

/**  * DumbPlayer implements the simplest game strategy by randomly
  * picking one from the set of legal moves
  */
public class DumbPlayer extends OthPlayer
{

  public DumbPlayer(int player_color )  {      _color = player_color;
  }

  public OthMove NextMove(int[][] board_status)  {    Vector moves = OthManager.GetLegalMoves (_color);
    if (moves.size() == 0)
    {
      throw new RuntimeException("No move!");
    }
    else
    {
      Date time = new Date();
      long seed = time.getTime();
      Random rand = new Random(seed);
      int i = Math.abs(rand.nextInt()) % moves.size();
      return (OthMove) moves.elementAt(i);
    }
  }

  public static void main(String[] argv)  {    
   /* OthPlayer black_player = new DumbPlayer(BLACK);
    OthPlayer white_player = new DumbPlayer(WHITE);
    if (argv.length==1 && argv[0].equals("-allmachine"))
    {
      OthManager.Singleton().PlayOthello(black_player, white_player);
    }
    else if (argv.length==1 && argv[0].equals("-humansecond"))
    {
      OthManager.Singleton().PlayOthelloWithHuman(OthPlayer.BLACK, black_player);
    }
    else if (argv.length==1 && argv[0].equals("-help"))
    {
      System.out.println ("Usage:\njava DumbPlayer [-help] [-allmachine] [-humansecond]");
    }
    else
    {
      OthManager.Singleton().PlayOthelloWithHuman(OthPlayer.WHITE, white_player);
    }*/
  }
}



