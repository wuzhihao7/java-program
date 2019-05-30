[TOC]

# Quartz API

- Scheduler: 用于与调度程序交互的主要API。
- Job: 由希望由调度程序执行的组件实现的接口。
- JobDetail: 用于定义job的实例。
- Trigger: 用于定义执行给定作业的计划的组件。
- JobBuilder: 用于构建/定义JobDetail的实例。
- TriggerBuilder: 用于构建/定义Trigger的实例。

一个任务就是Job接口的实现类，Job接口只有一个方法。

当任务的触发器触发时，`execute()`方法会被调度器的一个工作线程调用。

`JobExecutionContext`实例提供了任务的一些信息：执行它的调度器的句柄、触发执行的触发器的句柄、任务的Jobdetail的实例

`JobDetail`实例在客户端程序将任务加到调度器时被创建。它包含了任务的各种属性设置，以及一个可以存储给定任务实例的状态信息。它本质是任务实例的定义。

`Trigger`用来触发任务的执行。当你希望调度任务时，可以实例化触发器并调整其属性以提供你希望的调度。触发器也可能有一个与之关联的JobDataMap - 这对于将参数传递给特定于触发器触发的任务非常有用。





