package ������;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
/**
 * GamePanel�����ڽ�MapUtil�еĶ�ά�������ݣ�ת��Ϊ��ӦͼƬ��ִ��������¡���ʾͼƬ���ж��Ƿ�������Ľ�������
 * @author ljz
 *
 */
public class GamePanel extends JPanel implements ActionListener ,MouseListener,KeyListener{

	MapUtil mapUtil;
	int[][] map;
	Image[] pics;
	int leftX=100,leftY=40;
	int n,score=0;
	int clickX,clickY;
	boolean isClick = false;
	boolean isXiaochu = false;//�Ƿ���������־λ
	boolean isPause=false;//�Ƿ�����ͣ��־λ��������ͣʱ��ʾ��ͬ���
	int ResetMapToolNum=3;//��A���õ�ͼ���ܣ�һ��3��
	public static final int EMPTY = -1,W = 50;
	//���캯������ʼ����ͼ
	public GamePanel(int n){
		this.n = n;
		mapUtil = new MapUtil(n);
		mapUtil.initMap();
		map = mapUtil.getMap();//map�����mapUtil�����ýӿڷ��س����Ķ�ά��������
		pics = new Image[n-1];//һ��n-1��ͼ��
		setSize(600, 600);
		this.setVisible(true);//�������п�ʼ��ͼ��
		this.setFocusable(true);//���ؼ����óɿɻ�ȡ����״̬��Ĭ�����޷���ȡ����ģ�ֻ�����ó�true�����ܻ�ȡ�ؼ��ĵ���¼�
		getPics();//���ļ�����ͼƬ��pics
		
		//����ȷ����ʼ���ĵ�ͼû�п�����ȥ�ķ���
		//1ɨ���Ƿ���ڿ���������2ɨ�貢��������������
		while(globalSearach(1)){
			globalSearach(2);
			downPic();//��ͼƬ����
			updatePic();
		}
		score = 0;
		InfoPanel.score.setText(0+"");
		repaint();
	}

	//���ļ���ȡͼƬ
	private void getPics() {
		
		for(int i=0;i<n-1;i++){
			pics[i] = Toolkit.getDefaultToolkit().getImage("src//image//pic"+i+".jpg");
		}
	}
	//����map����ͼƬ�����Ʊ���ͼ
	public void paint(Graphics g){
		if(!isPause) {
			File file=new File("src//image//bgp.png");
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
		     
			//���Ʒ���
			for(int i=0;i<n;i++){
				for(int j=0;j<n;j++){
					if(map[i][j]!=EMPTY){
						g.drawImage(pics[map[i][j]],leftX+W*j,leftY+W*i,W,W,this);//img=pics[ͼƬ��map�ж�Ӧ����]
					}else{
						g.clearRect(leftX+W*j,leftY+W*i,W, W);
					}
				}
			}
		}
		else {
			File file=new File("src//image//bgp.png");
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
	}


	public boolean getisXiaochu(){//���������־λ���
		return isXiaochu;
	}
	
	public void startGame(){

		this.requestFocus();
		isClick = false;
		while(globalSearach(1)){

			globalSearach(2);
			downPic();
			updatePic();
			repaint();
		}
		score = 0;
	}
	
	public void resetMap(){
		isClick = false;
		map = mapUtil.getResetMap();
		repaint();
		while(globalSearach(1)){
			globalSearach(2);
			downPic();
			updatePic();
			repaint();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	
		int x = e.getX()-leftX;
		int y = e.getY()-leftY;
		if(x<0||y<0||x>=50*n||y>=50*n){
			return ;
		}
		int tempX = y/W;
		int tempY = x/W;
		
		
		if(isClick){//�ڶ��ε��
			if((tempX==clickX&&(tempY==clickY+1||tempY==clickY-1))||(tempY==clickY&&(tempX==clickX+1||tempX==clickX-1))){//������ε����ͼ������
				
				//����
				int help = map[tempX][tempY];
				map[tempX][tempY] = map[clickX][clickY];
				map[clickX][clickY] = help;
				repaint();
				if(isThreeLinked(tempX,tempY)||isThreeLinked(clickX,clickY)){//�ж��Ƿ���ڿ���ȥ�ķ���
				//	System.out.println("������ȥ");
					isXiaochu=true;//��������־λ=1Ϊ�˼�ʱ�佱����
					if(isThreeLinked(tempX,tempY)){
						removeThreeLinked(tempX,tempY);
					}
					if(isThreeLinked(clickX,clickY)){
						removeThreeLinked(clickX,clickY);						
					}
					downPic();
					updatePic();
					repaint();
					while(globalSearach(1)){
						globalSearach(2);
						downPic();
						updatePic();
						repaint();
					}
				}else{
					//System.out.println("û�п���ȥ�ķ���");
					//��������
					help = map[tempX][tempY];
					map[tempX][tempY] = map[clickX][clickY];
					map[clickX][clickY] = help;					
				}
				
				isClick = false;
			}else{//�����ڻ��߾��ǵ���Ļ�������
				isClick = true;
				clickX = tempX;
				clickY = tempY;
				drawSelectedBlock(tempY*W+leftX, tempX*W+leftY, this.getGraphics());
			}
		}else{
			isClick = true;
			clickX = tempX;
			clickY = tempY;
			drawSelectedBlock(tempY*W+leftX, tempX*W+leftY, this.getGraphics());
		}		
		
	}
	


	//1ɨ���Ƿ���ڿ���������
	//2ɨ�貢��������������
	private boolean globalSearach(int flag) {
		if(flag == 1){
			for(int i=0;i<n;i++){
				for(int j=0;j<n;j++){
					if(isThreeLinked(i, j)){
						return true;
					}
				}
			}			
		}else{//�������ķ�1�������������������ķ���
			for(int i=0;i<n;i++){
				for(int j=0;j<n;j++){
					if(isThreeLinked(i, j))
					removeThreeLinked(i, j);
				}
			}
		}
		
		return false;
	}

	//����ͼ������
	private void updatePic() {
	
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(map[i][j]==EMPTY){
					map[i][j] = (int) (Math.random()*(n-2));
				}
			}
		}
		
	}

	//ͼ������
	private void downPic() {
	
		for(int i=n-1;i>=0;i--){
			for(int j=0;j<n;j++){
				if(map[i][j]==EMPTY){
					int temp = i;
					while(temp>=0){
						if(map[temp][j]!=EMPTY){
							int help = map[i][j];
							map[i][j] = map[temp][j];
							map[temp][j] = help;
							break;
						}
						temp--;
					}
				}
			}
		}
		
	}

	//�����������������ķ���
	private void removeThreeLinked(int x, int y) {
		
		int count = 1;
		int linked = 1;
		
		if(x+1<n){//����̽��
			for(int i=x+1;i<n;i++){
				if(map[i][y]==map[x][y]){
					linked++;
				}else{
					break;
				}
			}
		}
		
		if(x-1>=0){//����̽��
			for(int i=x-1;i>=0;i--){
				if(map[i][y]==map[x][y]){
					linked++;
				}else{
					break;
				}
			}
		}
		
		if(linked>=3){//�������ڳ�����������
			for(int i=x-1;i>=0;i--){
				if(map[i][y]==map[x][y]){
					count++;
					map[i][y] = EMPTY;
				}else{
					break;
				}
			}
			for(int i=x+1;i<n;i++){
				if(map[i][y]==map[x][y]){
					count++;
					map[i][y] = EMPTY;					
				}else{
					break;
				}
			}
				
		}
		
		linked = 1;
		
		if(y+1<n){//����̽��
			for(int i=y+1;i<n;i++){
				if(map[x][i]==map[x][y]){
					linked++;
				}else{
					break;
				}
			}
		}
		
		if(y-1>=0){//����̽��
			for(int i=y-1;i>=0;i--){
				if(map[x][i]==map[x][y]){
					linked++;
				}else{
					break;
				}
			}
		}
		
		if(linked>=3){//�������ڳ�����������
			for(int i=y-1;i>=0;i--){
				if(map[x][i]==map[x][y]){
						count++;
						map[x][i] = EMPTY;
				}else{
						break;
					}
				}
			for(int i=y+1;i<n;i++){
				if(map[x][i]==map[x][y]){
						count++;
						map[x][i] = EMPTY;
				}else{
						break;
				}
			}
					
		}
		
		map[x][y] = EMPTY;
		score+=count*10;
		InfoPanel.score.setText(score+"");
	}

	//����Ƿ�����������������ķ���
	private boolean isThreeLinked(int x, int y) {

		int count = 1;
		if(x+1<n){
			for(int i=x+1;i<n;i++){
				if(map[i][y]==map[x][y]){
					count++;
				}else{
					break;
				}
			}
		}
		
		if(x-1>=0){
			for(int i=x-1;i>=0;i--){
				if(map[i][y]==map[x][y]){
					count++;
				}else{
					break;
				}
			}
		}
		
		if(count>=3){
			return true;
		}
		
		count = 1;
		
		if(y+1<n){
			for(int i=y+1;i<n;i++){
				if(map[x][i]==map[x][y]){
					count++;
				}else{
					break;
				}
			}
		}
		
		if(y-1>=0){
			for(int i=y-1;i>=0;i--){
				if(map[x][i]==map[x][y]){
					count++;
				}else{
					break;
				}
			}
		}

		if(count>=3){
			return true;
		}

		
		return false;
	}

	//��ѡ�п�
	private void drawSelectedBlock(int x, int y, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;//����Graphics����
		BasicStroke s = new BasicStroke(1);//���Ϊ1�Ļ���
		g2.setStroke(s);
		g2.setColor(Color.RED);
		g.drawRect(x+1, y+1, 48, 48);
	}
	
	@Override
	
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//��a���ҵ�ͼ
		if(e.getKeyCode() == KeyEvent.VK_A&&ResetMapToolNum>0){			
			resetMap();
			repaint();
			ResetMapToolNum--;//һ��3�λ������õ�ͼ
			System.out.println("ResetMapToolNum"+ResetMapToolNum);//�������̻�������õ���Ӧ
		}
	
	}
	//�������¿�ʼ��Ϸʱ�����ҵ�ͼ���ܴ�������
	public void ResetResetMapToolNum() {
		this.ResetMapToolNum=3;
	}
	
	//**********����û����
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
