package UISysetem.UI_my;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

//放置在空格上的棋盘  ，这是提供的第二种棋盘
public class ChessBoardPanel2 extends JPanel {
    private int[][] boardData; // 二维数组表示棋盘状态
    private int cellSize; // 每个格子的大小
    private int xOffset, yOffset; // X和Y方向的偏移量
    private int pieceRadius; // 棋子的半径

    private OnPiecePlacedListener piecePlacedListener;


    private int boardsize=8;

    public ChessBoardPanel2(int[][] boardData) {
        this.boardData = boardData;
    }


    // 添加一个用于处理点击事件的接口
    public interface OnPiecePlacedListener {
        void onPiecePlaced(int x, int y);
    }
    public ChessBoardPanel2(int[][] boardData, OnPiecePlacedListener listener) {
        this.boardData = boardData;
        this.piecePlacedListener = listener;
        // 添加鼠标事件监听器
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < boardsize; i++) {
                    for (int j = 0; j < boardsize; j++) {
                        int xCenter = xOffset + j * cellSize;
                        int yCenter = yOffset + i * cellSize;
                        double distance = Point2D.distance(e.getX(), e.getY(), xCenter, yCenter);
                        if (distance < pieceRadius) {
                            System.out.println("Clicked near intersection: (" + i + ", " + j + ")");
                            if (i >= 0 && i < boardsize && j >= 0 && j < boardsize) {
                                if (piecePlacedListener != null) {
                                    piecePlacedListener.onPiecePlaced(i, j);
                                }
                            }
                        }
                    }
                }
            }
        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawChessBoard(g);
    }

    private void drawChessBoard(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        cellSize = Math.min(width, height) / boardsize;
        xOffset = (width - cellSize * boardsize) / 2;
        yOffset = (height - cellSize * boardsize) / 2;
        pieceRadius = cellSize / 2;
        Graphics2D g2d = (Graphics2D) g;

        // 绘制棋盘格和线
        for (int i = 0; i <= boardsize; i++) {
            for (int j = 0; j <= boardsize; j++) {
                if (i < boardsize && j < boardsize) {
                    // 绘制棋盘格
                    g2d.setColor((i + j) % 2 == 0 ? new Color(128, 0, 128) : Color.LIGHT_GRAY);
                    g2d.fillRect(xOffset + j * cellSize, yOffset + i * cellSize, cellSize, cellSize);
                }
                // 绘制棋盘线
                g2d.setColor(Color.BLACK);
                g2d.drawLine(xOffset, yOffset + i * cellSize, xOffset + boardsize * cellSize, yOffset + i * cellSize); // 横线
                g2d.drawLine(xOffset + j * cellSize, yOffset, xOffset + j * cellSize, yOffset + boardsize * cellSize); // 竖线
            }
        }

        // 绘制棋子
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                if (boardData[i+1][j+1] == 1 || boardData[i+1][j+1] == -1) {
                    draw3DPiece(g2d, xOffset + j * cellSize, yOffset + i * cellSize, cellSize / 2, boardData[i+1][j+1]);
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
        frame.add(new ChessBoardPanel2(boardData));
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}



