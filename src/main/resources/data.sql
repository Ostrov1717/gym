
INSERT INTO "user" (isactive, firstname, lastname,  password, username) VALUES (true, 'Olga', 'Kurilenko', 'WRqqRQMsoy', 'Olga.Kurilenko'), (true, 'Kim','Johnson', '555555', 'Kim.Johnson'), (true, 'Tomas','Kuk', '12345', 'Tomas.Kuk'), (true, 'George','TheThird', '54321', 'George.TheThird'), (true, 'Monica', 'Dobs', '55555', 'Monica.Dobs'), (true, 'Wallace', 'Tim', '66666', 'Wallace.Tim'), (true, 'Tom','Robins', '77777', 'Tom.Robins'), (true, 'Bob','Getty', '88888', 'Bob.Getty');

INSERT INTO trainee (address, dateofbirth, user_id) VALUES ('California', '1986-12-30', 1), ('Chicago', '1972-02-01',2), ('Sweden Oslo', '1962-05-05',3), ('UK', '1900-12-30',4);

INSERT INTO trainingtype (id, trainingtype) VALUES (1, 'YOGA'), (2, 'FITNESS'),(3, 'ZUMBA'), (4, 'STRETCHING'), (5, 'RESISTANCE');

INSERT INTO trainer (specialization_id, user_id)  VALUES (1,5), (2,6), (3,7), (2,8);

INSERT INTO training (trainingdate, trainingduration, trainingname, trainee_id, trainer_id, trainingtype_id)  VALUES ('2022-12-09T18:19:00',300,'Sunday morning training',2,3,3), ('2024-10-07T20:04:00',1500,'Jogging',2,4,2), ('2024-10-09T10:15:00',2000,'Friday evening training',3,3,3), ('2024-10-21T18:15:00',3000,'Outside workout',3,2,2), ('2024-10-13T20:15:00',5000,'Yoga class',3,1,1), ('2024-10-12T07:15:00',10000,'Jogging',1,1,1), ('2024-10-11T19:15:00',20000,'Evening fitness',4,2,2);

INSERT INTO trainee2trainer (trainee_id, trainer_id) VALUES (2,3), (2,4), (3,3), (3,2), (3,1), (1,1), (4,2);