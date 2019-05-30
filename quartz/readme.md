[TOC]

# Quartz API

- Scheduler: 用于与调度程序交互的主要API。
- Job: 由希望由调度程序执行的组件实现的接口。
- JobDetail: 用于定义job的实例。
- Trigger: 用于定义执行给定作业的计划的组件。
- JobBuilder: 用于构建/定义JobDetail的实例。
- TriggerBuilder: 用于构建/定义Trigger的实例。

一个job就是Job接口的实现类，Job接口只有一个方法。

当作业的触发器触发时，`execute()`方法会被Scheduler的一个工作线程调用。

`JobExecutionContext`对象提供了job的一些信息：它的运行时环境、执行它的Scheduler的句柄、触发执行的Trigger的句柄、job的Jobdetail的实例