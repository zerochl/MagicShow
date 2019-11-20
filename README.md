### 作者联系方式：QQ：975804495
### 疯狂的程序员群：186305789，没准你能遇到绝影大神
### 作者个人兴趣网站，[zero接码平台](https://xinghai.party)
### 作者个人兴趣网站，[猿指](https://blog.xinghai.party)
# MagicShow
包含美颜等40余种实时滤镜相机，可拍照、图片修改 
图片编辑包含常规参数设置（对比度，饱和度等）、美颜（美白，磨皮）、滤镜
# Demo说明
本demo并不包含相册功能，所以demo中打开Album请预先在SD卡中存储如下图片：/sdcard/DCIM/dark.jpg
# 集成方法
```java
dependencies {
    implementation 'com.github.zerochl:MagicShow:1.0.4'
}
```
# 使用方式
```java
MagicShowManager.getInstance().openEdit(activity,imagePath, new ImageEditCallBack() {
    @Override
    public void onCompentFinished(MagicShowResultEntity magicShowResultEntity) {
        Log.e("HongLi","获取图片地址:" + magicShowResultEntity.getFilePath());
    }
});
```
    
打开拍照，拍照之后直接进图片编辑
```java
MagicShowManager.getInstance().openCameraAndEdit(activity,new ImageEditCallBack(){
    @Override
    public void onCompentFinished(MagicShowResultEntity magicShowResultEntity) {

    }
});
```
    
打开拍照,拍照实现了滤镜效果，CameraConfig可设置拍照图片的大小
```java
MagicShowManager.getInstance().openCamera(activity,new CameraShootCallBack(){
    @Override
    public void onCompentFinished(MagicShowResultEntity magicShowResultEntity) {

    }
});
```
    
提供了MagicShowManager方法，回调里面会返回编辑成功之后的图片信息

