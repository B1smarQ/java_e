CREATE TABLE `logs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `log_level` varchar(10) DEFAULT NULL,
  `metadata` varchar(256) DEFAULT NULL,
  `time_stamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `times_stamp` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci