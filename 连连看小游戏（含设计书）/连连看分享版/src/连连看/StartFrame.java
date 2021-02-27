package ������;

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
	
	JPanel actionPanel = new JPanel();//������Ϸ��ť��
	public JButton buttonStart = new JButton("��ʼ��Ϸ");
	public JLabel labelAuthor=new JLabel(" ");//������ͣ��ʱ����ʾ
	public StartFrame(){
		

		//���ô���ͼ��
		File file=new File("src//image//ico.png");
		Image image = null;
		//��ȡͼ��ͼƬ��image����try��Χ�������
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		this.setIconImage(image);
		
		actionPanel.add(buttonStart,BorderLayout.CENTER);
		this.getContentPane().add(actionPanel,BorderLayout.SOUTH);
		this.getContentPane().add(labelAuthor,BorderLayout.CENTER);
		this.setSize(700,700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//���ð�x��ʱ��رճ���
		this.setTitle("������");
		this.setLocationRelativeTo(null);//���ô��ھ�����ʾ
		this.setVisible(true);

		buttonStart.setFont(new  java.awt.Font("��Բ",  1,  25)); 
		//������Ϸ������
		buttonStart.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				new MyFrame();//������Ϸ����
				//System.exit(0);			
				buttonStart.setEnabled(false);//��ʼ�˾Ͳ����ٰ���ʼ��Ϸ��ť��
				}

			});

	}
	
	public void paint(Graphics g){
		File file=new File("src//image//startbgp.png");
		Image image = null;
		g.clearRect(0, 0, 700, 600);
		//���Ʊ���ͼ
		//��ȡ����ͼ��image����try��Χ�������
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	     g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);  
	}

		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new StartFrame();
	}
}
