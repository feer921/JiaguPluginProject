package com.fee.jiaguplugin

/**
 * ******************(^_^)***********************<br>
 * User: fee(QQ/WeiXin:1176610771)<br>
 * Date: 2020/10/18<br>
 * Time: 12:40<br>
 * <P>DESC:
 * 命令拼写
 * </p>
 * ******************(^_^)***********************
 */
class CmdSpell : ArrayList<String> (){
    fun javaCmd() :CmdSpell{
        add(0, "java")
        return this
    }

    fun _jar() :CmdSpell {
        return append("-jar")
    }

    fun append(element: String): CmdSpell {
        add(element)
        return this
    }
    fun clean(): CmdSpell {
        clear()
        return this
    }
}