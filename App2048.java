package cn.itcast.day02.jframe;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class MainFrame extends JFrame implements KeyListener, ActionListener {
    int data[][] = new int[4][4];

    int loseFlag = 1;
    int score = 0;

    // 提升为成员变量，创建JMenuItem
    JMenuItem item1 = new JMenuItem("经典");
    JMenuItem item2 = new JMenuItem("霓虹");
    JMenuItem item3 = new JMenuItem("糖果");

    // 图片资源的标识
    String theme = "A-";

    /**
     * 初始化数据    --初始化data
     */
    public void initData(){
        generatorNum();
        generatorNum();
    }

    public MainFrame(){
        // 初始化窗体
        initFrame();
        // 初始化菜单
        initMenu();
        // 初始化数据
        initData();
        // 绘制界面
        paintView();
        // 为窗体添加键盘监听
        this.addKeyListener(this);
        // 设置窗体可见
        setVisible(true);
    }

    /**
     * 初始化窗体
     */
    public void initFrame(){
        // 设置宽和高
        setSize(514, 538);
        // 设置窗体居中
        setLocationRelativeTo(null);
        // 设置窗体置顶
        setAlwaysOnTop(true);
        // 设置关闭模式
        setDefaultCloseOperation(3);
        // 设置窗体标题
        setTitle("2048小游戏");
        // 取消默认布局
        setLayout(null);
    }

    /**
     * 初始化菜单
     */
    public void initMenu(){
        // 1.创建JMenuBar
        JMenuBar menuBar = new JMenuBar();
        // 2.创建栏目对象JMenu
        JMenu menu1 = new JMenu("换肤");
        JMenu menu2 = new JMenu("关于我们");

        menuBar.add(menu1);
        menuBar.add(menu2);

        menu1.add(item1);
        menu1.add(item2);
        menu1.add(item3);

        // 给item绑定监听事件
        item1.addActionListener(this);
        item2.addActionListener(this);
        item3.addActionListener(this);

        // 4.给窗体设置菜单
        setJMenuBar(menuBar);
    }

    /**
     * 绘制游戏界面
     */
    public void paintView(){

        // 移除界面所有的内容
        getContentPane().removeAll();

        // 加载失败图片
        if(loseFlag == 2){
            JLabel loseLabel = new JLabel(new ImageIcon("src/images/"+theme+"lose.png"));
            loseLabel.setBounds(90, 100, 334,228);
            getContentPane().add(loseLabel);
        }

        // 随机创建16个JLabel
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                JLabel image_1 = new JLabel(new ImageIcon("src/images/"+ theme + data[i][j] + ".png"));
                image_1.setBounds(50 + 100 * j, 50 + 100 * i, 100, 100);
                getContentPane().add(image_1);
            }
        }

        JLabel background = new JLabel(new ImageIcon("src/images/"+ theme +"Background.jpg"));
        background.setBounds(40, 40, 420, 420);
        getContentPane().add(background);

        // 得分
        JLabel scoreLabel = new JLabel("得分：" + score);
        scoreLabel.setBounds(50, 20, 100, 20);
        getContentPane().add(scoreLabel);

        // 刷新界面
        getContentPane().repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }


    /**
    * 键盘按下时触发的方法，在此方法中区分上下左右键
    */
    @Override
    public void keyPressed(KeyEvent e) {
        int code  = e.getKeyCode();

        if(code == 37){
            moveToLeft();
            generatorNum();
        }else if(code == 38){
            moveToTop();
            generatorNum();
        }else if(code ==39){
            moveToRight();
            generatorNum();
        }else if(code == 40){
            moveToBottom();
            generatorNum();
        }

        // 每次移动完，都要检查游戏是否失败
        check();

        // 重新绘制界面
        paintView();
    }

    /**
     * 从空白位置，随机生成2号数字块
     */
    public void generatorNum(){
        // 1.创建两个数组记录二维数组中空白格子，i 和 j的索引位置
        int[] arrI = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int[] arrJ = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

        // 记录索引数组的长度
        int w = 0;

        // 2.遍历二维数组，取出每一个元素，判断是否为空白格（判断是否为0）
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] == 0){
                    // 3.是0的话，将索引存入arrI 和 arrJ 中
                    arrI[w] = i;
                    arrJ[w] = j;
                    w++;
                }
            }
        }

        // 4.w不为0代表还有空白格，可以产生新方块
        if (w != 0){
            Random r = new Random();
            int index = r.nextInt(w);
            int x = arrI[index];
            int y = arrJ[index];
            data[x][y] = 2;
        }

    }

    /**
     * 判断游戏是否失败
     */
    public void check(){
        if(checkLeft() == false && checkRight() == false && checkTop() == false && checkBottom() ==false){
            // 失败
           loseFlag = 2;
        }
    }

    /**
     * 二维数组的数据拷贝
     * @param src   原数组
     * @param dest  目标数组
     */
    public void copyArray(int[][] src, int[][] dest){
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                dest[i][j] = src[i][j];
            }
        }
    }

    /**
     * 判断是否能左移
     */
    public boolean checkLeft(){
        // 1.创建新数组，备份原数组
        int[][] newArr = new int[4][4];
        // 2.将原数组拷贝到新数组
        copyArray(data, newArr);
        // 3.对原数组调用左移动
        moveToLeft();
        // 4.比较移动后的原数组和备份数组中的数据，使用flag变量进行记录
        //设计思路：
        //true:可以移动
        //false:不能移动
        boolean flag = false;

        lo: // for循环的别名
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                // 只要有一个不相同，代表数据可以变换
                if(data[i][j] != newArr[i][j]){
                    flag = true;
                    break lo;
                }
            }
        }
        // 5.确定消息后，恢复原数组（再进行一次拷贝）
        copyArray(newArr, data);
        // 6.返回结果
        return flag;
    }

    /**
     * 判断是否能右移
     */
    public boolean checkRight(){
        int[][] newArr = new int[4][4];
        copyArray(data, newArr);
        moveToRight();
        boolean flag = false;
        lo:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(data[i][j] != newArr[i][j]){
                    flag = true;
                    break lo;
                }
            }
        }
        copyArray(newArr, data);
        return flag;
    }

    /**
     * 判断是否能上移
     */
    public boolean checkTop(){
        int[][] newArr = new int[4][4];
        copyArray(data, newArr);
        moveToTop();
        boolean flag = false;
        lo:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(data[i][j] != newArr[i][j]){
                    flag = true;
                    break lo;
                }
            }
        }
        copyArray(newArr, data);
        return flag;
    }

    /**
     * 判断是否能下移
     */
    public boolean checkBottom(){
        int[][] newArr = new int[4][4];
        copyArray(data, newArr);
        moveToBottom();
        boolean flag = false;
        lo:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(data[i][j] != newArr[i][j]){
                    flag = true;
                    break lo;
                }
            }
        }
        copyArray(newArr, data);
        return flag;
    }

    /**
     * 二维数组反转
     */
    public void horizontalSwap(){
        for (int i = 0; i < data.length; i++) {
            // 对每个一维数组进行反转
            reverseArray(data[i]);
        }
    }

    /**
     * 对一维数组进行反转
     */
    public void reverseArray(int[] arr){
        for (int start = 0, end = arr.length - 1; start < end; start++, end--){
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
        }
    }

    /**
     *  逆时针旋转数组
     */
    public void anticlockwise(){
        int[][] newArr = new int[4][4];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++){
                newArr[3 - j][i] = data[i][j];
            }
        }
        data = newArr;
    }

    /**
     * 顺时针旋转数组
     */
    public void clockwise(){
        int[][] newArr = new int[4][4];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++){
                newArr[j][3 - i] = data[i][j];
            }
        }
        data = newArr;
    }

    /**
     * 左移操作
     * */
    public void moveToLeft() {
        // 1.后置0
        for (int i = 0; i < data.length; i++) {
            // 克隆法
            int[] newArr = new int[4];
            int index = 0;
            for (int x = 0; x < data[i].length; x++) {
                if (data[i][x] != 0) {
                    newArr[index] = data[i][x];
                    index++;
                }
            }

            data[i] = newArr;

            // 2.合并
            for (int x = 0; x < 3; x++) {
                if (data[i][x] == data[i][x + 1]) {
                    data[i][x] *= 2;

                    // 计算得分
                    score += data[i][x];

                    // 合并后前移
                    for (int j = x + 1; j < 3; j++) {
                        data[i][j] = data[i][j + 1];
                    }
                    // 补零
                    data[i][3] = 0;
                }
            }
        }
    }

    /**
     * 右移动操作
     */
    private void moveToRight() {
        // 1.反转数组
        horizontalSwap();
        // 2.进行左移动处理
        moveToLeft();
        // 3. 反转数组
        horizontalSwap();
    }

    /**
     * 上移操作
     */
    public void moveToTop() {
        // 1.逆时针旋转数组
        anticlockwise();
        // 2.调用左移动操作
        moveToLeft();
        // 3.顺时针旋转数组
        clockwise();
    }

    /**
     * 下移操作
     */
    public void moveToBottom(){
        // 1.顺时针旋转数组
        clockwise();
        // 2.左移操作
        moveToLeft();
        // 3.逆时针旋转数组
        anticlockwise();
    }

    /**
    * 键盘松开时触发
    */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == item1){
            theme = "A-";
        }else if(e.getSource() == item2){
            theme = "B-";
        }else if(e.getSource() == item3){
            theme = "C-";
        }

        // 重新绘制界面
        paintView();
    }
}


package cn.itcast.day02.jframe;

import javax.swing.*;

public class App2048 {
    public static void main(String[] args) {
        new MainFrame();
    }
}
