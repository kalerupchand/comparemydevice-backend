-- V1__baseline.sql  (MySQL 8 / MariaDB 10.4+)
-- First-time baseline for CompareMyDevice on MySQL.
-- Uses utf8mb4 & InnoDB. No drops; Flyway will create history automatically.

SET NAMES utf8mb4;

-- Optional but recommended defaults
SET @old_sql_notes = @@sql_notes;
SET sql_notes = 0;

-- Ensure default charset/collation at DB level (safe if already set)
-- ALTER DATABASE `u229745468_comparemydevic` CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

-- brand
CREATE TABLE IF NOT EXISTS `brand` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `slug` VARCHAR(150) NOT NULL,
  `logo_url` TEXT,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_brand_name` (`name`),
  UNIQUE KEY `uk_brand_slug` (`slug`),
  KEY `idx_brand_slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- category
CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `slug` VARCHAR(150) NOT NULL,
  `icon_url` TEXT,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_name` (`name`),
  UNIQUE KEY `uk_category_slug` (`slug`),
  KEY `idx_category_slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- device
CREATE TABLE IF NOT EXISTS `device` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `processor` VARCHAR(255),
  `ram` VARCHAR(255),
  `storage` VARCHAR(255),
  `price_amount` DECIMAL(12,2),
  `price_currency` CHAR(3) DEFAULT 'INR',
  `release_date` DATE,
  `slug` VARCHAR(255) NOT NULL,
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0,

  `brand_id` BIGINT NOT NULL,
  `category_id` BIGINT NOT NULL,

  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device_slug` (`slug`),
  UNIQUE KEY `uq_device_brand_name_release` (`brand_id`, `name`, `release_date`),
  KEY `idx_device_brand_id` (`brand_id`),
  KEY `idx_device_category_id` (`category_id`),
  KEY `idx_device_release_date` (`release_date`),
  KEY `idx_device_price` (`price_amount`),
  KEY `idx_device_is_deleted` (`is_deleted`),

  CONSTRAINT `fk_device_brand` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`),
  CONSTRAINT `fk_device_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- image
CREATE TABLE IF NOT EXISTS `image` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `url` TEXT NOT NULL,
  `alt_text` TEXT,
  `is_primary` TINYINT(1) NOT NULL DEFAULT 0,
  `sort_order` INT,
  `device_id` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  -- emulate "unique where is_primary=1" using a generated column
  `primary_device_id` BIGINT GENERATED ALWAYS AS (CASE WHEN `is_primary` = 1 THEN `device_id` ELSE NULL END) VIRTUAL,

  PRIMARY KEY (`id`),
  KEY `idx_image_device_id` (`device_id`),
  CONSTRAINT `fk_image_device` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE CASCADE,
  UNIQUE KEY `uq_image_primary_per_device` (`primary_device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- review
CREATE TABLE IF NOT EXISTS `review` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `reviewer_name` TEXT,
  `content` TEXT,
  `rating` DECIMAL(2,1),
  `source_url` TEXT,
  `device_id` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_review_device_id` (`device_id`),
  CONSTRAINT `fk_review_device` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_review_rating_range` CHECK (`rating` IS NULL OR (`rating` >= 0 AND `rating` <= 5.0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- tag
CREATE TABLE IF NOT EXISTS `tag` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `slug` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tag_name` (`name`),
  UNIQUE KEY `uk_tag_slug` (`slug`),
  KEY `idx_tag_slug` (`slug`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- device_tag
CREATE TABLE IF NOT EXISTS `device_tag` (
  `device_id` BIGINT NOT NULL,
  `tag_id` BIGINT NOT NULL,
  PRIMARY KEY (`device_id`, `tag_id`),
  CONSTRAINT `fk_device_tag_device` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_device_tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- spec_key
CREATE TABLE IF NOT EXISTS `spec_key` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `spec_type` VARCHAR(50),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_spec_key_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- device_spec
CREATE TABLE IF NOT EXISTS `device_spec` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `device_id` BIGINT NOT NULL,
  `spec_key_id` BIGINT NOT NULL,
  `value_text` TEXT,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_device_spec` (`device_id`, `spec_key_id`),
  KEY `idx_device_spec_device` (`device_id`),
  KEY `idx_device_spec_key` (`spec_key_id`),
  CONSTRAINT `fk_device_spec_device` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_device_spec_key` FOREIGN KEY (`spec_key_id`) REFERENCES `spec_key` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- restore session notes
SET sql_notes = @old_sql_notes;