package UISysetem.UI_my;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;


//放置在空格上的棋盘
public class ChessBoardPanel extends JPanel {
    private int[][] boardData; // 二维数组表示棋盘状态
    private int cellSize; // 每个格子的大小
    private int xOffset, yOffset; // X和Y方向的偏移量
    private int boardsize;

    //
    private OnPiecePlacedListener piecePlacedListener;


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawChessBoard(g);
    }
    public void setChessBoardPanel(int[][] boardData) {
        this.boardData = boardData;
    }


    // 添加一个用于处理点击事件的接口
    public interface OnPiecePlacedListener {
        void onPiecePlaced(int x, int y);
    }

    public ChessBoardPanel(int[][] boardData,int board_size, OnPiecePlacedListener listener) {
        this.boardData = boardData;
        this.piecePlacedListener = listener;
        this.boardsize = board_size;
        System.out.println("B"+boardsize);
        // 添加鼠标事件监听器
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int y = (e.getX() - xOffset) / cellSize;
                int x = (e.getY() - yOffset) / cellSize;
                if (x >= 0 && x < boardsize && y >= 0 && y < boardsize) {
                    System.out.println("Clicked cell: (" + (x+1) + ", " + (y+1) + ")");
                    if (piecePlacedListener != null) {
                        piecePlacedListener.onPiecePlaced(x+1, y+1);
                    }
                }
            }
        });
    }


    private void drawChessBoard(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        cellSize = Math.min(width, height) / boardsize; // 计算每个格子的大小，确保棋盘是正方形
        xOffset = (width - cellSize * boardsize) / 2;// 计算X方向的偏移量
        yOffset = (height - cellSize * boardsize) / 2;// 计算Y方向的偏移量
        Graphics2D g2d = (Graphics2D) g;

        System.out.println(boardData[1][1]);
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                // 绘制棋盘格
                g2d.setColor((i + j) % 2 == 0 ? new Color(128, 0, 128) : Color.LIGHT_GRAY);
                g2d.fillRect(xOffset + j * cellSize, yOffset + i * cellSize, cellSize, cellSize);

                // 绘制棋子
                if (boardData[i+1][j+1] == 1 || boardData[i+1][j+1] == -1) {
                    System.out.println("我在绘制其长子");
                    draw3DPiece(g2d, xOffset + j * cellSize + cellSize / 2, yOffset + i * cellSize + cellSize / 2,
                            cellSize / 2, boardData[i+1][j+1]);
                }
            }
        }
    }

    private void draw3DPiece(Graphics2D g2d, int x, int y, int radius, int pieceType) {
        // 创建一个径向渐变，模拟光源效果
        float[] dist = {0.0f, 1.0f};
        Color[] colors = pieceType == 1 ? new Color[] {new Color(32, 32, 32), Color.BLACK}
                : new Color[] {new Color(192, 192, 192), Color.WHITE};
        RadialGradientPaint p = new RadialGradientPaint(new Point2D.Double(x, y), radius, dist, colors);
        g2d.setPaint(p);

        // 绘制一个圆形的棋子
        Ellipse2D circle = new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2);
        g2d.fill(circle);
    }

    public static void main(String[] args) {
        int[][] boardData = new int[10][10];
        boardData[3][3] = 1;
        boardData[4][4] = 1;
        boardData[3][4] = -1;
        boardData[7][7] = -1;

        JFrame frame = new JFrame("Chess Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(new ChessBoardPanel(boardData));
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}



