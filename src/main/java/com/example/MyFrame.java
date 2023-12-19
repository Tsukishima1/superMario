package com.example;

import javax.swing.*;

import javazoom.jl.decoder.JavaLayerException;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.*;

import java.awt.Component;

public class MyFrame extends JFrame implements KeyListener, Runnable {
    // 用于存储所有背景
    List<BackGround> allBg = new ArrayList<>();
    // 用于存储当前背景
    BackGround nowBg = new BackGround();

    public void setBackGround(BackGround nowBg) {
        this.nowBg = nowBg;
    }

    // 用于双缓存
    private Image offScreenImage = null;

    // 马里奥对象
    Mario mario = new Mario();
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

        // 播放音乐
        try {
            new Music();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public MyFrame(Mario mario2) {
        this.mario = mario2;
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
        // 绘制敌人 
        for (Enemy enemy : nowBg.getEnemyList()) {
            graphics.drawImage(enemy.getShow(), enemy.getX(), enemy.getY(), this);
        }
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
        // 添加分数
        Color c = graphics.getColor();
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("黑体", Font.BOLD, 25));
        graphics.drawString("当前你获得的分数为：" + mario.getScore(), 270, 100);
        graphics.setColor(c);
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
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            mario.leftMove();
        }
        // 向右移动
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            mario.rightMove();
        }
        // 跳跃
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            mario.jump();
        }
    }

    // 当键盘松开时触发
    @Override
    public void keyReleased(KeyEvent e) {
        // 向左停止
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            mario.leftStop();
        }
        // 向右停止
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
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

                // 判断马里奥是否死亡 
                if (mario.isDeath()) {
                    showMessageDialog(this, "很遗憾！你似了！");
                    exit(0); // 退出程序
                }

                // 判断游戏是否结束
                if (mario.isOK()) {
                    showMessageDialog(this, "恭喜你！通关成功！");
                    exit(0); // 退出程序
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getMario() {
        return mario;
    }

    public void showMessageDialog(Component parentComponent, Object message) {
        JOptionPane.showMessageDialog(parentComponent, message);
    }
    public void exit(int status) {
        System.exit(status);
    }
}