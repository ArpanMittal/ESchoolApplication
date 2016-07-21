
CREATE TABLE IF NOT EXISTS  tblsubject (
    subjectId char(100),
    subjectName varchar(255) NOT NULL,
    timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(subjectId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci

CREATE TABLE IF NOT EXISTS  tblchapter (
    chapterId char(100),
    chapterName varchar(255) NOT NULL,
    timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(chapterId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci

CREATE TABLE IF NOT EXISTS  tbltopic (
    topicId char(100),
    topicName varchar(255) NOT NULL,
    timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(topicId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci

CREATE TABLE IF NOT EXISTS  tblsubtopic (
    subtopicId char(100),
    subtopicName varchar(255) NOT NULL,
    timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(subtopicId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci

CREATE TABLE IF NOT EXISTS  tblclass (
    classId char(100),
    className varchar(255) NOT NULL,
    timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(classId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci

CREATE TABLE IF NOT EXISTS tblclasssubjectmap(
 classId char(100) NOT NULL,
 FOREIGN KEY (classId) REFERENCES tblclass(classId) ON DELETE CASCADE,
subjectId char(100) NOT NULL,
FOREIGN KEY (subjectId) REFERENCES tblsubject(subjectId) ON DELETE CASCADE,
 timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS tblsubjectchaptermap(
subjectId char(100) NOT NULL,
FOREIGN KEY (subjectId) REFERENCES tblsubject(subjectId) ON DELETE CASCADE,
chapterId char(100) NOT NULL,
 FOREIGN KEY (chapterId) REFERENCES tblchapter(chapterId) ON DELETE CASCADE,
 timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS tblchaptertopicmap(
chapterId char(100) NOT NULL,
 FOREIGN KEY (chapterId) REFERENCES tblchapter(chapterId) ON DELETE CASCADE,
 topicId char(100) NOT NULL,
FOREIGN KEY (topicId) REFERENCES tbltopic(topicId) ON DELETE CASCADE,
 timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS tbltopicsubtopicmap(
 topicId char(100) NOT NULL,
FOREIGN KEY (topicId) REFERENCES tbltopic(topicId) ON DELETE CASCADE,
subtopicId char(100) NOT NULL,
 FOREIGN KEY (subtopicId) REFERENCES tblsubtopic(subtopicId) ON DELETE CASCADE,
 timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS tblrole(
	roleId INT, 
	role varchar(255) NOT NULL,
	timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (roleId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `tbloperator` (
  `operatorId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `operatorName` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `roleId`  int  NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`operatorId`),
  FOREIGN KEY (roleId) REFERENCES tblrole(roleId) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;


CREATE TABLE IF NOT EXISTS tblusersubjectmap(
userId int(10) unsigned NOT NULL,
 FOREIGN KEY (userId) REFERENCES tbloperator(operatorId) ON DELETE CASCADE,
subjectId char(255) NOT NULL,
FOREIGN KEY (subjectId) REFERENCES tblsubject(subjectId) ON DELETE CASCADE,
 timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE IF NOT EXISTS tblquestiontype(questionTypeId char(100) NOT NULL, questionType varchar(255) NOT NULL, PRIMARY KEY (questionTypeId))ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS tblquestion(
 questionId int(10) unsigned NOT NULL,
 subtopicId char(100) NOT NULL,
 FOREIGN KEY (subtopicId) REFERENCES tblsubtopic(subtopicId) ON DELETE CASCADE,
 question TEXT NOT NULL,
 imagepath varchar(255),
 questionTypeId char(100) NOT NULL,
 FOREIGN KEY (questionTypeId) REFERENCES tblquestiontype(questionTypeId) ON DELETE CASCADE,
 time_Created DATETIME NOT NULL,
 time_Modified timestamp,
 createdBy char(100) NOT NULL,
 modifiedBy char(100) NOT NULL,
 solutionPath TEXT NOT NULL,
 PRIMARY KEY (questionId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS tbloption(
optionId int(10) unsigned NOT NULL,
questionId int(10) unsigned NOT NULL,
FOREIGN KEY(questionId) REFERENCES tblquestion(questionId) ON DELETE CASCADE,
opt TEXT NOT NULL,
columnId BOOLEAN DEFAULT NULL ,
timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY(optionId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS tblanswer(
questionId int(10) unsigned NOT NULL,
FOREIGN KEY(questionId) REFERENCES tblquestion(questionId) ON DELETE CASCADE,
answer TEXT NOT NULL,
timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
