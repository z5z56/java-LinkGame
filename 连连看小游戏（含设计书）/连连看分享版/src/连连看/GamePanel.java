package 连连看;

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
 * GamePanel类用于将MapUtil中的二维数组数据，转化为对应图片，执行载入更新、显示图片、判断是否可消除的交互功能
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
	boolean isXiaochu = false;//是否有消除标志位
	boolean isPause=false;//是否在暂停标志位，用于暂停时显示不同情况
	int ResetMapToolNum=3;//按A重置地图技能，一局3次
	public static final int EMPTY = -1,W = 50;
	//构造函数，初始化地图
	public GamePanel(int n){
		this.n = n;
		mapUtil = new MapUtil(n);
		mapUtil.initMap();
		map = mapUtil.getMap();//map保存从mapUtil类中用接口返回出来的二维整形数组
		pics = new Image[n-1];//一共n-1种图案
		setSize(600, 600);
		this.setVisible(true);//可以运行开始画图了
		this.setFocusable(true);//将控件设置成可获取焦点状态，默认是无法获取焦点的，只有设置成true，才能获取控件的点击事件
		getPics();//从文件载入图片到pics
		
		//首先确保初始化的地图没有可以消去的方块
		//1扫描是否存在可消除方块2扫描并消除可消除方块
		while(globalSearach(1)){
			globalSearach(2);
			downPic();//让图片下落
			updatePic();
		}
		score = 0;
		InfoPanel.score.setText(0+"");
		repaint();
	}

	//从文件读取图片
	private void getPics() {
		
		for(int i=0;i<n-1;i++){
			pics[i] = Toolkit.getDefaultToolkit().getImage("src//image//pic"+i+".jpg");
		}
	}
	//根据map绘制图片，绘制背景图
	public void paint(Graphics g){
		if(!isPause) {
			File file=new File("src//image//bgp.png");
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
		     
			//绘制方块
			for(int i=0;i<n;i++){
				for(int j=0;j<n;j++){
					if(map[i][j]!=EMPTY){
						g.drawImage(pics[map[i][j]],leftX+W*j,leftY+W*i,W,W,this);//img=pics[图片在map中对应数字]
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
	}


	public boolean getisXiaochu(){//获得消除标志位情况
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
		
		
		if(isClick){//第二次点击
			if((tempX==clickX&&(tempY==clickY+1||tempY==clickY-1))||(tempY==clickY&&(tempX==clickX+1||tempX==clickX-1))){//如果两次点击的图案相邻
				
				//交换
				int help = map[tempX][tempY];
				map[tempX][tempY] = map[clickX][clickY];
				map[clickX][clickY] = help;
				repaint();
				if(isThreeLinked(tempX,tempY)||isThreeLinked(clickX,clickY)){//判断是否存在可消去的方块
				//	System.out.println("可以消去");
					isXiaochu=true;//有消除标志位=1为了加时间奖励用
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
					//System.out.println("没有可消去的方块");
					//交换回来
					help = map[tempX][tempY];
					map[tempX][tempY] = map[clickX][clickY];
					map[clickX][clickY] = help;					
				}
				
				isClick = false;
			}else{//不相邻或者就是点击的还是自身
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
	


	//1扫描是否存在可消除方块
	//2扫描并消除可消除方块
	private boolean globalSearach(int flag) {
		if(flag == 1){
			for(int i=0;i<n;i++){
				for(int j=0;j<n;j++){
					if(isThreeLinked(i, j)){
						return true;
					}
				}
			}			
		}else{//如果输入的非1就消除三个以上相连的方块
			for(int i=0;i<n;i++){
				for(int j=0;j<n;j++){
					if(isThreeLinked(i, j))
					removeThreeLinked(i, j);
				}
			}
		}
		
		return false;
	}

	//更新图案数组
	private void updatePic() {
	
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(map[i][j]==EMPTY){
					map[i][j] = (int) (Math.random()*(n-2));
				}
			}
		}
		
	}

	//图案下落
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

	//消除三个以上相连的方块
	private void removeThreeLinked(int x, int y) {
		
		int count = 1;
		int linked = 1;
		
		if(x+1<n){//向下探测
			for(int i=x+1;i<n;i++){
				if(map[i][y]==map[x][y]){
					linked++;
				}else{
					break;
				}
			}
		}
		
		if(x-1>=0){//向上探测
			for(int i=x-1;i>=0;i--){
				if(map[i][y]==map[x][y]){
					linked++;
				}else{
					break;
				}
			}
		}
		
		if(linked>=3){//上下相邻超过三个方块
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
		
		if(y+1<n){//向右探测
			for(int i=y+1;i<n;i++){
				if(map[x][i]==map[x][y]){
					linked++;
				}else{
					break;
				}
			}
		}
		
		if(y-1>=0){//向左探测
			for(int i=y-1;i>=0;i--){
				if(map[x][i]==map[x][y]){
					linked++;
				}else{
					break;
				}
			}
		}
		
		if(linked>=3){//左右相邻超过三个方块
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

	//检测是否存在三个以上相连的方块
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

	//画选中框
	private void drawSelectedBlock(int x, int y, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;//生成Graphics对象
		BasicStroke s = new BasicStroke(1);//宽度为1的画笔
		g2.setStroke(s);
		g2.setColor(Color.RED);
		g.drawRect(x+1, y+1, 48, 48);
	}
	
	@Override
	
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//按a打乱地图
		if(e.getKeyCode() == KeyEvent.VK_A&&ResetMapToolNum>0){			
			resetMap();
			repaint();
			ResetMapToolNum--;//一共3次机会重置地图
			System.out.println("ResetMapToolNum"+ResetMapToolNum);//监听键盘会在这里得到相应
		}
	
	}
	//用于重新开始游戏时，打乱地图技能次数重置
	public void ResetResetMapToolNum() {
		this.ResetMapToolNum=3;
	}
	
	//**********以下没用上
	
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
