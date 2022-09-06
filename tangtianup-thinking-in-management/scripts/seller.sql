/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.6.36 : Database - seller
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`seller` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `seller`;

/*Table structure for table `order_t` */

DROP TABLE IF EXISTS `order_t`;

CREATE TABLE `order_t` (
  `order_id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '订单编号',
  `chan_id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '渠道编号',
  `product_id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '产品编号',
  `chan_user_id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '渠道用户编号',
  `order_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '类型,APPLY:申购,REDEEM:赎回',
  `order_status` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '状态,INIT:初始化,PROCESS:处理中,SUCESS:成功,FAIL:失败',
  `outer_order_id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '外部订单编号',
  `amount` decimal(15,3) NOT NULL COMMENT '金额',
  `memo` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `update_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
