# netty-sebscoket-server

>1
 修改本项目中redis的配置在工具类JredisUtils中的hostRedis
 
>2
 自行解压运行  WeChatServer 
 
>3
 把client.zip下载至本地php运行环境
 
>4
 发送消息体如上格式
```javascript
 var msgArray={
      "fromUser":xxx,//这条消息是谁发送的
			"fromToken":xxxx,// 这条消息发送者token token 完全自己定义，在登录时发送是聊天服务器，让聊天服务器记录你的token
			"toUser":xxx,// 这条消息是接收者的名称
			"toToken":xxx,// 这条消息接收者token token 完全接收者定义，在登录时接收者发送是聊天服务器，让聊天服务器记录接收者token
			"message":"我是消息",//
			"type":"LOGIN" //
		}
```
  其中 type 为 `LOGIN` | `MSGBODY` | `LEAVE` 分别代表 登录时发送的消息，普通的点对点消息，退出时发送的消息
