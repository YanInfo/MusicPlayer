

## :coffee: MusicPlayer
### 1 读取手机本地音频，用ListView展示
### 2 上一首，下一首，播放功能以实现
### 3 顺序播放，随机播放，单曲循环功能
### 4 歌名文字跑马灯效果
### 5 歌曲图片在开始播放时匀速旋转
![Image text](https://github.com/YanInfo/MusicPlayer/blob/master/EPlayer/app/picture/musicplayer.gif)

******
## :coffee: EPlayer
在MusicPlayer的基础上
### 1 加入了Service，来控制歌曲播放和通知栏
### 2 添加了进度条来显示歌曲播放进度，和歌曲时间
### 3 通知栏能进行后台播放和控制歌曲
### 4 加入了Material Design的UI设计（有Toobar标题栏，NavigatioView滑动菜单栏，右上角退出功能）
### 5 ListView进行了缓存优化

(
#### 1 采用了MVP代码结构；
#### 1 抽象出了Controller层，主要做一些数据处理；
#### 2 Service层主要用于完成服务的判断，然后调用Controller对应的方法实现业务功能；
#### 3 所有的字符串和常量，颜色等都进行了统一管理（新建类或者在配置文件里写）
#### 3 界面优化，代码最大程度的精简化。代码逻辑简洁。
)
