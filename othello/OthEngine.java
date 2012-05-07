
package othello;
import java.util.*;
public final class OthEngine{  
    private int board_status[][];

  OthEngine ()  {    int i, j;
    board_status = new int[OthBoard.SIZE][OthBoard.SIZE];
    for (i=0; i<OthBoard.SIZE; i++)
    {
      for (j=0; j<OthBoard.SIZE; j++)
      {
        board_status[i][j] = OthPlayer.EMPTY;
      }
    }
    board_status[OthBoard.SIZE/2-1][OthBoard.SIZE/2-1] =
         board_status[OthBoard.SIZE/2][OthBoard.SIZE/2] = OthPlayer.BLACK;
    board_status[OthBoard.SIZE/2][OthBoard.SIZE/2-1] =
         board_status[OthBoard.SIZE/2-1][OthBoard.SIZE/2] = OthPlayer.WHITE;
  }

  private OthEngine (OthEngine engine)  {    int i, j;
    board_status = new int[OthBoard.SIZE][OthBoard.SIZE];
    for (i=0; i<OthBoard.SIZE; i++)
    {
      for (j=0; j<OthBoard.SIZE; j++)
      {
        board_status[i][j] = engine.board_status[i][j];
      }
    }
  }

  public OthEngine GetEngineCopy ()  {    return new OthEngine(this);
  }

    public int GetScore (int player_color)
  {
     int cell_total = 0;
     int i, j;
     for (i=0; i<OthBoard.SIZE; i++)
     {
         for (j=0; j<OthBoard.SIZE; j++)
         {
            if (board_status[i][j] == player_color)
            {
               cell_total++;
            }
      }
    }
    return cell_total;
  }

  public int[][] GetBoardStatus  ()  {    int i, j;
    int[][] board_copy;

    board_copy = new int[OthBoard.SIZE][OthBoard.SIZE];    for (i=0; i<OthBoard.SIZE; i++)
    {
      for (j=0; j<OthBoard.SIZE; j++)
      {
        board_copy[i][j] = board_status[i][j];
      }
    }
    return board_copy;
  }

  public int GetCellColor (int x, int y)  {    return board_status[x][y];
  }

  public int GetNumEmptyCell ()  {    int cell_total = 0;
    int i, j;
    for (i=0; i<OthBoard.SIZE; i++)
    {
      for (j=0; j<OthBoard.SIZE; j++)
      {
	      if (board_status[i][j] == OthPlayer.EMPTY)
         {
	         cell_total++;
	      }
      }
    }
    return cell_total;
  }

  public boolean DetectMoreMove(int player_color)  {    Vector move_vector = GetLegalMoves(player_color);
    return (move_vector.size()>0);
  }


  public boolean IsMoveLegal(int player_color, OthMove move)  {    if (!IsInBound(move))
      return false;
    if (board_status[move.x][move.y] != OthPlayer.EMPTY)
      return false;
    int opponent_color = GetOpponentColor(player_color);
    int x = move.x;
    int y = move.y;
    int dx, dy, oppx, oppy;
    for (dx=-1; dx<=1; dx++)
    {
      for (dy=-1; dy<=1; dy++)
      {
        oppx = x+dx;
        oppy = y+dy;
        OthMove opp_move = new OthMove(oppx, oppy);
        if (IsInBound(opp_move) &&
            board_status[oppx][oppy]==opponent_color &&            IsFlippable(opp_move, dx, dy))
        {
          return true;
        }
      }
    }
    return false;
  }

  public void MakeMove (int player_color, OthMove move)  {    if (!IsMoveLegal(player_color, move))
    {
      throw new RuntimeException("Illegal move!");
    }

    UpdateBoard(player_color, move);  }

  private void UpdateBoard(int player_color, OthMove move)  {    board_status[move.x][move.y] = player_color;
    FlipCells(move);
  }

  /**  * flip all possible opponent's cells beginning at move cell  */  private void FlipCells (OthMove move)
  {
    int x = move.x;
    int y = move.y;
    int player_color = board_status[x][y];
    int opponent_color = GetOpponentColor(player_color);
    int dx, dy, oppx, oppy;
    for (dx=-1; dx<=1; dx++)
    {
      for (dy=-1; dy<=1; dy++)
      {
        oppx = x+dx;
        oppy = y+dy;
        OthMove opp_move = new OthMove(oppx, oppy);
        if (IsInBound(opp_move) &&
            board_status[oppx][oppy]==opponent_color &&
            IsFlippable(opp_move, dx, dy))
        {
            while (board_status[oppx][oppy] == opponent_color)
            {
               board_status[oppx][oppy] = player_color;
               oppx += dx;
               oppy += dy;
            }
        }
      }
    }
  }

  public Vector GetLegalMoves(int player_color)  {    int i, j;
    Vector move_vector = new Vector();
    for (i=0; i<OthBoard.SIZE; i++)
    {
      for (j=0; j<OthBoard.SIZE; j++)
      {
        OthMove move = new OthMove(i, j);
        if (IsMoveLegal(player_color, move))
          move_vector.addElement(move);
      }
    }
    return move_vector;
  }

  public static int GetOpponentColor(int player_color)  {    return (player_color == OthPlayer.BLACK)? OthPlayer.WHITE:OthPlayer.BLACK;
  }

  private boolean IsInBound (OthMove move)  {    int x = move.x;
    int y = move.y;
    return ((0<=x) && (0<=y) && (x<OthBoard.SIZE) && (y<OthBoard.SIZE));
  }

  private boolean IsFlippable (OthMove move, int dx, int dy)  {    int x = move.x;
    int y = move.y;
    int next_x = x+dx;
    int next_y = y+dy;
    int player_color = board_status[x][y];
    int opponent_color = GetOpponentColor(player_color);
    OthMove next_move = new OthMove(next_x, next_y);
    while (IsInBound(next_move) &&
	   board_status[next_x][next_y]==player_color)
    {
      next_x += dx;
      next_y += dy;
      next_move = new OthMove(next_x, next_y);
    }
    if (IsInBound(next_move) && board_status[next_x][next_y]==opponent_color)
    {
      return true;
    }
    return false;
  }


  private static void PrintMoves (Vector move_vector)  {    int i;
    for (i=0; i<move_vector.size(); i++)
    {
      OthMove move = (OthMove) move_vector.elementAt(i);
      System.out.println(move);
    }
  }

  public void PrintBoard()  {    int i, j;
    System.out.println("Othello Board Status:");
    for (i=0; i<OthBoard.SIZE; i++)
    {
      for (j=0; j<OthBoard.SIZE; j++)
      {
        switch (board_status[i][j])
        {
            case OthPlayer.EMPTY: System.out.print(" _");  break;
            case OthPlayer.WHITE: System.out.print(" W");  break;
            case OthPlayer.BLACK: System.out.print(" B");  break;
            default: throw new RuntimeException("Bizzare cell color");
        }
      }
      System.out.println();
    }
  }

  public static void main(String[] argv)  {    
    OthEngine game = new OthEngine();
   
   // OthBoard gameboard = new OthBoard(game.board_status);
    //gameboard.DisplayBoard();
    Vector move_vector;
    
    move_vector = game.GetLegalMoves(OthPlayer.WHITE);
    PrintMoves(move_vector);
    move_vector = game.GetLegalMoves(OthPlayer.BLACK);
    PrintMoves(move_vector);
    game.PrintBoard();       
  } 
}



