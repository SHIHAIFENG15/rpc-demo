# rpc-demo(rpc项目历史版本记录)

## version1.0

- 最简单的Demo, 基于sockect+ ObjectOutputStream 实现网络传输和序列化， 对UserService对象紧耦合实现远程'getUserByUserId'服务调用。
- 真正的可用服务应当接口透明传输，利用动态代理包装成统一的服务供使用，包括协议应当也定制成统一格式。
- 其它未做：网络运输、负载均衡、线程优化...

