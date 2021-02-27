package 连连看;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MyFrame extends JFrame {

	InfoPanel infoPanel = new InfoPanel();//上方信息提示栏
	GamePanel gamePanel = new GamePanel(10);//中间游戏界面
	JPanel actionPanel = new JPanel();//下方开始游戏按钮栏

	int nowTime = 20;
	boolean flag = false;
	boolean NoPauseFlag=true;
	boolean CatchNoPauseFlag=true;
	
	public JButton buttonRestart = new JButton("开始游戏");
	public JButton buttonPause = new JButton("暂停");
	public JLabel labelResetMapNum=new JLabel("按a重置地图技能（一局3次）");
	public JLabel labelGamestate=new JLabel("游戏正在进行");//仅在暂停的时候显示
	public MyFrame(){
		this.setFocusable(true);//将控件设置成可获取焦点状态，默认是无法获取焦点的，只有设置成true，才能获取控件的点击事件
		this.setResizable(false);//固定窗口不能缩放
		//设置窗口图标
		File file=new File("src//image//ico.png");
		Image image = null;
		//读取图标图片到image，用try包围避免错误
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		this.setIconImage(image);
		
		buttonRestart.setFont(new  java.awt.Font("幼圆", 1,  18)); 
		buttonPause.setFont(new  java.awt.Font("幼圆",  1,  18)); 
		labelResetMapNum.setFont(new  java.awt.Font("幼圆",  1,  18)); 
		labelGamestate.setFont(new  java.awt.Font("幼圆",  1,  18)); 
		
		actionPanel.setLayout(new FlowLayout()); 
		actionPanel.add(labelResetMapNum,BorderLayout.CENTER);
		actionPanel.add(buttonRestart,BorderLayout.CENTER);
		actionPanel.add(buttonPause,BorderLayout.CENTER);


		actionPanel.setBackground(new Color(176,196,222));//指定背景色

		
		buttonPause.setEnabled(false);//还没有开始游戏的时候不能按暂停
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(infoPanel,BorderLayout.NORTH);
		this.getContentPane().add(gamePanel,BorderLayout.CENTER);
		this.getContentPane().add(actionPanel,BorderLayout.SOUTH);
		this.setSize(700,700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置按x的时候关闭程序
		this.setTitle("连连看");
		this.setLocationRelativeTo(null);//设置窗口居中显示
		this.setVisible(true);

		//暂停和继续功能
		buttonPause.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(NoPauseFlag) {//没有在暂停则按下按钮暂停
					buttonPause.setText("继续");//暂停后按钮变成继续
					NoPauseFlag=false;
					//System.out.println("getNoPauseFlag  "+NoPauseFlag);
					gamePanel.removeMouseListener(gamePanel);//暂停的时候不能操作鼠标移除鼠标监听
					labelGamestate.setText("游戏已暂停");
					gamePanel.isPause=true;//按isPause标志位情况将绘制不同图片，此句做到暂停时候隐藏游戏界面
					gamePanel.repaint();
					return;
				}
				else if(!NoPauseFlag) {
					buttonPause.setText("暂停");
					NoPauseFlag=true;
					//System.out.println("getNoPauseFlag  "+NoPauseFlag);
					gamePanel.addMouseListener(gamePanel);//继续的时候加入鼠标监听
					labelGamestate.setText("游戏正在进行");
					gamePanel.isPause=false;
					gamePanel.repaint();
				}
			}
		});
		
		buttonRestart.addMouseListener(new MouseAdapter(){//"开始游戏"按钮按下的执行函数
			public void mouseClicked(MouseEvent e){
				if(flag)//如果为真说明游戏已经开始，再按按钮就不执行下面的初始化了
					return ;
				flag = true;//游戏开始
				//当按了开始游戏之后下一次暂停又继续的时候才会显示"游戏正在进行"
				actionPanel.add(labelGamestate,BorderLayout.CENTER);
				gamePanel.addKeyListener(gamePanel);
				//加入鼠标监听，在这个类里面的mousePressed将由于鼠标点击而执行
				gamePanel.addMouseListener(gamePanel);
				gamePanel.startGame();//开始游戏
				buttonRestart.setEnabled(false);//游戏开始了就不能再按开始游戏按钮了
				buttonPause.setEnabled(true);
				InfoPanel.score.setText(0+"");
				new Thread(new Runnable(){
					@Override
					public void run() {
						nowTime = 20;
						while(true) {//这个死循环在暂停的时候，不断监控是否标志位有改变，使得可以继续
							System.out.println("getNoPauseFlag  "+NoPauseFlag);
							CatchNoPauseFlag=getNoPauseFlag();
							System.out.println("CatchNoPauseFlag  "+CatchNoPauseFlag);
						while(CatchNoPauseFlag){
						try {
							if(!getNoPauseFlag()) {
								break;
							}
							//System.out.println("getNoPauseFlag"+getNoPauseFlag());
							Thread.currentThread().sleep(1000);//倒计时线程每过1s就把时间--
							nowTime--;
							//以下是消除奖励时间功能
							if(gamePanel.getisXiaochu()) {
								nowTime+=3;//有消除则加3s，消除奖励时间
								System.out.println(gamePanel.getisXiaochu());
								InfoPanel.setTime(nowTime);//有消除则加3s，消除奖励时间
								gamePanel.isXiaochu=false;//奖励完时间要清空消除标志位
							}
							InfoPanel.setTime(nowTime);
							//infoPanel里面设置的时间100这里也要跟着变
							if(nowTime==0){
								gamePanel.removeMouseListener(gamePanel);
								gamePanel.removeKeyListener(gamePanel);
								int score = Integer.parseInt(infoPanel.score.getText());
								int record = Integer.parseInt(infoPanel.record.getText()); 
								if(score>record){
									JOptionPane.showMessageDialog(null, "游戏结束，你的得分是"+score+",刷新了历史记录"+record);
									infoPanel.updateBestScore();
								}else{
									
									
									JOptionPane.showMessageDialog(null, "游戏结束，你的得分是"+InfoPanel.score.getText());
								}
								//游戏结束再次更新下栏状态
								buttonRestart.setEnabled(true);
								buttonPause.setEnabled(false);
								labelGamestate.setText("");
								flag = false;
								gamePanel.ResetResetMapToolNum();//重置打乱地图技能次数
								Thread.currentThread().stop();//停止倒计时线程，如果没有这句再次启动速度会快一倍
								break;
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
					}
					}
				}).start();;
			}
		});

	}
	
	
	public boolean getNoPauseFlag()	{
		//System.out.println("getNoPauseFlag"+NoPauseFlag);
		return this.NoPauseFlag;
	}
	



}
