

# 1 无界面启动VirtualBox虚拟机

![image-20240522091145532](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522091145532.png)



测试是否启动成功

![image-20240522085759272](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522085759272.png)



# 2 启动并连接MySQL数据库

## 2.1 使用Vagrant 连接VritualBox的Linux

**需要到Vagrantfile 文件所在的目录下进入cmd命令行来连接Linux虚拟机**

![image-20240522090108947](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522090108947.png)



Vagrantfile 文件中的内容如下: 注意文件类型为   .   类型

~~~
# -*- mode: ruby -*-
# vi: set ft=ruby :

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://vagrantcloud.com/search.
  config.vm.box = "centos/7"

  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  # config.vm.box_check_update = false

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # NOTE: This will enable public access to the opened port
  # config.vm.network "forwarded_port", guest: 80, host: 8080

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine and only allow access
  # via 127.0.0.1 to disable public access
  # config.vm.network "forwarded_port", guest: 80, host: 8080, host_ip: "127.0.0.1"

  # Create a private network, which allows host-only access to the machine
  # using a specific IP.
config.vm.network "private_network", ip: "192.168.56.100"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  # config.vm.network "public_network"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  # config.vm.synced_folder "../data", "/vagrant_data"

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  # config.vm.provider "virtualbox" do |vb|
  #   # Display the VirtualBox GUI when booting the machine
  #   vb.gui = true
  #
  #   # Customize the amount of memory on the VM:
  #   vb.memory = "1024"
  # end
  #
  # View the documentation for the provider you are using for more
  # information on available options.

  # Enable provisioning with a shell script. Additional provisioners such as
  # Ansible, Chef, Docker, Puppet and Salt are also available. Please see the
  # documentation for more information about their specific syntax and use.
  # config.vm.provision "shell", inline: <<-SHELL
  #   apt-get update
  #   apt-get install -y apache2
  # SHELL
end

~~~





![image-20240522090352101](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522090352101.png)



VirtualBox  Linux虚拟机的ip如下：

![image-20240522090458226](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522090458226.png)



启动mysql

![image-20240522090730715](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522090730715.png)





## 2.2 使用SQLyog连接Mysql数据库



**后端数据库所在的VirtualBox Linux地址为 192.168.56.100**

**数据库使用的是docker 创建的 mysql**

**, 在docker中容器名为mysql**

**,启动命令为sudo docker start mysql**

**,查看是否启动成功的docker命令为sudo docker ps**

**用户名为root,密码为hsp**



![image-20240522092934354](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522092934354.png)



# 3 启动Nacos服务



![image-20240522091448103](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522091448103.png)



![image-20240522091707730](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522091707730.png)



# 4 启动后端项目的各个微服务模块

![image-20240522085022872](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522085022872.png)





# 5 启动前端项目

![image-20240522091314412](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522091314412.png)



# 6 前端登录

![image-20240522092059855](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522092059855.png)



![image-20240522092237193](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522092237193.png)



# 7 测试逻辑删除功能

1

![image-20240522093734992](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522093734992.png)

2

![image-20240522094003259](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522094003259.png)



3

![image-20240522094102643](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522094102643.png)



4

![image-20240522094352915](https://raw.githubusercontent.com/EXsYang/PicGo-images-hosting/main/images/image-20240522094352915.png)





# 8 分布式微服务mysql数据库建库建表相关的sql脚本执行语句



~~~
#1. 创建对应的数据库/表
CREATE DATABASE hspliving_commodity;
USE hspliving_commodity;
# 老韩说明
# commodity_category 是商品分类表
# 1.CHARSET=utf8mb4 能更好的处理中文编码(utf8mb4 兼容 4 字节的 unicode , 支持常见的表情图标)
# 2.如果字段是关键字，尽量使用 _ 线来区别
CREATE TABLE `commodity_category` (
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
`name` CHAR(50) NOT NULL COMMENT '名称', 
`parent_id` BIGINT NOT NULL COMMENT '父分类 id',
`cat_level` INT NOT NULL COMMENT '层级',
`is_show` TINYINT NOT NULL COMMENT '0 不显示，1 显示]',
`sort` INT NOT NULL COMMENT '排序',
`icon` CHAR(255) NOT NULL COMMENT '图标', 
`pro_unit` CHAR(50) NOT NULL COMMENT '统计单位', 
`pro_count` INT NOT NULL COMMENT '商品数量', 
PRIMARY KEY (`id`)
) CHARSET=utf8mb4 COMMENT='商品分类表';
SELECT * FROM commodity_category;

# 测试数据
# 第 1 组 家用电器
INSERT INTO
`commodity_category`(`id`,`name`,`parent_id`,`cat_level`,`is_show`,`sort`,`icon`,`pro_unit`,`pro_count`)
VALUES (1,'家用电器',0,1,1,0,'','',0);
INSERT INTO
`commodity_category`(`id`,`name`,`parent_id`,`cat_level`,`is_show`,`sort`,`icon`,`pro_unit`,`pro_count`)
VALUES (21,'大 家 电',1,2,1,0,'','',0);
INSERT INTO
`commodity_category`(`id`,`name`,`parent_id`,`cat_level`,`is_show`,`sort`,`icon`,`pro_unit`,`pro_count`)
VALUES (22,'厨卫大电',1,2,1,0,'','',0);
INSERT INTO
`commodity_category`(`id`,`name`,`parent_id`,`cat_level`,`is_show`,`sort`,`icon`,`pro_unit`,`pro_count`)
VALUES (201,'燃气灶',22,3,1,0,'','',0),(202,'油烟机',22,3,1,0,'','',0);
INSERT INTO
`commodity_category`(`id`,`name`,`parent_id`,`cat_level`,`is_show`,`sort`,`icon`,`pro_unit`,`pro_count`)
VALUES (301,'平板电视',21,3,1,0,'','',0);
# 第 2 组 家居家装
INSERT INTO
`commodity_category`(`id`,`name`,`parent_id`,`cat_level`,`is_show`,`sort`,`icon`,`pro_unit`,`pro_count`)
VALUES (2,'家居家装',0,1,1,0,'','',0);
INSERT INTO
`commodity_category`(`id`,`name`,`parent_id`,`cat_level`,`is_show`,`sort`,`icon`,`pro_unit`,`pro_count`)
VALUES (41,'家纺',2,2,1,0,'','',0);

INSERT INTO
`commodity_category`(`id`,`name`,`parent_id`,`cat_level`,`is_show`,`sort`,`icon`,`pro_unit`,`pro_count`)
VALUES (601,'桌布/罩件',41,3,1,0,'','',0);
INSERT INTO
`commodity_category`(`id`,`name`,`parent_id`,`cat_level`,`is_show`,`sort`,`icon`,`pro_unit`,`pro_count`)
VALUES (602,'地毯地垫',41,3,1,0,'','',0);
INSERT INTO
`commodity_category`(`id`,`name`,`parent_id`,`cat_level`,`is_show`,`sort`,`icon`,`pro_unit`,`pro_count`)
VALUES (42,'灯具',2,2,1,0,'','',0);
INSERT INTO `commodity_category`(`id`,`name`,`parent_id`,`cat_level`,`is_show`,`sort`,`icon`,`pro_unit`,`pro_count`)
VALUES (651,'台灯',42,3,1,0,'','',0);
INSERT INTO `commodity_category`(`id`,`name`,`parent_id`,`cat_level`,`is_show`,`sort`,`icon`,`pro_unit`,`pro_count`)
VALUES (659,'西瓜灯',42,3,1,0,'','',0);

SELECT * FROM `commodity_category`;

SELECT * FROM `commodity_category` WHERE `name` LIKE '%灯%';
# WHERE `name` LIKE '%%'; 相当于查询所有
SELECT * FROM `commodity_category` WHERE `name` LIKE '%%';




# COMMENT，它是用来添加对列或表的注释，方便开发者和其他人员理解该列或表的作用、规则或其他相关信息。
# 在你的语句中，'id' 这个注释提供了关于 id 列的额外说明，这在维护和理解数据库结构时可能会很有用。
## 家居品牌表
USE hspliving_commodity
CREATE TABLE `commodity_brand` (
id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id', 
`name` CHAR(50) COMMENT '品牌名',
logo VARCHAR(1200) COMMENT 'logo',
description LONGTEXT COMMENT '说明',
isShow TINYINT COMMENT '显示',
first_letter CHAR(1) COMMENT '检索首字母',
sort INT COMMENT '排序',
PRIMARY KEY (id)
)CHARSET=utf8mb4 COMMENT='家居品牌';

# 家居品牌
# sort 排序值, 有时很难确定，不管给什么 int 值,都不是很合适,这里给 null
# 家居品牌测试数据
INSERT INTO
`commodity_brand` (id,`name`, logo,description,isShow,first_letter,sort)
VALUES(1, '海信','','',1,'',NULL);
SELECT * FROM `commodity_brand`;




# 家居商品属性分组表
USE hspliving_commodity;
CREATE TABLE `commodity_attrgroup` (
id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id', 
`name` CHAR(20) COMMENT '组名', 
sort INT COMMENT '排序', 
description VARCHAR(255) COMMENT '说明',
icon VARCHAR(255) COMMENT '组图标', 
category_id BIGINT COMMENT '所属分类 id', 
PRIMARY KEY (id)
)CHARSET=utf8mb4 COMMENT='家居商品属性分组';

# 测试数据【后面我们通过管理系统来完成增删改查】, 我们的家居商品属性分组
#，是针对第三级家居分类的, 第一级和第二级没有商品属性分组信息
INSERT INTO
`commodity_attrgroup` (id,`name`, sort,description,icon,category_id)
VALUES(1, '主体',0,'主体说明','',301);
INSERT INTO
`commodity_attrgroup` (id,`name`, sort,description,icon,category_id)
VALUES(2, '规格',0,'规格说明','',301);
INSERT INTO
`commodity_attrgroup` (id,`name`, sort,description,icon,category_id)
VALUES(3, '功能',0,'功能说明','',301);
## 测试数据
SELECT * FROM `commodity_attrgroup`;

#SELECT id,icon,name,description,sort,category_id FROM commodity_attrgroup WHERE id=4




#1. 创建数据表
/*==============================================================*/
/*1. 品牌分类关联表
/*2. 一个分类下可以对应多个品牌，比如 电视，可以对应海信、小米、夏普品牌
/*3. 一个品牌可以对应多个分类，比如 小米品牌，可以对应电视，空调, 电热水器等
/*4. 对于两种表 brand 和 category 是多对多的关系时，通常数据库的设计会搞一个中间
表，关联二者的关系
/*5. 注意，对于大表，会根据需要设计一些冗余字段来提高效率，比如这里的 brand_name
和 category_name
/*==============================================================*/
USE hspliving_commodity;

# 创建品牌分类关联表 `commodity_category_brand_relation`
CREATE TABLE `commodity_category_brand_relation`
(
id BIGINT NOT NULL AUTO_INCREMENT,
brand_id BIGINT COMMENT '品牌 id',
category_id BIGINT COMMENT '分类 id',
brand_name VARCHAR(255) COMMENT '品牌名称',
category_name VARCHAR(255) COMMENT '分类名称',
PRIMARY KEY (id)
)CHARSET=utf8mb4 COMMENT='品牌分类关联表';

SELECT * FROM `commodity_category_brand_relation`;



/*===================================================*/
/* 1. 商品属性表
/* 2. 商品属性分为(业务设计): 销售属性[比如颜色，尺寸等], 基本属性 [比如: 质保年限，上市时间]
/* 3. 重要说明(我们的业务重要规定): 对于 基本属性 一般是归属于/关联一个商品属性组 ,(而且只能被关联 1 次)
/*==================================================*/

USE hspliving_commodity;

CREATE TABLE commodity_attr
(
attr_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '属性 id',
attr_name CHAR(30) COMMENT '属性名',
search_type TINYINT COMMENT '是否需要检索[0-不需要，1-需要]',
icon VARCHAR(255) COMMENT '图标',
value_select CHAR(255) COMMENT '可选值列表[用逗号分隔]',
attr_type TINYINT COMMENT '属性类型[0-销售属性，1-基本属性]',
ENABLE BIGINT COMMENT '启用状态[0 - 禁用，1 - 启用]',
category_id BIGINT COMMENT '所属分类',
show_desc TINYINT COMMENT '快速展示【是否展示在介绍上；0-否 1-是】
',
PRIMARY KEY (attr_id)
)CHARSET=utf8mb4 COMMENT='商品属性表';
SELECT * FROM `commodity_attr`;



/*===================================================*/
/*1. 创建数据表 , 保存 商品属性(基本属性)和商品属性组的关联关系
/*
/* 1. 商品属性和商品属性组的关联表 */
/*===================================================*/
USE hspliving_commodity;
CREATE TABLE `commodity_attr_attrgroup_relation`
(
id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id',
attr_id BIGINT COMMENT '属性 id',
attr_group_id BIGINT COMMENT '属性分组 id',
attr_sort INT COMMENT '属性组内排序',
PRIMARY KEY (id)
)CHARSET=utf8mb4 COMMENT='商品属性和商品属性组的关联表';

SELECT * FROM `commodity_attr_attrgroup_relation`;


# 批量删除属性组和属性的关联关系的sql语句
# 这些条件用 OR 组合在一个 DELETE 语句中，是在告诉数据库：
# “检查每一条记录，如果它符合这些条件组中的任何一个，那么删除它。
DELETE FROM `commodity_attr_attrgroup_relation` 
WHERE (`attr_id`=10 AND `attr_group_id` = 10) 
OR (`attr_id`=20 AND `attr_group_id` = 20);

# sql语句查询是，IN语句 中如果没有指定任何值，查询会报错，语法错误You have an error in your SQL syntax;
SELECT id,isshow,NAME,description,logo,sort,first_letter FROM commodity_brand WHERE (id IN ()); 
# sql语句查询是，IN语句 中如果指定值不存在，查询不会报错，只是查询结果为空
SELECT id,isshow,NAME,description,logo,sort,first_letter FROM commodity_brand WHERE (id IN (1000,2000)); 




/*==============================================================*/
/* 1. 保存商品 spu 信息
/* 2. 1 个 spu 信息可能对于多个 sku
/* 3. 1 个 spu+1 个 sku 就是一个商品的组合关系()
*/
/*==============================================================*/
USE hspliving_commodity;
CREATE TABLE `commodity_spu_info`
(
id BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品 id',
spu_name VARCHAR(200) COMMENT '商品名称',
spu_description VARCHAR(1000) COMMENT '商品描述',
catalog_id BIGINT COMMENT '所属分类 id',
brand_id BIGINT COMMENT '品牌 id',
weight DECIMAL(18,4),
publish_status TINYINT COMMENT '上架状态[0 - 新建,1 - 上架,2 - 下架]',
create_time DATETIME,
update_time DATETIME,
PRIMARY KEY (id)
)CHARSET=utf8mb4 COMMENT='商品 spu 信息';

SELECT * FROM commodity_spu_info;

UPDATE `commodity_spu_info` SET publish_status = 0,update_time=NOW() WHERE id = 100

/*======================================================*/
/* 1. 保存商品 spu 的介绍图片,单独创建一张表, 可能有多张图片路径(使用,隔开)，也可以没有*/
/* 2. 这里的主键 商品 id 即spu_id 和前面的表commodity_spu_info的id关联，因此这里没有设置为自增长的
/*=====================================================*/
USE hspliving_commodity;
CREATE TABLE `commodity_spu_info_desc`
(
spu_id BIGINT NOT NULL COMMENT '商品 id', 
decript LONGTEXT COMMENT '商品介绍图片', 
PRIMARY KEY (spu_id)
)CHARSET=utf8mb4 COMMENT='商品 spu 信息介绍';

SELECT * FROM commodity_spu_info_desc;



/*======================================================*/
/* 1. 保存商品 spu 的介绍图片集, 就是商品最前面的按一组图片来展示图片的集合 , 点击可以切换图片
/*======================================================*/
USE hspliving_commodity;
CREATE TABLE `commodity_spu_images`
(
id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id', spu_id BIGINT COMMENT 'spu_id',
img_name VARCHAR(200) COMMENT '图片名',
img_url VARCHAR(255) COMMENT '图片地址',
img_sort INT COMMENT '顺序', 
default_img TINYINT COMMENT '是否默认图', 
PRIMARY KEY (id)
)CHARSET=utf8mb4 COMMENT='spu 图片集';

SELECT * FROM commodity_spu_images;



/*====================================================*/
/* 1. 保存商品 spu 基本属性值, 有多个 */
/*====================================================*/
USE hspliving_commodity;
CREATE TABLE `commodity_product_attr_value`
(
id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id', 
spu_id BIGINT COMMENT '商品 id', 
attr_id BIGINT COMMENT '属性 id', 
attr_name VARCHAR(200) COMMENT '属性名', 
attr_value VARCHAR(200) COMMENT '属性值', 
attr_sort INT COMMENT '顺序', 
quick_show TINYINT COMMENT '快速展示【是否展示在介绍上；0-否 1-是】', 
PRIMARY KEY (id)
)CHARSET=utf8mb4 COMMENT='spu 基本属性值';

SELECT * FROM `commodity_product_attr_value`;


#创建保存 sku 的基本信息的表

USE hspliving_commodity;
CREATE TABLE `commodity_sku_info`
(
sku_id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'skuId', 
spu_id BIGINT COMMENT 'spuId', 
sku_name VARCHAR(255) COMMENT 'sku 名称', 
sku_desc VARCHAR(2000) COMMENT 'sku 介绍描述', 
catalog_id BIGINT COMMENT '所属分类 id', 
brand_id BIGINT COMMENT '品牌 id', 
sku_default_img VARCHAR(255) COMMENT '默认图片', 
sku_title VARCHAR(255) COMMENT '标题', 
sku_subtitle VARCHAR(2000) COMMENT '副标题', 
price DECIMAL(18,4) COMMENT '价格',
sale_count BIGINT COMMENT '销量', 
PRIMARY KEY (sku_id)
)CHARSET=utf8mb4 COMMENT='sku 信息';

SELECT * FROM commodity_sku_info;




/*==============================================================*/
/* 1. 保存 某一个 sku 对应的图片[1 个 sku 可能有多个图片]
/*=====================================================*/
USE hspliving_commodity;
CREATE TABLE `commodity_sku_images`
(
id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id', 
sku_id BIGINT COMMENT 'sku_id',
img_url VARCHAR(255) COMMENT '图片地址',
img_sort INT COMMENT '排序', 
default_img INT COMMENT '默认图[0 - 不是默认图，1 - 是默认图]', 
PRIMARY KEY (id)
)CHARSET=utf8mb4 COMMENT='sku 图片';

SELECT * FROM commodity_sku_images;


/*====================================================*/
/* 1.保存 sku 的销售属性/值, 一个 sku 可以有多个销售属性/值
/* 2.比如 1 个 sku 有颜色(黑色)和尺寸(100*300)两个销售属性
/*=====================================================*/
USE hspliving_commodity;
CREATE TABLE `commodity_sku_sale_attr_value`
(
id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id', 
sku_id BIGINT COMMENT 'sku_id', 
attr_id BIGINT COMMENT 'attr_id', 
attr_name VARCHAR(200) COMMENT '销售属性名', 
attr_value VARCHAR(200) COMMENT '销售属性值', 
attr_sort INT COMMENT '顺序', 
PRIMARY KEY (id)
)CHARSET=utf8mb4 COMMENT='sku 的销售属性/值表';

SELECT * FROM commodity_sku_sale_attr_value;
~~~

