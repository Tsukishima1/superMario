package com.example;

import java.awt.image.BufferedImage;

public class Mario implements Runnable {
    // 用于表示横纵坐标
    private int x;
    private int y;
    // 用于表示当前状态
    private String status;
    // 用于显示当前状态对应的图像
    private BufferedImage show = null;
    // 定义一个BackGround对象，用于获取障碍物的信息
    private BackGround backGround = new BackGround();
    // 用来实现马里奥的动作
    private Thread thread = null;

    // 马里奥移动速度
    private int xSpeed;
    // 马里奥跳跃速度
    private int ySpeed;
    // 定义一个索引
    private int index;
    // 表示马里奥上升的时间
    private int upTime;
    // 是否走到城堡门口
    private boolean isOK;

    public Mario() {

    }

    public Mario(int x, int y) {
        this.x = x;
        this.y = y;
        this.status = "stand--right";
        this.show = StaticValue.stand_R;
        thread = new Thread(this);
        thread.start();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BufferedImage getShow() {
        return show;
    }

    public void setShow(BufferedImage show) {
        this.show = show;
    }

    public void setBackGround(BackGround backGround) {
        this.backGround = backGround;
    }
    
    public boolean isOK() {
        return isOK;
    }

    @Override
    public void run() {
        while (true) {
            // 判断是否处于障碍物上
            boolean onObstacle = false;

            // 判断是否可以往左走
            boolean canLeft = true;
            // 判断是否可以往右走
            boolean canRight = true;

            // 判断是否到达旗杆位置
            if (backGround.isFlag() && this.x >= 500) {
                this.backGround.setReach(true);

                // 判断是否下落完成
                if (this.backGround.isBase()) {
                    status = "move--right";
                    if (x < 690) {
                        x += 5;
                    } else {
                        isOK = true;
                    }
                } else {
                    if (y < 395) {
                        this.xSpeed = 0;
                        this.y += 5;
                        status = "jump--right";
                    } else if (y > 395) {
                        this.y = 395;
                        status = "stop--right";
                    }
                }
            } else {
                // 遍历场景中的所有障碍物
                for (int i = 0; i < backGround.getObstacleList().size(); i++) {
                    Obstacle ob = backGround.getObstacleList().get(i);
                    // 判断是否位于障碍物上
                    if (ob.getY() == this.y + 25 && (ob.getX() > this.x - 30 && ob.getX() < this.x + 25)) {
                        onObstacle = true;
                    }
                    // 判断是否跳起来顶到砖块
                    if (ob.getY() >= this.y - 30 && ob.getY() <= this.y - 20
                            && (ob.getX() > this.x - 30 && ob.getX() < this.x + 25)) {
                        if (ob.getType() == 0) {
                            // 撞碎砖块
                            backGround.getObstacleList().remove(ob);
                        } else {
                            // 跳起来顶到砖块
                            upTime = 0;
                        }
                    }

                    // 判断是否可以往右走
                    if (ob.getX() == this.x + 25 && (ob.getY() > this.y - 30 && ob.getY() < this.y + 25)) {
                        canRight = false;
                    }
                    // 判断是否可以往左走
                    if (ob.getX() == this.x - 30 && (ob.getY() > this.y - 30 && ob.getY() < this.y + 25)) {
                        canLeft = false;
                    }

                }

                // 进行跳跃的操作
                if (onObstacle && upTime == 0) {
                    if (status.indexOf("left") != -1) {
                        if (xSpeed != 0) {
                            status = "move--left";
                        } else {
                            status = "stop--left";
                        }
                    } else {
                        if (xSpeed != 0) {
                            status = "move--right";
                        } else {
                            status = "stop--right";
                        }
                    }
                } else {
                    if (upTime != 0) {
                        upTime--;
                    } else {
                        down();
                    }
                    y += ySpeed;
                }

            }
            if ((canLeft && xSpeed < 0) || (canRight && xSpeed > 0)) {
                x += xSpeed;
                // 判断是否运动到了最左边
                if (x <= 0) {
                    x = 0;
                }
            }
            // 判断是否处于移动状态
            if (status.contains("move")) {
                index = index == 0 ? 1 : 0;
            }
            // 判断是否向左移动
            if ("move--left".equals(status)) {
                show = StaticValue.run_L.get(index);
            }
            // 判断是否向右移动
            if ("move--right".equals(status)) {
                show = StaticValue.run_R.get(index);
            }
            // 判断是否向左停止
            if ("stop--left".equals(status)) {
                show = StaticValue.stand_L;
            }
            // 判断是否向右停止
            if ("stop--right".equals(status)) {
                show = StaticValue.stand_R;
            }
            // 判断是否向左跳跃
            if ("jump--left".equals(status)) {
                show = StaticValue.jump_L;
            }
            // 判断是否向右跳跃
            if ("jump--right".equals(status)) {
                show = StaticValue.jump_R;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    // 马里奥向左移动
    public void leftMove() {
        // 改变速度
        xSpeed = -5;
        // 判断是否碰到旗子
        if (backGround.isReach()) {
            xSpeed=0;
        }

        // 判断是否处于空中
        if (status.indexOf("jump") != -1) {
            status = "jump--left";
        } else {
            status = "move--left";
        }
    }

    // 马里奥向右移动
    public void rightMove() {
        // 改变速度
        xSpeed = 5;
        // 判断是否碰到旗子
        if (backGround.isReach()) {
            xSpeed=0;
        }

        // 判断是否处于空中
        if (status.indexOf("jump") != -1) {
            status = "jump--right";
        } else {
            status = "move--right";
        }
    }

    // 马里奥向左停止
    public void leftStop() {
        // 改变速度
        xSpeed = 0;
        // 判断是否处于空中
        if (status.indexOf("jump") != -1) {
            status = "jump--left";
        } else {
            status = "stop--left";
        }
    }

    // 马里奥向右停止
    public void rightStop() {
        // 改变速度
        xSpeed = 0;
        // 判断是否处于空中
        if (status.indexOf("jump") != -1) {
            status = "jump--right";
        } else {
            status = "stop--right";
        }
    }

    // 马里奥跳跃
    public void jump() {
        if (status.indexOf("jump") == -1) {
            // 判断方向
            if (status.indexOf("left") != -1) {
                status = "jump--left";
            } else {
                status = "jump--right";
            }
            // 改变速度
            ySpeed = -10;
            // 改变时间
            upTime = 7;
        }
        // 判断是否碰到旗子
        if (backGround.isReach()) {
            ySpeed=0;
        }
    }

    // 马里奥下落
    public void down() {
        // 判断方向
        if (status.indexOf("left") != -1) {
            status = "jump--left";
        } else {
            status = "jump--right";
        }
        // 改变速度
        ySpeed = 10;
    }
}