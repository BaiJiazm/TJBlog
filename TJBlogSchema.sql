
DROP database IF EXISTS DBTJBlog;

CREATE database DBTJBlog DEFAULT character set utf8 collate utf8_general_ci;

USE DBTJBlog;

-- 附件表
DROP TABLE IF EXISTS `t_attach`;

CREATE TABLE `t_attach` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `file_name` varchar(100) NOT NULL DEFAULT '',
  `file_type` varchar(50) DEFAULT '',
  `file_key` varchar(100) NOT NULL DEFAULT '',
  `author_id` int(10) DEFAULT NULL,
  `created` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 日志表
DROP TABLE IF EXISTS `t_log`;

CREATE TABLE `t_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT ,
  `action` varchar(100) DEFAULT NULL ,
  `data` varchar(2000) DEFAULT NULL ,
  `author_id` int(10) DEFAULT NULL ,
  `ip` varchar(20) DEFAULT NULL ,
  `created` int(10) DEFAULT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 评论表
DROP TABLE IF EXISTS `t_comment`;

CREATE TABLE `t_comment` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `content_id` int(10) unsigned DEFAULT '0' ,
  `created` int(10) unsigned DEFAULT '0' ,
  `author` varchar(200) DEFAULT NULL ,
  `author_id` int(10) unsigned DEFAULT '0' ,
  `owner_id` int(10) unsigned DEFAULT '0' ,
  `mail` varchar(200) DEFAULT NULL ,
  `url` varchar(200) DEFAULT NULL ,
  `ip` varchar(64) DEFAULT NULL  ,
  `agent` varchar(200) DEFAULT NULL  ,
  `content` text ,
  `type` varchar(16) DEFAULT 'comment' ,
  `status` varchar(16) DEFAULT 'approved' ,
  `parent` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `content_id` (`content_id`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 内容表
DROP TABLE IF EXISTS `t_content`;

CREATE TABLE `t_content` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT ,
  `title` varchar(200) DEFAULT NULL ,
  `slug` varchar(200) DEFAULT NULL ,
  `created` int(10) unsigned DEFAULT '0' ,
  `modified` int(10) unsigned DEFAULT '0' ,
  `content` text COMMENT '内容文字',
  `author_id` int(10) unsigned DEFAULT '0' ,
  `type` varchar(16) DEFAULT 'post'  ,
  `status` varchar(16) DEFAULT 'publish' ,
  `tags` varchar(200) DEFAULT NULL ,
  `categories` varchar(200) DEFAULT NULL ,
  `hits` int(10) unsigned DEFAULT '0' ,
  `comments_number` int(10) unsigned DEFAULT '0' ,
  `allow_comment` tinyint(1) DEFAULT '1' ,
  `allow_ping` tinyint(1) DEFAULT '1' ,
  `allow_feed` tinyint(1) DEFAULT '1' ,
  PRIMARY KEY (`id`),
  UNIQUE KEY `slug` (`slug`),
  KEY `created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_content` WRITE;

INSERT INTO `t_content` (`id`, `title`, `slug`, `created`, `modified`, `content`, `author_id`, `type`, `status`, `tags`, `categories`, `hits`, `comments_number`, `allow_comment`, `allow_ping`, `allow_feed`)
VALUES
	(1,'about my blog','about',1487853610,1487872488,'### Hello World\r\n\r\nabout me\r\n\r\n### ...\r\n\r\n...',1,'page','publish',NULL,NULL,0,0,1,1,1),
	(2,'Hello My Blog',NULL,1487861184,1487872798,'## Hello  World.\r\n\r\n> ...\r\n\r\n----------\r\n\r\n\r\n<!--more-->\r\n\r\n```java\r\npublic static void main(String[] args){\r\n    System.out.println(\"Hello 13 Blog.\");\r\n}\r\n```',1,'post','publish','','default',10,0,1,1,1);

UNLOCK TABLES;

-- 元信息表
DROP TABLE IF EXISTS `t_meta`;

CREATE TABLE `t_meta` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `slug` varchar(200) DEFAULT NULL,
  `type` varchar(32) NOT NULL DEFAULT '' ,
  `description` varchar(200) DEFAULT NULL ,
  `sort` int(10) unsigned DEFAULT '0',
  `parent` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_meta` WRITE;

INSERT INTO `t_meta` (`id`, `name`, `slug`, `type`, `description`, `sort`, `parent`)
VALUES
	(1,'default',NULL,'category',NULL,0,0),
	(6,'my github','https://github.com/BaiJiazm','link',NULL,0,0);

UNLOCK TABLES;

-- 选项表
DROP TABLE IF EXISTS `t_option`;

CREATE TABLE `t_option` (
  `name` varchar(32) NOT NULL DEFAULT '',
  `value` varchar(1000) DEFAULT '',
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_option` WRITE;

INSERT INTO `t_option` (`name`, `value`, `description`)
VALUES
	('site_title','My Blog',''),
	('social_weibo','',NULL),
	('social_zhihu','',NULL),
	('social_github','',NULL),
	('social_twitter','',NULL),
	('site_theme','default',NULL),
	('site_keywords','13 Blog',NULL),
	('site_description','13 Blog',NULL),
	('site_record','','备案号');

UNLOCK TABLES;

-- 关系表
DROP TABLE IF EXISTS `t_relationship`;

CREATE TABLE `t_relationship` (
  `content_id` int(10) unsigned NOT NULL,
  `meta_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`content_id`,`meta_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `t_relationship` WRITE;

INSERT INTO `t_relationship` (`content_id`, `meta_id`)
VALUES
	(2,1);

UNLOCK TABLES;

-- 用户表
DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `home_url` varchar(200) DEFAULT NULL,
  `screen_name` varchar(32) DEFAULT NULL,
  `created` int(10) unsigned DEFAULT '0',
  `activated` int(10) unsigned DEFAULT '0' ,
  `logged` int(10) unsigned DEFAULT '0',
  `group_name` varchar(16) DEFAULT 'visitor' ,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `mail` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `t_user` (`id`, `name`, `password`, `email`, `home_url`, `screen_name`, `created`, `activated`, `logged`, `group_name`)
VALUES
	(1, 'admin', 'a66abb5684c45962d887564f08346e8d', 'b001100@qq.com', NULL, 'admin', 1490756162, 0, 0, 'visitor');