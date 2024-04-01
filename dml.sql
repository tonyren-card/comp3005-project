-- Insert sample users including members, trainers, and admins
INSERT INTO Users(FirstName, LastName, Email)
VALUES ('Tate', 'Rapp', 'trapp@email.com'),
('Olivia', 'Mcrae', 'omcrae@email.com'),
('Renee', 'Rodrigo', 'rrodrigo@email.com'),
('John', 'Stan', 'jstan@email.com'),
('Jane', 'Milk', 'jmilk@email.com'),
('Jonny', 'Air', 'jair@email.com'),
('Manny', 'Heron', 'mheron@email.com'),
('Henry', 'George', 'hgeorge@email.com'),
('Cindy', 'Ford', 'cford@email.com');

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