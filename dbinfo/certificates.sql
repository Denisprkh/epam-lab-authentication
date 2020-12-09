SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema certificates
-- -----------------------------------------------------

CREATE SCHEMA IF NOT EXISTS `certificates` DEFAULT CHARACTER SET utf8 ;
USE `certificates` ;

-- -----------------------------------------------------
-- Table `certificates`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `certificates`.`tag` (
  `tag_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(70) NOT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC));


-- -----------------------------------------------------
-- Table `certificates`.`gift_certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `certificates`.`gift_certificate` (
  `gift_certificate_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `price` DECIMAL(5,2) NOT NULL,
  `create_date` TIMESTAMP NOT NULL,
  `last_update_date` TIMESTAMP NOT NULL,
  `duration` BIGINT NOT NULL,
  `description` TEXT NOT NULL,
  PRIMARY KEY (`gift_certificate_id`),
  UNIQUE INDEX `Name_UNIQUE` (`name` ASC));


-- -----------------------------------------------------
-- Table `certificates`.`gift_certificate_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `certificates`.`gift_certificate_tag` (
  `tag_id` INT NOT NULL,
  `gift_certificate_id` INT NOT NULL,
  PRIMARY KEY (`tag_id`, `gift_certificate_id`),
  INDEX `fk_lnk_tags_gift_certificates_tags_idx` (`tag_id` ASC),
  INDEX `fk_lnk_tags_gift_certificates_gift_certificates1_idx` (`gift_certificate_id` ASC),
  CONSTRAINT `fk_lnk_tags_gift_certificates_tags`
    FOREIGN KEY (`tag_id`)
    REFERENCES `certificates`.`tag` (`tag_id`),
  CONSTRAINT `fk_lnk_tags_gift_certificates_gift_certificates1`
    FOREIGN KEY (`gift_certificate_id`)
    REFERENCES `certificates`.`gift_certificate` (`gift_certificate_id`));


-- -----------------------------------------------------
-- Table `certificates`.`user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `certificates`.`user_role` (
  `user_role_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`user_role_id`));


-- -----------------------------------------------------
-- Table `certificates`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `certificates`.`user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(70) NOT NULL,
  `password` VARCHAR(500) NOT NULL,
  `user_role_id` INT NOT NULL,
  PRIMARY KEY (`user_id`),
  INDEX `fk_user_user_role1_idx` (`user_role_id` ASC),
  CONSTRAINT `fk_user_user_role1`
    FOREIGN KEY (`user_role_id`)
    REFERENCES `certificates`.`user_role` (`user_role_id`));



-- -----------------------------------------------------
-- Table `certificates`.`order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `certificates`.`order` (
  `order_id` INT NOT NULL AUTO_INCREMENT,
  `cost` DECIMAL(5,2) NOT NULL,
  `purchase_date` TIMESTAMP NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`order_id`),
  INDEX `fk_order_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_order_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `certificates`.`user` (`user_id`));


-- -----------------------------------------------------
-- Table `certificates`.`order_gift_certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `certificates`.`order_gift_certificate` (
  `order_id` INT NOT NULL,
  `gift_certificate_id` INT NOT NULL,
  PRIMARY KEY (`order_id`, `gift_certificate_id`),
  INDEX `fk_orders_gift_certificate_order1_idx` (`order_id` ASC),
  INDEX `fk_orders_gift_certificate_gift_certificate1_idx` (`gift_certificate_id` ASC),
  CONSTRAINT `fk_orders_gift_certificate_order1`
    FOREIGN KEY (`order_id`)
    REFERENCES `certificates`.`order` (`order_id`),
  CONSTRAINT `fk_orders_gift_certificate_gift_certificate1`
    FOREIGN KEY (`gift_certificate_id`)
    REFERENCES `certificates`.`gift_certificate` (`gift_certificate_id`));

INSERT INTO user_role(name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');
INSERT INTO user(login, password) VALUES ('admin', '$2a$10$3A0xvWaRn8YR2dvhSfFJDOCyDxhVJmLuCxxN/ArFcKDkSjtap1YUe')

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
