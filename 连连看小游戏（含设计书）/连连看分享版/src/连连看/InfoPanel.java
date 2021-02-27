package ������;

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
 * HelpPanel��������Ϸ�����Ϸ���Ϣ����Ϣ����ʾ�͸��£���ǰ��������ʷ��ߣ�ʱ�䵹��ʱ��
 * @author ljz
 *
 */
public class InfoPanel extends JPanel{

	public static JLabel LabScore=new JLabel("��ǰ������");
	public static JLabel LabRecord=new JLabel("��ʷ��ߣ�");
	public static JLabel LabTime=new JLabel("ʱ�䣺");
	
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
		progress.setMaximum(20);//���õ���ʱʱ��20s
		score.setEditable(false);
		LabScore.setFont(new  java.awt.Font("��Բ",  1,  15)); 
		LabRecord.setFont(new  java.awt.Font("��Բ",  1,  15)); 
		LabTime.setFont(new  java.awt.Font("��Բ",  1,  15)); 
		score.setFont(new  java.awt.Font("��Բ",  1,  15)); 
		record.setFont(new  java.awt.Font("��Բ",  1,  15)); 
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
	
	
	//���õ�ǰ����
	public void setNowScore(int now_score){
		nowScore = now_score;
		score.setText(nowScore+"");
	}

	//��ȡ��ʷ��¼
	public int getBestScore(){
		File file = new File("src//record");
		
		if(!file.exists()){
			file.mkdirs();
		}
		File record = new File("src//record//record.txt");

		try{
		if(!record.exists()){//���������,�½��ı�
			record.createNewFile();
			fos = new FileOutputStream(record);
			dos = new DataOutputStream(fos);
			String s = "0";
			dos.writeBytes(s);
		}
		//��ȡ��¼
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
	
	//������ʷ��¼
	public void updateBestScore(){
		File record = new File("src//record//record.txt");
		
		try {
			//���ԭ�м�¼
			FileWriter fileWriter =new FileWriter(record);
	        fileWriter.write("");
			fileWriter.flush();
			fileWriter.close();
	        //����д���ı�
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
