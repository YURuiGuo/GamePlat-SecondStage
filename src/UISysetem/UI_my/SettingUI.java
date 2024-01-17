package UISysetem.UI_my;

import Game.GamePlat2;

import javax.swing.*;
import java.awt.*;


public class SettingUI extends JPanel {
    private GamePlat2 gamePlat;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private JTextField textNewUsername;
    private JPasswordField textOldPassword;
    private JPasswordField textNewPassword;

    public SettingUI(GamePlat2 gamePlat, JPanel mainPanel, CardLayout cardLayout) {
        this.gamePlat = gamePlat;
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;

        setLayout(new GridLayout(5, 2, 10, 10)); // 5行2列的布局

        // 添加新用户名、旧密码、新密码字段
        add(new JLabel("新用户名:"));
        textNewUsername = new JTextField();
        add(textNewUsername);

        add(new JLabel("旧密码:"));
        textOldPassword = new JPasswordField();
        add(textOldPassword);

        add(new JLabel("新密码:"));
        textNewPassword = new JPasswordField();
        add(textNewPassword);

        // 添加提交按钮
        JButton submitButton = new JButton("提交");
        submitButton.addActionListener(e -> handleSubmit());
        add(submitButton);

        // 添加取消按钮
        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> handleCancel());
        add(cancelButton);
    }

    private void handleSubmit() {
        String newUsername = textNewUsername.getText();
        String oldPassword = new String(textOldPassword.getPassword());
        String newPassword = new String(textNewPassword.getPassword());
        if (gamePlat.updateAcc(newUsername, oldPassword, newPassword)) {
            // 提交成功的逻辑
            JOptionPane.showMessageDialog(this, "修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // 清除表单并提示错误
            clearForm();
            JOptionPane.showMessageDialog(this, "错误，重新输入", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        textNewUsername.setText("");
        textOldPassword.setText("");
        textNewPassword.setText("");
    }

    private void handleCancel() {
        // 取消操作，返回主菜单
        cardLayout.show(mainPanel, "DefaultPanel"); // 假设主菜单的面板键为 "MainMenuPanel"
    }
}