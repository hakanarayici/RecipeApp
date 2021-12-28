CREATE MEMORY TABLE PUBLIC.RECIPE(
    ID BIGINT NOT NULL,
    CREATE_DATE TIMESTAMP,
    INSTRUCTIONS CLOB,
    NAME VARCHAR(255),
    SUITABLE_PEOPLE_COUNT INTEGER,
    IS_VEGETARIAN BOOLEAN
);
ALTER TABLE PUBLIC.RECIPE ADD CONSTRAINT PUBLIC.CONSTRAINT_8 PRIMARY KEY(ID);


CREATE MEMORY TABLE PUBLIC.INGREDIENT(
    ID BIGINT NOT NULL,
    NAME VARCHAR(255),
    RECIPE_ID BIGINT NOT NULL
);
ALTER TABLE PUBLIC.INGREDIENT ADD CONSTRAINT PUBLIC.CONSTRAINT_9 PRIMARY KEY(ID);

CREATE SEQUENCE HIBERNATE_SEQUENCE START WITH 50 INCREMENT BY 1;