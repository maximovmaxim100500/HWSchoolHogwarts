alter table student add constraint age_constraint check (age>16 or age is null)

ALTER TABLE student ADD CONSTRAINT name_unique UNIQUE (name)

ALTER  TABLE student ALTER COLUMN name SET NOT null

ALTER TABLE faculty ADD CONSTRAINT color_name_unique UNIQUE (color, name)

CREATE OR REPLACE FUNCTION set_default_age()    //функция вставки возраста 20
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.age IS NULL THEN
        NEW.age = 20;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_default_age_trigger     //тригер запуска функции
BEFORE INSERT ON student
FOR EACH ROW
EXECUTE FUNCTION set_default_age();