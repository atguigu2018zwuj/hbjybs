define(function(require) {
    $(function() {
        require('mock');
        // Mock.mock( rurl, rtype, function(options){} )
        Mock.mock(/\/example\/ajax\/data\/arrays\.txt/, 'get', function(options) {
            return {
                "code": "200",
                "data": [
                    [
                        "Tiger Nixon",
                        "System Architect",
                        "Edinburgh",
                        "5421",
                        "2011\/04\/25",
                        "$320,800"
                    ],
                    [
                        "Charde Marshall",
                        "Regional Director",
                        "San Francisco",
                        "6741",
                        "2008\/10\/16",
                        "$470,600"
                    ],
                    [
                        "Haley Kennedy",
                        "Senior Marketing Designer",
                        "London",
                        "3597",
                        "2012\/12\/18",
                        "$313,500"
                    ],
                    [
                        "Tatyana Fitzpatrick",
                        "Regional Director",
                        "London",
                        "1965",
                        "2010\/03\/17",
                        "$385,750"
                    ],
                    [
                        "Michael Silva",
                        "Marketing Designer",
                        "London",
                        "1581",
                        "2012\/11\/27",
                        "$198,500"
                    ],
                    [
                        "Paul Byrd",
                        "Chief Financial Officer (CFO)",
                        "New York",
                        "3059",
                        "2010\/06\/09",
                        "$725,000"
                    ],
                    [
                        "Gloria Little",
                        "Systems Administrator",
                        "New York",
                        "1721",
                        "2009\/04\/10",
                        "$237,500"
                    ],
                    [
                        "Bradley Greer",
                        "Software Engineer",
                        "London",
                        "2558",
                        "2012\/10\/13",
                        "$132,000"
                    ],
                    [
                        "Dai Rios",
                        "Personnel Lead",
                        "Edinburgh",
                        "2290",
                        "2012\/09\/26",
                        "$217,500"
                    ],
                    [
                        "Jenette Caldwell",
                        "Development Lead",
                        "New York",
                        "1937",
                        "2011\/09\/03",
                        "$345,000"
                    ],
                    [
                        "Yuri Berry",
                        "Chief Marketing Officer (CMO)",
                        "New York",
                        "6154",
                        "2009\/06\/25",
                        "$675,000"
                    ]
                ]

            }
        });


        //查阅 表格
        Mock.mock('/gap/consult_list', 'post', function(options) {
            //return 返回的数据在 success:function(data){}中接受，返回内容可以根据ajax传参判断是否返回内容
            //options.body就是前端发送到后台的参数，此接口为json方式，用JSON.parse(options.body)解析
            return {
                bodyData: [
                    { indicatorName: "燃气生产和供应业工业生产者出厂价格指数(上年=100)", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产者出厂价格指数(上年=100)", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产者出厂价格指数(上年=100)", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产者出厂价格指数(上年=100)", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产者出厂价格指数(上年=100)", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产者出厂价格指数(上年=100)", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产者出厂价格指数(上年=100)", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产者出厂价格指数(上年=100)", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产者出厂价格指数(上年=100)", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" }

                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"}
                ],
                pageNo: 1,
                pageSize: 2,
                //statusCode: 200,
                totalCount: 9,
            }
        });
//查阅 表格
        Mock.mock('/gap/assets_list', 'post', function(options) {
            //return 返回的数据在 success:function(data){}中接受，返回内容可以根据ajax传参判断是否返回内容
            //options.body就是前端发送到后台的参数，此接口为json方式，用JSON.parse(options.body)解析
            return {
                bodyData: [
                    { indicatorName: "燃气生产和供应业工业生产", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" },
                    { indicatorName: "燃气生产和供应业工业生产", area: "郑州", dataTime: "2017-01-05 10:20:20", value: "234.99", tableName: "居民消费和商品零售价格指数", checkTable: "查看报表" }

                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"},
                    // {giveDate: "2016年12月", comeDate: "2016-11-10 10:20:10", storageDate: "2016-11-10 10:20:10", state: "成功"}
                ],
                pageNo: 1,
                pageSize: 2,
                //statusCode: 200,
                totalCount: 9,
            }
        });
        //企业查询 大表格
        Mock.mock('/gap/companySearch_list', 'post', function(options) {
            //return 返回的数据在 success:function(data){}中接受，返回内容可以根据ajax传参判断是否返回内容
            //options.body就是前端发送到后台的参数，此接口为json方式，用JSON.parse(options.body)解析
            return {
                bodyData: [
                    { loanOrganization: "平顶山", releaseDate: "2016/09/18", deadLine: "2017/09/17", amountPaid: "3000", interestRate: "4.785" },
                    { loanOrganization: "平顶山", releaseDate: "2106/09/28", deadLine: "2017/09/27", amountPaid: "3000", interestRate: "4.785" },
                    { loanOrganization: "平顶山", releaseDate: "2017/01/17", deadLine: "2017/01/16", amountPaid: "3000", interestRate: "4.785" },
                    { loanOrganization: "平顶山", releaseDate: "2017/05/08", deadLine: "2017/05/07", amountPaid: "7000", interestRate: "4.785" }
                    // {loanOrganization: "平顶山", releaseDate: "2016/01/08", deadLine: "2016/09/18", amountPaid: "3000",interestRate:"4.785"},
                ],
                pageNo: 1,
                pageSize: 2,
                //statusCode: 200,
                totalCount: 9,
            }
        });
        //企业查询 左表格
        Mock.mock('/gap/companyLeftSearch_list', 'post', function(options) {
            //return 返回的数据在 success:function(data){}中接受，返回内容可以根据ajax传参判断是否返回内容
            //options.body就是前端发送到后台的参数，此接口为json方式，用JSON.parse(options.body)解析
            return {
                bodyData: [
                    { bondCode: "900sz112209", bondShort: "14雏鹰债", issueAmount: "8000", issueRate: "8.8" },
                    { bondCode: "900sz118535", bondShort: "16雏鹰债01", issueAmount: "6400", issueRate: "7.2" },
                    { bondCode: "900sz118668", bondShort: "16雏鹰债02", issueAmount: "8600", issueRate: "6.8" }
                ],
                pageNo: 1,
                pageSize: 2,
                //statusCode: 200,
                totalCount: 9,
            }
        });
        //企业查询 左表格
        Mock.mock('/gap/companyRightSearch_list', 'post', function(options) {
            //return 返回的数据在 success:function(data){}中接受，返回内容可以根据ajax传参判断是否返回内容
            //options.body就是前端发送到后台的参数，此接口为json方式，用JSON.parse(options.body)解析
            return {
                bodyData: [
                    { stockCode: "002477.sz", publishTime: "2010/09/15", issueAmount: "1172500" }
                ],
                pageNo: 1,
                pageSize: 2,
                //statusCode: 200,
                totalCount: 9,
            }
        });

    });
});
