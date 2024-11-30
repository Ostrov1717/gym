
INSERT INTO "user" (active, first_name, last_name,  password, username) VALUES (true, 'Olga', 'Kurilenko', '$2a$10$dksj1.woqq4VzAH6gG61v.P7pwqMDNl91UKyYVfvIz/N7G2IGPhNy', 'Olga.Kurilenko'), (true, 'Kim','Johnson', '$2a$10$c/zsM4W71ijkSfghmuJRV.qDCoz9ECswPf.B9RFAW4sMxNTPQRnBO', 'Kim.Johnson'), (true, 'Tomas','Kuk', '$2a$10$.IsccR8G0mt21FLW6YlMsOS0Pjkn.HK4/wvuEVcO0IoBVNudJswk.', 'Tomas.Kuk'), (true, 'George','TheThird', '$2a$10$.IsccR8G0mt21FLW6YlMsOS0Pjkn.HK4/wvuEVcO0IoBVNudJswk.', 'George.TheThird'), (true, 'Monica', 'Dobs', '$2a$10$QclcSxyB.BIaFJvIeIDxmOG4CH9o6j48/YOZ/mrq3FX8e5/d6qMri', 'Monica.Dobs'), (true, 'Wallace', 'Tim', '$2a$10$4FLMHMTwgX3OFR38MqpCBeomrBZwHY0tKg5pWiyw3ZUzdWS29CEHW', 'Wallace.Tim'), (true, 'Tom','Robins', '$2a$10$fX62Stm49tYkAlHeKH6hL.lNxiFZ.muA6U3VlrKxOW9WMklEih1/K', 'Tom.Robins'), (true, 'Bob','Getty', '$2a$10$fX62Stm49tYkAlHeKH6hL.lNxiFZ.muA6U3VlrKxOW9WMklEih1/K', 'Bob.Getty');

INSERT INTO trainee (address, date_of_birth, user_id) VALUES ('California', '1986-12-30', 1), ('Chicago', '1972-02-01',2), ('Sweden Oslo', '1962-05-05',3), ('UK', '1900-12-30',4);

INSERT INTO training_type (id, training_type) VALUES (1, 'YOGA'), (2, 'FITNESS'),(3, 'ZUMBA'), (4, 'STRETCHING'), (5, 'RESISTANCE');

INSERT INTO trainer (specialization_id, user_id)  VALUES (1,5), (2,6), (3,7), (2,8);

INSERT INTO training (training_date, training_duration, training_name, trainee_id, trainer_id, training_type_id)  VALUES ('2022-12-09T18:19:00',300,'Sunday morning training',2,3,3), ('2024-10-07T20:04:00',1500,'Jogging',2,4,2), ('2024-10-09T10:15:00',2000,'Friday evening training',3,3,3), ('2024-10-21T18:15:00',3000,'Outside workout',3,2,2), ('2024-10-13T20:15:00',5000,'Yoga class',3,1,1), ('2024-10-12T07:15:00',10000,'Jogging',1,1,1), ('2024-10-11T19:15:00',20000,'Evening fitness',4,2,2);

INSERT INTO trainee2trainer (trainee_id, trainer_id) VALUES (2,3), (2,4), (3,3), (3,2), (3,1), (1,1), (4,2);