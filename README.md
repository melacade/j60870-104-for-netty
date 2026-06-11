# one_zero_four

`one_zero_four` 是一个基于 Netty 的 IEC 60870-5-104 协议实现。该项目提供客户端和服务器端基础结构、104 报文编码/解码、以及可扩展的消息处理接口。

## 功能

- IEC 60870-5-104 客户端和服务器支持
- Netty 管道中 `FrameCat` + `ApduDecoder` + `ApduEncoder` 的报文处理
- 104 报文事件和状态管理
- 可扩展的 `MessageHandler` 处理逻辑

## 快速开始

### 1. 本地打包

```bash
mvn clean package
```

### 2. 运行客户端示例

```java
Client client = Client.builder().build("127.0.0.1");
client.start();
client.sendStartDt();
```

### 3. 运行服务器示例

```java
Server server = Server.builder().setPort(2404).build();
server.start();
```

## 主要类说明

- `com.melody.j60870.Client`
- `com.melody.j60870.Server`
- `com.melody.j60870.datapack.decode.FrameCat`
- `com.melody.j60870.datapack.decode.ApduDecoder`
- `com.melody.j60870.datapack.encode.ApduEncoder`
- `com.melody.j60870.datapack.message.MessageHandler`

## MessageHandler 扩展点

你可以实现 `com.melody.j60870.datapack.message.MessageHandler`，在客户端和服务器端分别处理来自对端的 `APduNetty` 报文：

- `toServer(APduNetty netty, ChannelHandlerContext ctx)`
- `toClient(APduNetty netty, ChannelHandlerContext ctx)`

同样需要实现 `register()` 方法，用于注册对应的 104 报文类型处理函数。

## 依赖说明

当前 `pom.xml` 中包含：

- `org.openmuc:j60870:1.6.1`
- `io.netty:netty-all:4.1.94.Final`（可选依赖）
- `org.projectlombok:lombok:1.18.26`

> 注意：`netty-all` 在本项目中被设置为可选依赖。如果你把本项目作为库引用，请在目标项目中显式声明你希望使用的 Netty 版本，以避免版本冲突。

### 目标项目引用示例

```xml
<dependency>
  <groupId>com.sac</groupId>
  <artifactId>one_zero_four</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>

<dependency>
  <groupId>io.netty</groupId>
  <artifactId>netty-all</artifactId>
  <version>4.1.94.Final</version>
</dependency>
```

## 编译与运行要求

- Java 8
- Maven 3.x

## 说明

该项目主要用于演示和扩展 IEC 60870-5-104 协议处理逻辑。

你可以直接使用现有客户端/服务器框架，也可以只使用 `ApduEncoder` 和 `ApduDecoder` 来解析/生成 104 数据：

- `ApduDecoder`：将 Netty `ByteBuf` 中的 104 帧解析为 `APduNetty` Java 对象
- `ApduEncoder`：将 `APduNetty` Java 对象编码为 Netty `ByteBuf`

这种用法适合：

- 仅解析报文数据，不使用完整 Netty 通道逻辑
- 自定义自己的 Netty pipeline，仅嵌入编码/解码器
- 将 104 数据转换为业务对象，或将业务对象转换为 104 帧发送

例如：

```java
// 从字节流解析为 APduNetty
ByteBuf in = Unpooled.wrappedBuffer(rawBytes);
FrameCat frameCat = new FrameCat();
// 如果需要先做帧拆分，建议使用 FrameCat 将完整帧切出后，再用 ApduDecoder 解析

// 直接使用 ApduDecoder 的典型流程：
ApduDecoder decoder = new ApduDecoder(new ConnectionNettySettings());
List<Object> out = new ArrayList<>();
decoder.decode(ctx, in, out);
APduNetty apdu = (APduNetty) out.get(0);

// 将 APduNetty 编码为字节流
ApduEncoder encoder = new ApduEncoder(new ConnectionNettySettings());
ByteBuf outBuf = Unpooled.buffer();
encoder.encode(ctx, apdu, outBuf);
byte[] bytes = new byte[outBuf.readableBytes()];
outBuf.readBytes(bytes);
```

## 参考

- `org.openmuc:j60870`

