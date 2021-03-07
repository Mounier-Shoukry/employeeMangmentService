CREATE DATABASE IF NOT EXISTS workMotion;
USE workMotion;
CREATE TABLE IF NOT EXISTS `workMotion`.`employee`(`id` bigint NOT NULL AUTO_INCREMENT,`name` varchar(255) DEFAULT NULL,`email` varchar(255) NOT NULL,`age` int DEFAULT NULL,`status` varchar(50) DEFAULT NULL,PRIMARY KEY (`id`),UNIQUE KEY `email_UNIQUE` (`email`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

