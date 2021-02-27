package ������;

import java.util.ArrayList;
/****
 * MapUtil�����ڵ�ͼ���ݵĽ���������
 * @author ljz
 *
 */
public class MapUtil {

	int[][] map;
	int n;
	
	public MapUtil(int n){
		this.n = n;
		map = new int[n][n];
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				map[i][j] = -1;
			}
		}
	}
	
	public void initMap(){

		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
			map[i][j]=(int) (Math.random()*(n-2));//�������n-2��ͼƬ��ID;
			}
		}
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				System.out.print(map[i][j]+" ");
			}
			System.out.println();
		}
		
	}
	
	public int[][] getMap(){
		return map;
	}
	
	public int[][] getResetMap(){//��ȡ�ٴδ��Һ�ĵ�ͼ��Ϣ
		
		ArrayList<Integer> list = new ArrayList<Integer>();//list�����洢ԭ�ȵĵ�ͼ��Ϣ
		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				list.add(map[i][j]);		
				map[i][j]=-1;
			}
		}
		
		//��ԭ�ȵ�ͼ��ʣ���ͼƬ����
		while(!list.isEmpty()){
			
			int	index = (int) (Math.random()*list.size());//��list�����ȡһ��ͼƬID����������ӵ������У��ٴ�list��ɾ������
			boolean flag = false;
			
			while(!flag){
				int i = (int) (Math.random()*n);//��ȡ����ĵ�ͼ����
				int j = (int) (Math.random()*n);
				if(map[i][j]==-1){//�����λ����ͼƬ
					map[i][j] = list.get(index);
					list.remove(index);
					flag = true;
				}	
			}
			
		}
		
		return map;
		
	}
	
}
