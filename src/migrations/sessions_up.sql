CREATE TABLE `session` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created` datetime(6) DEFAULT NULL,
  `expiry_time` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci