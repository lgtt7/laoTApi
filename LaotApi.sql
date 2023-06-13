/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.36 : Database - my_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`my_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `my_db`;

/*Table structure for table `interface_info` */

DROP TABLE IF EXISTS `interface_info`;

CREATE TABLE `interface_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(256) NOT NULL COMMENT '名称',
  `description` varchar(256) DEFAULT NULL COMMENT '描述',
  `url` varchar(512) NOT NULL COMMENT '接口地址',
  `requestParams` text NOT NULL COMMENT '请求参数',
  `requestHeader` text COMMENT '请求头',
  `responseHeader` text COMMENT '响应头',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '接口状态（0-关闭，1-开启）',
  `method` varchar(256) NOT NULL COMMENT '请求类型',
  `userId` bigint(20) NOT NULL COMMENT '创建人',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='接口信息';

/*Data for the table `interface_info` */

insert  into `interface_info`(`id`,`name`,`description`,`url`,`requestParams`,`requestHeader`,`responseHeader`,`status`,`method`,`userId`,`createTime`,`updateTime`,`isDelete`) values (1,'获取用户名','demo','/api/name/username','[\n  {\"name\":\"username\",\"type\":\"string\"}\n]','{\"Content-Type\":\"application/json\"}','{\"Content-Type\":\"application/json\"}',1,'post',1,'2023-06-04 08:33:25','2023-06-10 06:16:15',0),(2,'测试网关',NULL,'/api/name/username','[\n {\"name\":\"username\",\"type\":\"string\"}\n]','{\"Content-Type\":\"application/json\"}',NULL,1,'get',1,'2023-06-05 09:04:28','2023-06-07 07:45:02',1),(3,'deno','demo','/api/name/laot','无','{\"Content-Type\":\"application/json\"}','{\"Content-Type\":\"application/json\"}',1,'post',1,'2023-06-10 06:08:28','2023-06-10 06:15:37',0),(4,'getDemo','测试get方法的调用','/api/name','[\n  {\"name\":\"name\",\"type\":\"string\"}\n]','{\n \"Content-Type\":\"application/json\"\n}','{\n \"Content-Type\":\"application/json\"\n}',1,'get',1,'2023-06-10 07:21:59','2023-06-10 07:23:23',0);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userName` varchar(256) DEFAULT NULL COMMENT '用户昵称',
  `userAccount` varchar(256) NOT NULL COMMENT '账号',
  `userAvatar` varchar(1024) DEFAULT NULL COMMENT '用户头像',
  `gender` tinyint(4) DEFAULT NULL COMMENT '性别',
  `userRole` varchar(256) NOT NULL DEFAULT 'user' COMMENT '用户角色：user / admin',
  `userPassword` varchar(512) NOT NULL COMMENT '密码',
  `accessKey` varchar(512) NOT NULL COMMENT 'accessKey',
  `secretKey` varchar(512) NOT NULL COMMENT 'secretKey',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_userAccount` (`userAccount`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户';

/*Data for the table `user` */

insert  into `user`(`id`,`userName`,`userAccount`,`userAvatar`,`gender`,`userRole`,`userPassword`,`accessKey`,`secretKey`,`createTime`,`updateTime`,`isDelete`) values (1,'laot','laot',NULL,NULL,'admin','b0dd3697a192885d7c055db46155b26a','cd7fb4a6557ce84221048d1aeeee75ec','8f296515f449c57f655718d509512838','2023-06-04 07:38:03','2023-06-04 07:38:57',0);

/*Table structure for table `user_interface_info` */

DROP TABLE IF EXISTS `user_interface_info`;

CREATE TABLE `user_interface_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` bigint(20) NOT NULL COMMENT '调用用户 id',
  `interfaceInfoId` bigint(20) NOT NULL COMMENT '接口 id',
  `totalNum` int(11) NOT NULL DEFAULT '0' COMMENT '总调用次数',
  `leftNum` int(11) NOT NULL DEFAULT '0' COMMENT '剩余调用次数',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0-正常，1-禁用',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除(0-未删, 1-已删)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='用户调用接口关系';

/*Data for the table `user_interface_info` */

insert  into `user_interface_info`(`id`,`userId`,`interfaceInfoId`,`totalNum`,`leftNum`,`status`,`createTime`,`updateTime`,`isDelete`) values (1,1,1,1014,99986,0,'2023-06-07 07:02:47','2023-06-10 06:06:48',0),(2,1,3,4,99999997,0,'2023-06-10 06:18:46','2023-06-10 09:14:37',0),(3,1,4,0,100000000,0,'2023-06-10 07:24:19','2023-06-10 07:24:19',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
