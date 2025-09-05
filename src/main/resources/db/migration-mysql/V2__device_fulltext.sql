-- V2__device_fulltext.sql (MySQL/MariaDB only)
CREATE FULLTEXT INDEX `ft_device_text`
  ON `device` (`name`, `processor`, `ram`, `storage`, `slug`);