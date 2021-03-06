SELECT     Imsi, Lac, Ci, DATEPART(DAYOFYEAR, ConnectTime) AS ConnectDay, DATEPART(HOUR, ConnectTime) AS ConnectHour, 
                      SUM((CASE WHEN LastPkgTime > RequestTime THEN CAST(datediff(s, RequestTime, LastPkgTime) AS decimal(18, 2)) END)) AS ConnTimeLength, COUNT(*) 
                      AS ConnCount
FROM         dbo.GN
into TimelengthCountByImsiLacCiDayHourUnfiltered
WHERE     (ConnectTime > '2000-01-01') AND (LastPkgTime > '2000-01-01') AND (RequestTime > '2000-01-01')
GROUP BY Imsi, Lac, Ci, DATEPART(DAYOFYEAR, ConnectTime), DATEPART(HOUR, ConnectTime)
-------------------------------------
Select * 
into TimelengthCountByImsiLacCiDayHour
From TimelengthCountByImsiLacCiDayHourUnfiltered
where ConnectHour in (7,8,9,10,11,17,18,19,20,21)
GO
----------------------------------------
CREATE VIEW [dbo].[vwTimelengthImsiDayHour]
as
SELECT [Imsi]
      ,[ConnectDay]
      ,[ConnectHour]
      ,sum(ConnTimeLength) as  ConnTimeLength
      ,sum (ConnCount) as ConnCount
      ,COUNT(distinct location) as LocationCount
  FROM [ZhuData].[dbo].[TimelengthCountByImsiLacCiDayHour]
  group by imsi, connectday, connecthour
GO
Select * 
into TimelengthImsiDayHour
From vwTimelengthImsiDayHour
GO
------------------------------------------------
CREATE VIEW [dbo].[vwTimelengthLocationDayHour]
AS
SELECT     Location, ConnectDay, ConnectHour, SUM(ConnTimeLength) AS ConnTimeLength, SUM(ConnCount) AS ConnCount, COUNT(DISTINCT Imsi) AS UserCount, 
                      SUM(ConnTimeLength) / COUNT(DISTINCT Imsi) AS AvgConnTimeLengthPerUser
FROM         dbo.TimelengthCountByImsiLacCiDayHour
GROUP BY Location, ConnectDay, ConnectHour
GO
Select *
into TimelengthLocationDayHour
From vwTimelengthLocationDayHour
GO
------------------------------------
create view [dbo].[vwTimelengthLocationHour]
as
SELECT  [Location]
      ,[ConnectHour]
      ,sum(ConnTimeLength) / SUM(userCount) as AvgConnTimeLengthPerUser
      ,sum(ConnCount) / SUM(userCount) as AvgConnCountPerUser
      ,avg(UserCount) as AvgUserCount
  FROM [ZhuData].[dbo].[TimelengthLocationDayHour]
  group by Location, ConnectHour
GO
Select *
into TimelengthLocationHour
From vwTimelengthLocationHour
GO
----------------------------------------------
create view [dbo].[vwTimelengthImsiHour]
as
SELECT  [Imsi]
      ,[ConnectHour]
      ,sum(ConnTimeLength) / SUM(LocationCount) as AvgConnTimeLengthPerLocation
      ,sum(ConnCount) / SUM(LocationCount) as AvgConnCountPerLocation
      ,avg(LocationCount) as AvgLocationCount
      ,AVG(ConnTimeLength) as AvgConnTimeLength
  FROM [ZhuData].[dbo].[TimelengthImsiDayHour]
  group by Imsi, ConnectHour
GO
Select * 
into TimelengthImsiHour
From vwTimelengthImsiHour
GO
--------------------------------------
CREATE VIEW [dbo].[vwTimelengthPercentMultipleByImsiLocationDayHour]
AS
SELECT     dbo.TimelengthCountByImsiLacCiDayHour.ConnTimeLength / dbo.TimelengthLocationDayHour.AvgConnTimeLengthPerUser AS TimelengthLocationMutiple, 
                      dbo.TimelengthCountByImsiLacCiDayHour.ConnTimeLength / dbo.TimelengthImsiDayHour.ConnTimeLength AS TimelengthUserPercent, 
                      dbo.TimelengthCountByImsiLacCiDayHour.Imsi, dbo.TimelengthCountByImsiLacCiDayHour.Location, dbo.TimelengthCountByImsiLacCiDayHour.ConnectHour, 
                      dbo.TimelengthCountByImsiLacCiDayHour.ConnectDay, dbo.TimelengthCountByImsiLacCiDayHour.ConnTimeLength
FROM         dbo.TimelengthCountByImsiLacCiDayHour INNER JOIN
                      dbo.TimelengthLocationDayHour ON dbo.TimelengthCountByImsiLacCiDayHour.Location = dbo.TimelengthLocationDayHour.Location AND 
                      dbo.TimelengthCountByImsiLacCiDayHour.ConnectDay = dbo.TimelengthLocationDayHour.ConnectDay AND 
                      dbo.TimelengthCountByImsiLacCiDayHour.ConnectHour = dbo.TimelengthLocationDayHour.ConnectHour INNER JOIN
                      dbo.TimelengthImsiDayHour ON dbo.TimelengthCountByImsiLacCiDayHour.Imsi = dbo.TimelengthImsiDayHour.Imsi AND 
                      dbo.TimelengthCountByImsiLacCiDayHour.ConnectHour = dbo.TimelengthImsiDayHour.ConnectHour AND 
                      dbo.TimelengthCountByImsiLacCiDayHour.ConnectDay = dbo.TimelengthImsiDayHour.ConnectDay
WHERE     (dbo.TimelengthCountByImsiLacCiDayHour.ConnTimeLength > 0)
GO
Select * 
into TimelengthPercentMultipleByImsiLocationDayHour
from vwTimelengthPercentMultipleByImsiLocationDayHour
-----------------------------------------------------
CREATE VIEW [dbo].[vwTimelengthDaysCountByImsiLocationHour]
AS
SELECT     SUM(CASE WHEN TimelengthLocationMutiple > 2 THEN 1 ELSE 0 END) AS LongTimeDays, SUM(CASE WHEN TimelengthUserPercent > 0.5 THEN 1 ELSE 0 END) 
                      AS StableDays, COUNT(ConnectDay) AS TotalDays, Imsi, Location, ConnectHour, AVG(ConnTimeLength) AS ConnTimeLength
FROM         dbo.TimelengthPercentMultipleByImsiLocationDayHour
GROUP BY Location, Imsi, ConnectHour
GO
Select *
into TimelengthDaysCountByImsiLocationHour
from vwTimelengthDaysCountByImsiLocationHour
------------------------------------------------
CREATE VIEW [dbo].[vwTimelengthFinalTable]
AS
SELECT     LongTimeDays, StableDays, TotalDays, Imsi, Location, ConnectHour, ConnTimeLength
FROM         dbo.TimelengthDaysCountByImsiLocationHour
WHERE     (TotalDays >= 4) AND (StableDays >= TotalDays * 0.5) AND (LongTimeDays >= TotalDays * 0.5)
GO
Select *
into TimelengthFinalTable
from vwTimelengthFinalTable
--------------------------------------------
SELECT     Imsi, ConnectDay, ConnectHour, SUM(ConnTimeLength) AS ConnTimeLength, SUM(ConnCount) AS ConnCount, COUNT(DISTINCT Location) AS LocationCount
into ImsiDayHour
FROM         dbo.TimelengthCountByImsiLacCiDayHour
GROUP BY Imsi, ConnectDay, ConnectHour
-------------------------------------------------
SELECT     Imsi, ConnectHour, SUM(ConnTimeLength) / SUM(LocationCount) AS AvgConnTimeLengthPerLocation, SUM(ConnCount) / SUM(LocationCount) 
                      AS AvgConnCountPerLocation, AVG(LocationCount) AS AvgLocationCount, AVG(ConnTimeLength) AS AvgConnTimeLength
into ImsiHour
FROM         dbo.ImsiDayHour
GROUP BY Imsi, ConnectHour
-------------------------------
USE [ZhuData]
GO

/****** Object:  View [dbo].[vwFinalTableWithData]    Script Date: 10/16/2012 20:21:28 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [dbo].[vwTimelengthFinalTableWithData]
AS
SELECT     dbo.TimelengthFinalTable.*, dbo.LocationHour.AvgConnTimeLengthPerUser AS LocationAvgConnTimeLengthPerUser, 
                      dbo.ImsiHour.AvgConnTimeLength AS UserAvgConnTimeLength
FROM         dbo.TimelengthFinalTable INNER JOIN
                      dbo.ImsiHour ON dbo.TimelengthFinalTable.Imsi = dbo.ImsiHour.Imsi AND dbo.TimelengthFinalTable.ConnectHour = dbo.ImsiHour.ConnectHour INNER JOIN
                      dbo.LocationHour ON dbo.TimelengthFinalTable.Location = dbo.LocationHour.Location AND dbo.TimelengthFinalTable.ConnectHour = dbo.LocationHour.ConnectHour

GO
