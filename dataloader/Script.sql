--<ScriptOptions statementTerminator=";"/>

CREATE TABLE AportData (
	timespanid DATETIME NOT NULL,
	CDRID VARCHAR(20) NOT NULL,
	RemoteNumber VARCHAR(20) NOT NULL,
	imsi VARCHAR(20) NOT NULL,
	SessionType INT NOT NULL,
	LAC INT NOT NULL,
	CI INT NOT NULL,
	PreviousLAI INT NOT NULL
) ENGINE=MyISAM;

