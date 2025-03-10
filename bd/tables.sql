CREATE SCHEMA GesTurnos;

USE GesTurnos;

CREATE TABLE Uc (
Id INT PRIMARY KEY NOT NULL,
Nome VARCHAR(50) NOT NULL
);

CREATE TABLE Aluno (
Id VARCHAR(10) PRIMARY KEY NOT NULL,
Nome VARCHAR(100) NOT NULL,
Senha VARCHAR(50) NOT NULL,
Curso VARCHAR(50) NOT NULL,
Estatuto BOOLEAN NOT NULL,
Media FLOAT
);

CREATE TABLE Aluno_UC (
Id_Uc INT NOT NULL,
Id_Aluno VARCHAR(10) NOT NULL,
PRIMARY KEY (Id_Uc, Id_Aluno),
FOREIGN KEY (Id_Uc) REFERENCES Uc(Id),
FOREIGN KEY (Id_Aluno) REFERENCES Aluno(Id)
);

CREATE TABLE Diretor (
Id VARCHAR(10) PRIMARY KEY NOT NULL,
Nome VARCHAR(100) NOT NULL,
Senha VARCHAR(50) NOT NULL,
Curso VARCHAR(50) NOT NULL
);

CREATE TABLE Sala (
Numero FLOAT PRIMARY KEY NOT NULL,
Capacidade INT NOT NULL
);

CREATE TABLE Turno (
Id INT PRIMARY KEY NOT NULL,
HoraInicio TIME NOT NULL,
HoraFim TIME NOT NULL,
DiaSemana VARCHAR(20) NOT NULL,
Tipo VARCHAR(20) NOT NULL,
Id_Sala FLOAT NOT NULL,
Id_Uc INT NOT NULL,
Restricao ENUM('NENHUMA', 'ESTATUTO', 'LIMITE_ALUNOS'),
Capacidade INT NOT NULL,
LugaresReservados INT NOT NULL,
FOREIGN KEY (Id_Sala) REFERENCES Sala(Numero),
FOREIGN KEY (Id_Uc) REFERENCES Uc(Id)
);

CREATE TABLE Horario(
Id_Aluno VARCHAR(10) NOT NULL,
Id_Turno INT NOT NULL,
PRIMARY KEY (Id_Aluno, Id_Turno),
FOREIGN KEY (Id_Aluno) REFERENCES Aluno(Id),
FOREIGN KEY (Id_Turno) REFERENCES Turno(Id)
);