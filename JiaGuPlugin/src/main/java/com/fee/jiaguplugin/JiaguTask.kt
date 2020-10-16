package com.fee.jiaguplugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

/**
 * ******************(^_^)***********************<br>
 * User: fee(QQ/WeiXin:1176610771)<br>
 * Date: 2020/10/18<br>
 * Time: 20:04<br>
 * <P>DESC:
 * 真正执行加固的任务
 * </p>
 * ******************(^_^)***********************
 */
// 如果不写成 open类会报错： Could not create task of type 'JiaguTask'.
//      > Class JiaguTask is final.
/* 注意，因为这里是使用Kotlin编写，默认为 final类，而插件需要为可继承的类 */
open class JiaguTask @Inject constructor(private val apkFile: File, private val jiaguConfigs: JiaguConfigs) : DefaultTask() {
    private val TAG = "JiaguTask"

    init {
        group = "jiagu"
    }
    /**
     * 方法名可以随便命名，只需要在方法上面 使用 [@TaskAction]注解
     */
    @TaskAction
    fun doJiaguWork() {
        if (!apkFile.exists()) {
            println("$TAG --> doJiaguWork() the apkFile [${apkFile}] not exists")
            return
        }
        val platformJarFilePath = jiaguConfigs.platformJarFilePath
        //一：先登录到 360加固服务器
        project.exec {execSpec ->
            //执行命令行命令
            //-login <username> 			首次使用必须先登录 <360用户名>
            //		<password>				<登录密码>
            val cmdParams = CmdSpell().javaCmd()._jar().append(platformJarFilePath).append("-login")
                .append(jiaguConfigs.platformAccount)
                .append(jiaguConfigs.platformAccountPw)
            println("$TAG --> doJiaguWork() start to login jiagu platform")
            execSpec.commandLine = cmdParams
        }

        //二： 导入签名信息
        project.exec {execSpec ->
//            -importsign <keystore_path> 		导入签名信息 <密钥路径>
//            <keystore_password> 			<密钥密码>
//            <alias> 				<别名>
//            <alias_password>			<别名密码>
            val importSignParams =  CmdSpell().javaCmd()._jar().append(platformJarFilePath)
                .append("-importsign")
                .append(jiaguConfigs.keyStoreFilePath)
                .append(jiaguConfigs.keyStorePw)
                .append(jiaguConfigs.keyStoreKeyAlias)
                .append(jiaguConfigs.keyStoreKeyPw)
            println("$TAG --> doJiaguWork() start to 导入签名文件配置...")
            execSpec.commandLine = importSignParams
        }
        //三：-importmulpkg <mulpkg_filepath>		导入多渠道配置信息，txt格式，如果有的话
        val jiaguChannelFilePath = jiaguConfigs.multyChannelsFilePath
        val hasMultyChannelFilePath = jiaguChannelFilePath.isNotBlank()
        if (hasMultyChannelFilePath) {
            val result = project.exec { execSpec->
                println("$TAG --> doJiaguWork() start to 导入多渠道文件配置...")
                execSpec.commandLine = CmdSpell().javaCmd()._jar().append(platformJarFilePath).append("-importmulpkg").append(jiaguChannelFilePath)
                //这个执行在 360加固服务器上是一种 追加的效果
            }
            println("$TAG --> doJiaguWork() 导入 多渠道配置文件 result = [$result]")
        }
        //四：执行加固
       val result = project.exec {execSpec ->
            /**
             * -jiagu <inputAPKpath> 			加固命令 <APK路径>
            <outputPath> 				<输出路径>
            [-autosign] 				【自动签名】
            [-automulpkg] 				【自动多渠道】
            [-pkgparam mulpkg_filepath]		【自定义文件生成多渠道】
             */
            var jiaguOutputDirPath = jiaguConfigs.jiaguOutputDirPath
            if (jiaguOutputDirPath.isBlank()) {
                jiaguOutputDirPath = apkFile.parent
            }
            val jiaguCmdParams = CmdSpell().javaCmd()._jar().append(platformJarFilePath)
                .append("-jiagu")
                .append(apkFile.absolutePath)
                .append(jiaguOutputDirPath)
                .append("-autosign") //【自动签名】
                .append("-automulpkg")//【自动多渠道】
                .append(jiaguChannelFilePath)
            println("$TAG --> doJiaguWork() start to 加固APK...")
            execSpec.commandLine = jiaguCmdParams
        }


    }
}