点击流日志实时分析系统：
- 包org.dan.storm Storm拓扑相关程序，从kafka中获取数据；
- 包org.dan.app  定时任务将pv/uv增量保存到mysql数据库中；
- 类org.dan.utils.LogKafkaSender模拟日志采集程序将日志数据写入kafka。

