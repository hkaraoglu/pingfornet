BEGIN TRANSACTION;
DROP TABLE IF EXISTS `request_log`;
CREATE TABLE IF NOT EXISTS `request_log` (
	`request_log_id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`request_id`	INTEGER,
	`date_added`	TEXT,
	`date_modified`	TEXT,
	`date_ended`	TEXT,
	`success_count`	INTEGER DEFAULT 0,
	`fail_count`	INTEGER DEFAULT 0
);
DROP TABLE IF EXISTS `request`;
CREATE TABLE IF NOT EXISTS `request` (
	`request_id`	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,
	`protocol`	TEXT,
	`ip_host`	TEXT,
	`port`	INTEGER,
	`interval`	INTEGER,
	`timeout`	INTEGER,
	`response_codes`	TEXT
);
DROP TABLE IF EXISTS `ping_log`;
CREATE TABLE IF NOT EXISTS `ping_log` (
	`ping_log_id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`ping_id`	INTEGER,
	`date_added`	TEXT,
	`date_ended`	TEXT,
	`date_modified`	TEXT,
	`success_count`	INTEGER,
	`fail_count`	INTEGER
);
DROP TABLE IF EXISTS `ping`;
CREATE TABLE IF NOT EXISTS `ping` (
	`ping_id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`ip_hostname`	TEXT UNIQUE,
	`port`	INTEGER,
	`interval`	INTEGER
);
DROP TABLE IF EXISTS `mail_list`;
CREATE TABLE IF NOT EXISTS `mail_list` (
	`item_id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`from`	TEXT,
	`to`	TEXT,
	`smtp`	TEXT,
	`port`	INTEGER,
	`username`	TEXT,
	`password`	TEXT
);
COMMIT;
