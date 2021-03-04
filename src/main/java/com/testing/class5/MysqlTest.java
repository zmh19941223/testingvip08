package com.testing.class5;

import com.testing.common.MysqlUtils;

/**
 * @Classname MysqlTest
 * @Description 类型说明
 * @Date 2021/1/15 17:46
 * @Created by 特斯汀Roy
 */
public class MysqlTest {
    public static void main(String[] args) {
        MysqlUtils mysql=new MysqlUtils();
        mysql.createConnector();
        System.out.println(mysql.queryResult("select goods_name,goods_id,shop_price from tp_goods where goods_name='VIP08商品roy' "));

    }
}
