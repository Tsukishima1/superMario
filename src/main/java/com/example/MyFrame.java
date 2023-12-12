package com.example;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.*;

public class MyFrame extends JFrame implements KeyListener {
    // 用于存储所有背景
    private List<BackGround> allBg = new ArrayList<>();
    // 用于存储当前背景
    private BackGround nowBg = new BackGround();
    // 用于双缓存
    private Image offScreenImage = null;

    public MyFrame() throws IOException {
        // 设置窗口大小
        this.setSize(800, 600);
        // 设置窗口居中显示
        this.setLocationRelativeTo(null);
        // 设置窗口可见
        this.setVisible(true);
        // 设置窗口关闭方式
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口大小不可变
        this.setResizable(false);
        // 向窗口对象添加键盘监听器
        this.addKeyListener(this);
        // 设置窗口名称
        this.setTitle("超级马里奥！");
        // 初始化图片
        StaticValue.init();

        // 创建全部场景
        for (int i=1; i<=3;i++) {
            allBg.add(new BackGround(i, i==3?true:false));
        }
        // 将第一个场景设置为当前场景
        nowBg = allBg.get(2);
        // 绘制图像
        repaint();
    }

    public static void main(String[] args) throws IOException {
        new MyFrame();
    }

    @Override
    public void paint(Graphics g) {
        if(offScreenImage==null) {
            offScreenImage = createImage(800,600);
        } 
        Graphics graphics = offScreenImage.getGraphics();
        graphics.fillRect(0, 0, 800, 600);

        // 绘制背景
        graphics.drawImage(nowBg.getBgImage(), 0, 0, this);
        // 绘制障碍物
        for (Obstacle obstacle : nowBg.getObstacleList()) {
            graphics.drawImage(obstacle.getShow(), obstacle.getX(), obstacle.getY(), this);
        }
        // 绘制城堡
        graphics.drawImage(nowBg.getCastle(), 620, 270, this);
        // 绘制旗杆
        graphics.drawImage(nowBg.getFlagpole(), 500, 220, this);
        // 把图像绘制在窗口中
        g.drawImage(offScreenImage, 0, 0, this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}