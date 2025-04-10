-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.7.31 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  12.4.0.6659
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 tuijian 的数据库结构
CREATE DATABASE IF NOT EXISTS `tuijian` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `tuijian`;

-- 导出  表 tuijian.collect 结构
CREATE TABLE IF NOT EXISTS `collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `anther_id` int(11) NOT NULL,
  `person_id` int(11) NOT NULL,
  `file_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- 正在导出表  tuijian.collect 的数据：~1 rows (大约)
DELETE FROM `collect`;
INSERT INTO `collect` (`id`, `anther_id`, `person_id`, `file_id`) VALUES
	(12, 1, 1, 1);

-- 导出  表 tuijian.filecontent 结构
CREATE TABLE IF NOT EXISTS `filecontent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `upload` datetime DEFAULT NULL,
  `collect` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `file_image` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT '0' COMMENT '0代表java，1代表c，2代表html',
  `comment` bigint(20) DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `down_count` bigint(20) unsigned DEFAULT '0',
  `user_image` varchar(255) DEFAULT NULL,
  `price` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- 正在导出表  tuijian.filecontent 的数据：~17 rows (大约)
DELETE FROM `filecontent`;
INSERT INTO `filecontent` (`id`, `user_id`, `file_name`, `upload`, `collect`, `title`, `file_path`, `file_image`, `type`, `comment`, `user_name`, `down_count`, `user_image`, `price`) VALUES
	(1, 1, 'java课程设计', '2024-02-01 00:00:11', 1, 'java程序设计等你来', '/file/1', 'https://picst.sunbangyan.cn/2024/01/31/bac0d491e32eff85c7868d6bfeb9fa75.jpeg', 0, 15, NULL, 0, NULL, NULL),
	(2, 1, 'c语言课程设计', '2024-02-01 14:55:37', 0, 'c等你来', '/file/1', 'https://picst.sunbangyan.cn/2024/01/31/bac0d491e32eff85c7868d6bfeb9fa75.jpeg', 0, 12, NULL, 0, NULL, NULL),
	(3, 1, 'java课程设计', '2024-02-01 15:13:19', 0, 'java程序设计等你来', '/file/1', 'https://picst.sunbangyan.cn/2024/01/31/bac0d491e32eff85c7868d6bfeb9fa75.jpeg', 0, 3, NULL, 0, NULL, NULL),
	(5, 1, 'java课程设计', '2024-02-01 15:14:33', 0, 'java程序设计等你来', '/file/1', 'https://picst.sunbangyan.cn/2024/01/31/bac0d491e32eff85c7868d6bfeb9fa75.jpeg', 0, 35, NULL, 0, NULL, NULL),
	(6, 1, '1', '2024-10-08 00:00:00', NULL, '1', '', '', 0, NULL, 'user', 0, NULL, NULL),
	(7, 1, '1', '2024-10-08 00:00:00', NULL, '1', '', '', 0, NULL, 'user', 0, NULL, NULL),
	(8, 1, '1', '2024-10-08 00:00:00', NULL, '1', '', '', 0, NULL, 'user', 0, NULL, NULL),
	(9, 1, '1', '2024-10-08 00:00:00', NULL, '1', '', '', 0, NULL, 'user', 0, NULL, NULL),
	(10, 1, '2', '2024-10-08 00:00:00', NULL, '1', '', '', 1, NULL, 'user', 0, NULL, NULL),
	(11, 1, '1', '2024-10-08 00:00:00', NULL, '1', '', '', 0, NULL, 'user', 0, NULL, NULL),
	(12, 1, '1', '2024-10-08 00:00:00', NULL, '1', '', '', 0, NULL, 'user', 0, NULL, NULL),
	(13, 1, '1', '2024-10-08 00:00:00', NULL, '1', '', '', 0, NULL, 'user', 0, NULL, NULL),
	(14, 1, '1', '2024-10-08 00:00:00', NULL, '1', '', '', 0, NULL, 'user', 0, NULL, NULL),
	(15, 1, '1', '2024-10-08 00:00:00', NULL, '1', NULL, NULL, 0, NULL, 'user', 0, NULL, NULL),
	(16, 1, '1', '2024-10-08 00:00:00', NULL, '1', '/blogs/7/5/0148ee23-de33-4d97-8b45-3cc5a0111bf3.txt', '/blogs/4/13/0d9adc0c-025c-4ac8-a7fe-0741f938b472.jpg', 1, NULL, 'user', 0, NULL, NULL),
	(17, 1, '1', '2024-10-08 00:00:00', NULL, '1', '/blogs/2/0/c8f5f21e-8d62-410f-b820-a772500e5a60.txt', '', 0, NULL, 'user', 0, NULL, NULL),
	(18, 1, '1', '2024-10-08 00:00:00', NULL, '1', '', NULL, 1, NULL, 'user', 0, NULL, NULL);

-- 导出  表 tuijian.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `userLKnm` varchar(100) DEFAULT NULL,
  `comment_user` bigint(10) DEFAULT '15',
  `down_count` bigint(20) DEFAULT '0',
  `user_icon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 正在导出表  tuijian.user 的数据：~1 rows (大约)
DELETE FROM `user`;
INSERT INTO `user` (`id`, `username`, `password`, `icon`, `userLKnm`, `comment_user`, `down_count`, `user_icon`) VALUES
	(1, 'user', '123123', '123.jpeg\r\n', '学生一号', 15, NULL, NULL);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
