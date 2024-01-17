package UISysetem.UI_my;//package UISysetem.UI_my;
//
//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.io.File;
//import java.io.IOException;
//
//public class background extends JPanel {
//    private Image backgroundImage;
//
//    public background(String fileName) {
//        try {
//            backgroundImage = ImageIO.read(new File(fileName));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        // 绘制背景图
//        if (backgroundImage != null) {
//            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
//        }
//    }
//}

import javax.swing.*;
import java.awt.*;

public class background extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Color color1 = Color.BLUE; // 渐变的起始颜色
        Color color2 = Color.GREEN; // 渐变的结束颜色
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}
