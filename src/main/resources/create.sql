create table public.profile
(
    id            BIGSERIAL    not null primary key,
    first_name    varchar(50),
    email         varchar(50)  not null,
    image         bigint,
    password      varchar(255) not null,
    credit        integer default 0,
    create_at     date,
    last_modified date
);
create table public.role
(
    id_profile bigint       not null,
    role       varchar(100) not null,
    constraint role_fk foreign key (id_profile) references public.profile (id) on UPDATE cascade on DELETE cascade
);
create table public.words
(
    id        BIGSERIAL    not null primary key,
    word      varchar(100) not null,
    translate varchar(100) not null
);

create table public.result_game
(
    id         BIGSERIAL   not null primary key,
    id_profile bigint      not null,
    type_game  varchar(50) not null,
    count      integer default 0,
    constraint english_fk foreign key (id_profile) references public.profile (id) on UPDATE cascade on DELETE cascade
);
create table public.book
(
    id         Bigserial    not null primary key,
    id_profile bigint       not null,
    word       varchar(100) not null,
    translate  varchar(100) not null,
    constraint book_fk foreign key (id_profile) references public.profile (id) on UPDATE cascade on DELETE cascade
);

-- ALTER TABLE public.profile
--     OWNER TO abnkxgdxakdjgr;
-- ALTER TABLE public.role
--     OWNER TO abnkxgdxakdjgr;
-- ALTER TABLE public.words
--     OWNER TO abnkxgdxakdjgr;
-- ALTER TABLE public.result_game
--     OWNER TO abnkxgdxakdjgr;

-- ================== INSERT ==========================

-- insert into public.profile( email, password)
-- VALUES ( 'bot-1', '$2a$10$HZLjPcuBehaPThFuBktD8u.g7qNKvrZ/3z7Rwn3u2ee3twwduJgMq');

--
-- insert into public.role(id_profile, role)
-- VALUES (1, 'USER');

