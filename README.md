Im-server 1.0.2
===============

im-server底层使用netty框架通信，其主要特性包括：

 + NIO支持更大连接数
 + Redis缓存数据提高响应速度
 + 点对点即时通信
 + 支持群聊消息发送
 + 消息推送功能
 + 历史消息自定义sql存储
 + 用户信息多表支持及自定义sql获取用户信息

> im-server的运行环境要求jdk8以上，Maven项目管理工具。
### step 1
  此服务是java开发需要安装Jdk,Maven 并将其加入环境变量<br>
  修改本项目中conf.properties
  在项目目录下执行 `sh initServer.sh `<br>
  然后启动执行 `sh imServer.sh start`<br>
  停止服务请执行 `sh imServer.sh stop`  <br>
  第一次运行时必须先执行 `sh initServer.sh `，以后可以不执行<br>
  如果有需要修改jvm一些参数的话可以`vim imServer.sh` <br>
  ```bash
     JAVA_OPTS="-Xms256M -Xmx512M -Xmn256M"
  ```
```properties

#scocket 监听端口
im.server.port=7272 
#可以同时多少端登录聊天
im.client.num=3
#这是redis配置用缓存已经从数据库中取到的数据 开启redis可以提高响应速度
im.redis.host=192.168.3.201
#redis端口
im.redis.port=6379
#redis访问密码
im.redis.auth=
#使用redis中的哪个数据库
im.redis.default.db=1
#数据存放在redis个的集合名称
im.redis.user.cache.table=im:users
#jdbc连接数据库的配置 
im.jdbc.host=jdbc:mysql://localhost:3306/yangkun?characterEncoding=utf8&useSSL=false
#数据库用户名
im.jdbc.username=root
#数据库密码
im.jdbc.password=
#jdbc驱动
im.jdbc.driver=com.mysql.cj.jdbc.Driver
#如果是一个表来保存所有平台用户的话请 im.jdbc.s.user.info.sql 
#和 im.jdbc.t.user.info.sql 配置一样就行 im.jdbc.s.user.info.sql
#与im.jdbc.t.user.info.sql 设计为了后台用户与前台用户通信用的
im.jdbc.s.user.info.sql=SELECT id,user_name as username,head_img as face FROM hd_user WHERE id = %s
im.jdbc.t.user.info.sql=SELECT mid as id ,name as username,head_img as face FROM hd_analyst WHERE mid = %s
#发送的消息保存到数据库中的sql
im.jdbc.inser.msg.record.sql=INSERT INTO `hd_im_messages` \
  (`user_name`,`uid`,`touid`,`touname`,`room_id`,`content`,`type`,`stype`,`inputtime`,`role`,`head_img`,`status`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
#群聊时用户与其所在房间关联表
im.jdbc.room.sql=SELECT id,room_id as roomId,uid as userId,role FROM hd_room_user WHERE id = %s

  ```
 
### step 2
 发送消息体如上格式如下
 
>1 登录时发送
```json
    {
    "uid":"1474", 
    "touid":"117",
    "role":"s", 
    "type":"login"
    }
```
`uid` 当前用户id <br>
`touid`接收者用户id <br>
`role`当前用户角色 s=>普通用户,t=>后台用户，如果一个表存放所有用户的话就固定s<br>
`type` 消息类型 [login|say|logout|group]

>2发送点对点实时消息
```json
    {
    "uid":"1474",
    "touid":"117",
    "role":"s",
    "type":"say",
    "content":"hello",
    "stype":"0"
    }
```
`uid` 当前用户id <br>
`touid`接收者用户id <br>
`role`当前用户角色 s=>普通用户,t=>后台用户，如果一个表存放所有用户的话就固定s<br>
`content` 发送的内容<br>
`type` 消息类型 [login|say|logout|group]
`stype` 此参数保留，im-server不进行逻辑参与，可以随便给值

>3发送实时群聊消息
```json
    {
    "uid":"1474",
    "role":"s",
    "type":"group",
    "room_id":"8273f9938f1cca53cb8990604023800b",
    "content":"hello",
    "stype":"0"
    }
```
`uid` 当前用户id <br>
`role`当前用户角色 s=>普通用户,t=>后台用户，如果一个表存放所有用户的话就固定s<br>
`type` 消息类型 [login|say|logout|group]
>4退出时发送消息
```json
{
    "uid":"1474", 
    "touid":"117",
    "role":"s", 
    "type":"logout"
}
```
`uid` 当前用户id <br>
`touid`接收者用户id <br>
`role`当前用户角色 s=>普通用户,t=>后台用户，如果一个表存放所有用户的话就固定s<br>
`type` 消息类型 [login|say|logout|group]

>4接收到的消息体
```json
{
    "content":"HELLO",
    "head_img":"/Public/home/pic/tou2.jpg",
    "inputtime":"2018-06-07 10:21:23",
    "role":"t",
    "status":"SUCCESS",
    "stype":"0",
    "touid":1474,
    "type":"say",
    "uid":117,
    "user_name":"何老师"
}
```
`content` 发送的内容<br>
`head_img` 发送者的头像<br>
`inputtime`发送时间<br>
`status` 消息状态 [SUCCESS|FAIL]<br>
`uid` 发送者的用户id <br>
`touid`接收者用户id <br>
`user_name` 发送者的用户名
`role`当前用户角色 s=>普通用户,t=>后台用户，如果一个表存放所有用户的话就固定s<br>
`type` 消息类型 [login|say|logout]


### step 3
   查看服务是否启动 在项目目录下执行 jps 会看如下效果<br>
![view](http://www.fang99.cc/Public/upload/article/2018/06-07/5b189e28c9521.png)
### 在一项目中使用效果<br>
![user](http://www.fang99.cc/Public/upload/article/2018/06-07/5b189caa7b570.png)
![admin](http://www.fang99.cc/Public/upload/article/2018/06-07/5b189d583a274.png)


###  NOTE
   有问题可以加本人QQ或微信<br>
   QQ：1976215678 <br>
   WX：16602112169