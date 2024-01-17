package UISysetem.UI_my;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import Game.GamePlat2;
public class AIGameUI {
    private GamePlat2 gamePlat;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private JPanel aiSelectionPanel;
    private infoShowUI gameInfoPanel;

    private boolean aiVSai;

    private int ai1Level = 0;
    private int ai2Level = 0;

    public AIGameUI(GamePlat2 gamePlat, JPanel mainPanel, CardLayout cardLayout) {
        this.gamePlat = gamePlat;
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        aiVSai=false;
    }

    public JPanel getAISelectionPanel() {
        aiSelectionPanel = new JPanel(new GridLayout(2, 1));
        JButton aiVsAiButton = new JButton("AI vs. AI");
        JButton playerVsAiButton = new JButton("玩家 vs. AI");

        aiSelectionPanel.add(aiVsAiButton);
        aiSelectionPanel.add(playerVsAiButton);

        aiVsAiButton.addActionListener(e -> {
            mainPanel.add(showAIGamePanel("AI vs. AI"), "AIvsAIPanel");
            cardLayout.show(mainPanel, "AIvsAIPanel");
        });
        playerVsAiButton.addActionListener(e -> {
            mainPanel.add(showAIGamePanel("玩家 vs. AI"), "AIvsAIPanel");
            cardLayout.show(mainPanel, "AIvsAIPanel");
        });

        return aiSelectionPanel;
    }
    private JPanel showAIGamePanel(String type) {
        JPanel gamePanel;
        if (type.equals("AI vs. AI")) {
            gamePanel = createAIGamePanelForAIVsAI();
        } else {
            gamePanel = createAIGamePanelForPlayerVsAI();
        }
        return gamePanel;
    }
    private JPanel createAIGamePanelForAIVsAI() {
        JPanel aiVsAiPanel = new JPanel();
        aiVsAiPanel.setLayout(new BoxLayout(aiVsAiPanel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("选择AI等级");
        JComboBox<Integer> ai1LevelSelector = new JComboBox<>(new Integer[]{1, 2, 3}); // 假设AI有3个等级
        JComboBox<Integer> ai2LevelSelector = new JComboBox<>(new Integer[]{1, 2, 3});

        JButton startGameButton = new JButton("开始游戏");

        aiVsAiPanel.add(label);
        aiVsAiPanel.add(new JLabel("AI 1 等级:"));
        aiVsAiPanel.add(ai1LevelSelector);
        aiVsAiPanel.add(new JLabel("AI 2 等级:"));
        aiVsAiPanel.add(ai2LevelSelector);
        aiVsAiPanel.add(startGameButton);

        startGameButton.addActionListener(e -> {
            ai1Level = (Integer) ai1LevelSelector.getSelectedItem();
            ai2Level = (Integer) ai2LevelSelector.getSelectedItem();

            if (ai1Level != 0 && ai2Level != 0) {
                aiVSai=true;
                startGameWithAI("AI vs. AI", ai1Level, ai2Level);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "请为两个AI选择等级", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });

        return aiVsAiPanel;
    }

    private JPanel createAIGamePanelForPlayerVsAI() {
        JPanel playerVsAiPanel = new JPanel(new GridLayout(1, 2));
        JButton level1Button = new JButton("一级 AI");
        JButton level2Button = new JButton("二级 AI");

        playerVsAiPanel.add(level1Button);
        playerVsAiPanel.add(level2Button);

        level1Button.addActionListener(e -> startGameWithAI("玩家 vs. AI", 1,1));
        level2Button.addActionListener(e -> startGameWithAI("玩家 vs. AI", 2,2));

        return playerVsAiPanel;
    }

    private Timer aiGameTimer;
    private void startGameWithAI(String type, int ai1Level, int ai2Level) {
        if (type.equals("玩家 vs. AI")) {
            // 启动玩家 vs AI游戏
            gamePlat.startGameForAI(ai1Level);
            gamePlat.setAiMode(1);
            // 创建并显示棋局面板
            showGameBoardPanel();
        } else if (type.equals("AI vs. AI")) {
            if (ai1Level != 0 && ai2Level != 0) {
                gamePlat.startGameForAI(ai1Level, ai2Level);
                gamePlat.setAiMode(2);
                startAIGameTimer();
                showGameBoardPanel();

            } else {
                JOptionPane.showMessageDialog(mainPanel, "请选择两个AI的等级", "提示", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    private void startAIGameTimer() {
        if (aiGameTimer != null) {
            aiGameTimer.stop();
        }
        aiGameTimer = new Timer(1000, e -> {
            try {
                int flag = gamePlat.playgameForAI(); // 1正常落子，-1非法落子，0棋局结束
                if (flag == 1) {
                    gameInfoPanel.updateInfo();
                    gameInfoPanel.repaint();
                    mainPanel.repaint();
                } else if (flag == -1) {
                    JOptionPane.showMessageDialog(mainPanel, "不合法的落子，请重新落子", "错误", JOptionPane.ERROR_MESSAGE);
                } else if (flag == 0) {
                    JOptionPane.showMessageDialog(mainPanel, "游戏结束！"+gamePlat.getWinner()+"胜利！", "游戏结束", JOptionPane.INFORMATION_MESSAGE);
                    aiGameTimer.stop(); // 停止计时器
                    gamePlat.setAiMode(0);
                    cardLayout.show(mainPanel, "DefaultPanel");
                }
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        aiGameTimer.start();
    }


    private void showGameBoardPanel() {
        JPanel othelloGamePanel = new JPanel(new BorderLayout());
        ChessBoardPanel chessBoardPanel = new ChessBoardPanel(gamePlat.getBoard(), 8,this::handlePiecePlacement);

        gameInfoPanel = new infoShowUI(gamePlat);
        gameInfoPanel.updateInfo();
        if(aiVSai){
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton exitButton = new JButton("退出");
            // 添加控制按钮
            controlPanel.add(exitButton);
            othelloGamePanel.add(controlPanel, BorderLayout.WEST);
            exitButton.addActionListener(e -> {
                aiGameTimer.stop(); // 停止计时器
                gamePlat.setAiMode(0);
                cardLayout.show(mainPanel, "DefaultPanel");
            });
        }else {
            SideBar sidebar = new SideBar(gamePlat, mainPanel, cardLayout,chessBoardPanel,gameInfoPanel);
            othelloGamePanel.add(sidebar, BorderLayout.WEST);
        }


        othelloGamePanel.add(chessBoardPanel, BorderLayout.CENTER);

        othelloGamePanel.add(gameInfoPanel, BorderLayout.SOUTH);
        mainPanel.add(othelloGamePanel, "AIIGamePanel");
        cardLayout.show(mainPanel, "AIIGamePanel");
    }


    private void handlePiecePlacement(int x, int y) {
        if (aiVSai){
            gameInfoPanel.repaint();
            mainPanel.repaint();
            return;
        }
        int flag = gamePlat.playgameForAI(x, y); // 1正常落子，-1非法落子，0棋局结束
        if (flag == 1) {
            gameInfoPanel.updateInfo();
            gameInfoPanel.repaint();
            mainPanel.repaint();
        } else if (flag == -1) {
            JOptionPane.showMessageDialog(mainPanel, "不合法的落子，请重新落子", "错误", JOptionPane.ERROR_MESSAGE);
        } else if (flag == 0) {
            JOptionPane.showMessageDialog(mainPanel, "游戏结束！"+gamePlat.getWinner()+"胜利！", "游戏结束", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "DefaultPanel");
        }
    }

}