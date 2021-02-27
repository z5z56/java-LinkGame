package 连连看;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartFrame extends JFrame{
	MyFrame myframe;
	
	JPanel actionPanel = new JPanel();//进入游戏按钮栏
	public JButton buttonStart = new JButton("开始游戏");
	public JLabel labelAuthor=new JLabel(" ");//仅在暂停的时候显示
	public StartFrame(){
		

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
		
		actionPanel.add(buttonStart,BorderLayout.CENTER);
		this.getContentPane().add(actionPanel,BorderLayout.SOUTH);
		this.getContentPane().add(labelAuthor,BorderLayout.CENTER);
		this.setSize(700,700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置按x的时候关闭程序
		this.setTitle("连连看");
		this.setLocationRelativeTo(null);//设置窗口居中显示
		this.setVisible(true);

		buttonStart.setFont(new  java.awt.Font("幼圆",  1,  25)); 
		//进入游戏主界面
		buttonStart.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				new MyFrame();//进入游戏界面
				//System.exit(0);			
				buttonStart.setEnabled(false);//开始了就不能再按开始游戏按钮了
				}

			});

	}
	
	public void paint(Graphics g){
		File file=new File("src//image//startbgp.png");
		Image image = null;
		g.clearRect(0, 0, 700, 600);
		//绘制背景图
		//读取背景图到image，用try包围避免错误
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	     g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);  
	}

		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new StartFrame();
	}
}
