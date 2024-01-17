package UISysetem;
import Game.GamePlat2;
import UISysetem.UI_my.*;

import javax.swing.*;
import java.awt.*;


//棋盘的显示使用建造者模式，选用不同的不同的棋盘，来实现棋盘交叉线放置棋子和棋盘中间放置棋子
public class GameMenu extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel; // 主面板，用于承载其他面板
    private JButton newGameButton, continueGameButton, aiGameButton, videoButton, exitButton, settingButton;
    private JLabel titleLabel;

    private SideBar sidebarPanel;//侧边栏
    private int[][] boardData; // 棋盘数据

    //用户信息：
    private String name;
    private int gameacc;
    private int loseacc;

    //游戏相关变量：
    private GamePlat2 gamePlat;

    public GameMenu(GamePlat2 gamePlat) {
        //游戏信息：
        this.gamePlat = gamePlat;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("棋类对战平台");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(cardLayout = new CardLayout());
//        mainPanel = new background("D:/Project/02.IDEA/Course_autumn_Java/GamePlat/GamePlat/image/1.png")
//        mainPanel = new background();
//        mainPanel.setLayout(new CardLayout());


        JPanel defaultPanel = new JPanel(new GridBagLayout());
        defaultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0); // 设置按钮间的间距

        titleLabel = new JLabel("欢迎您！ "+gamePlat.getUsername());
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        defaultPanel.add(titleLabel, gbc);

        newGameButton = createStyledButton("新游戏（玩家 vs.玩家）");
        continueGameButton = createStyledButton("继续游戏");
        aiGameButton = createStyledButton("五子棋AI对战");
        videoButton = createStyledButton("录像");
        settingButton = createStyledButton("设置");
        exitButton = createStyledButton("退出");

        defaultPanel.add(newGameButton, gbc);
        defaultPanel.add(continueGameButton, gbc);
        defaultPanel.add(aiGameButton, gbc);
        defaultPanel.add(videoButton, gbc);
        defaultPanel.add(settingButton, gbc);
        defaultPanel.add(exitButton, gbc);

        mainPanel.add(defaultPanel, "DefaultPanel");
        add(mainPanel);



        // 事件：
        newGameButton.addActionListener(e -> {
            NewGameUI newGamePanel = new NewGameUI(gamePlat, mainPanel, cardLayout);
            mainPanel.add(newGamePanel.getPanel(), "NewGamePanel");
            cardLayout.show(mainPanel, "NewGamePanel");
        });
        continueGameButton.addActionListener(e -> {
            ContinueGameUI continueGamePanel = new ContinueGameUI(gamePlat, mainPanel, cardLayout);
            JPanel panel = continueGamePanel.getPanel();
            if (panel != null) {
                mainPanel.add(panel, "ContinueGamePanel");
                cardLayout.show(mainPanel, "ContinueGamePanel");
            }
        });
        aiGameButton.addActionListener(e -> {
            AIGameUI aiGameUI = new AIGameUI(gamePlat, mainPanel, cardLayout);
            JPanel aiSelectionPanel = aiGameUI.getAISelectionPanel();
            mainPanel.add(aiSelectionPanel, "AISelectionPanel");
            cardLayout.show(mainPanel, "AISelectionPanel");
        });

        videoButton.addActionListener(e -> {
            videoUI vui = new videoUI(gamePlat, mainPanel, cardLayout);
            JPanel panel = vui.getPanel();
            if (panel != null) {
                mainPanel.add(panel, "ReplayPanel");
                cardLayout.show(mainPanel, "ReplayPanel");
            }
        });
        settingButton.addActionListener(e -> {
            SettingUI setui = new SettingUI(gamePlat, mainPanel, cardLayout);
            mainPanel.add(setui, "setPanel");
            cardLayout.show(mainPanel, "setPanel");
        });
        exitButton.addActionListener(e -> {
            System.exit(0);
        });
    }
    private JButton createStyledButton(String text) {//按钮央视
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
//        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.DARK_GRAY);
        button.setMargin(new Insets(5, 15, 5, 15));
        button.setPreferredSize(new Dimension(200, 40)); // 设置统一大小
        return button;
    }


}
