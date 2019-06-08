package com.mininglamp.currencySys.common.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 描述：数字工具类
 * 
 * @author ZhangXiaolei(Raven)
 * @version 1.0
 * @since 2007-10-25
 *  
 */
@SuppressWarnings("unchecked")
public class NumberUtilz {

	/**
	 * 检查此字符串是否为数字
	 * @param str
	 * @return true 如果是数字
	 */
	public static boolean isNumberic(String str) {
        
        if (str == null || str.equals("")) {
            return false;
        }
        
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * <p>检查此字符串是否为正整数</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>false</code>.</p>
     *
     * <pre>
     * StringUtils.isPositiveInteger(null)   = false
     * StringUtils.isPositiveInteger("")     = false
     * StringUtils.isPositiveInteger("  ")   = false
     * StringUtils.isPositiveInteger("123")  = true
     * StringUtils.isPositiveInteger("12 3") = false
     * StringUtils.isPositiveInteger("ab2c") = false
     * StringUtils.isPositiveInteger("12-3") = false
     * StringUtils.isPositiveInteger("12.3") = false
     * StringUtils.isPositiveInteger("00")   = false
     * </pre>
     *
     * @param str  要检查的字符串，可能是null
     * @return <code>true</code> 如果它是正整数的话
     */
    public static boolean isPositiveInteger(String str) {
        
        return isNumberic(str) && !(str.startsWith("0") && str.length()>1);
    }
    
    /**
     * 检查此字符串是否为小数
     * @param num
     * @return <code>true</code> 如果它是小数的话
     */
    public static boolean isDecimal(String num) {
        
        if(num == null || (num.indexOf('.') == -1) || (num.split("\\.").length != 2)) {
            return false;
        }
        String[] str = num.split("\\.");
        if(!isPositiveInteger(str[0]) || !isNumberic(str[1])) {
            return false;
        }
        return true;
    }
    
    /**
     * 去除数组中重复的元素
     * @param array int数组
     * @return 无重复的int数组
     */
	public int[] removeDuplicateNumber(int[] array) {
    	List list = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			if (!list.contains(new Integer(array[i]))) {
				list.add(new Integer(array[i]));
			}
		}
		int[] temp = new int[list.size()];
		for(int i=0; i<list.size(); i++) {
			temp[i] = ((Integer)list.get(i)).intValue();
		}
		return temp;
	}
    
    /**
	 * 冒泡排序
	 * 
	 * @param x int数组
	 */
	public static int[] BubbleSort(int[] x) {
		for (int i = 0; i < x.length; i++) {
			for (int j = i + 1; j < x.length; j++) {
				if (x[i] > x[j]) {
					int temp = x[i];
					x[i] = x[j];
					x[j] = temp;
				}
			}
		}
		return x;
	}

	/**
     * 选择排序
     * @param x int数组
     */  
	public static int[] selectSort(int[] x) {
		for (int i = 0; i < x.length; i++) {
			int lowerIndex = i;
			// 找出最小的一个索引   
			for (int j = i + 1; j < x.length; j++) {
				if (x[j] < x[lowerIndex]) {
					lowerIndex = j;
				}
			}
			// 交换   
			int temp = x[i];
			x[i] = x[lowerIndex];
			x[lowerIndex] = temp;
		}
		return x;
	}

	/**
     * 插入排序
     * @param x int数组
     */  
	public static int[] insertionSort(int[] x) {
		for (int i = 1; i < x.length; i++) {// i从一开始，因为第一个数已经是排好序的啦   
			for (int j = i; j > 0; j--) {
				if (x[j] < x[j - 1]) {
					int temp = x[j];
					x[j] = x[j - 1];
					x[j - 1] = temp;
				}
			}
		}
		return x;
	}

	/**
     * 希尔排序
     * @param x int数组
     */  
	public static int[] shellSort(int[] x) {
		// 分组   
		for (int increment = x.length / 2; increment > 0; increment /= 2) {
			// 每个组内排序   
			for (int i = increment; i < x.length; i++) {
				int temp = x[i];
				int j = 0;
				for (j = i; j >= increment; j -= increment) {
					if (temp < x[j - increment]) {
						x[j] = x[j - increment];
					} else {
						break;
					}
				}
				x[j] = temp;
			}
		}
		return x;
	}
	
	/**
	 * 得到6为随机数
	 * */
	public static int getSixFigures(){
		int i=new Random().nextInt(999999);
		return i<100000?i+100000:i;
	}
	public static void main(String[] args) {
		System.out.println(isNumberic("aaa123"));
	}
	
	/**
	 * 计算两个数的百分比
	 * @param x         分子
	 * @param total     分母-总数
	 * @return
	 */
	public static String getPercent(int x,int total){   
		double x_double=x*1.0;  
		double total_double=total*1.0;
			   
		NumberFormat format = NumberFormat.getPercentInstance();// 获取格式化类实例
		format.setMinimumFractionDigits(2);// 设置小数位
		return format.format(x_double / total_double);  
	}
	/**
	 * 数值格式化
	 * @param number
	 * @return
	 */
	public static String numberFormat(Double number){
		DecimalFormat df = new DecimalFormat("##0.00");
		
		return df.format(number);
	}
}
