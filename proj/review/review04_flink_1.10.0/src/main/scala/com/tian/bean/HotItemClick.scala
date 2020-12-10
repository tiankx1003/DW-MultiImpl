package com.tian.bean

/**
 * @author tiankx
 * @date 2020/12/8 19:22
 * @version 1.0.0
 */

/**
 * 全窗口输出的样例类
 *
 * @param itemID     商品id
 * @param clickCount 点击次数
 * @param windowEnd  窗口结束时间
 */
case class HotItemClick(itemID: Long, clickCount: Long, windowEnd: Long)

