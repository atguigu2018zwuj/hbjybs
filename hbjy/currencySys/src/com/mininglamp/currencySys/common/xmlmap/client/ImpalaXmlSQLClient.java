package com.mininglamp.currencySys.common.xmlmap.client;


 
 

public class ImpalaXmlSQLClient extends XmlSQLClient {

    @Override
    protected String additionalPageSQL(String sql,String fromSize,String returnSize) {
        return sql + " limit " + returnSize + " offset " + fromSize;
    }

    

  

}
