package com.mtjsoft.www.kotlinmvputils.rxhttp

import com.google.gson.GsonBuilder
import com.mtjsoft.www.kotlinmvputils.rxhttp.entity.PageList
import com.mtjsoft.www.kotlinmvputils.rxhttp.entity.Response
import com.mtjsoft.www.kotlinmvputils.rxhttp.gson.DoubleDefault0Adapter
import com.mtjsoft.www.kotlinmvputils.rxhttp.gson.IntegerDefault0Adapter
import com.mtjsoft.www.kotlinmvputils.rxhttp.gson.LongDefault0Adapter
import com.mtjsoft.www.kotlinmvputils.rxhttp.gson.MyTypeAdapterFactory
import com.mtjsoft.www.kotlinmvputils.utils.KLog
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.converter.GsonConverter
import rxhttp.wrapper.entity.ParameterizedTypeImpl
import rxhttp.wrapper.exception.ExceptionHelper
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.parse.AbstractParser
import java.io.IOException
import java.lang.reflect.Type

/**
 * 输入T,输出T,并对code统一判断
 */
@Parser(name = "Response", wrappers = [MutableList::class, PageList::class])
open class ResponseParser<T> : AbstractParser<T> {
    /**
     * 此构造方法适用于任意Class对象，但更多用于带泛型的Class对象，如：List<Student>
     *
     * 用法:
     * Java: .asParser(new ResponseParser<List<Student>>(){})
     * Kotlin: .asParser(object : ResponseParser<List<Student>>() {})
     *
     * 注：此构造方法一定要用protected关键字修饰，否则调用此构造方法将拿不到泛型类型
     */
    protected constructor() : super()

    /**
     * 此构造方法仅适用于不带泛型的Class对象，如: Student.class
     *
     * 用法
     * Java: .asParser(new ResponseParser<>(Student.class))   或者  .asResponse(Student.class)
     * Kotlin: .asParser(ResponseParser(Student::class.java)) 或者  .asResponse(Student::class.java)
     */
    constructor(type: Type) : super(type)

    @Throws(IOException::class)
    override fun onParse(response: okhttp3.Response): T {
        //获取泛型类型
        val type: Type = ParameterizedTypeImpl[Response::class.java, mType]
        // 自定义Gson解析，将null替换成空字符串
        val gson = GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapter(Integer::class.java, IntegerDefault0Adapter())
            .registerTypeAdapter(Double::class.java, DoubleDefault0Adapter())
            .registerTypeAdapter(Long::class.java, LongDefault0Adapter())
            .registerTypeAdapterFactory(MyTypeAdapterFactory())
            .create()
        RxHttpPlugins.setConverter(GsonConverter.create(gson))
        val data: Response<T> = convert(response, type)
        var t = data.datas //获取data字段
        if (t == null && mType === String::class.java) {
            /*
             * 考虑到有些时候服务端会返回没有data的数据
             * 此时code正确，但是data字段为空，直接返回data的话，会报空指针错误，
             * 所以，判断泛型为String类型时，重新赋值，并确保赋值不为null
             */
            @Suppress("UNCHECKED_CAST")
            t = data.msg as T
        }
        if (!data.isSuccess) { //isSuccess == false，说明数据不正确，抛出异常
            throw ParseException(data.code.toString(), data.msg, response)
        }
        return t
    }
}

