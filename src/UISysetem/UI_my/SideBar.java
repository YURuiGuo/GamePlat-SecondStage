package UISysetem.UI_my;

//import UI.GamePlat;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import Game.GamePlat2;

//该类是其中的一个部分，即侧边栏
//不同的游戏类型，有不同的侧边栏
public class SideBar extends JPanel {
    private JLabel currentTypeLabel; // 当前回合标签
    private JButton undoButton, saveButton, exitButton, restartButton, surrButton,  hidenButton, reoverButton, videoButton, passButton; // 功能键

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private ChessBoardPanel chessBoardPanel;

    private infoShowUI infoShow;

    private GamePlat2 gamePlat;

    private boolean flag;//是否开启bar

    public SideBar(GamePlat2 gamePlat, JPanel mainPanel, CardLayout cardLayout, ChessBoardPanel chessBoardPanel, infoShowUI ins) {
        this.gamePlat = gamePlat;
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.infoShow = ins;
        this.flag = true;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeComponents();
        this.chessBoardPanel = chessBoardPanel;
    }

    private void initializeComponents() {
        currentTypeLabel = new JLabel("当前类型: ");
        restartButton = new JButton("重新开始");
        surrButton = new JButton("认输");
        hidenButton = new JButton("隐藏/显示控制栏");
        reoverButton = new JButton("请求结束");

        videoButton = new JButton("保存录像");

        undoButton = new JButton("悔棋");
        saveButton = new JButton("存档");
        exitButton = new JButton("退出");

        add(currentTypeLabel);
        add(restartButton);
        add(surrButton);
        add(hidenButton);
        add(reoverButton);
        add(undoButton);
        add(saveButton);
        add(exitButton);
        add(videoButton);
        currentTypeLabel.setText("当前类型："+gamePlat.getGametype());

        restartButton.addActionListener(e -> {
            restartGame();
        });
        surrButton.addActionListener(e -> {
            surrGame();
        });
        hidenButton.addActionListener(e -> hidenGame());
        reoverButton.addActionListener(e -> {
            reoverGame();
        });
        undoButton.addActionListener(e -> {
            undoGame();
        });
        saveButton.addActionListener(e -> {
            saveGame();
        });
        exitButton.addActionListener(e -> {
            exitGame();
        });
        videoButton.addActionListener(e -> {
            videoGame();
        });


        //额外功能：
        if(Objects.equals(gamePlat.getGametype(), "围棋")){
            passButton = new JButton("跳过");
            add(passButton);
            passButton.addActionListener(e -> {
                passGame();
            });

        }


    }



    //功能实现：
    private void restartGame() {
        System.out.println("点击了重新开始");
        gamePlat.BarControll("R");
        Container parent = this.getParent();
        if (parent != null) {
            parent.revalidate();
            parent.repaint();
        }
    }
    private void surrGame()  {
        System.out.println("点击了认输");
        gamePlat.BarControll("S");
        JOptionPane.showMessageDialog(this, "游戏结束！"+gamePlat.getWinner()+"胜利！", "游戏结束", JOptionPane.INFORMATION_MESSAGE);
        cardLayout.show(mainPanel, "DefaultPanel");
        Container parent = this.getParent();
        if (parent != null) {
            parent.remove(this); // 移除侧边栏
            parent.revalidate();
            parent.repaint();
        }
    }
    private void hidenGame(){
        System.out.println("点击了隐藏");
        setVisible(!isVisible());
        revalidate();
        repaint();
    }
    private void reoverGame()  {
        System.out.println("点击了请求结束");
        gamePlat.BarControll("D");
        JOptionPane.showMessageDialog(this, "是否结束！", "请求结束", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(this, "游戏结束！"+gamePlat.getWinner()+"胜利！", "游戏结束", JOptionPane.INFORMATION_MESSAGE);
        cardLayout.show(mainPanel, "DefaultPanel");
        Container parent = this.getParent();
        if (parent != null) {
            parent.remove(this); // 移除侧边栏
            parent.revalidate();
            parent.repaint();
        }
    }


    private void undoGame() {
        System.out.println("点击了悔棋");
        if(gamePlat.BarControll("T")){
            chessBoardPanel.setChessBoardPanel(gamePlat.getBoard());
            chessBoardPanel.revalidate();
            chessBoardPanel.repaint();
            infoShow.updateInfo();
            infoShow.revalidate();
            infoShow.repaint();
        }
    }
    private void saveGame(){
        System.out.println("点击了存档");
        gamePlat.BarControll("A");
        cardLayout.show(mainPanel, "DefaultPanel");
        Container parent = this.getParent();
        if (parent != null) {
            parent.remove(this); // 移除侧边栏
            parent.revalidate();
            parent.repaint();
        }
    }
    private void exitGame() {
        System.out.println("点击了退出");
        gamePlat.BarControll("R");
        cardLayout.show(mainPanel, "DefaultPanel");
        Container parent = this.getParent();
        if (parent != null) {
            parent.remove(this); // 移除侧边栏
            parent.revalidate();
            parent.repaint();
        }
    }
    private void videoGame() {
        System.out.println("点击了录像");
        gamePlat.BarControll("V");
    }
    private void passGame() {
        System.out.println("点击了跳过");
        gamePlat.BarControll("P");
        infoShow.updateInfo();
        infoShow.revalidate();
        infoShow.repaint();
    }


}