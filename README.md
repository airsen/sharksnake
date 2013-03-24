Shark Snake 贪食蛇多人版
============

# 启动

1.安装base的jar包
 cd base & mvn clean install

2.启动Server
  cd ../server & mvn jetty:run
 这个时候可以打开[Server:http://localhost:8080](http://localhost:8080)

3.启动Client
  cd ../client & mvn jetty:run
 这个时候可以在网页的第一个框里面输入[Client:http://localhost:8081](http://localhost:8081)来调用客户端了
