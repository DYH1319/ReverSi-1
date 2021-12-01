package view;

import components.ChessGridComponent;
import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameFrame extends JFrame {
    public static GameController controller;
    private ChessBoardPanel chessBoardPanel;
    private StatusPanel statusPanel;
    private Container container=this.getContentPane();
    private static ArrayList<int[][]>boardPanelsList=new ArrayList<>(5);
    public static int stepNum=0;

    public static ArrayList<int[][]> getBoardPanelsList(){
        return GameFrame.boardPanelsList;
    }


    public GameFrame(int frameSize) {
        
        this.setTitle("2021F CS102A Project Reversi");
        this.setLayout(null);
        this.setSize(frameSize, frameSize);
        this.setLocationRelativeTo(null);
        

        chessBoardPanel = new ChessBoardPanel((int) (this.getWidth() * 0.7), (int) (this.getHeight() * 0.7));
        chessBoardPanel.setLocation((this.getWidth() - chessBoardPanel.getWidth()) / 2, (this.getHeight() - chessBoardPanel.getHeight()) / 3);
        

        statusPanel = new StatusPanel(560, (int) (this.getHeight() * 0.1));
        statusPanel.setLocation((this.getWidth() - chessBoardPanel.getWidth()) / 2, 0);
        
        
        controller = new GameController(chessBoardPanel, statusPanel);
        //controller.setGamePanel(chessBoardPanel);

        
        this.add(chessBoardPanel);
        this.add(statusPanel);

        //new一个自制的事件监听；
        MyBtnActionListener myBtnActionListener = new MyBtnActionListener(this);

        JButton restartBtn = new JButton("Restart");
        restartBtn.setSize(120, 50);
        restartBtn.setLocation((this.getWidth() - chessBoardPanel.getWidth()) / 2, (this.getHeight() + chessBoardPanel.getHeight()) / 2);
        add(restartBtn);
        restartBtn.addActionListener(myBtnActionListener);

        
        JButton loadGameBtn = new JButton("Load");
        loadGameBtn.setSize(120, 50);
        loadGameBtn.setLocation(restartBtn.getX()+restartBtn.getWidth()+30, restartBtn.getY());
        add(loadGameBtn);
        loadGameBtn.addActionListener(myBtnActionListener);
        
        
        JButton saveGameBtn = new JButton("Save");
        saveGameBtn.setSize(120, 50);
        saveGameBtn.setLocation(loadGameBtn.getX()+restartBtn.getWidth()+30, restartBtn.getY());
        add(saveGameBtn);
        saveGameBtn.addActionListener(myBtnActionListener);
        
        //new一个自制夜间监听事件；
        NightModeSetter nightModeSetter=new NightModeSetter(this);

        JButton nightModeBtn= new JButton("Nightmode");
        nightModeBtn.setSize(120,50);
        nightModeBtn.setLocation(saveGameBtn.getX()+saveGameBtn.getWidth()+30, restartBtn.getY());
        add(nightModeBtn);
        nightModeBtn.addActionListener(nightModeSetter);

        
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        
    }

    //构建一个事件监听的内部类；
    private class MyBtnActionListener implements ActionListener{

        //设置GameFrame的成员变量；
        private GameFrame gameFrame;

        //重写构造器，使监听器可以接触当前类；
        public MyBtnActionListener(GameFrame gameFrame) {
            this.gameFrame = gameFrame;
        }

        /*
        实现ActionListener方法，restart则使当前GameFrame不可见，并重新new一个GameFrame;除此之外，加入Save和Load的事件监听方法；
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            //使监听器能区分触发的不同button以触发不同效果；
            if(e.getActionCommand().equals("Restart")){
                System.out.println("clicked Restart button.");
                this.gameFrame.setVisible(false);
                new GameFrame(800);
                GameFrame.stepNum=0;
                GameFrame.getBoardPanelsList().clear();
            }else if(e.getActionCommand().equals("Load")){
                System.out.println("clicked Load Btn");
                String filePath = JOptionPane.showInputDialog("Load the game:", "input the path here");
                controller.readFileData(filePath);
            }else if(e.getActionCommand().equals("Save")){System.out.println("clicked Save Btn");
                String filePath = JOptionPane.showInputDialog("Save the game:", "input the path here");
                controller.writeDataToFile(filePath);
            }
        }
    }

    //创建夜间模式事件监听类；
    private class NightModeSetter implements ActionListener{
        private int nightModeInt=-1;
        private GameFrame client;

        //构造器重写，引入GameFrame变量；
        public NightModeSetter(GameFrame gameFrame){
            this.client=gameFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(this.nightModeInt==1){
                client.container.setBackground(Color.WHITE);
                client.statusPanel.setBackground(Color.WHITE);
                client.statusPanel.setPlayerLabelColor(Color.BLACK);
                client.statusPanel.setScoreLabelColor(Color.BLACK);
                this.nightModeInt=-1;
            }else if(this.nightModeInt==-1){
                client.container.setBackground(Color.BLACK);
                client.statusPanel.setPlayerLabelColor(Color.white);
                client.statusPanel.setScoreLabelColor(Color.WHITE);
                client.statusPanel.setBackground(Color.BLACK);
                this.nightModeInt=1;
            }
        }
    }
    //维修函数；
    public static void main(String[] args) {
        new GameFrame(800);
    }
}
