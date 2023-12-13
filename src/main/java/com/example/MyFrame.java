package com.example;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.*;

public class MyFrame extends JFrame implements KeyListener, Runnable {
    // 用于存储所有背景
    private List<BackGround> allBg = new ArrayList<>();
    // 用于存储当前背景
    private BackGround nowBg = new BackGround();
    // 用于双缓存
    private Image offScreenImage = null;

    // 马里奥对象
    private Mario mario = new Mario();
    // 定义一个线程对象，实现马里奥的运动
    private Thread thread = new Thread(this);

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
        // 初始化马里奥
        mario = new Mario(10, 355);

        // 创建全部场景
        for (int i=1; i<=3;i++) {
            allBg.add(new BackGround(i, i==3?true:false));
        }
        // 将第一个场景设置为当前场景
        nowBg = allBg.get(0);
        mario.setBackGround(nowBg);
        // 绘制图像
        repaint();
        // 启动线程
        thread.start();
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
        // 绘制马里奥
        graphics.drawImage(mario.getShow(), mario.getX(), mario.getY(), this);
        // 把图像绘制在窗口中
        g.drawImage(offScreenImage, 0, 0, this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // 当键盘按下时触发
    @Override
    public void keyPressed(KeyEvent e) {
        // 向左移动
        if (e.getKeyCode() == 37) {
            mario.leftMove();
        }
        // 向右移动
        if (e.getKeyCode() == 39) {
            mario.rightMove();
        }
        // 跳跃
        if (e.getKeyCode() == 38) {
            mario.jump();
        }
    }

    // 当键盘松开时触发
    @Override
    public void keyReleased(KeyEvent e) {
        // 向左停止
        if (e.getKeyCode() == 37) {
            mario.leftStop();
        }
        // 向右停止
        if (e.getKeyCode() == 39) {
            mario.rightStop();
        }
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(50);
                // 判断是否移动到了最右边，进行场景切换
                if (mario.getX()>=775) {
                    nowBg = allBg.get(nowBg.getSort());
                    mario.setBackGround(nowBg);
                    // 重置坐标
                    mario.setX(10);
                }

                // 判断游戏是否结束
                if (mario.isOK()) {
                    JOptionPane.showMessageDialog(this, "恭喜你！通关成功！");
                    System.exit(0); // 退出程序
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}