-- =============================================
-- 餐厅点餐系统 - 完整数据（dishID = dish1, dish2...）
-- =============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 修改字段长度
-- ----------------------------
ALTER TABLE `dish` MODIFY COLUMN `dishID` VARCHAR(10);
ALTER TABLE `dish_image` MODIFY COLUMN `dish_id` VARCHAR(10);
ALTER TABLE `orderdetail` MODIFY COLUMN `dishID` VARCHAR(10);

ALTER TABLE `collect` MODIFY COLUMN `dishID` VARCHAR(10);

-- 然后重新执行插入语句

-- ----------------------------
-- 2. 清空所有表数据
-- ----------------------------
TRUNCATE `orderdetail`;
TRUNCATE `collect`;
TRUNCATE `comment`;
TRUNCATE `order`;
TRUNCATE `dish_image`;
TRUNCATE `dish`;
TRUNCATE `menu`;
TRUNCATE `customer`;
TRUNCATE `administrator`;
TRUNCATE `user`;

-- ----------------------------
-- 3. user 表（用户ID = 手机号）
-- ----------------------------
INSERT INTO `user` (`userID`, `userPassword`) VALUES
('root', 'admin123'),
('13800138001', '123456'),
('13800138002', '456789'),
('13800138003', '111111'),
('13800138004', '222222'),
('13800138005', '333333'),
('13800138006', '444444'),
('13800138007', '555555');

-- ----------------------------
-- 4. administrator 表（商家）
-- ----------------------------
INSERT INTO `administrator` (`userID`, `merchantName`) VALUES
('root', '美味餐厅');

-- ----------------------------
-- 5. customer 表（顾客）
-- ----------------------------
INSERT INTO `customer` (`userID`, `username`, `securityQuestion`, `securityAnswer`) VALUES
('13800138001', '张三', '我的生日', '20000101'),
('13800138002', '李四', '最喜欢的食物', '火锅'),
('13800138003', '王小明', '你的小学名字？', '第一小学'),
('13800138004', '赵小红', '你的出生城市？', '南京'),
('13800138005', '陈小华', '你的生日？', '1995-05-05'),
('13800138006', '13800138006', '你最喜欢的颜色？', '蓝色'),
('13800138007', '13800138007', '你的宠物名字？', '咪咪');

-- ----------------------------
-- 6. menu 表（菜单分类）
-- ----------------------------
INSERT INTO `menu` (`menuID`, `menuName`, `createTime`, `remark`) VALUES
('m1', '特色', NOW(), '招牌推荐'),
('m2', '素菜', NOW(), '清淡健康'),
('m3', '荤菜', NOW(), '招牌肉菜'),
('m4', '汤类', NOW(), '滋补靓汤'),
('m5', '主食', NOW(), '米饭面食'),
('m6', '饮料', NOW(), '饮料酒水'),
('m7', '甜品', NOW(), '饭后甜点');

-- ----------------------------
-- 7. dish 表（菜品）
-- ----------------------------
INSERT INTO `dish` (`dishID`, `dishName`, `dishPrice`, `dishIntroduction`, `menuID`) VALUES
-- 特色（m1）
('dish1', '麻辣火锅', 68, '四川特色麻辣火锅，鲜香麻辣', 'm1'),
('dish2', '酸菜鱼', 58, '酸爽开胃，鱼片鲜嫩', 'm1'),
('dish3', '水煮牛肉', 68, '麻辣鲜香，牛肉嫩滑', 'm1'),
('dish4', '北京烤鸭', 88, '皮脆肉嫩，配薄饼酱料', 'm1'),
-- 素菜（m2）
('dish5', '番茄炒蛋', 18, '家常素菜，酸甜可口', 'm2'),
('dish6', '干煸豆角', 22, '香辣脆嫩，下饭好菜', 'm2'),
('dish7', '蒜蓉西兰花', 18, '清淡爽口，营养健康', 'm2'),
('dish8', '酸辣土豆丝', 12, '酸辣脆爽，家常必点', 'm2'),
-- 荤菜（m3）
('dish9', '红烧肉', 38, '肥而不腻，入口即化', 'm3'),
('dish10', '宫保鸡丁', 32, '酸甜微辣，鸡肉嫩滑', 'm3'),
('dish11', '糖醋排骨', 45, '酸甜可口，外酥里嫩', 'm3'),
('dish12', '青椒肉丝', 25, '青椒脆嫩，肉丝鲜美', 'm3'),
-- 汤类（m4）
('dish13', '排骨玉米汤', 28, '清甜滋补，营养美味', 'm4'),
('dish14', '西红柿蛋汤', 12, '家常汤品，酸甜开胃', 'm4'),
('dish15', '紫菜蛋花汤', 10, '清淡爽口，简单美味', 'm4'),
-- 主食（m5）
('dish16', '米饭', 2, '香软米饭', 'm5'),
('dish17', '蛋炒饭', 15, '鸡蛋炒饭，香气扑鼻', 'm5'),
('dish18', '牛肉面', 20, '手工面条，牛肉鲜嫩', 'm5'),
('dish19', '手工水饺', 18, '手工水饺，馅大皮薄', 'm5'),
-- 饮料（m6）
('dish20', '可口可乐', 5, '冰爽碳酸饮料', 'm6'),
('dish21', '雪碧', 5, '冰爽柠檬味', 'm6'),
('dish22', '鲜榨橙汁', 12, '新鲜榨取，富含维C', 'm6'),
('dish23', '奶茶', 10, '香浓丝滑', 'm6'),
-- 甜品（m7）
('dish24', '冰淇淋', 8, '香草味，清凉解暑', 'm7'),
('dish25', '红豆双皮奶', 12, '奶香浓郁，红豆绵软', 'm7'),
('dish26', '芒果布丁', 10, 'Q弹爽滑，芒果香甜', 'm7'),
('dish27', '芝士蛋糕', 15, '芝士浓郁，入口即化', 'm7');

-- ----------------------------
-- 8. dish_image 表（菜品图片）
-- ----------------------------
INSERT INTO `dish_image` (`dish_id`, `dish_name`, `image_url`, `create_time`, `update_time`) VALUES
('dish1', '麻辣火锅', '/images/dish/hotpot.jpg', NOW(), NOW()),
('dish2', '酸菜鱼', '/images/dish/sauerkraut_fish.jpg', NOW(), NOW()),
('dish3', '水煮牛肉', '/images/dish/boiled_beef.jpg', NOW(), NOW()),
('dish4', '北京烤鸭', '/images/dish/peking_duck.jpg', NOW(), NOW()),
('dish5', '番茄炒蛋', '/images/dish/tomato_egg.jpg', NOW(), NOW()),
('dish6', '干煸豆角', '/images/dish/dry_fried_beans.jpg', NOW(), NOW()),
('dish7', '蒜蓉西兰花', '/images/dish/garlic_broccoli.jpg', NOW(), NOW()),
('dish8', '酸辣土豆丝', '/images/dish/sour_spicy_potato.jpg', NOW(), NOW()),
('dish9', '红烧肉', '/images/dish/braised_pork.jpg', NOW(), NOW()),
('dish10', '宫保鸡丁', '/images/dish/kung_pao_chicken.jpg', NOW(), NOW()),
('dish11', '糖醋排骨', '/images/dish/sweet_sour_ribs.jpg', NOW(), NOW()),
('dish12', '青椒肉丝', '/images/dish/green_pepper_pork.jpg', NOW(), NOW()),
('dish13', '排骨玉米汤', '/images/dish/ribs_corn_soup.jpg', NOW(), NOW()),
('dish14', '西红柿蛋汤', '/images/dish/tomato_egg_soup.jpg', NOW(), NOW()),
('dish15', '紫菜蛋花汤', '/images/dish/seaweed_egg_soup.jpg', NOW(), NOW()),
('dish16', '米饭', '/images/dish/rice.jpg', NOW(), NOW()),
('dish17', '蛋炒饭', '/images/dish/fried_rice.jpg', NOW(), NOW()),
('dish18', '牛肉面', '/images/dish/beef_noodles.jpg', NOW(), NOW()),
('dish19', '手工水饺', '/images/dish/dumplings.jpg', NOW(), NOW()),
('dish20', '可口可乐', '/images/dish/coke.jpg', NOW(), NOW()),
('dish21', '雪碧', '/images/dish/sprite.jpg', NOW(), NOW()),
('dish22', '鲜榨橙汁', '/images/dish/orange_juice.jpg', NOW(), NOW()),
('dish23', '奶茶', '/images/dish/milk_tea.jpg', NOW(), NOW()),
('dish24', '冰淇淋', '/images/dish/ice_cream.jpg', NOW(), NOW()),
('dish25', '红豆双皮奶', '/images/dish/double_skin_milk.jpg', NOW(), NOW()),
('dish26', '芒果布丁', '/images/dish/mango_pudding.jpg', NOW(), NOW()),
('dish27', '芝士蛋糕', '/images/dish/cheese_cake.jpg', NOW(), NOW());

-- ----------------------------
-- 9. order 表（订单）
-- ----------------------------
INSERT INTO `order` (`orderID`, `userID`, `orderPrice`, `orderTime`, `orderNote`, `orderStatus`) VALUES
('o01', '13800138001', 86, '2026-05-20 12:00:00', '不要辣', '2'),
('o02', '13800138002', 60, '2026-05-20 12:30:00', '多放葱', '1'),
('o03', '13800138003', 68, '2026-05-21 18:00:00', '加辣', '0'),
('o04', '13800138004', 106, '2026-05-21 19:00:00', '', '2'),
('o05', '13800138001', 26, '2026-05-22 12:00:00', '少盐', '1');

-- ----------------------------
-- 10. orderdetail 表（订单详情）
-- ----------------------------
INSERT INTO `orderdetail` (`orderID`, `dishID`, `dishNum`, `dishPrice`) VALUES
('o01', 'dish1', 1, 68),
('o01', 'dish5', 1, 18),
('o02', 'dish9', 1, 38),
('o02', 'dish5', 1, 18),
('o02', 'dish14', 1, 12),
('o03', 'dish1', 1, 68),
('o04', 'dish10', 1, 32),
('o04', 'dish11', 1, 45),
('o04', 'dish7', 1, 18),
('o05', 'dish6', 1, 22),
('o05', 'dish16', 2, 2);

-- ----------------------------
-- 11. comment 表（评论）
-- ----------------------------
INSERT INTO `comment` (`CommentID`, `OrderID`, `userID`, `Content`, `PublishTime`, `likes`) VALUES
('c01', 'o01', '13800138001', '麻辣火锅很正宗，番茄炒蛋也好吃！', NOW(), 5),
('c02', 'o02', '13800138002', '红烧肉肥而不腻，服务不错', NOW(), 3),
('c03', 'o04', '13800138004', '宫保鸡丁味道不错，下次还来', NOW(), 2),
('c04', 'o01', '13800138001', '价格实惠，份量足', NOW(), 1);

-- ----------------------------
-- 12. collect 表（收藏）
-- ----------------------------
INSERT INTO `collect` (`CollectID`, `dishID`, `userID`, `CollectTime`) VALUES
('cl01', 'dish1', '13800138001', NOW()),
('cl02', 'dish9', '13800138002', NOW()),
('cl03', 'dish10', '13800138003', NOW()),
('cl04', 'dish4', '13800138001', NOW()),
('cl05', 'dish24', '13800138004', NOW());

SET FOREIGN_KEY_CHECKS = 1;