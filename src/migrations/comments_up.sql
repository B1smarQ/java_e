CREATE TABLE `comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `author_id` int NOT NULL,
  `reply_to` int DEFAULT NULL,
  `reply_to_comment` int DEFAULT NULL,
  `body` varchar(256) NOT NULL,
  `creation_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `comments_ibfk_2_idx` (`author_id`),
  KEY `comments_ibfk_1_idx` (`reply_to`),
  KEY `FKrnq3dhmtqabifrt2yj7epll69` (`reply_to_comment`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`reply_to`) REFERENCES `posts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FKrnq3dhmtqabifrt2yj7epll69` FOREIGN KEY (`reply_to_comment`) REFERENCES `comments` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci