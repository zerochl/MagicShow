# MagicShow
包含美颜等40余种实时滤镜相机，可拍照、图片修改 
图片编辑包含常规参数设置（对比度，饱和度等）、美颜（美白，磨皮）、滤镜
# 注意
此Demo暂时只修整了图片编辑部分，拍照部分还需要继续修改，图片常规设置FBO保存作者没有实现，使用了费时费力的方式后续会进行优化。
# 使用方式
MagicShowManager.getInstance().openEdit(MainActivity.this, new ImageEditCallBack() {
                    @Override
                    public void onCompentFinished(MagicShowResultEntity magicShowResultEntity) {
                        Log.e("HongLi","获取图片地址:" + magicShowResultEntity.getFilePath());
                    }
                });
提供了MagicShowManager方法，此方法暂时只支持图片编辑调用，回调里面会返回编辑成功之后的图片信息

作者联系方式：QQ：975804495
疯狂的程序员群：186305789，没准你能遇到绝影大神
