CREATE TABLE Users (
	UserID SERIAL PRIMARY KEY,
	FirstName VARCHAR(100) NOT NULL,
	LastName VARCHAR(100) NOT NULL,
	Email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE Member (
	MemberID INTEGER PRIMARY KEY,
	RegisterDate DATE,
	CONSTRAINT fk_user FOREIGN KEY (MemberID) REFERENCES Users(UserID)
);

CREATE TABLE Trainer (
	TrainerID INTEGER PRIMARY KEY,
	Specialization VARCHAR(255) NOT NULL,
	CONSTRAINT fk_user FOREIGN KEY (TrainerID) REFERENCES Users(UserID)
);

CREATE TABLE Admin (
	AdminID INTEGER PRIMARY KEY,
	Title VARCHAR(100) NOT NULL,
	CONSTRAINT fk_user FOREIGN KEY (AdminID) REFERENCES Users(UserID)
);

CREATE TYPE bill_status AS ENUM ('PENDING', 'PAID', 'CANCELED');

CREATE TABLE Bill (
	BillID SERIAL PRIMARY KEY,
	MemberID INTEGER NOT NULL,
	AdminID INTEGER NOT NULL,
	BillDate Date NOT NULL,
	Amount NUMERIC,
	Status bill_status NOT NULL,
	CONSTRAINT fk_member FOREIGN KEY (MemberID) REFERENCES Member(MemberID),
	CONSTRAINT fk_admin FOREIGN KEY (AdminID) REFERENCES Admin(AdminID)
);

CREATE TABLE Room (
	RoomID SERIAL PRIMARY KEY,
	RoomName VARCHAR(255) NOT NULL,
	Capacity INTEGER NOT NULL
);

CREATE TYPE class_status AS ENUM ('OPEN', 'FULL', 'FINISHED');

CREATE TABLE FitnessClass (
	ClassID SERIAL PRIMARY KEY,
	ClassName VARCHAR(255) NOT NULL,
	TrainerID INTEGER NOT NULL,
	RoomID INTEGER NOT NULL,
	Status class_status NOT NULL,
	CONSTRAINT fk_trainer FOREIGN KEY (TrainerID) REFERENCES Trainer(TrainerID),
	CONSTRAINT fk_room FOREIGN KEY (RoomID) REFERENCES Room(RoomID)
);

CREATE TABLE ClassRegistered (
	MemberID INTEGER NOT NULL,
	ClassID INTEGER NOT NULL,
	CONSTRAINT fk_member FOREIGN KEY (MemberID) REFERENCES Member(MemberID),
	CONSTRAINT fk_class FOREIGN KEY (ClassID) REFERENCES FitnessClass(ClassID)
);

CREATE TYPE personal_training_status AS ENUM ('UPCOMING', 'FINISHED', 'CANCELED');

CREATE TABLE PersonalTrainingSession (
	SessionID SERIAL PRIMARY KEY,
	MemberID INTEGER NOT NULL,
	TrainerID INTEGER NOT NULL,
	Status personal_training_status NOT NULL,
	CONSTRAINT fk_trainer FOREIGN KEY (TrainerID) REFERENCES Trainer(TrainerID),
	CONSTRAINT fk_member FOREIGN KEY (MemberID) REFERENCES Member(MemberID)
);

CREATE TYPE goal_status AS ENUM ('IN PROGRESS', 'FULFILLED', 'CANCELED');

CREATE TABLE FitnessGoal (
	GoalID SERIAL PRIMARY KEY,
	MemberID INTEGER NOT NULL,
	StartDate Date NOT NULL,
	EndDate Date,
	TargetWeight INTEGER NOT NULL,
	Status goal_status NOT NULL,
	CONSTRAINT fk_member FOREIGN KEY (MemberID) REFERENCES Member(MemberID)
);

CREATE TABLE Routine (
	RoutineID SERIAL PRIMARY KEY,
	MemberID INTEGER NOT NULL,
	Reps INTEGER NOT NULL,
	Sets INTEGER NOT NULL,
	CONSTRAINT fk_member FOREIGN KEY (MemberID) REFERENCES Member(MemberID)
);

CREATE TABLE FitnessAchievement (
	AchievementID SERIAL PRIMARY KEY,
	MemberID INTEGER NOT NULL,
	Description VARCHAR(255) NOT NULL,
	DateAchieved DATE NOT NULL,
	CONSTRAINT fk_member FOREIGN KEY (MemberID) REFERENCES Member(MemberID)
);

CREATE TABLE HealthMetrics (
	MetricsID SERIAL PRIMARY KEY,
	MemberID INTEGER NOT NULL,
	Weight INTEGER NOT NULL,
	Height INTEGER NOT NULL,
	RecordDate Date NOT NULL,
	CONSTRAINT fk_member FOREIGN KEY (MemberID) REFERENCES Member(MemberID)
);

CREATE TYPE day_of_week AS ENUM ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday');
CREATE TYPE entity_type AS ENUM ('Room', 'Trainer', 'Class', 'Session');

CREATE TABLE Schedule (
	ScheduleID SERIAL PRIMARY KEY,
	EntityID INTEGER NOT NULL,
	EntityType entity_type NOT NULL,
	DayOfWeek day_of_week NOT NULL
);

CREATE TABLE ManageSchedule (
	AdminID INTEGER NOT NULL,
	ScheduleID INTEGER NOT NULL,
	CONSTRAINT fk_admin FOREIGN KEY (AdminID) REFERENCES Admin(AdminID),
	CONSTRAINT fk_schedule FOREIGN KEY (ScheduleID) REFERENCES Schedule(ScheduleID)
);

CREATE TABLE HasSchedule (
	ScheduleID INTEGER NOT NULL,
	TrainerID INTEGER NOT NULL,
	CONSTRAINT fk_schedule FOREIGN KEY (ScheduleID) REFERENCES Schedule(ScheduleID),
	CONSTRAINT fk_trainer FOREIGN KEY (TrainerID) REFERENCES Trainer(TrainerID)
);

CREATE TABLE TimeSlot (
	SlotID SERIAL PRIMARY KEY,
	ScheduleID INTEGER NOT NULL,
	StartTime TIME NOT NULL,
	EndTime TIME NOT NULL,
	CONSTRAINT fk_schedule FOREIGN KEY (ScheduleID) REFERENCES Schedule(ScheduleID)
);

CREATE TABLE RoomScheduled (
	ScheduleID INTEGER NOT NULL,
	RoomID INTEGER NOT NULL,
	CONSTRAINT fk_schedule FOREIGN KEY (ScheduleID) REFERENCES Schedule(ScheduleID),
	CONSTRAINT fk_room FOREIGN KEY (RoomID) REFERENCES Room(RoomID)
);

CREATE TABLE Equipment (
	EquipmentID SERIAL PRIMARY KEY,
	EquipmentName VARCHAR(255) NOT NULL,
	PurchaseDate DATE NOT NULL,
	LastMaintenanceDate DATE NOT NULL
);

CREATE TABLE EquipManaged (
	AdminID INTEGER NOT NULL,
	EquipID INTEGER NOT NULL,
	CONSTRAINT fk_admin FOREIGN KEY (AdminID) REFERENCES Admin(AdminID),
	CONSTRAINT fk_equip FOREIGN KEY (EquipID) REFERENCES Equipment(EquipmentID)
);