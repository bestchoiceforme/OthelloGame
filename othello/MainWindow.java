
package othello;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import players.*;

public final class OthManager extends MouseAdapter{  
    private static OthManager singleton = new OthManager();
  public  static OthManager Singleton()
  {
    return singleton;
  }

  /** @SBGen Variable (,view,,64)   */
	private OthBoard oth_board;  /** @SBGen Variable (,model,,64)   */
	private OthEngine oth_engine;

  private static class Player
  {
    public int player_color;
    public OthPlayer player;

    public Player(int player_color, OthPlayer player)    {      
      this.player_color = player_color;
      this.player = player;
    }
  }

  private Player black_player;  private Player white_player;
  private Player machine_player;

  class Timer implements Runnable  {    //static
    final int MOVE_TIME = 100;    
    private int player_color;
    Thread  timer_thread;
    private boolean disable_timer;

    private Timer()    {      
        timer_thread = null;
      disable_timer = false;
    }

    void DisableTimer()
    {
      disable_timer = true;
    }

    void StartTimer(int player_color)    {     
        if (disable_timer) return;
      if (timer_thread !=null)
      {
	      timer_thread = null;
      }
      this.player_color = player_color;
      timer_thread = new Thread(this);
      timer_thread.start();
    }

    void StopTimer()    {      
       if (timer_thread !=null)
      {
	      timer_thread = null;
      }
    }

        @Override
    public void run()    {  
      try
      {
	      Thread.sleep(MOVE_TIME);
      }
      catch (InterruptedException e)
      {
      }
      if (timer_thread == null)
	   return;
      String player = (player_color == OthPlayer.BLACK)?
                                       new String("black"): new String("white");
      String opponent_player = (player_color == OthPlayer.BLACK)?
                                       new String("white"): new String("black");
      System.out.println(player+" runs out of time. Game over!");
      System.out.println(opponent_player+" win!");
      System.exit(1);
    }
  }

  private Timer timer;
  private OthManager()  {    oth_engine = new OthEngine();
    oth_board = new OthBoard(oth_engine.GetBoardStatus());
    timer = new OthManager.Timer();
    timer.DisableTimer();
  }

  private OthPlayer GetPlayer(int player_color)  {    
      return (player_color == OthPlayer.BLACK)? black_player.player:white_player.player;
  }

  private void PrintPass(int player_color)  {    
      String player = (player_color == OthPlayer.BLACK)? new String("black"): new String("white");
    System.out.println(player+" "+"pass\n");
  }

  private void PrintOneMove (int player_color, OthMove move)  {    
      String player = (player_color == OthPlayer.BLACK)?
                                  new String("black"): new String("white");
    System.out.println(player+" goes "+move);
    oth_engine.PrintBoard();
    System.out.println();
  }

  /* return true if one move taken,   * return false if pass
   */
  private boolean PlayOneMove (int player_color)
  {
    if (oth_engine.DetectMoreMove(player_color))
    {
      timer.StartTimer(player_color);
      OthMove move = GetPlayer(player_color).
	   NextMove(oth_engine.GetBoardStatus());
      timer.StopTimer();
      oth_engine.MakeMove(player_color, move);
      PrintOneMove(player_color, move);
      oth_board.DisableMove(move);
      oth_board.DisplayBoard();
      /* add some delay to show board */
      {
	      int wait_time = 1000;
	      try
         {
	         Thread.sleep(wait_time);
   	   }
         catch (InterruptedException e)
         {
	      }
      }
      return true;
    }
    return false;
  }

  private void SignalGameOver()  {   
    int black_score = oth_engine.GetScore(OthPlayer.BLACK);
    int white_score = oth_engine.GetScore(OthPlayer.WHITE);
    System.out.println("Game over! "+GetGameStatus());
    if (black_score>white_score)
    {
      oth_board.SetFinalMessage("black win!");
      System.out.println("black win!");
    }
    else if (black_score<white_score)
    {
      oth_board.SetFinalMessage("white win!");
      System.out.println("white win!");
    }
    else
    {
      oth_board.SetFinalMessage("tie!");
      System.out.println("tie!");
    }
  }

  public void PlayOthello (OthPlayer black, OthPlayer white)  {    
    black_player = new Player(OthPlayer.BLACK, black);
    white_player = new Player(OthPlayer.WHITE, white);
    boolean game_over = false;
    int turn = OthPlayer.BLACK;
    while (!game_over)
    {
      if (!PlayOneMove(turn))
      {
	      if (oth_engine.DetectMoreMove(OthEngine.GetOpponentColor(turn)))
         {
	         PrintPass(turn);
	      }
	      turn = OthEngine.GetOpponentColor(turn);
	      if (!PlayOneMove(turn))
         {
	         game_over = true;
   	   }
      }
      turn = OthEngine.GetOpponentColor(turn);
    }
    SignalGameOver();
  }

  public void PlayOthelloWithHuman(int player_color, OthPlayer player)  {   
      
    machine_player = new Player(player_color, player);
    if (player_color == OthPlayer.BLACK)
    {
      timer.StartTimer(player_color);
      OthMove move = player.NextMove(oth_engine.GetBoardStatus());
      timer.StopTimer();
      PlayOneMove(player_color, move);
    }
    oth_board.PlayWithHuman(this);
  }

  private boolean IsGameOver()  {    return (!oth_engine.DetectMoreMove(OthPlayer.BLACK)
	    && !oth_engine.DetectMoreMove(OthPlayer.WHITE));
  }

  private void PlayOneMove(int player_color, OthMove move)  {    
    oth_engine.MakeMove(player_color, move);
    PrintOneMove(player_color, move);
    oth_board.DisableMove(move);
    oth_board.DisplayBoard();
  }

  //public void mouseEntered(MouseEvent e) {}  
  //public void mouseExited(MouseEvent e) {}
  //public void mousePressed(MouseEvent e) {}
  //public void mouseReleased(MouseEvent e) {}
  public void mouseClicked(MouseEvent e)
  {
    OthBoard.OthCell cell = (OthBoard.OthCell) e.getSource();
    OthMove move = new OthMove(cell.GetX(), cell.GetY());
    int machine_color = machine_player.player_color;
    int human_color = OthEngine.GetOpponentColor(machine_color);
    if (oth_engine.IsMoveLegal(human_color, move))
    {
      PlayOneMove(human_color, move);
      if (oth_engine.DetectMoreMove(machine_color))
      {
	      timer.StartTimer(machine_color);
	      move = machine_player.player.NextMove(oth_engine.GetBoardStatus());
	      timer.StopTimer();
	      PlayOneMove(machine_color, move);
      	while (!oth_engine.DetectMoreMove(human_color)&& !IsGameOver())
         {
	         PrintPass(human_color);
	         timer.StartTimer(machine_color);
	         move = machine_player.player.NextMove(oth_engine.GetBoardStatus());
	         timer.StopTimer();
	         PlayOneMove(machine_color, move);
	      }
      }
      else if (!IsGameOver())
      {
	      PrintPass(machine_color);
      }
      if (IsGameOver())
      {
	      oth_board.DisableBoard();
	      SignalGameOver();
      }
    }
  }

  Vector GetLegalMovesAdaptor(int player_color)  {    return oth_engine.GetLegalMoves(player_color);
  }

  public static Vector GetLegalMoves(int player_color)  {    return Singleton().GetLegalMovesAdaptor(player_color);
  }

  int[][] GetBoardStatusAdaptor()  {    return oth_engine.GetBoardStatus();
  }

  public static int[][] GetBoardStatus()   {    return Singleton().GetBoardStatusAdaptor();
  }

  String GetGameStatusAdaptor()  {    return new String("black: "+oth_engine.GetScore(OthPlayer.BLACK)+
		      ", white: "+oth_engine.GetScore(OthPlayer.WHITE));
  }

  static String GetGameStatus()  {    return Singleton().GetGameStatusAdaptor();
  }


  OthEngine GetEngineCopyAdaptor()  {    return oth_engine.GetEngineCopy();
  }

  public static OthEngine GetEngineCopy()  {    return Singleton().GetEngineCopyAdaptor();
  }

   
  public static void main(String[] argv) {
    OthPlayer black_player = new DumbPlayer(OthPlayer.BLACK);
    OthPlayer white_player = new DumbPlayer(OthPlayer.WHITE);
    argv = new String[1];
    argv[0]="-allmachine";
    if (argv.length==1 && argv[0].equals("-allmachine")) {
      OthManager.Singleton().PlayOthello(black_player, white_player);
    } else 
      if (argv.length==1 && argv[0].equals("-humansecond")) {
      OthManager.Singleton().PlayOthelloWithHuman(OthPlayer.BLACK,
                                                  black_player);
    } else 
     {
      OthManager.Singleton().PlayOthelloWithHuman(OthPlayer.WHITE,
                                                  white_player);
    }
  }
  
}
