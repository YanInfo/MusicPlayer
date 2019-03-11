

## :coffee: MusicPlayer
### 1 读取手机本地音频，用ListView展示
### 2 上一首，下一首，播放功能以实现
### 3 顺序播放，随机播放，单曲循环功能
### 4 歌名文字跑马灯效果
### 5 歌曲图片在开始播放时匀速旋转

******
## :coffee: EPlayer
在MusicPlayer的基础上
### 1 加入了Service，来控制歌曲播放和通知栏
### 2 通知栏能进行后台播放和控制歌曲
### 3 代码进行适当的封装
(#### 1 抽象出了Controller层，主要做一些数据处理；
#### 2 Service层主要用于完成服务的判断，然后调用Controller对应的方法实现业务功能
#### 3 界面优化，代码最大程度的精简化)
