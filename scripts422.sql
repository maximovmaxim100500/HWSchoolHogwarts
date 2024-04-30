CREATE TABLE public.man (
    man_Id SERIAL PRIMARY KEY,
    Name text,
    Age INTEGER,
    Having_a_driving_license BOOLEAN
)

CREATE TABLE public.car (
    Car_Id SERIAL PRIMARY KEY,
    Brand text,
    Model text,
    Price MONEY
)

ALTER TABLE car ADD CONSTRAINT car_Model_unique UNIQUE (Model)

CREATE TABLE public.car_ownership (
    ownership_Id SERIAL PRIMARY KEY,
    man_Id INTEGER,
    Car_Id INTEGER,
    FOREIGN KEY (man_Id) REFERENCES man(man_Id),
    FOREIGN KEY (Car_Id) REFERENCES car(Car_Id)
)