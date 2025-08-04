CREATE TABLE IF NOT EXISTS `shelving_units` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `unit` INT(20) NOT NULL,
    `side_A` VARCHAR(180) NOT NULL,
    `side_B` VARCHAR(180)
);
