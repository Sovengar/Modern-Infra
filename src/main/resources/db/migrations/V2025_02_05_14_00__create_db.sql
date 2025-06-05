create SCHEMA IF NOT EXISTS mi; --AUTHORIZATION admin

create TABLE mi.ROLES (
    role_code TEXT PRIMARY KEY,
    description TEXT
);

create TABLE mi.USERS (
    user_id UUID PRIMARY KEY,
    realname TEXT,
    username TEXT,
    email TEXT,
    internal_enterprise_email TEXT,
    password VARCHAR(70),
    status TEXT,
    country TEXT,
    phone_numbers TEXT,
    role_code TEXT,
    version INTEGER,
    created_by TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    modified_by TEXT,
    modified_at TIMESTAMP WITHOUT TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (role_code) REFERENCES MI.ROLES(role_code)
);