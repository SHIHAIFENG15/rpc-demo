# rpc-demo(rpc项目历史版本记录)

## version1.0

- 最简单的Demo, 基于sockect+ ObjectOutputStream 实现网络传输和序列化， 对UserService对象紧耦合实现远程'getUserByUserId'服务调用。
- 真正的可用服务应当接口透明传输，利用动态代理包装成统一的服务供使用，包括协议应当也定制成统一格式。
  - 只能调用服务端Service唯一确定的方法，如果有两个方法需要调用呢?（Reuest需要抽象）
  - 返回值只支持User对象，如果需要传一个字符串或者一个Dog，String对象呢（Response需要抽象）
  - 客户端不够通用，host，port， 与调用的方法都特定（需要抽象）
- 其它未做：网络运输、负载均衡、线程优化...

