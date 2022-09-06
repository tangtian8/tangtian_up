/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 5.6.36 : Database - manager
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`manager` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `manager`;

/*Table structure for table `product` */

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '产品编号',
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '产品名称',
  `threshold_amount` decimal(15,3) NOT NULL COMMENT '起投金额',
  `step_amount` decimal(15,3) NOT NULL COMMENT '投资步长',
  `lock_term` smallint(6) NOT NULL COMMENT '锁定期',
  `reward_rate` decimal(5,3) NOT NULL COMMENT '收益率,0-100 百分比值',
  `status` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '状态,AUDINTING:审核中,IN_SELL:销售中,LOCKED:暂停销售,FINISHED:已结束',
  `memo` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建者',
  `update_at` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
