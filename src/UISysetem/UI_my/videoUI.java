package UISysetem.UI_my;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Game.GamePlat2;

public class videoUI {
    private GamePlat2 gamePlat;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Timer replayTimer;
    private JButton pauseButton;

    private int len;
    private int i;

    public videoUI(GamePlat2 gamePlat, JPanel mainPanel, CardLayout cardLayout) {
        this.gamePlat = gamePlat;
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        i = 1;
        len=0;
    }
    public JPanel getPanel() {
        this.len = gamePlat.VideoGame();
        System.out.println("回放长度："+len);
        if(len == 0){
            JOptionPane.showMessageDialog(mainPanel, "没有存档，请开始新游戏", "错误", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        gamePlat.backByOneCheck(0);
        // 创建并设置回放面板
        JPanel replayPanel = new JPanel(new BorderLayout());
        ChessBoardPanel chessBoardPanel = new ChessBoardPanel(gamePlat.getBoard(), 8, null);
        replayPanel.add(chessBoardPanel, BorderLayout.CENTER);

        // 添加控制按钮
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton exitButton = new JButton("退出");
        pauseButton = new JButton("暂停");
        controlPanel.add(exitButton);
        controlPanel.add(pauseButton);
        replayPanel.add(controlPanel, BorderLayout.SOUTH);

        // 为按钮添加动作监听器
        exitButton.addActionListener(e -> {
            replayTimer.stop();
            cardLayout.show(mainPanel, "DefaultPanel");
        });
        pauseButton.addActionListener(this::pauseReplay);

        // 设置回放定时器
        replayTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateChessBoard(e, chessBoardPanel);
            }
        });
        replayTimer.start();

        return replayPanel;
    }
    private void updateChessBoard(ActionEvent e,ChessBoardPanel chessBoardPanel) {
        System.out.println("回放长度："+i);
        if (i>=len||!gamePlat.backByOneCheck(i)) {
            System.out.println("正常");
            len=0;
            i=1;
            JOptionPane.showMessageDialog(mainPanel, "回放结束！", "结束", JOptionPane.INFORMATION_MESSAGE);
            replayTimer.stop();
            cardLayout.show(mainPanel, "DefaultPanel");

            return;
        }

        chessBoardPanel.setChessBoardPanel(gamePlat.getBoard());
        chessBoardPanel.repaint();
        i++ ;
    }

    private void pauseReplay(ActionEvent e) {
        if (replayTimer.isRunning()) {
            replayTimer.stop();
            pauseButton.setText("继续");
        } else {
            replayTimer.start();
            pauseButton.setText("暂停");
        }
    }



}
