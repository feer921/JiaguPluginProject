# 前置步骤

因为本插件未发布在 maven公共仓库上，仅作为学习、练习参考，所以本工程或者其他工程要使用的话，需要先将该插件发布到maven本地仓库，操作如下：

将本工程使用下载到(git)本地后，使用 AS打开并编译好后，打开AS上的 **gradle** 视图

![image-20230302194733743](https://github.com/feer921/JiaguPluginProject/blob/main/image-20230302194733743.png)

可以看到 publishing组的相关任务，我们双击 publishJiaguPluginPublicationToMavenLocal 任务，将本插件发布到当前电脑的 maven 本地仓库中，以便接下来依赖使用。

# 使用

1. 在工程/项目的根目录下的 *build.gradle*文件中配置如下代码：

```groovy
buildscript {
    ext.kotlin_version = "1.8.0"
    repositories {
        google()
        mavenCentral()
        mavenLocal() //增加本地 maven仓库
    }
    dependencies {
        classpath "com.fee.jiaguplugin:jiagu:1.2"//因为该插件发布在本地仓库
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal() //增加本地 maven仓库
    }
}
```

2. 再在 app模块目录下的 *build.gradle*文件中增加对该插件的依赖使用,如下：

   ```groovy
   plugins {
       id 'com.android.application'
       id 'kotlin-android'
       id 'com.fee.jiagu'//引用 自定义的插件 
   }
   ```

3. 并且仍然在app模块目录下的*build.gradle* 文件中配置自己的 360加固平台的账号、密码、签名信息

   ```groovy
   jiagu {
       platformAccount accountStr //如：360加固保平台的账号
       platformAccountPw accountPwStr //如：360加固保平台账号的密码
       platformJarFilePath 'D:\\Program Files (x86)\\jiagu360\\jiagu\\jiagu.jar' //360
       jiaguOutputDirPath 'D:\\outputs\\test'
       keyStoreFilePath keyStoreFile//配置自己的签名文件路径
       keyStorePw storePassWordStr// Key Store库密钥
       keyStoreKeyAlias keyAliasStr //Key Store 别名
       keyStoreKeyPw keyPassWordStr //Key的密钥
       multyChannelsFilePath 'D:\\outputs\\test\\channels-copy.txt' //多渠道包的配置文件
   }
   ```

在上面的配置代码后，AS会提示需要让 **gradle** 对项目进行同步，在同步完成后，我们打开 **gradle** 视图面板，即会出现 jiagu组的 两个任务，双击任何一个任务即可对构建出的 **APK** 包进行加固、签名、多渠道

![image-20230302193415896](https://github.com/feer921/JiaguPluginProject/blob/main/image-20230302193415896.png)

# 注

本项目仅作为练习、参考如何编写 **gradle** 插件项目，不保证真实环境下的360加固保可正确执行，具体可以参考其官网的使用文档。