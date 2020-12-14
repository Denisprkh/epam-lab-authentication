-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema certificates
-- -----------------------------------------------------

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
  `price` DECIMAL NOT NULL,
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
-- Table `certificates`.`purchase`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `certificates`.`purchase` (
  `purchase_id` INT NOT NULL AUTO_INCREMENT,
  `cost` DECIMAL NOT NULL,
  `purchase_date` TIMESTAMP NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`purchase_id`),
  INDEX `fk_purchase_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_purchase_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `certificates`.`user` (`user_id`));


-- -----------------------------------------------------
-- Table `certificates`.`purchase_gift_certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `certificates`.`purchase_gift_certificate` (
  `gift_certificate_id` INT NOT NULL,
  `purchase_id` INT NOT NULL,
  PRIMARY KEY (`gift_certificate_id`, `purchase_id`),
  INDEX `fk_orders_gift_certificate_gift_certificate1_idx` (`gift_certificate_id` ASC),
  INDEX `fk_purchase_gift_certificate_purchase1_idx` (`purchase_id` ASC),
  CONSTRAINT `fk_orders_gift_certificate_gift_certificate1`
    FOREIGN KEY (`gift_certificate_id`)
    REFERENCES `certificates`.`gift_certificate` (`gift_certificate_id`),
  CONSTRAINT `fk_purchase_gift_certificate_purchase1`
    FOREIGN KEY (`purchase_id`)
    REFERENCES `certificates`.`purchase` (`purchase_id`));

INSERT INTO user_role(name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');
INSERT INTO user(login, password, user_role_id) VALUES ('admin', '$2a$10$3A0xvWaRn8YR2dvhSfFJDOCyDxhVJmLuCxxN/ArFcKDkSjtap1YUe', 2);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

