-- 2주차 세션용 MySQL 실습 계정/DB 생성 스크립트
-- root 권한으로 1회 실행하세요.
CREATE DATABASE IF NOT EXISTS likelion_blog
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'likelion'@'localhost' IDENTIFIED BY 'Likelion2026!';
GRANT ALL PRIVILEGES ON likelion_blog.* TO 'likelion'@'localhost';
FLUSH PRIVILEGES;
