create table public.category
(
    id   uuid not null
        primary key,
    name varchar(255)
        unique
);

create table public.engine
(
    displacement numeric(38, 2),
    power        integer,
    torque       integer,
    id           uuid not null
        primary key,
    fuel_type    varchar(255),
    type         varchar(255)
);

create table public.image_info
(
    id uuid not null
        primary key
);

create table public.image_info_urls
(
    image_info_id uuid not null
        constraint fk7c9bkeyt8sv2he11d285affar
            references public.image_info,
    urls          varchar(255)
);

create table public.manufacturer
(
    id      uuid not null
        primary key,
    country varchar(255),
    name    varchar(255)
        unique
);

create table public.sparkplug_user
(
    id                  uuid         not null
        primary key,
    authority           varchar(255),
    password            varchar(255),
    username            varchar(255) not null
        unique,
    profile_picture_url varchar(255)
);

create table public.transmission
(
    number_of_gears integer,
    id              uuid not null
        primary key,
    gearbox_type    varchar(255)
);

create table public.car
(
    mileage         integer,
    year            integer,
    category_id     uuid
        constraint fk377w213doepxacos5ptnph70e
            references public.category,
    engine_id       uuid
        unique
        constraint fknednv54lgu9rfucgemr5eal0j
            references public.engine,
    id              uuid not null
        primary key,
    manufacturer_id uuid
        constraint fk3up36si6vweiengpl21jsri71
            references public.manufacturer,
    transmission_id uuid
        unique
        constraint fkdvm7535riwqbql65a4otb50yh
            references public.transmission,
    color           varchar(255),
    drivetrain      varchar(255),
    model           varchar(255)
);

create table public.posting
(
    creation_date timestamp(6),
    car_id        uuid
        unique
        constraint fk1cqb6pjblvj2sngfy0nqpvx0v
            references public.car,
    creator_id    uuid
        constraint fkposr9hynl49r5cqn0b083i59e
            references public.sparkplug_user,
    id            uuid not null
        primary key,
    image_info_id uuid
        unique
        constraint fki4ocgn92pa94o5oje7utcl77k
            references public.image_info,
    description   varchar(2048),
    price         numeric
);

create table public.posting_wishlist
(
    user_id    uuid not null
        constraint fk8nmncva0qh4fprohw33lwstp7
            references public.sparkplug_user,
    posting_id uuid not null
        constraint fkn8ipirnttiyvf86qovxnw27lx
            references public.posting
);

create table public.chat
(
    id         uuid not null
        primary key,
    buyer_id   uuid
        constraint fkf9hwartryocytnqtitdwuan36
            references public.sparkplug_user,
    posting_id uuid
        constraint fk90u1mt8jvysrfb1qluyu5q5c9
            references public.posting,
    seller_id  uuid
        constraint fkbxo3omwf1auuwj57a84pj5ry4
            references public.sparkplug_user
);

create table public.message
(
    id         uuid not null
        primary key,
    content    varchar(4095),
    created_at timestamp(6),
    is_read    boolean,
    chat_id    uuid
        constraint fkmejd0ykokrbuekwwgd5a5xt8a
            references public.chat,
    sender_id  uuid
        constraint fk8pxnormhq7i0e05dj0gjbm4gx
            references public.sparkplug_user
);
