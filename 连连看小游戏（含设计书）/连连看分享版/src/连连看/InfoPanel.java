package 连连看;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
/**
 * HelpPanel类用于游戏界面上方信息栏信息的提示和更新（当前分数，历史最高，时间倒计时）
 * @author ljz
 *
 */
public class InfoPanel extends JPanel{

	public static JLabel LabScore=new JLabel("当前分数：");
	public static JLabel LabRecord=new JLabel("历史最高：");
	public static JLabel LabTime=new JLabel("时间：");
	
	public static JTextField score = new JTextField(10);
	public static JTextField record = new JTextField(10);
	public static JProgressBar progress = new JProgressBar();
	
	 
	int nowScore = 0;
	int bestScore = 0;
	int time = 0;
	FileInputStream fis = null;
	FileOutputStream fos = null;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	
	public InfoPanel(){
		setLayout(new FlowLayout());
		progress.setMaximum(20);//设置倒计时时间20s
		score.setEditable(false);
		LabScore.setFont(new  java.awt.Font("幼圆",  1,  15)); 
		LabRecord.setFont(new  java.awt.Font("幼圆",  1,  15)); 
		LabTime.setFont(new  java.awt.Font("幼圆",  1,  15)); 
		score.setFont(new  java.awt.Font("幼圆",  1,  15)); 
		record.setFont(new  java.awt.Font("幼圆",  1,  15)); 
		record.setEditable(false);
		//record.setBackground(Color.CYAN);
		this.add(LabScore);
		this.add(score);
		this.add(LabRecord);
		this.add(record);
		this.add(LabTime);
		this.add(progress);	
		
		this.setBackground(new Color(255,228,225));
		record.setText(getBestScore()+"");		
	}
	
	
	//设置当前分数
	public void setNowScore(int now_score){
		nowScore = now_score;
		score.setText(nowScore+"");
	}

	//读取历史记录
	public int getBestScore(){
		File file = new File("src//record");
		
		if(!file.exists()){
			file.mkdirs();
		}
		File record = new File("src//record//record.txt");

		try{
		if(!record.exists()){//如果不存在,新建文本
			record.createNewFile();
			fos = new FileOutputStream(record);
			dos = new DataOutputStream(fos);
			String s = "0";
			dos.writeBytes(s);
		}
		//读取记录
		fis = new FileInputStream(record);
		dis = new DataInputStream(fis);
		String str = dis.readLine();
		bestScore = Integer.parseInt(str);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			 try {
					if(fis!=null)
					 fis.close();
					if(dis!=null)
					 dis.close();			
					if(fos!=null)
			    	 fos.close();
					if(dos!=null)
					 dos.close();				
			     } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
			
		return bestScore;
	}
	
	//更新历史记录
	public void updateBestScore(){
		File record = new File("src//record//record.txt");
		
		try {
			//清空原有记录
			FileWriter fileWriter =new FileWriter(record);
	        fileWriter.write("");
			fileWriter.flush();
			fileWriter.close();
	        //重新写入文本
			fos = new FileOutputStream(record);
			dos = new DataOutputStream(fos);
			String s = score.getText();
			bestScore = Integer.parseInt(score.getText());
			dos.writeBytes(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		     try {
				if(fos!=null)
		    	 fos.close();
				if(dos!=null)
				 dos.close();				
		     } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		       
		}
        InfoPanel.record.setText(bestScore+"");
	}
	
	public static void setTime(int time){
		//System.out.println("currentProcess"+currentProcess);
		progress.setValue(time);	
	}
	
	
}
