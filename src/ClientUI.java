import Game.GamePlat2;
import UISysetem.GameMenu;
import UISysetem.UI_my.background;

import javax.swing.*;
import java.awt.*;

public class ClientUI extends JFrame {
    private JButton loginButton, registerButton, touristButton;
    private JLabel titleLabel;
    private JPanel cardsPanel; // 卡片布局容器
    private CardLayout cardLayout;

    //后端验证：GamePlat，将其传递给下一个界面。
    //密码需为6-16位，且包含大小写

    private GamePlat2 platform;

    public ClientUI() {
        this.platform = new GamePlat2();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("棋类对战平台");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        JPanel mainPanel = new background();
        mainPanel.setLayout(new CardLayout());



        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        titleLabel = new JLabel("棋类对战平台", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton = new JButton("登录");
        registerButton = new JButton("注册");
        touristButton = new JButton("游客");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        touristButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(loginButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(registerButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(touristButton);
        mainPanel.add(Box.createVerticalGlue());

        // 创建登录界面面板
        JPanel loginPanel = createLoginFormPanel("LoginCard");

        // 创建注册界面面板
        JPanel registerPanel = createResFormPanel("RegisterCard");

        // 添加面板到卡片布局容器
        cardsPanel.add(mainPanel, "MainCard");
        cardsPanel.add(loginPanel, "LoginCard");
        cardsPanel.add(registerPanel, "RegisterCard");

        add(cardsPanel);

        // 为按钮添加事件监听器
        loginButton.addActionListener(e -> cardLayout.show(cardsPanel, "LoginCard"));
        registerButton.addActionListener(e -> cardLayout.show(cardsPanel, "RegisterCard"));
        touristButton.addActionListener(e -> {
            String name = "游客";
            String password = "Aa1";
            if (platform.Login(name,password)) {
                GameMenu gameMenu = new GameMenu(platform);
                JOptionPane.showMessageDialog(this, "欢迎您！"+name+"!", "欢迎！", JOptionPane.ERROR_MESSAGE);
                gameMenu.setVisible(true);
                this.dispose(); // 关闭当前（登录）窗口
            }
        });


    }

    private JPanel createLoginFormPanel(String cardName) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // 设置边距

        // 使用 GridBagLayout 来更灵活地控制组件位置
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 设置组件之间的间距

        JLabel userLabel = new JLabel("用户名:");
        JTextField userTextField = new JTextField(15); // 设置合适的长度
        JLabel pwdLabel = new JLabel("密码 (6-16位):");
        JPasswordField pwdField = new JPasswordField(15);



        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldsPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        fieldsPanel.add(userTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldsPanel.add(pwdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        fieldsPanel.add(pwdField, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("提交");
        JButton cancelButton = new JButton("取消");

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        // 添加面板到主面板
        panel.add(Box.createVerticalGlue());
        panel.add(fieldsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());

        submitButton.addActionListener(e -> {
            // 处理登录/注册逻辑
            String name = userTextField.getText();
            String password = new String(pwdField.getPassword());
            if (platform.Login(name,password)) {
                GameMenu gameMenu = new GameMenu(platform);
                gameMenu.setVisible(true);
                this.dispose(); // 关闭当前（登录）窗口
            } else {
                // 验证失败
                JOptionPane.showMessageDialog(this, "密码或用户名错误，请重新输入", "验证失败", JOptionPane.ERROR_MESSAGE);
                // 清空表单
                userTextField.setText("");
                pwdField.setText("");
            }
        });
        cancelButton.addActionListener(e -> cardLayout.show(cardsPanel, "MainCard"));
        return panel;
    }



    private JPanel createResFormPanel(String cardName) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 用户名和密码面板
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel userLabel = new JLabel("用户名(需唯一，不可已经注册):");
        JTextField userTextField = new JTextField();
        JLabel pwdLabel = new JLabel("密码(6-16,需包含大小写字母和数字):");
        JPasswordField pwdField = new JPasswordField();

        fieldsPanel.add(userLabel);
        fieldsPanel.add(userTextField);
        fieldsPanel.add(pwdLabel);
        fieldsPanel.add(pwdField);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("提交");
        JButton cancelButton = new JButton("取消");

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        // 添加面板到主面板
        panel.add(Box.createVerticalGlue());
        panel.add(fieldsPanel);
        panel.add(buttonPanel);
        panel.add(Box.createVerticalGlue());

        submitButton.addActionListener(e -> {
            // 处理登录/注册逻辑
            String name = userTextField.getText();
            String password = new String(pwdField.getPassword());
            if (platform.register(name,password)) {
                GameMenu gameMenu = new GameMenu(platform);
                JOptionPane.showMessageDialog(this, "欢迎您！"+name+"!", "欢迎！", JOptionPane.ERROR_MESSAGE);
                gameMenu.setVisible(true);
                this.dispose(); // 关闭当前（登录）窗口
            } else {
                JOptionPane.showMessageDialog(this, "用户名已注册或密码不合法，请重新输入", "验证失败", JOptionPane.ERROR_MESSAGE);
                userTextField.setText("");
                pwdField.setText("");
            }
        });
        cancelButton.addActionListener(e -> cardLayout.show(cardsPanel, "MainCard"));
        return panel;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientUI().setVisible(true));
    }
}
