# rpc-demo(rpc项目历史版本记录)

## Version1.0

- 最简单的Demo, 基于sockect+ ObjectOutputStream 实现网络传输和序列化， 对UserService对象紧耦合实现远程'getUserByUserId'服务调用。
- 真正的可用服务应当接口透明传输，利用动态代理包装成统一的服务供使用，包括协议应当也定制成统一格式。
  - 只能调用服务端Service唯一确定的方法，如果有两个方法需要调用呢?（Reuest需要抽象）
  - 返回值只支持User对象，如果需要传一个字符串或者一个Dog，String对象呢（Response需要抽象）
  - 客户端不够通用，host，port， 与调用的方法都特定（需要抽象）
- 其它未做：网络运输、负载均衡、线程优化...



## Version2.0

- 对Response，Request抽象， 同时设置ServiceManager, 以及抽象ServiceDesprition和serviceInstance（分别为描述信息和具体实例），使Rpc service在server端注册，并根据客户端发来的Request找到对应服务，最终生成JDK代理的对象供客户端解析使用。
- 该版本提升之处在于可以对多个服务代理。
- 程序不足之处在于：耦合度过高，尤其是Server；网络部分和序列化部分依赖于现有的工具, socket监听用的BIO方式，效率太低；监听结点应该有多个，并使用负载均衡策略处理客户端请求；每次连接Server新开一个线程，属于短连接，效率相对较低。

## Version3.0

- 对网络和序列/反序列化进行自定义编码。
- 客户端用URLConnection发起网络请求，具体动作在invoke代理中实现，最后返回代理对象供调用。除此以外，Selector选择使用的Client,达到负载均衡的效果。
- 服务器使用jetty监听处理请求，请求具体动作由自定义Servlet负责，逻辑包括在Handler接口中，交给实际RpcServer实现。ServiceManager通过map提供服务注册和发现。
- 坑点记录： 
  - map对键值需要重写equals和hashcode
  - 引用类型参数在序列化/反序列化后需转化类型，需要再次优化
  - 客户端连接未及时释放等等问题会导致错误

