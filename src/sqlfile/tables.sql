DROP TABLE IF EXISTS role_permission;
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS ticket_type;
DROP TABLE IF EXISTS screening;
DROP TABLE IF EXISTS seat;
DROP TABLE IF EXISTS screen;
DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS permission;


CREATE TABLE role
(
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE permission
(
    permission_id SERIAL PRIMARY KEY,
    permission_name VARCHAR(20) UNIQUE NOT NULL,
	permission_description VARCHAR(255)
);

CREATE TABLE role_permission
(
    role_permission_id SERIAL PRIMARY KEY,
    role_id INTEGER NOT NULL REFERENCES role(role_id) ON DELETE CASCADE,
	permission_id INTEGER NOT NULL REFERENCES permission(permission_id) ON DELETE CASCADE,
	UNIQUE(role_id, permission_id)
);

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
	birth_date DATE NOT NULL,
	role_id INTEGER NOT NULL REFERENCES role(role_id),
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE movie (
    movie_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    duration_minutes INTEGER NOT NULL CHECK (duration_minutes > 0),
    release_date DATE NOT NULL,
    genre VARCHAR(100),
    rating VARCHAR(10),
	director VARCHAR(255) NOT NULL,
	description VARCHAR(255),
	is_deleted BOOL DEFAULT FALSE
);

CREATE TABLE screen (
    screen_id SERIAL PRIMARY KEY,
    screen_name VARCHAR(100) UNIQUE NOT NULL,
    capacity INTEGER NOT NULL CHECK (capacity > 0),
	is_deleted BOOL DEFAULT FALSE
);

CREATE TABLE seat (
    seat_id SERIAL PRIMARY KEY,
    screen_id INTEGER NOT NULL REFERENCES screen(screen_id),
    seat_row VARCHAR(2) NOT NULL,
    seat_number INTEGER NOT NULL CHECK (seat_number > 0),
    is_vip BOOLEAN NOT NULL DEFAULT FALSE,
    is_handicap BOOLEAN NOT NULL DEFAULT FALSE,
	is_active BOOL DEFAULT TRUE,
    UNIQUE (screen_id, seat_row, seat_number)
);

CREATE TABLE screening (
    screening_id SERIAL PRIMARY KEY,
    movie_id INTEGER NOT NULL REFERENCES movie(movie_id),
    screen_id INTEGER NOT NULL REFERENCES screen(screen_id),
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
	--end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    ticket_price NUMERIC(5, 2) NOT NULL CHECK (ticket_price >= 0),
	is_deleted BOOL DEFAULT FALSE
);

CREATE TABLE ticket_type (
    type_id SERIAL PRIMARY KEY,
    type_name VARCHAR(50) UNIQUE NOT NULL,
    base_discount_percent NUMERIC(5, 2) DEFAULT 0.00 CHECK (base_discount_percent >= 0),
    base_price_addendum NUMERIC(5, 2) DEFAULT 0.00
);

CREATE TABLE ticket (
    ticket_id SERIAL PRIMARY KEY,
    screening_id INTEGER NOT NULL REFERENCES screening(screening_id),
    type_id INTEGER NOT NULL REFERENCES ticket_type(type_id),
    seat_id INTEGER NOT NULL REFERENCES seat(seat_id),
	user_id INTEGER REFERENCES users(user_id),
    purchase_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    final_price NUMERIC(5, 2) NOT NULL CHECK (final_price > 0),
    UNIQUE (screening_id, seat_id)
);