package com.fee.jiaguplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.BaseVariantOutput
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * ******************(^_^)***********************<br>
 * User: fee(QQ/WeiXin:1176610771)<br>
 * Date: 2020/10/14<br>
 * Time: 13:12<br>
 * <P>DESC:
 * 自定义一个 使用 360进行加固的插件
 * </p>
 * ******************(^_^)***********************
 */
class JiaguPlugin : Plugin<Project> {
    private val TAG = "JiaguPlugin"

    /**
     * Apply this plugin to the given target object.
     *
     * @param target The target object
     */
    override fun apply(curProject: Project) {
        //获取项目中所有 扩展管理器(容器)
        val extensionsContainer = curProject.extensions

        /**
         * 创建一个扩展
         */
        val jiaguConfig = extensionsContainer.create("jiagu", JiaguConfigs::class.java)


        //再找到 官方的安卓插件
        curProject.afterEvaluate {project ->
            println("$TAG --> afterEvaluate() jiaguConfig = $jiaguConfig")
            val extensions = project.extensions
            //获取到 Android 的扩展,即项目中 android{}闭包块
            val androidExtension: AppExtension = extensions.getByType(AppExtension::class.java)

            if (jiaguConfig.keyStorePw.isEmpty()) {
                //如果用户没有配置 签名信息,则去 android{}闭包下找配置的签名信息
                val signingConfigs = androidExtension.signingConfigs
                val hasConfigSignConfigfind = signingConfigs.find { signConfig ->
                    !signConfig.storePassword.isNullOrEmpty()
                }
                hasConfigSignConfigfind?.let {signConfig ->
                    jiaguConfig.keyStoreFilePath = signConfig.storeFile?.absolutePath.toString()
                    jiaguConfig.keyStorePw = signConfig.keyPassword.toString()
                    jiaguConfig.keyStoreKeyAlias = signConfig.keyAlias.toString()
                    jiaguConfig.keyStoreKeyPw = signConfig.keyPassword.toString()
                }
            }

            //获取 项目打包的变体
            androidExtension.applicationVariants.all {applicationVariant ->
                //debug/release 类型的变体
                //构建的变体名称,如果没有指定变体风味，默认为 debug/release
                val name = applicationVariant.name
                val buildType = applicationVariant.buildType//com.android.build.gradle.internal.api.ReadOnlyBuildType
                val curBuildTypeName = buildType.name
                val isSameName = name == curBuildTypeName
                var buildTypeNameFirstCharUpper: String = ""
                val assembleTaskName = when (curBuildTypeName) {
                    "debug" -> {
                        buildTypeNameFirstCharUpper = "Debug"
                        "assembleDebug"
                    }
                    "release" ->{
                        buildTypeNameFirstCharUpper = "Release"
                        "assembleRelease"
                    }
                    else -> {
                        ""
                    }
                }
                val jiaguTaskName = if(isSameName){"jiagu${buildTypeNameFirstCharUpper}"} else "jiagu${name.capitalize()}${buildTypeNameFirstCharUpper}"
                //构建的变体输出配置信息,目的是找到 输出的APK路径
                applicationVariant.outputs.all { outPut: BaseVariantOutput ->
                    val apkFile = outPut.outputFile
                    println("$TAG --> cur applicationVariant name is [$name] the apkFilePath is [$apkFile] buildTypeName is [$curBuildTypeName]")
                    //创建 加固任务
                    val jiaguTask = curProject.tasks.create(
                        jiaguTaskName,
                        JiaguTask::class.java,
                        apkFile,
                        jiaguConfig
                    )
                    if (jiaguConfig.isNeedDependOnAssembelTask) {
                        val assembleDebugTask: Task? = curProject.tasks.getByName(assembleTaskName)
                        assembleDebugTask?.let {assembleTask ->
                            jiaguTask.dependsOn(assembleTask)
                        }
                    }
                }
            }
        }
    }

}