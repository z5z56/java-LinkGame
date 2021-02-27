package ������;

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

	InfoPanel infoPanel = new InfoPanel();//�Ϸ���Ϣ��ʾ��
	GamePanel gamePanel = new GamePanel(10);//�м���Ϸ����
	JPanel actionPanel = new JPanel();//�·���ʼ��Ϸ��ť��

	int nowTime = 20;
	boolean flag = false;
	boolean NoPauseFlag=true;
	boolean CatchNoPauseFlag=true;
	
	public JButton buttonRestart = new JButton("��ʼ��Ϸ");
	public JButton buttonPause = new JButton("��ͣ");
	public JLabel labelResetMapNum=new JLabel("��a���õ�ͼ���ܣ�һ��3�Σ�");
	public JLabel labelGamestate=new JLabel("��Ϸ���ڽ���");//������ͣ��ʱ����ʾ
	public MyFrame(){
		this.setFocusable(true);//���ؼ����óɿɻ�ȡ����״̬��Ĭ�����޷���ȡ����ģ�ֻ�����ó�true�����ܻ�ȡ�ؼ��ĵ���¼�
		this.setResizable(false);//�̶����ڲ�������
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
		
		buttonRestart.setFont(new  java.awt.Font("��Բ", 1,  18)); 
		buttonPause.setFont(new  java.awt.Font("��Բ",  1,  18)); 
		labelResetMapNum.setFont(new  java.awt.Font("��Բ",  1,  18)); 
		labelGamestate.setFont(new  java.awt.Font("��Բ",  1,  18)); 
		
		actionPanel.setLayout(new FlowLayout()); 
		actionPanel.add(labelResetMapNum,BorderLayout.CENTER);
		actionPanel.add(buttonRestart,BorderLayout.CENTER);
		actionPanel.add(buttonPause,BorderLayout.CENTER);


		actionPanel.setBackground(new Color(176,196,222));//ָ������ɫ

		
		buttonPause.setEnabled(false);//��û�п�ʼ��Ϸ��ʱ���ܰ���ͣ
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(infoPanel,BorderLayout.NORTH);
		this.getContentPane().add(gamePanel,BorderLayout.CENTER);
		this.getContentPane().add(actionPanel,BorderLayout.SOUTH);
		this.setSize(700,700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//���ð�x��ʱ��رճ���
		this.setTitle("������");
		this.setLocationRelativeTo(null);//���ô��ھ�����ʾ
		this.setVisible(true);

		//��ͣ�ͼ�������
		buttonPause.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(NoPauseFlag) {//û������ͣ���°�ť��ͣ
					buttonPause.setText("����");//��ͣ��ť��ɼ���
					NoPauseFlag=false;
					//System.out.println("getNoPauseFlag  "+NoPauseFlag);
					gamePanel.removeMouseListener(gamePanel);//��ͣ��ʱ���ܲ�������Ƴ�������
					labelGamestate.setText("��Ϸ����ͣ");
					gamePanel.isPause=true;//��isPause��־λ��������Ʋ�ͬͼƬ���˾�������ͣʱ��������Ϸ����
					gamePanel.repaint();
					return;
				}
				else if(!NoPauseFlag) {
					buttonPause.setText("��ͣ");
					NoPauseFlag=true;
					//System.out.println("getNoPauseFlag  "+NoPauseFlag);
					gamePanel.addMouseListener(gamePanel);//������ʱ�����������
					labelGamestate.setText("��Ϸ���ڽ���");
					gamePanel.isPause=false;
					gamePanel.repaint();
				}
			}
		});
		
		buttonRestart.addMouseListener(new MouseAdapter(){//"��ʼ��Ϸ"��ť���µ�ִ�к���
			public void mouseClicked(MouseEvent e){
				if(flag)//���Ϊ��˵����Ϸ�Ѿ���ʼ���ٰ���ť�Ͳ�ִ������ĳ�ʼ����
					return ;
				flag = true;//��Ϸ��ʼ
				//�����˿�ʼ��Ϸ֮����һ����ͣ�ּ�����ʱ��Ż���ʾ"��Ϸ���ڽ���"
				actionPanel.add(labelGamestate,BorderLayout.CENTER);
				gamePanel.addKeyListener(gamePanel);
				//������������������������mousePressed�������������ִ��
				gamePanel.addMouseListener(gamePanel);
				gamePanel.startGame();//��ʼ��Ϸ
				buttonRestart.setEnabled(false);//��Ϸ��ʼ�˾Ͳ����ٰ���ʼ��Ϸ��ť��
				buttonPause.setEnabled(true);
				InfoPanel.score.setText(0+"");
				new Thread(new Runnable(){
					@Override
					public void run() {
						nowTime = 20;
						while(true) {//�����ѭ������ͣ��ʱ�򣬲��ϼ���Ƿ��־λ�иı䣬ʹ�ÿ��Լ���
							System.out.println("getNoPauseFlag  "+NoPauseFlag);
							CatchNoPauseFlag=getNoPauseFlag();
							System.out.println("CatchNoPauseFlag  "+CatchNoPauseFlag);
						while(CatchNoPauseFlag){
						try {
							if(!getNoPauseFlag()) {
								break;
							}
							//System.out.println("getNoPauseFlag"+getNoPauseFlag());
							Thread.currentThread().sleep(1000);//����ʱ�߳�ÿ��1s�Ͱ�ʱ��--
							nowTime--;
							//��������������ʱ�书��
							if(gamePanel.getisXiaochu()) {
								nowTime+=3;//���������3s����������ʱ��
								System.out.println(gamePanel.getisXiaochu());
								InfoPanel.setTime(nowTime);//���������3s����������ʱ��
								gamePanel.isXiaochu=false;//������ʱ��Ҫ���������־λ
							}
							InfoPanel.setTime(nowTime);
							//infoPanel�������õ�ʱ��100����ҲҪ���ű�
							if(nowTime==0){
								gamePanel.removeMouseListener(gamePanel);
								gamePanel.removeKeyListener(gamePanel);
								int score = Integer.parseInt(infoPanel.score.getText());
								int record = Integer.parseInt(infoPanel.record.getText()); 
								if(score>record){
									JOptionPane.showMessageDialog(null, "��Ϸ��������ĵ÷���"+score+",ˢ������ʷ��¼"+record);
									infoPanel.updateBestScore();
								}else{
									
									
									JOptionPane.showMessageDialog(null, "��Ϸ��������ĵ÷���"+InfoPanel.score.getText());
								}
								//��Ϸ�����ٴθ�������״̬
								buttonRestart.setEnabled(true);
								buttonPause.setEnabled(false);
								labelGamestate.setText("");
								flag = false;
								gamePanel.ResetResetMapToolNum();//���ô��ҵ�ͼ���ܴ���
								Thread.currentThread().stop();//ֹͣ����ʱ�̣߳����û������ٴ������ٶȻ��һ��
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
