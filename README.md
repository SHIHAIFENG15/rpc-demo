# rpc-demo(rpc项目历史版本记录)

## version1.0

- 最简单的Demo, 基于sockect+ ObjectOutputStream 实现网络传输和序列化， 对UserService对象紧耦合实现远程'getUserByUserId'服务调用。
- 真正的可用服务应当接口透明传输，利用动态代理包装成统一的服务供使用，包括协议应当也定制成统一格式。
  - 只能调用服务端Service唯一确定的方法，如果有两个方法需要调用呢?（Reuest需要抽象）
  - 返回值只支持User对象，如果需要传一个字符串或者一个Dog，String对象呢（Response需要抽象）
  - 客户端不够通用，host，port， 与调用的方法都特定（需要抽象）
- 其它未做：网络运输、负载均衡、线程优化...



## version2.0

- 对Response，Request抽象， 同时设置ServiceManager, 以及抽象ServiceDesprition和serviceInstance（分别为描述信息和具体实例），使Rpc service在server端注册，并根据客户端发来的Request找到对应服务，最终生成JDK代理的对象供客户端解析使用。
- 该版本提升之处在于可以对多个服务代理。
- 程序不足之处在于：耦合度过高，尤其是Server；网络部分和序列化部分依赖于现有的工具, socket监听用的BIO方式，效率太低；监听结点应该有多个，并使用负载均衡策略处理客户端请求。

