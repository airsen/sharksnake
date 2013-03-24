Shark Snake 贪食蛇多人版
============

## 启动

1. 安装base的jar包
    cd base & mvn clean install

2. 启动Server
    cd ../server & mvn jetty:run
   这个时候可以打开[Server:http://localhost:8080](http://localhost:8080)

3. 启动Client
    cd ../client & mvn jetty:run
   这个时候可以在网页的第一个框里面输入[Client:http://localhost:8081](http://localhost:8081)来调用客户端了

## 其他说明

1. 现在的Client的算法是很简单的，按照头部跟食物的横纵距离，先往x方向走，再往y方向走。
   如果要走的方向有障碍，就会先先走别的方向。

2. GameContext是整个游戏地图和上下文，各个参数可以参考这个类的源代码，在base的包里面。
   width        :场地的宽度
   height       :场地的长度
   count        :玩家数量
   alive        :存活的玩家数目
   food         :食物的横纵坐标
   snakeHeads   :所有蛇的头部位置
   snakeLengths :所有蛇的长度
   totalRound   :最多几个回合
   round        :当前地几个回合
   map          :地图的二维数组
   getTail()    :获取蛇的尾部
   ...