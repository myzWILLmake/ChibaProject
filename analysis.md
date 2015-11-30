#APP模块分析

##数据结构
###用户 User
这里列出全部可能用到的的数据类型，实际程序中可能只需要定义一部分，剩下的数据在将来可以通过向服务器数据库随用随调

|数据 | 类型|
|---|---|
|id|string|
|password | string|
|phone number| string|
|nickname | string | 
|location | Location(待定,根据地图API来定？)|
|eventPartIn[]|Event[]|


### 活动 Event
|数据| 类型|
|---|---|
|id | string|
|manager| User|
|time| Time|
|location| Location|
|members[]| User[]|
|state | int (这里定义活动状态 发起中-进行中-已结束-已取消)|
|decision|Decision|

### 决策讨论 Decision
|数据| 类型|
|---|---|
|eventId | string|
(这一部分再议，整体先搭建起来再说)

## 各个页面
### 个人页面 （界面未设计）
- 个人信息
- 好友列表
- 参加的活动
- 设置选项

### 主界面
全部活动 好友及自己发起的活动

参与活动 用户目前参与的活动

使用活动卡片展示一个个活动

- 需要活动信息概况
- 时间、发起人、文字地点信息
- 卡片背景？

### 活动发起界面 （未设计）

### 活动页面
这个页面分不同版本，一般用户预览版本/参与者版本/管理者版本

活动详情

- 发起人
- 时间
- 地点
- 参与者列表
- 决策讨论区


	