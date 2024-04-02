-- Insert sample users including members, trainers, and admins
INSERT INTO Users(FirstName, LastName, Email, Password)
VALUES ('Tate', 'Rapp', 'trapp@email.com', '1245245'),
('Olivia', 'Mcrae', 'omcrae@email.com', '5243564'),
('Renee', 'Rodrigo', 'rrodrigo@email.com', '214536'),
('John', 'Stan', 'jstan@email.com', '24243526'),
('Jane', 'Milk', 'jmilk@email.com', '2153546'),
('Jonny', 'Air', 'jair@email.com', '932589'),
('Manny', 'Heron', 'mheron@email.com', '12425436'),
('Henry', 'George', 'hgeorge@email.com', '2534636'),
('Cindy', 'Ford', 'cford@email.com', '1253646');

-- Assigning users to members
INSERT INTO Members
VALUES (1, '2024-03-01'),
(2, '2024-03-02'),
(3, '2024-03-03');

-- Assigning users to admins
INSERT INTO Admins
VALUES (4, 'Owner'),
(5, 'Manager'),
(6, 'Receptionist');

-- Assigning users to trainers
INSERT INTO Trainers
VALUES (7, 'Cardio Trainer'),
(8, 'Bodyfat Reducer'),
(9, 'Muscle Gainer');


INSERT INTO PersonalTrainingSession(MemberID, TrainerID, Status)
VALUES (1, 7, 'UPCOMING'),
(2, 8, 'FINISHED'),
(3, 9, 'CANCELED');


INSERT INTO Room(RoomName, Capacity)
VALUES ('GymRoom1', 50),
('GymRoom2', 75),
('GymRoom3', 100);


INSERT INTO FitnessClass(ClassName, TrainerID, RoomID, Status)
VALUES ('Beginner to Cardio', 7, 3, 'OPEN'),
('Intro to Bodyfat Shredding', 8, 2, 'FULL'),
('Muscle Builder', 9, 1, 'FINISHED');

-- MemberID, ClassID
INSERT INTO ClassRegistered
VALUES (1,1),
(2,1),
(3,1),
(1,2),
(2,3);


INSERT INTO HealthMetrics(MemberID,Weight,Height,RecordDate)
VALUES (1,50,165,'2024-03-01'),
(2,55,170,'2024-03-02'),
(3,60,175,'2024-03-03');


INSERT INTO FitnessAchievement(MemberID,Description,DateAchieved)
VALUES (1,'Burned 500 calories','2024-03-08'),
(2,'Burned 550 calories','2024-03-08'),
(3,'Burned 580 calories','2024-03-08');

INSERT INTO Routine(MemberID,Category,Reps,Sets)
VALUES (1, 'Dumbbell Bench Press', 10, 3),
(2, 'Machine Biceps Curl', 11, 4),
(3, 'Seated Row', 12, 5);


INSERT INTO FitnessGoal(MemberID,StartDate,EndDate,TargetWeight,Status)
VALUES (1,'2024-03-01', '2024-03-05', 160, 'IN PROGRESS'),
(2,'2024-03-02', NULL, 160, 'FULFILLED'),
(3,'2024-03-03', NULL, 160, 'CANCELED');


INSERT INTO Bill(MemberID,AdminID,BillDate,Amount,Status)
VALUES (1,4,'2024-03-01',100,'PENDING'),
(2,5,'2024-03-02',100,'PAID'),
(3,6,'2024-03-03',100,'CANCELED');


INSERT INTO Equipment(EquipmentName,PurchaseDate,LastMaintenanceDate)
VALUES ('Dumbbell Set', '2020-01-01', '2023-01-01'),
('Barbell station', '2020-01-01', '2023-01-01'),
('Rowing machine', '2020-01-01', '2023-01-01');

-- AdminID, EquipID
INSERT INTO Equipmanaged
VALUES (4,1),
(5,2),
(6,3);

-- INSERT INTO 