package com.mininglamp.currencySys.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.util.Assert;


/**
 * 描述：字符串工具类
 * 
 * @author ZhangXiaolei(Raven)
 * @version 1.0
 * @since 2007-4-18
 */
@SuppressWarnings("unchecked")
public class StringUtilz {
	
	/**
     * The empty String <code>""</code>.
     */
    private static final String EMPTY = "";
	
	/**
     * Represents a failed index search.
     */
    private static final int INDEX_NOT_FOUND = -1;
    
    /**
     * 用于被替换的标识符
     */
    private static final String IDENTIFIER = "{}";
 
	/**
	 * Trim <i>all</i> whitespace from the given String:
	 * leading, trailing, and inbetween characters.
	 * @param str the String to check
	 * @return the trimmed String
	 */
	public static String trimAllWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		int index = 0;
		while (buf.length() > index) {
			if (Character.isWhitespace(buf.charAt(index))) {
				buf.deleteCharAt(index);
			}
			else {
				index++;
			}
		}
		return buf.toString();
	}
	
    /**
	 * 替换字符串中的标识符
	 * @param str
	 * @param values
	 * @return String
	 */
	public static String anchorReplace(String str, Object[] values) {
		if(isEmpty(str) || CommonUtil.isEmpty(values)) 
			return str;
		//判断要查询的字符串是否存在
		int totalNum = countMatches(str, IDENTIFIER);
		if(totalNum != values.length || totalNum <= 0) {
			return str;
		}
		
		StringBuffer sb = new StringBuffer(str);
		for(int i=0; i<values.length; i++) {
			Object value = values[i];
			if(value == null) {
				value = EMPTY;
			}
			int anchorIndex = sb.indexOf(IDENTIFIER);
			sb.replace(anchorIndex, anchorIndex + 2, value.toString());
		}
		return sb.toString();
	}
	
	/**
	 * 将str中第ordinal个字符串searchString替换为replacement
	 * @param str
	 * @param searchStr
	 * @param replacement
	 * @param ordinal 文本中第几个被替换的字符串 the n-th <code>searchStr</code> to find
	 * @return 替换后的string
	 */
	public static String ordinalReplace(String str, String searchStr, String replacement, int ordinal) {
		//判断要查询的字符串和要替换的字符串是否为空
		if(isEmpty(searchStr) || isEmpty(replacement)) 
			return str;
		//判断要查询的字符串是否存在
		int totalNum = countMatches(str, searchStr);
		if(ordinal > totalNum || ordinal <= 0) {
			return str;
		}
		
		int position = ordinalIndexOf(str, searchStr, ordinal);
		StringBuffer sb = new StringBuffer(str);
		sb.replace(position, position + replacement.length(), replacement);
		return sb.toString();
	}
	
	/**
	 * 统计sub在str中出现的次数
     * <p>Counts how many times the substring appears in the larger String.</p>
     *
     * <p>A <code>null</code> or empty ("") String input returns <code>0</code>.</p>
     *
     * <pre>
     * StringUtils.countMatches(null, *)       = 0
     * StringUtils.countMatches("", *)         = 0
     * StringUtils.countMatches("abba", null)  = 0
     * StringUtils.countMatches("abba", "")    = 0
     * StringUtils.countMatches("abba", "a")   = 2
     * StringUtils.countMatches("abba", "ab")  = 1
     * StringUtils.countMatches("abba", "xxx") = 0
     * </pre>
     *
     * @param str  the String to check, may be null
     * @param sub  the substring to count, may be null
     * @return the number of occurrences, 0 if either String is <code>null</code>
     */
    public static int countMatches(String str, String sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != INDEX_NOT_FOUND) {
            count++;
            idx += sub.length();
        }
        return count;
    }
	
	/**
	 * 查询searchStr在str中的位置
     * <p>Finds the n-th index within a String, handling <code>null</code>.
     * This method uses String.indexOf(String).</p>
     *
     * <p>A <code>null</code> String will return <code>-1</code>.</p>
     *
     * <pre>
     * StringUtils.ordinalIndexOf(null, *, *)          = -1
     * StringUtils.ordinalIndexOf(*, null, *)          = -1
     * StringUtils.ordinalIndexOf("", "", *)           = 0
     * StringUtils.ordinalIndexOf("aabaabaa", "a", 1)  = 0
     * StringUtils.ordinalIndexOf("aabaabaa", "a", 2)  = 1
     * StringUtils.ordinalIndexOf("aabaabaa", "b", 1)  = 2
     * StringUtils.ordinalIndexOf("aabaabaa", "b", 2)  = 5
     * StringUtils.ordinalIndexOf("aabaabaa", "ab", 1) = 1
     * StringUtils.ordinalIndexOf("aabaabaa", "ab", 2) = 4
     * StringUtils.ordinalIndexOf("aabaabaa", "", 1)   = 0
     * StringUtils.ordinalIndexOf("aabaabaa", "", 2)   = 0
     * </pre>
     *
     * @param str  the String to check, may be null
     * @param searchStr  the String to find, may be null
     * @param ordinal  the n-th <code>searchStr</code> to find
     * @return the n-th index of the search String,
     *  <code>-1</code> (<code>INDEX_NOT_FOUND</code>) if no match or <code>null</code> string input
     */
    public static int ordinalIndexOf(String str, String searchStr, int ordinal) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return 0;
        }
        int found = 0;
        int index = INDEX_NOT_FOUND;
        do {
            index = str.indexOf(searchStr, index + 1);
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }
	
	//-------------------------------------------------------------------
	// NOTE Convenience methods about validate
	//-------------------------------------------------------------------
    
    /**
     * <p>Checks if a String is empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the String.
     * That functionality is available in isBlank().</p>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
	
    /**
     * 判断某个字符串是否匹配给定正则表达式
     * 
     * @param str 要匹配的字符串
     * @param regex 正则表达式
     * @return true为匹配，false为不匹配
     */
    public static boolean isMatching(String str, String regex) {

        if (str == null || str.equals("")) {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.find();
    }
    
    /**
	 * Check that the given String is neither <code>null</code> nor of length 0.
	 * Note: Will return <code>true</code> for a String that purely consists of whitespace.
	 * <p><pre>
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 * @param str the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not null and has length
	 */
	public static boolean hasLength(String str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * Check whether the given String has actual text.
	 * More specifically, returns <code>true</code> if the string not <code>null<code>,
	 * its length is greater than 0, and it contains at least one non-whitespace character.
	 * <p><pre>
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText("") = false
	 * StringUtils.hasText(" ") = false
	 * StringUtils.hasText("12345") = true
	 * StringUtils.hasText(" 12345 ") = true
	 * </pre>
	 * @param str the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not <code>null</code>, its length is
	 * greater than 0, and is does not contain whitespace only
	 */
	public static boolean hasText(String str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}
    
	/**
	 * Test if the given String starts with the specified prefix,
	 * ignoring upper/lower case.
	 * @param str the String to check
	 * @param prefix the prefix to look for
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		String lcStr = str.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}

	/**
	 * Test if the given String ends with the specified suffix,
	 * ignoring upper/lower case.
	 * @param str the String to check
	 * @param suffix the suffix to look for
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		if (str == null || suffix == null) {
			return false;
		}
		if (str.endsWith(suffix)) {
			return true;
		}
		if (str.length() < suffix.length()) {
			return false;
		}

		String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
		String lcSuffix = suffix.toLowerCase();
		return lcStr.equals(lcSuffix);
	}
	
	//-------------------------------------------------------------------
	// NOTE Convenience methods about remove
	//-------------------------------------------------------------------
    
    /**
     * 从字符串尾部去掉一定长度的字符串，如"abcde"去掉2位变成"abc"
     * @param str 字符串
     * @param len 要去掉的字符串长度
     * @return 新的字符串
     */
    public static String removeEnd(String str, int len) {
    	
        return (str == null) ? null : str.substring(0, str.length() - len);
    }

    /**
     * 去掉字符串数组中值为null的元素并生成一个新的数组
     * 
     * @param aArray
     */
    public static String[] removeNull(String[] aArray) {

        int nullNum = 0;//记录空值的数量
        int newArrayNum = 0;//记录新数组的长度

        if (aArray == null || aArray.length == 0) {
            return null;
        }

        for (int i = 0; i < aArray.length; i++) {
            if (aArray[i] == null) {
                nullNum++;
            }
        }
        String[] newArray = new String[aArray.length - nullNum];

        for (int i = 0; i < aArray.length; i++) {
            if (aArray[i] != null) {
                newArray[newArrayNum] = aArray[i];
                newArrayNum++;
            }
        }
        return newArray;
    }
    
    
	/**
	 * 去除数组中重复的元素
	 * Remove duplicate Strings from the given array.
	 * Also sorts the array, as it uses a TreeSet.
	 * @param array the String array
	 * @return an array without duplicates, in natural sort order
	 */
	public static String[] removeDuplicateStrings(String[] array) {
		if (CommonUtil.isEmpty(array)) {
			return array;
		}
		Set set = new TreeSet();
		for (int i = 0; i < array.length; i++) {
			set.add(array[i]);
		}
		return toStringArray(set);
	}
	
	//-------------------------------------------------------------------
	// NOTE Convenience methods for compositor
	//-------------------------------------------------------------------
    
	/**
	 * Turn given source String array into sorted array.
	 * @param array the source array
	 * @return the sorted array (never <code>null</code>)
	 */
	public static String[] sortStringArray(String[] array) {
		if (CommonUtil.isEmpty(array)) {
			return new String[0];
		}
		Arrays.sort(array);
		return array;
	}

    /**
     * 将数组中的某个元素挪到首位
     * @param aArray
     * @param location 数组的元素位置，从0开始
     * @return 返回按规则排列好的数组
     */
    public static String[] toTop(String[] aArray, int location) {

        if (location < aArray.length - 1 && location > 0) {

	        String temp = aArray[0];
	        for (int i = 0; i < location; i++) {
	            aArray[i] = aArray[i + 1];
	        }
	        aArray[location] = temp;
    	}
        return aArray;
    }
    /**
     * 删除数组中的某个元素
     * @author Raven(ZhangXiaolei)
     * @date 2016-8-18
     * @describe 
     * @param array
     * @param location
     * @return
     */
    public static String [] removeAssignStrings(String[] array,int location){
		if (CommonUtil.isEmpty(array)) {
			return array;
		}
		List result = new ArrayList();
		result.addAll(Arrays.asList(array));
		
		result.remove(location);
    	
    	return toStringArray(result);
    }

    /**
     * 将数组中的某个元素挪到末位
     * @param aArray
     * @param location 数组的元素位置，从0开始
     * @return 返回按规则排列好的数组
     */
    public static String[] toBottom(String[] aArray, int location) {

        if (location < aArray.length - 1 && location > 0) {

	        String temp = aArray[aArray.length - 1];
	        for (int i = aArray.length - 1; i > location; i--) {
	            aArray[i] = aArray[i - 1];
	        }
	        aArray[location] = temp;
        }
        return aArray;
    }
    
	//-------------------------------------------------------------------
	// NOTE Convenience methods for array
	//-------------------------------------------------------------------
    
    /**
     * 判断数组是否包含元素
     * @param array 字符串数组
     * @return true 如果数组包含元素
     */
    public static boolean isEmpty(String[] array) {
    	return (array == null || array.length < 0);
    }
    
    /**
     * 判断某个字符串是否和属于某个字符串数组
     * @param str 某个字符串
     * @param array 某个字符串数组
     * @return true 如果这个字符串和这个数组中的某个元素相等
     */
    public static boolean isBelongToArray(String str, String[] array) {
        Assert.notNull(str);
        Assert.notNull(array);
        for(int i=0; i<array.length; i++) {
            if(str.equals(array[i])) {
                return true;
            }
        }
        return false;
    }
	
	/**
	 * 合并两个数组，元素无重复
	 * Merge the given String arrays into one, with overlapping
	 * array elements only included once.
	 * <p>The order of elements in the original arrays is preserved
	 * (with the exception of overlapping elements, which are only
	 * included on their first occurence).
	 * @param array1 the first array (can be <code>null</code>)
	 * @param array2 the second array (can be <code>null</code>)
	 * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
	 */
	public static String[] mergeStringArrays(String[] array1, String[] array2) {
		if (CommonUtil.isEmpty(array1)) {
			return array2;
		}
		if (CommonUtil.isEmpty(array2)) {
			return array1;
		}
		List result = new ArrayList();
		result.addAll(Arrays.asList(array1));
		for (int i = 0; i < array2.length; i++) {
			String str = array2[i];
			if (!result.contains(str)) {
				result.add(str);
			}
		}
		return toStringArray(result);
	}
	
    /**
     * 将Object[] 转化成 String[]
     * @param aArray Object数组
     */
    public static String[] toStringArray(Object[] aArray) {

        if (aArray == null || aArray.length == 0) {
            return null;
        }

        String[] newArray = new String[aArray.length];
        for (int i = 0; i < aArray.length; i++) {
            if (aArray[i] == null) {
                newArray[i] = null;
            } else {
                newArray[i] = String.valueOf(aArray[i]);
            }
        }
        return newArray;
    }
	
	/**
	 * Copy the given Collection into a String array.
	 * The Collection must contain String elements only.
	 * @param collection the Collection to copy
	 * @return the String array (<code>null</code> if the passed-in
	 * Collection was <code>null</code>)
	 */
	public static String[] toStringArray(Collection collection) {
		if (collection == null) {
			return null;
		}
		return (String[]) collection.toArray(new String[collection.size()]);
	}
	
    
    /**
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     *
     * <p>No delimiter is added before or after the list.
     * A <code>null</code> separator is the same as an empty String ("").
     * Null objects or empty strings within the array are represented by
     * empty strings.</p>
     *
     * <pre>
     * StringUtils.join(null, *)                = null
     * StringUtils.join([], *)                  = ""
     * StringUtils.join([null], *)              = ""
     * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringUtils.join(["a", "b", "c"], null)  = "abc"
     * StringUtils.join(["a", "b", "c"], "")    = "abc"
     * StringUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array  the array of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @return the joined String, <code>null</code> if null array input
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }
    
	//-------------------------------------------------------------------
	// NOTE Convenience methods for private
	//-------------------------------------------------------------------

    /**
     * <p>Joins the elements of the provided array into a single String
     * containing the provided list of elements.</p>
     *
     * <p>No delimiter is added before or after the list.
     * A <code>null</code> separator is the same as an empty String ("").
     * Null objects or empty strings within the array are represented by
     * empty strings.</p>
     *
     * <pre>
     * StringUtils.join(null, *)                = null
     * StringUtils.join([], *)                  = ""
     * StringUtils.join([null], *)              = ""
     * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringUtils.join(["a", "b", "c"], null)  = "abc"
     * StringUtils.join(["a", "b", "c"], "")    = "abc"
     * StringUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array  the array of values to join together, may be null
     * @param separator  the separator character to use, null treated as ""
     * @param startIndex the first index to start joining from.  It is
     * an error to pass in an end index past the end of the array
     * @param endIndex the index to stop joining from (exclusive). It is
     * an error to pass in an end index past the end of the array
     * @return the joined String, <code>null</code> if null array input
     */
    private static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }

        // endIndex - startIndex > 0:   Len = NofStrings *(len(firstString) + len(separator))
        //           (Assuming that all Strings are roughly equally long)
        int bufSize = (endIndex - startIndex);
        if (bufSize <= 0) {
            return "";
        }

        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length())
                        + separator.length());

        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
    
    
    /**
     * 返回20位的序列数值
     * @return
     */
	public static String getSerialId() {

		String nowTime = DateUtilz.getCurrentDate("yyyyMMddHHmmssSSS");// 获取当前时间
		String rand = RandomStringUtils.randomNumeric(3);// 获取随即码

		return nowTime + rand;
	}
    public static void main(String[] args) {
    }
}
