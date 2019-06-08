package com.mininglamp.currencySys.util;

import java.util.HashMap;
import java.util.Map;

import jxl.biff.DisplayFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class JXLFormat {
	public int font_size = 11;
    public static Map<String,WritableCellFormat> wcfs = new HashMap<String,WritableCellFormat>();
    public WritableFont bold_font = new WritableFont(WritableFont.ARIAL, font_size, WritableFont.BOLD, false);; 
    public WritableFont nobold_font = new WritableFont(WritableFont.ARIAL, font_size, WritableFont.NO_BOLD, false);
    
	public WritableCellFormat getCellFormat(WritableFont font,  DisplayFormat format){
		if(format == null){
	        return new WritableCellFormat(font);
		}
        return new WritableCellFormat(font, format);
	}
}
