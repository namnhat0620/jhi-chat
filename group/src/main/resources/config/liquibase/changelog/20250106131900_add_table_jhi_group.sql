drop table if exists jhi_user_group;
drop table if exists jhi_group;

-- Create the table jhi_group
CREATE TABLE jhi_group (
    id BIGINT PRIMARY KEY NOT NULL,
    type VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    avatar VARCHAR(255),
    last_message_id VARCHAR(255)
);

CREATE TABLE jhi_user_group (
    id BIGINT PRIMARY KEY NOT NULL,
    group_id BIGINT, -- Foreign key column
    login VARCHAR(255) NOT NULL,
    is_seen BOOLEAN,
    is_turn_on_noti BOOLEAN,

    CONSTRAINT fk_jhi_user_group__group FOREIGN KEY (group_id) REFERENCES jhi_group(id) ON DELETE CASCADE,
    CONSTRAINT uq_jhi_user_group_group_id_login UNIQUE (group_id, login)
);