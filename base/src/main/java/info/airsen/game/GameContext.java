package info.airsen.game;

import info.airsen.common.RandomUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p></p>
 *
 * @author airsen
 * @since 13-1-25 下午11:26
 */
@Data
public class GameContext {

	// 方向常量
	public static final int DIR_UP = 1;
	public static final int DIR_RIGHT = 2;
	public static final int DIR_DOWN = 3;
	public static final int DIR_LEFT = 4;

	// 物体常量
	public static final int FOOD = 1;

	// 该场游戏的标识
	private String key;

	// 地图的宽度和高度
	private int width; // 地图宽度
	private int height; // 地图高度

	// 玩家数量（蛇的数量）
	private int count; // 玩家数目
	private int alive; // 存活的玩家数目

	private Coordinate food; // 食物位置
	private Coordinate[] snakeHeads; // 蛇头的位置
	private int[] snakeLengths; // 蛇的长度

	private int totalRound; // 总共几个回合
	private int round; // 第几回合，用来保证幂等性和保证上下文推理


	/**
	 * <p>整个地图，0表示可以通过，-1表示不能通过，1表示食物，其他数字表示各自蛇的占用空间，个位数表示下一格的方向</p>
	 * <ul>
	 * <li>1:DIR_UP</li>
	 * <li>2:RIGHT</li>
	 * <li>3:DOWN</li>
	 * <li>4:LEFT</li>
	 * </ul>
	 * <p>比方说11表示第1条蛇的蛇身并且下一节在右边(X+1)</p>
	 */
	private int[][] map;

	private List<Coordinate> increMap;

	public GameContext() {
		width = 10;
		height = 10;
		count = 1;
		round = -1;
		totalRound = 50;
		key = UUID.randomUUID().toString();
		init();
	}


	/**
	 * <p>设置食物位置</p>
	 *
	 * @param newFood 新食物的位置
	 */
	public void setFood(Coordinate newFood) {
		clearValue(food);
		setValue(newFood, FOOD);
	}

	public List<Coordinate> popIncreMap() {
		List<Coordinate> result = this.increMap;
		this.increMap = new ArrayList<Coordinate>();
		return result;
	}

	private void setValue(Coordinate xy, int value) {
		map[xy.getX()][xy.getY()] = value;
		increMap.add(new Coordinate(xy.getX(), xy.getY(), value));
	}

	private void clearValue(Coordinate xy) {
		setValue(xy, 0);
	}

	private int getValue(int x, int y) {
		return map[x][y];
	}

	private int getValue(Coordinate xy) {
		return map[xy.getX()][xy.getY()];
	}

	/**
	 * <p>需要有width、height和count</p>
	 */
	public void init() {
		map = new int[width][height];
		alive = count > 1 ? count : 2;
		snakeLengths = new int[count + 1];
		for (int snakeLength : snakeLengths) {
			snakeLength = 1;
		}
		increMap = new ArrayList<Coordinate>();

		// 初始化食物
		food = RandomUtil.getRandomCoordinate(width, height);
		setValue(food, 1);

		// 初始化玩家蛇
		snakeHeads = new Coordinate[count + 1]; // 一楼给度娘
		for (int i = 1; i <= count; i++) {
			snakeHeads[i] = getRandomNotExistXY();
			setValue(snakeHeads[i], 10 * i);
		}
	}

	public void killSnake(int which) {
		Coordinate temp1 = snakeHeads[which];
		Coordinate temp2;
		while (temp1 != null) {
			temp2 = getNext(temp1);
			clearValue(temp1);
			temp1 = temp2;
		}
		snakeHeads[which] = null;
		snakeLengths[which] = 0;
		--alive;
	}

	/**
	 * <p>包装了判断逻辑的nextRound</p>
	 *
	 * @param directions 玩家们的决策方向，记得空出一楼给度娘
	 * @return 是否能继续玩
	 */
	public boolean nextRound(int[] directions) {
		if (++round >= totalRound)
			return false;

		List<Integer> deathQueue = new ArrayList<Integer>(); // 死亡的队列
		List<Integer> longerQueue = new ArrayList<Integer>(); // 吃到食物变长的队列
		List<Integer> normalQueue = new ArrayList<Integer>(); // 正常队列

		// 1，擦除所有尾巴
		moveTail();

		// 2，移动头部
		// 2.1，记录头部
		List<Coordinate> fetureSnakeHeadList = new ArrayList<Coordinate>();
		fetureSnakeHeadList.add(null);
		for (int i = 1; i <= count; i++) {
			fetureSnakeHeadList.add(getNextHead(i, directions[i]));
		}
		// 2.2，要判断是否重叠，或者碰到障碍，或者吃到东西
		for (int i = 1; i <= count; i++) {
			Coordinate currSnake = fetureSnakeHeadList.get(i);
			if (snakeHeads[i] == null)
				continue;
			if (fetureSnakeHeadList.get(i) == null)
				deathQueue.add(i);
			else if (isDuplicate(i, fetureSnakeHeadList)) // 跟其他蛇头相撞了，拜了个拜
				deathQueue.add(i);
			else if (getValue(currSnake) == 1)
				longerQueue.add(i);
			else if (getValue(currSnake) == 0)
				normalQueue.add(i);
			else // 已撞墙，勿念
				deathQueue.add(i);

		}
		// 3，开始处理后事
		// 3.1，擦除死亡的玩家
		for (Integer whichSnake : deathQueue)
			killSnake(whichSnake);

		// 3.2，给吃到食物的蛇们发福利
		for (Integer whichSnake : longerQueue) {
			moveHead(whichSnake, fetureSnakeHeadList.get(whichSnake), directions[whichSnake]);
			haveFood(whichSnake);
			snakeLengths[whichSnake]++;
		}
		// 3.3，给正常的蛇们开绿灯
		for (Integer whichSnake : normalQueue) {
			moveHead(whichSnake, fetureSnakeHeadList.get(whichSnake), directions[whichSnake]);
		}

		return alive > 1;

	}

	public int getWinner() {
		return 0;
	}

	/**
	 * <p>移动蛇的第一步，移动蛇尾，从地图上消失蛇尾</p>
	 * <p>传入蛇的序号，就可以消掉蛇尾，改变地图</p>
	 */
	public void moveTail() {
		for (int i = 1; i <= count; i++) {
			moveTail(i);
		}
	}

	public void moveTail(int whichSnake) {
		Coordinate tail = getTail(whichSnake);
		if (tail != null) // 如果拿不到tail表示这条蛇可能已经死了，死了，了…
			clearValue(tail);
	}

	/**
	 * <p>移动蛇的第二步，获得要移动的蛇头的位置</p>
	 * <p>传入蛇的序号和要走的方向，就会改变地图</p>
	 *
	 * @param whichSnake 蛇的序号
	 * @param direction  方向
	 * @return 返回蛇头的位置，如果撞上障碍物了返回null
	 */
	public Coordinate getNextHead(int whichSnake, int direction) {
		if (snakeHeads[whichSnake] == null)
			return null;
		int x = snakeHeads[whichSnake].getX();
		int y = snakeHeads[whichSnake].getY();
		int _x = 0;
		int _y = 0;
		switch (direction) {
			case DIR_UP:
				_y = 1;
				break;
			case DIR_RIGHT:
				_x = 1;
				break;
			case DIR_DOWN:
				_y = -1;
				break;
			case DIR_LEFT:
				_x = -1;
				break;
			default:
				return null;
		}
		x += _x;
		y += _y;
		if (x < 0 || x >= width || y < 0 || y >= height)
			return null;
		Coordinate willMove = new Coordinate(x, y);
		if (willMove.equals(getNext(snakeHeads[whichSnake])))
			return null;
		return willMove;
	}

	public void moveHead(int whichSnake, Coordinate newHead, int direction) {
		setValue(newHead, 10 * whichSnake + getReverseDirection(direction));
		snakeHeads[whichSnake] = newHead;
	}

	/**
	 * <p>输入哪条蛇，执行吃了食物增长的过程，并且重新更新食物位置</p>
	 *
	 * @param whichSnake 蛇的序号
	 * @return 是否成功
	 */
	public boolean haveFood(int whichSnake) {
		Coordinate tail = getTail(whichSnake);
		Coordinate newTail = getNext(tail);
		setValue(newTail, 10 * whichSnake);
		food = getRandomNotExistXY();
		if (food == null)
			return false;
		setValue(food, 1);
		return true;
	}

	private boolean isDuplicate(int it, List<Coordinate> list) {
		Coordinate xy = list.get(it);
		for (int i = 0; i < list.size(); i++) {
			if (it == i || list.get(i) == null) continue;
			if (xy.equals(list.get(i)))
				return true;
		}
		return false;
	}


	/**
	 * <p>给我一个头，还你一个尾</p>
	 *
	 * @param whichSnake 第几头蛇
	 * @return 蛇尾坐标
	 */
	private Coordinate getTail(int whichSnake) {
		Coordinate head = snakeHeads[whichSnake];
		if (head == null) // 已经逝去…致敬
			return null;
		Coordinate current, next = head;
		do {
			current = next;
			next = getNext(current);
		} while (checkSameBody(current, next) && !next.equals(head));
		return current;
	}


	/**
	 * <p>取得方向的反转</p>
	 *
	 * @param direction 方向
	 * @return 反转方向
	 */
	private int getReverseDirection(int direction) {
		switch (direction) {
			case DIR_UP:
				return DIR_DOWN;
			case DIR_RIGHT:
				return DIR_LEFT;
			case DIR_DOWN:
				return DIR_UP;
			case DIR_LEFT:
				return DIR_RIGHT;
			default:
				return -1;
		}
	}


	/**
	 * <p>取身体的下一节的坐标</p>
	 *
	 * @param bodyUnit 某节身体的坐标
	 * @return 返回下一节坐标
	 */
	private Coordinate getNext(Coordinate bodyUnit) {
		int x = bodyUnit.getX();
		int y = bodyUnit.getY();
		int _x = 0;
		int _y = 0;
		switch (getValue(x, y) % 10) { // 模10，取个位数
			case DIR_UP: // 向上走
				_y = 1;
				break;
			case DIR_RIGHT: // 向右走
				_x = 1;
				break;
			case DIR_DOWN: // 向下走
				_y = -1;
				break;
			case DIR_LEFT: // 向左走
				_x = -1;
				break;
			default:
				return null;
		}
		return new Coordinate(x + _x, y + _y);
	}

	private boolean checkSameBody(Coordinate head, Coordinate next) {
		return !(next == null || getValue(head) / 10 == 0) && getValue(head) / 10 == getValue(next) / 10;
	}

	/**
	 * <p>返回随机一个坐标并且该坐标上面没有东西</p>
	 *
	 * @return 坐标
	 */
	private Coordinate getRandomNotExistXY() {
		int a = 0;
		for (int snakeLength : snakeLengths) {
			a += snakeLength;
		}
		if (a >= width * height)
			return null;
		Coordinate result;
		do {
			result = RandomUtil.getRandomCoordinate(width, height);
		} while (getValue(result) != 0);
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = height - 1; i >= 0; i--) {
			for (int j = 0; j < width; j++)
				builder.append(getValue(j, i) == 0 ? "_" : getValue(j, i)).append('\t');
			builder.append('\n');
		}
		return builder.toString();
	}

//	public static void main(String[] args) throws IOException {
//		GameContext gameContext = new GameContext();
//		gameContext.setCount(1);
//		gameContext.setTotalRound(100);
//		gameContext.setWidth(6);
//		gameContext.setHeight(6);
//		gameContext.init();
//		LOGGER.info("初始化以后的界面如下：" + "\n" + gameContext + "===================");
//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//		String input;
//		int dir = 0;
//		boolean cont = true;
//		while (cont && StringUtils.isNotBlank(input = reader.readLine())) {
//			switch (input) {
//				case "w":
//					dir = DIR_UP;
//					break;
//				case "d":
//					dir = DIR_RIGHT;
//					break;
//				case "s":
//					dir = DIR_DOWN;
//					break;
//				case "a":
//					dir = DIR_LEFT;
//					break;
//				default:
//					break;
//			}
//			cont = gameContext.nextRound(new int[]{0, dir});
//			LOGGER.info("界面如下：" + "\n" + gameContext + "===================");
//		}
//	}

}
