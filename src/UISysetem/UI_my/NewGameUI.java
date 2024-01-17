package UISysetem.UI_my;

import javax.swing.*;
import java.awt.*;
import Game.GamePlat2;

public class NewGameUI extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePlat2 gamePlat;

    private JPanel gameSelectionPanel;
    private SideBar sidebarPanel;
    private infoShowUI gameInfoPanel; // 游戏信息面板

    //功能键：
    public NewGameUI(GamePlat2 gamePlat, JPanel mainPanel, CardLayout cardLayout){
        this.gamePlat = gamePlat;
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        createGameSelectionPanel();
    }

    private void createGameSelectionPanel() {
        gameSelectionPanel = new JPanel(new GridLayout(3, 1)); // 网格布局

        JButton weiqiButton = new JButton("围棋");
        JButton gomokuButton = new JButton("五子棋");
        JButton othelloButton = new JButton("黑白棋");

        gameSelectionPanel.add(weiqiButton);
        gameSelectionPanel.add(gomokuButton);
        gameSelectionPanel.add(othelloButton);

        // 为黑白棋按钮添加事件监听器
        othelloButton.addActionListener(e -> {
            int boardSize = promptForBoardSize();  // 弹出对话框询问棋盘大小
            mainPanel.add(createOthelloGamePanel("黑白棋",boardSize), "OthelloGamePanel");
            cardLayout.show(mainPanel, "OthelloGamePanel");
        });
        gomokuButton.addActionListener(e -> {
            int boardSize = promptForBoardSize();  // 弹出对话框询问棋盘大小
            mainPanel.add(createOthelloGamePanel("五子棋",boardSize), "OthelloGamePanel");
            cardLayout.show(mainPanel, "OthelloGamePanel");
        });
        weiqiButton.addActionListener(e -> {
            int boardSize = promptForBoardSize();  // 弹出对话框询问棋盘大小
            mainPanel.add(createOthelloGamePanel("围棋",boardSize), "OthelloGamePanel");
            cardLayout.show(mainPanel, "OthelloGamePanel");
        });
    }
    private int promptForBoardSize() {
        String[] options = {"8x8", "16x16"};
        int response = JOptionPane.showOptionDialog(this, "请选择棋盘大小：(默认为8)",
                "棋盘大小选择", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        // 根据用户选择设置棋盘大小
        return response == 1 ? 16 : 8;
    }
    private JPanel createOthelloGamePanel(String type,int boardSize) {
        gamePlat.startgame(type,boardSize); // 启动游戏
        JPanel othelloGamePanel = new JPanel(new BorderLayout());
        ChessBoardPanel chessBoardPanel = new ChessBoardPanel(gamePlat.getBoard(), boardSize, this::handlePiecePlacement);

        gameInfoPanel = new infoShowUI(gamePlat);
        gameInfoPanel.updateInfo();
        SideBar sidebar = new SideBar(gamePlat, mainPanel, cardLayout,chessBoardPanel,gameInfoPanel);
        othelloGamePanel.add(chessBoardPanel, BorderLayout.CENTER);
        othelloGamePanel.add(sidebar, BorderLayout.WEST);
        othelloGamePanel.add(gameInfoPanel, BorderLayout.SOUTH);
        return othelloGamePanel;
    }
    public JPanel getPanel() {
        return gameSelectionPanel;
    }
    private void handlePiecePlacement(int x, int y) {//使用接口实现
        int flag = gamePlat.playgame(x,y);//1正常落子，-1非法落子，0棋局结束
        if(flag == 1){
            SwingUtilities.invokeLater(() -> {
                gameInfoPanel.updateInfo();
                mainPanel.revalidate();
                mainPanel.repaint();

            });
//            mainPanel.repaint(); // 重绘棋盘面板
        }else if(flag == -1){
            JOptionPane.showMessageDialog(this, "不合法的落子，请重新落子", "错误", JOptionPane.ERROR_MESSAGE);
        }else if(flag == 0){
            JOptionPane.showMessageDialog(this, "游戏结束！"+gamePlat.getWinner()+"胜利！", "游戏结束", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "DefaultPanel"); // 切换回主界面
        }
    }


    private void initializeComponents() {
        //棋盘和Bar

    }



}
