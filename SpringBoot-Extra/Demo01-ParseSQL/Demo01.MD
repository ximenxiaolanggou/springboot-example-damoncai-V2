# SQL文件解析
- 包含MySQL和SQLServer
- 支持单个表文件解析和整个数据库文件解析
- 可以自定义一个数据库属性bean对象，用于封装结果
- 采用BufferReader 缓冲流读取数据，可以通过上传文件获取字节流再通过 InputStreamReader 将字节流转成字符流