package UISysetem.UI_my;

import javax.swing.*;
import java.awt.*;
import Game.GamePlat2;

public class ContinueGameUI {
    private GamePlat2 gamePlat;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private infoShowUI gameInfoPanel; // 游戏信息面板


    public ContinueGameUI(GamePlat2 gamePlat, JPanel mainPanel, CardLayout cardLayout) {
        this.gamePlat = gamePlat;
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
    }

    public JPanel getPanel() {
        // 检查是否有存档可继续
        if (!gamePlat.continueGame()) {
            JOptionPane.showMessageDialog(mainPanel, "没有存档，请开始新游戏", "错误", JOptionPane.ERROR_MESSAGE);
            return null; // 或者返回一个默认的面板
        }
        // 创建并返回继续游戏的面板
        JPanel othelloGamePanel = new JPanel(new BorderLayout());
        ChessBoardPanel chessBoardPanel = new ChessBoardPanel(gamePlat.getBoard(), 8, this::handlePiecePlacement);
        gameInfoPanel = new infoShowUI(gamePlat);
        gameInfoPanel.updateInfo();

        othelloGamePanel.add(chessBoardPanel, BorderLayout.CENTER);

        gameInfoPanel = new infoShowUI(gamePlat);
        gameInfoPanel.updateInfo();
        SideBar sidebar = new SideBar(gamePlat, mainPanel, cardLayout,chessBoardPanel,gameInfoPanel);

        othelloGamePanel.add(gameInfoPanel, BorderLayout.SOUTH);
        othelloGamePanel.add(sidebar, BorderLayout.WEST);

        return othelloGamePanel;
    }

    private void handlePiecePlacement(int x, int y) {
        // 处理棋子放置的逻辑
        int flag = gamePlat.playgame(x,y);//1正常落子，-1非法落子，0棋局结束
        if(flag == 1){
            gameInfoPanel.updateInfo();
            mainPanel.revalidate();
            mainPanel.repaint(); // 重绘棋盘面板
        }else if(flag == -1){
            JOptionPane.showMessageDialog(mainPanel, "不合法的落子，请重新落子", "错误", JOptionPane.ERROR_MESSAGE);
        }else if(flag == 0){
            JOptionPane.showMessageDialog(mainPanel, "游戏结束！"+gamePlat.getWinner()+"胜利！", "游戏结束", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "DefaultPanel"); // 切换回主界面
        }
    }
}
