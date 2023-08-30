DROP TABLE IF EXISTS groups CASCADE;

CREATE TABLE groups (
    group_id INT NOT NULL,
    group_name VARCHAR(10) NOT NULL,
    PRIMARY KEY (group_id)
);
