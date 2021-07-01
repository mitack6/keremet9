# XPopup
等做好再写！

实现一个通用的，集PopupMenu和Dialog于一体
## 功能
1. 中间弹出对话框
2. 底部上推动画框
3. 可以像PopupMenu那样指定弹出位置
4. 可以自定义内容View
5. 默认支持返回键关闭和点击外部消失
6. 支持自定义动画
7. 支持手势拖动操作，像BottomSheet那样

## 动画
1. 支持背景明暗渐变，甚至局部的明暗渐变
2. 支持缩放，上下，左右动画，参考系统PopupMenu实现


## 分析
1. 需要一个全屏幕透明Window，可以在上面策马崩腾
2. Window内的容器，本质上弹窗的位置就是容器的位置，弹窗的动画就是容器的动画
3. 不能用Dialog做，虽然可以将Dialog变成全屏透明，但是Dialog默认有动画时间，消失的时候会延迟
4. 自定义Window实现会更加灵活