package com.xinzhuxiansheng.jnebulae.http.core.common.utils;

import com.xinzhuxiansheng.common.utils.NetUtil;
import com.xinzhuxiansheng.common.utils.StrUtil;
import com.xinzhuxiansheng.jnebulae.http.core.exception.ValidationException;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * GenericsUtil
 * @author houyi.wh
 * @date 2017-10-20
 */
public class GenericsUtil {
	
	/**
	 * 通过反射获得Class声明的范型Class.
	 * 通过反射,获得方法输入参数第index个输入参数的所有泛型参数的实际类型. 如: public void add(Map<String, Buyer> maps, List<String> names){}
	 * @param method 方法
	 * @param index 第几个输入参数
	 * @return 输入参数的泛型参数的实际类型集合, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回空集合
	 */
	@SuppressWarnings("rawtypes")
	public static List<Class> getMethodGenericParameterTypes(Method method, int index) {
		List<Class> results = new ArrayList<Class>();
		Type[] genericParameterTypes = method.getGenericParameterTypes();
		if (index >= genericParameterTypes.length || index < 0) {
			throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
		}
		Type genericParameterType = genericParameterTypes[index];
		if (genericParameterType instanceof ParameterizedType) {
			ParameterizedType aType = (ParameterizedType) genericParameterType;
			Type[] parameterArgTypes = aType.getActualTypeArguments();
			for (Type parameterArgType : parameterArgTypes) {
				Class parameterArgClass = (Class) parameterArgType;
				results.add(parameterArgClass);
			}
			return results;
		}
		return results;
	}


	/**
	 * 断言非空
	 * @param dataName 参数
	 * @param values 值
	 */
	public static void checkNull(String dataName, Object... values){
		if(values == null){
			throw new ValidationException("["+ dataName + "] cannot be null");
		}
		for (Object value : values) {
			if (value == null) {
				throw new ValidationException("[" + dataName + "] cannot be null");
			}
		}
	}

	/**
	 * 断言非空
	 * @param dataName 参数
	 * @param values 值
	 */
	public static void checkBlank(String dataName, Object... values){
		if(values == null){
		//	throw new ValidationException("["+ dataName + "] cannot be null");
		}
		for (Object value : values) {
			if (StrUtil.isBlank(value)) {
				throw new ValidationException("[" + dataName + "] cannot be blank");
			}
		}
	}

	/**
	 * 获取ipV4
	 * @return ipV4
	 */
	public static String getLocalIpV4(){
		LinkedHashSet<String> ipV4Set = NetUtil.localIpv4s();
		return ipV4Set.isEmpty()?"":ipV4Set.toArray()[0].toString();
	}

}
