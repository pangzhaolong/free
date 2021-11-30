# 介绍

### 如何单独运行
```
1、根目录下gradle.properties 文件isBuildModule=true
```
### 如何查看在运行的进程
```
ps -A | grep com.donews.keepalive
```

### 如何真正杀死进程
```
am force-stop com.donews.keepalive
```
