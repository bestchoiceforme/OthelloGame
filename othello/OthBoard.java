package othello;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

class OthBoard{  /**
    * specify the number of chess board rows and columns.
    */
  public static final int SIZE=8;
  private static final int CELLSIZE=40;
  private JFrame oth_frame;
  private JLabel status_msg;
  private JLabel final_msg;
  private OthCell oth_cells[][];
  private GridBagConstraints location;
  private int board_status[][];
  private int old_board_status[][];

  private void CreateOthBoard(int board_status[][])  {    this.board_status = old_board_status = board_status;
    oth_frame = new JFrame("Othello in Java");
    oth_frame.setBackground(Color.lightGray);
    oth_frame.getContentPane().setLayout(new GridBagLayout());
    oth_frame.setLocation(300, 200);
    oth_frame.addWindowListener
      (new WindowAdapter()
      {
        public void windowClosing(WindowEvent e)
        {
          oth_frame.dispose();
          System.exit(0);
        }

      });
    location = new GridBagConstraints();
    location.anchor = GridBagConstraints.CENTER;
    location.weightx = 1.0;
    location.weighty = 1.0;
    location.fill = GridBagConstraints.BOTH;
    oth_cells = new OthCell[OthBoard.SIZE][OthBoard.SIZE];
    for (int i=0; i<OthBoard.SIZE; i++)
    {
      for (int j=0; j<OthBoard.SIZE; j++)
      {
        OthCell cell = new OthCell(i, j, this);
        cell.setEnabled(false);
        AddComponent(oth_frame, cell, i, j);
        oth_cells[i][j] = cell;
      }
    }

    status_msg = new JLabel();
    status_msg.setFont( new Font("Monospaced", Font.BOLD, 24) );
    location.gridwidth = OthBoard.SIZE;
    AddComponent(oth_frame, status_msg, OthBoard.SIZE, 0);
    final_msg = new JLabel();
    final_msg.setFont( new Font("Monospaced", Font.BOLD, 24) );
    location.gridwidth = OthBoard.SIZE;
    AddComponent(oth_frame, final_msg, OthBoard.SIZE+1, 0);

    JPanel control = new JPanel(new FlowLayout());    JButton exit_button = new JButton("Exit");
    exit_button.setFont( new Font("Monospaced", Font.BOLD, 24) );
    exit_button.addActionListener
    (new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
         System.exit(0);
      }
    });
    control.add(exit_button);
    location.gridwidth = OthBoard.SIZE;
    AddComponent(oth_frame, control, OthBoard.SIZE+2, 0);
    oth_frame.setSize(OthBoard.CELLSIZE*OthBoard.SIZE, OthBoard.CELLSIZE*(OthBoard.SIZE+3));
    oth_frame.setVisible(true);
    oth_frame.show();
  }

  void SetStatusMessage(String txt)  {    status_msg.setText(txt);
    status_msg.repaint();
  }

  void SetFinalMessage(String txt)  {    final_msg.setText(txt);
    final_msg.repaint();
  }


  private void AddComponent (JFrame jframe, Component comp, int row, int col)  {    location.gridx = col;  location.gridy = row;
    jframe.getContentPane().add(comp, location);
  }

  private void DrawRect (Component comp, Graphics g)  {    Image shadow;
    Graphics shadow_g;
    Rectangle box;
    box = comp.getBounds();
    shadow = comp.createImage(comp.getSize().width, comp.getSize().height);
    shadow_g = shadow.getGraphics();
    shadow_g.setColor(Color.blue);
    shadow_g.drawRect(0, 0, box.width-1, box.height-1);
    g.drawImage(shadow, 0, 0, comp);
  }

  private void DrawRectCircle (Component comp, Graphics g, Color color)  {    Image shadow;
    Graphics shadow_g;
    Rectangle box;
    box = comp.getBounds();
    shadow = comp.createImage(comp.getSize().width, comp.getSize().height);
    shadow_g = shadow.getGraphics();
    shadow_g.setColor(Color.blue);
    shadow_g.drawRect(0, 0, box.width-1, box.height-1);
    shadow_g.setColor(color);
    shadow_g.fillOval(2, 2, box.width-4, box.height-4);
    g.drawImage(shadow, 0, 0, comp);
  }

  private void PaintCell (OthCell cell, Graphics g)  {    switch (board_status[cell.GetX()][cell.GetY()])
    {
      case OthPlayer.EMPTY: DrawRect(cell, g);  break;
      case OthPlayer.BLACK: DrawRectCircle(cell, g, Color.black); break;
      case OthPlayer.WHITE: DrawRectCircle(cell, g, Color.white); break;
    }
  }

  OthBoard (int board_status[][])  {    
      CreateOthBoard(board_status);
  }

  void DisplayBoard ()  {    String game_msg = OthManager.GetGameStatus();
    SetStatusMessage(game_msg);
    board_status = OthManager.GetBoardStatus();
    for (int i=0; i<OthBoard.SIZE; i++)
    {
      for (int j=0; j<OthBoard.SIZE; j++)
      {
	      if (board_status[i][j] == old_board_status[i][j])
	         continue;
         oth_cells[i][j].repaint();
      }
    }
    old_board_status = board_status;
  }

  void PlayWithHuman(MouseListener human)
  {
    int i, j;
    for (i=0; i<OthBoard.SIZE; i++)
    {
      for (j=0; j<OthBoard.SIZE; j++)
      {
        oth_cells[i][j].setEnabled(true);
        oth_cells[i][j].addMouseListener(human);
      }
    }
    oth_cells[OthBoard.SIZE/2-1][OthBoard.SIZE/2-1].setEnabled(false);
    oth_cells[OthBoard.SIZE/2][OthBoard.SIZE/2].setEnabled(false);
    oth_cells[OthBoard.SIZE/2][OthBoard.SIZE/2-1].setEnabled(false);
    oth_cells[OthBoard.SIZE/2-1][OthBoard.SIZE/2].setEnabled(false);
    SetStatusMessage(OthManager.GetGameStatus());
  }

  void DisableMove (OthMove move)  {    oth_cells[move.x][move.y].setEnabled(false);
  }

  void DisableBoard ()  {    int i, j;
    for (i=0; i<OthBoard.SIZE; i++)
    {
      for (j=0; j<OthBoard.SIZE; j++)
      {
        oth_cells[i][j].setEnabled(false);
      }
    }
  }

  /**    * <tt>OthCell</tt> implements the single board cell of
    * the chess board.
    */
  static class OthCell extends Canvas
  {
    private OthBoard owner;
    private int x, y;

    public OthCell (int x, int y, OthBoard owner)    {      this.x = x;
      this.y = y;
      this.owner = owner;
    }

    public int GetX ()    {      return x;
    }

    public int GetY ()    {      return y;
    }

    public void paint (Graphics g)    {      //Image shadow;
      //Graphics shadow_g;
      //Rectangle box;
      super.paint(g);
      owner.PaintCell(this, g);
    }
  }
}

