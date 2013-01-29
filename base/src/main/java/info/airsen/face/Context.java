package info.airsen.face;

import lombok.Data;

import java.util.Map.Entry;

/**
 * <p>上下文模型类</p>
 *
 * @author airsen
 * @since 13-1-23 上午1:02
 */
@Data
public class Context {

	private int width; // 地图宽度
	private int height; // 地图高度

	private int count; // 玩家数目

	private Entry<Integer, Integer> food; // 食物位置

	private int[][] barrier; // 障碍物占用空间，0表示无障碍，1表示有障碍
	private int[][][] players; // 第几位玩家的占用空间

	private String key; // 同一个key的连接代表同一个场地
	private int round; // 第几回合，用来保证幂等性和保证上下文推理

	public Context() {
		this.width = 10;
		this.height = 10;
		this.count = 1;
		this.barrier = new int[width][height];
		this.players = new int[count][width][height];
	}

	public static void main(String[] args) {
		new Context();
	}


}
