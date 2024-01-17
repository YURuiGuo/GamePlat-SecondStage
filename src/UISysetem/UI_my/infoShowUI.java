package UISysetem.UI_my;

import Game.GamePlat2;

import javax.swing.*;

public class infoShowUI extends JPanel {
    private GamePlat2 gamePlat;
    private JLabel currentPlayerLabel; // 当前执棋手标签
    private JLabel currentRoundLabel; // 当前回合标签
    private JLabel user1Label; // 用户1的用户名标签
    private JLabel user2Label; // 用户2的用户名标签
    private JLabel perfo;
    public infoShowUI(GamePlat2 gamePlat) {
        this.gamePlat = gamePlat;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeComponents();
    }

    private void initializeComponents() {
        currentPlayerLabel = new JLabel("执棋手: ");
        currentRoundLabel = new JLabel("当前回合: ");
        user1Label = new JLabel("用户1: ");
        user2Label = new JLabel("用户2: ");
        perfo = new JLabel("战绩：");

        add(currentPlayerLabel);
        add(currentRoundLabel);
        add(user1Label);
        add(user2Label);
        add(perfo);
    }

    public void updateInfo() {
        currentPlayerLabel.setText("执棋手: " + gamePlat.getPlayer());
        currentRoundLabel.setText("当前回合: " + gamePlat.getStep());
        user1Label.setText("用户1: " + gamePlat.getUsername());
        user2Label.setText("用户2: "+gamePlat.getOtherPlayer());
        perfo.setText("战绩："+gamePlat.getUsername()+"的胜率为"+gamePlat.getPerformance()*100+"%");

    }




}
