CREATE TABLE IF NOT EXISTS books ( id         SERIAL       NOT NULL
                                 , author     VARCHAR(255) NOT NULL
                                 , country    VARCHAR(255)
                                 , image_link VARCHAR(255)
                                 , language   VARCHAR(255)
                                 , link       VARCHAR(255)
                                 , pages      INT4
                                 , title      VARCHAR(255) NOT NULL
                                 , year       INT4
                                 , PRIMARY KEY(id)
                                 );