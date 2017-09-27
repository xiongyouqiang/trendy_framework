package com.trendy.fw.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListKit {
	// private static Logger log = LoggerFactory.getLogger(ListKit.class);

	/**
	 * 随机重排List
	 * 
	 * @param <E>
	 * @param list
	 * @return
	 */
	static public <E> List<E> rearrangeRandomList(List<E> list) {
		if (list.size() <= 1) {
			return list;
		}
		List<E> arrayList = new ArrayList<E>(list);
		List<E> result = new ArrayList<E>();
		Random random = new Random();
		while (arrayList.size() > 0) {
			int index = random.nextInt(arrayList.size());
			E obj = arrayList.get(index);
			result.add(obj);
			arrayList.remove(index);
		}
		return result;
	}

	/**
	 * 过滤字段内容相同的List对象
	 * 
	 * @param <E>
	 * @param list
	 * @param fieldName
	 *            字段名称
	 * @param max
	 *            条数
	 * @return
	 * @throws Exception
	 */
	static public <E> List<E> filterDuplicateElement(List<E> list, String fieldName, int max) {
		List<E> result = new ArrayList<E>();
		if (list.size() <= 1) {
			return list;
		}

		HashSet<Object> set = new HashSet<Object>();
		for (int i = 0; i < list.size(); i++) {
			E bean = list.get(i);
			Object key = ReflectKit.getPropertyValue(bean, fieldName);
			if (!set.contains(key)) {
				set.add(key);
				result.add(bean);
				if (max > 0 && result.size() >= max) {
					break;
				}
			}
		}

		return result;
	}

	/**
	 * 过滤字段内容相同的List对象
	 * 
	 * @param <E>
	 * @param list
	 * @param fieldName
	 *            字段名称
	 * @return
	 * @throws Exception
	 */
	static public <E> List<E> filterDuplicateElement(List<E> list, String fieldName) {
		return filterDuplicateElement(list, fieldName, -1);
	}

	/**
	 * 取子List
	 * 
	 * @param <E>
	 * @param list
	 * @param fromIndex
	 *            起始条数
	 * @param toIndex
	 *            终止条数
	 * @return
	 */
	static public <E> List<E> subList(List<E> list, int fromIndex, int toIndex) {
		if (list == null || list.size() == 0) {
			return new ArrayList<E>();
		} else if (list.size() < fromIndex) {
			return new ArrayList<E>();
		} else if (list.size() < toIndex) {
			return list.subList(fromIndex, list.size());
		}
		return list.subList(fromIndex, toIndex);
	}

	/**
	 * 分割List
	 * 
	 * @param list
	 * @param pageSize
	 *            每页条数
	 * @return
	 */
	static public <E> List<List<E>> splitList(List<E> list, int pageSize) {
		List<List<E>> result = new ArrayList<List<E>>();
		int totalPageNumber = (int) (list.size() / pageSize);
		if ((list.size() % pageSize) != 0) {
			totalPageNumber = totalPageNumber + 1;
		}

		for (int i = 0; i < totalPageNumber; i++) {
			int fromIndex = pageSize * i;
			int toIndex = fromIndex + pageSize;
			List<E> subList = subList(list, fromIndex, toIndex);
			result.add(subList);
		}

		return result;
	}

	/**
	 * 取List指定条数的对象
	 * 
	 * @param <E>
	 * @param list
	 * @param index
	 *            第几条
	 * @return
	 */
	static public <E> E getElement(List<E> list, int index) {
		E element = null;
		if (list.size() > index && index > 0 && list.size() > 0) {
			element = list.get(index);
		}
		return element;
	}

	/**
	 * 链接两个List
	 * 
	 * @param <E>
	 * @param listA
	 * @param listB
	 * @return
	 */
	static public <E> List<E> combineList(List<E> listA, List<E> listB) {
		List<E> list = new ArrayList<E>();
		if (listA != null) {
			list.addAll(listA);
		}
		if (listB != null) {
			list.addAll(listB);
		}
		return list;
	}

	/**
	 * 从List间隔获取对象
	 * 
	 * @param <E>
	 * @param list
	 * @param fromIndex
	 *            起始条数
	 * @param index
	 *            间隔
	 * @return
	 */
	static public <E> List<E> getIntervalList(List<E> list, int fromIndex, int index) {
		List<E> result = new ArrayList<E>();
		if (list.size() <= 1 || index < 2) {
			return list;
		}
		for (int i = fromIndex; i < list.size(); i = i + index) {
			result.add(list.get(i));
		}
		return result;
	}

	/**
	 * 将List<Object>转换成字符串
	 * 
	 * @param list
	 * @param regex
	 *            分隔符
	 * @return
	 */
	static public <E> String list2String(List<E> list, String regex) {
		StringBuilder sb = new StringBuilder();
		for (E obj : list) {
			if (sb.length() > 0) {
				sb.append(regex);
			}
			sb.append(obj.toString());
		}
		return sb.toString();
	}

	/**
	 * 将List<E>中某一字段转换成字符串
	 * 
	 * @param list
	 * @param fieldName
	 *            字段名称
	 * @param regex
	 *            分隔符
	 * @return
	 */
	static public <E> String list2String(List<E> list, String fieldName, String regex) {
		StringBuilder sb = new StringBuilder();

		for (E bean : list) {
			if (sb.length() > 0) {
				sb.append(regex);
			}
			Object obj = ReflectKit.getPropertyValue(bean, fieldName);
			sb.append(obj.toString());
		}
		return sb.toString();
	}

	/**
	 * 将list中的bean某个字段转换成map
	 * 
	 * @param list
	 * @param keyFieldName
	 * @return
	 * @throws Exception
	 */
	static public <E> Map<String, E> list2Map(List<E> list, String keyFieldName) {
		Map<String, E> map = new HashMap<String, E>();

		for (E bean : list) {
			Object key = ReflectKit.getPropertyValue(bean, keyFieldName);
			map.put(key.toString(), bean);
		}
		return map;
	}

	/**
	 * 将list中的bean某个字段转换成map
	 * 
	 * @param list
	 * @param keyFieldName
	 * @param valueFieldName
	 * @return
	 * @throws Exception
	 */
	static public <E> Map<String, String> list2Map(List<E> list, String keyFieldName, String valueFieldName) {
		Map<String, String> map = new HashMap<String, String>();
		for (E bean : list) {
			Object key = ReflectKit.getPropertyValue(bean, keyFieldName);
			Object value = ReflectKit.getPropertyValue(bean, valueFieldName);
			map.put(key.toString(), value.toString());
		}
		return map;
	}

	/**
	 * 字符串转换成list
	 * 
	 * @param str
	 * @param regex
	 * @return
	 */
	static public List<String> string2List(String str, String regex) {
		List<String> list = new ArrayList<String>();
		String[] array = str.split(regex);
		for (String value : array) {
			list.add(value);
		}

		return list;
	}

	/**
	 * 键值对字符串转成数组列表
	 * 
	 * @param str
	 *            字符串，如a=1&b=2
	 * @param regex1
	 *            标记1，行之间分隔
	 * @param regex2
	 *            标记2，行内分隔
	 * @return
	 */
	static public List<String[]> string2ListOfArray(String str, String regex1, String regex2) {
		List<String[]> list = new ArrayList<String[]>();
		String[] array = str.split(regex1);
		for (String line : array) {
			list.add(line.split(regex2, 2));
		}
		return list;
	}

	/**
	 * 数组列表转成键值对字符串
	 * 
	 * @param list
	 * @param regex1
	 *            标记1，行之间分隔
	 * @param regex2
	 *            标记2，行内分隔
	 * @return
	 */
	static public <E> String listOfArray2String(List<E[]> list, String regex1, String regex2) {
		StringBuilder sb = new StringBuilder();
		for (E[] array : list) {
			if (sb.length() > 0) {
				sb.append(regex1);
			}
			sb.append(array[0].toString() + regex2 + array[1].toString());
		}
		return sb.toString();
	}

	/**
	 * 对list的某一字段进行排序
	 * 
	 * @param list
	 * @param compareKey
	 * @return
	 */
	static public <E> List<E> sortList(List<E> list, String compareKey) {
		ContentComparator<E> comparator = new ContentComparator<E>();
		comparator.setCompareKey(compareKey);
		Collections.sort(list, comparator);
		return list;
	}

	/**
	 * 集合转换成list
	 * 
	 * @param collection
	 * @return
	 */
	static public <E> List<E> collection2List(Collection<E> collection) {
		List<E> list = new ArrayList<E>();
		for (E e : collection) {
			list.add(e);
		}
		return list;
	}
}
