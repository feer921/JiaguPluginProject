package com.fee.jiaguplugin

/**
 * ******************(^_^)***********************<br>
 * User: fee(QQ/WeiXin:1176610771)<br>
 * Date: 2020/10/14<br>
 * Time: 15:10<br>
 * <P>DESC:
 * 承载 用户配置的 加固SDK的配置信息，
 * 如：360的账号、密码、
 * 签名文件信息等
 * </p>
 * ******************(^_^)***********************
 */
open class JiaguConfigs {

    /**
     * 加固平台服务器的账号
     */
    var platformAccount: String = ""

    /**
     * 加固平台服务器的账号的密码
     */
    var platformAccountPw: String = ""

    /**
     * 加固平台 可执行模块[Jar]文件的路径
     */
    var platformJarFilePath: String = ""

    /**
     * 签名文件路径
     */
    var keyStoreFilePath: String = ""

    /**
     * 签名文件的密钥(密钥库的)
     */
    var keyStorePw :String = ""

    /**
     * 密钥 Key的别名
     */
    var keyStoreKeyAlias:String = ""

    /**
     * 签名文件　Key 的密钥
     */
    var keyStoreKeyPw: String = ""

    /**
     * 多渠道的配置文件路径
     */
    var multyChannelsFilePath: String = ""

    /**
     * 加固输出的目录路径
     */
    var jiaguOutputDirPath: String = ""

    /**
     * 当执行加固任务时，是否需要 同时依赖项目的构建出 APK任务
     * def: true,因为要加固，则先要构建生成出 APK文件,不然需要手动先构建出 APK，然后再执行加固任务
     */
    var isNeedDependOnAssembelTask = true

    override fun toString(): String {
        return "JiaguConfigs(platformAccount='$platformAccount', platformAccountPw='$platformAccountPw', platformJarFilePath='$platformJarFilePath', keyStoreFilePath='$keyStoreFilePath', keyStorePw='$keyStorePw', keyStoreKeyAlias='$keyStoreKeyAlias', keyStoreKeyPw='$keyStoreKeyPw', multyChannelsFilePath='$multyChannelsFilePath', jiaguOutputDirPath='$jiaguOutputDirPath', isNeedDependOnAssembelTask=$isNeedDependOnAssembelTask)"
    }


}