package 连连看;

import java.util.ArrayList;
/****
 * MapUtil类用于地图数据的建立，打乱
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
			map[i][j]=(int) (Math.random()*(n-2));//随机生成n-2种图片的ID;
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
	
	public int[][] getResetMap(){//获取再次打乱后的地图信息
		
		ArrayList<Integer> list = new ArrayList<Integer>();//list用来存储原先的地图信息
		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				list.add(map[i][j]);		
				map[i][j]=-1;
			}
		}
		
		//将原先地图上剩余的图片打乱
		while(!list.isEmpty()){
			
			int	index = (int) (Math.random()*list.size());//从list中随机取一个图片ID，并将其添加到数组中，再从list中删除掉它
			boolean flag = false;
			
			while(!flag){
				int i = (int) (Math.random()*n);//获取随机的地图行列
				int j = (int) (Math.random()*n);
				if(map[i][j]==-1){//如果该位置无图片
					map[i][j] = list.get(index);
					list.remove(index);
					flag = true;
				}	
			}
			
		}
		
		return map;
		
	}
	
}
