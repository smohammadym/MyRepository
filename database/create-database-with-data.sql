-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema db_todo_list
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `db_todo_list` ;

-- -----------------------------------------------------
-- Schema db_todo_list
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `db_todo_list` DEFAULT CHARACTER SET utf8 ;
USE `db_todo_list` ;

-- -----------------------------------------------------
-- Table `db_todo_list`.`person`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_todo_list`.`person` ;

CREATE TABLE IF NOT EXISTS `db_todo_list`.`person` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_todo_list`.`item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_todo_list`.`item` ;

CREATE TABLE IF NOT EXISTS `db_todo_list`.`item` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(300) NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT current_timestamp(),
  `done` BIT NULL,
  `person_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_items_users_idx` (`person_id` ASC) VISIBLE,
  CONSTRAINT `fk_items_users`
    FOREIGN KEY (`person_id`)
    REFERENCES `db_todo_list`.`person` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_todo_list`.`tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_todo_list`.`tag` ;

CREATE TABLE IF NOT EXISTS `db_todo_list`.`tag` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `db_todo_list`.`item_tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `db_todo_list`.`item_tag` ;

CREATE TABLE IF NOT EXISTS `db_todo_list`.`item_tag` (
  `item_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  PRIMARY KEY (`item_id`, `tag_id`),
  INDEX `fk_tags_has_item_item1_idx` (`item_id` ASC) VISIBLE,
  INDEX `fk_tags_has_item_tags1_idx` (`tag_id` ASC) VISIBLE,
  CONSTRAINT `fk_tags_has_item_tags1`
    FOREIGN KEY (`tag_id`)
    REFERENCES `db_todo_list`.`tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tags_has_item_item1`
    FOREIGN KEY (`item_id`)
    REFERENCES `db_todo_list`.`item` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SET SQL_MODE = '';
DROP USER IF EXISTS db_todo_list;
SET SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
CREATE USER 'db_todo_list' IDENTIFIED BY 'Pass4CRUD2020';

GRANT SELECT, INSERT, TRIGGER, UPDATE, DELETE ON TABLE `db_todo_list`.* TO 'db_todo_list';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `db_todo_list`.`person`
-- -----------------------------------------------------
START TRANSACTION;
USE `db_todo_list`;
INSERT INTO `db_todo_list`.`person` (`id`, `username`, `password`) VALUES (1, 'user', '$2a$10$.tI3oz9Oo5LqjQUBRvDqE.A3jKIY8aAOHVDo0b2tBEC133lnRQZCK');

COMMIT;


-- -----------------------------------------------------
-- Data for table `db_todo_list`.`item`
-- -----------------------------------------------------
START TRANSACTION;
USE `db_todo_list`;
INSERT INTO `db_todo_list`.`item` (`id`, `name`, `description`, `created_at`, `done`, `person_id`) VALUES (1, 'Aufgabe 1', 'Beschreibung Aufgabe 1', DEFAULT, NULL, 1);
INSERT INTO `db_todo_list`.`item` (`id`, `name`, `description`, `created_at`, `done`, `person_id`) VALUES (2, 'Aufgabe 2', 'Beschreibung Aufgabe 2', DEFAULT, NULL, 1);
INSERT INTO `db_todo_list`.`item` (`id`, `name`, `description`, `created_at`, `done`, `person_id`) VALUES (3, 'Essen kaufen', 'Früchte und Brot', DEFAULT, NULL, 1);
INSERT INTO `db_todo_list`.`item` (`id`, `name`, `description`, `created_at`, `done`, `person_id`) VALUES (4, 'Mathe lernen', 'Seite 5-8', DEFAULT, NULL, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `db_todo_list`.`tag`
-- -----------------------------------------------------
START TRANSACTION;
USE `db_todo_list`;
INSERT INTO `db_todo_list`.`tag` (`id`, `name`) VALUES (1, 'Dringend');
INSERT INTO `db_todo_list`.`tag` (`id`, `name`) VALUES (2, 'Essen');
INSERT INTO `db_todo_list`.`tag` (`id`, `name`) VALUES (3, 'Kleider');
INSERT INTO `db_todo_list`.`tag` (`id`, `name`) VALUES (4, 'Schule');

COMMIT;


-- -----------------------------------------------------
-- Data for table `db_todo_list`.`item_tag`
-- -----------------------------------------------------
START TRANSACTION;
USE `db_todo_list`;
INSERT INTO `db_todo_list`.`item_tag` (`item_id`, `tag_id`) VALUES (1, 1);
INSERT INTO `db_todo_list`.`item_tag` (`item_id`, `tag_id`) VALUES (2, 1);
INSERT INTO `db_todo_list`.`item_tag` (`item_id`, `tag_id`) VALUES (3, 2);
INSERT INTO `db_todo_list`.`item_tag` (`item_id`, `tag_id`) VALUES (4, 4);

COMMIT;

