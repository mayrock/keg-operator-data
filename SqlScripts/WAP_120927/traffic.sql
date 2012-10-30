CREATE VIEW vwTrafficCountByImsiLacCiDayHour
AS
SELECT [IMSI]
      ,Lac*100000+Ci as Location
      ,DATEPART(DAYOFYEAR, ConnectTime) AS ConnectDay
      ,DATEPART(HOUR, ConnectTime) AS ConnectHour
      ,sum(TotalTraffic) As ConnTraffic
      ,sum(TotalCount) As ConnCount
  FROM [ZhuData].[dbo].[TrafficIP]
  WHERE DATEPART(HOUR, ConnectTime) in (7,8,9,10,11,17,18,19,20,21)
  AND Imsi in (Select Imsi from StableUsers)
  group by IMSI,Lac,Ci,ConnectTime
  
SELECT *
into TrafficCountByImsiLacCiDayHour
FROM vwTrafficCountByImsiLacCiDayHour
----------------------------------------
CREATE VIEW [dbo].[vwTrafficImsiDayHour]
as
SELECT [Imsi]
      ,[ConnectDay]
      ,[ConnectHour]
      ,sum(ConnTraffic) as  ConnTraffic
      ,sum (ConnCount) as ConnCount
      ,COUNT(distinct location) as LocationCount
  FROM [ZhuData].[dbo].[TrafficCountByImsiLacCiDayHour]
  group by imsi, connectday, connecthour
GO
Select * 
into TrafficImsiDayHour
From vwTrafficImsiDayHour
GO
------------------------------------------------
CREATE VIEW [dbo].[vwTrafficLocationDayHour]
AS
SELECT     Location, ConnectDay, ConnectHour, SUM(ConnTraffic) AS ConnTraffic, SUM(ConnCount) AS ConnCount, COUNT(DISTINCT Imsi) AS UserCount, 
                      SUM(ConnTraffic) / COUNT(DISTINCT Imsi) AS AvgConnTrafficPerUser
FROM         dbo.TrafficCountByImsiLacCiDayHour
GROUP BY Location, ConnectDay, ConnectHour
GO
Select *
into TrafficLocationDayHour
From vwTrafficLocationDayHour
GO
------------------------------------
create view [dbo].[vwTrafficLocationHour]
as
SELECT  [Location]
      ,[ConnectHour]
      ,sum(ConnTraffic) / SUM(userCount) as AvgConnTrafficPerUser
      ,sum(ConnCount) / SUM(userCount) as AvgConnCountPerUser
      ,avg(UserCount) as AvgUserCount
  FROM [ZhuData].[dbo].[TrafficLocationDayHour]
  group by Location, ConnectHour
GO
Select *
into TrafficLocationHour
From vwTrafficLocationHour
GO
----------------------------------------------
create view [dbo].[vwTrafficImsiHour]
as
SELECT  [Imsi]
      ,[ConnectHour]
      ,sum(ConnTraffic) / SUM(LocationCount) as AvgConnTrafficPerLocation
      ,sum(ConnCount) / SUM(LocationCount) as AvgConnCountPerLocation
      ,avg(LocationCount) as AvgLocationCount
      ,AVG(ConnTraffic) as AvgConnTraffic
  FROM [ZhuData].[dbo].[TrafficImsiDayHour]
  group by Imsi, ConnectHour
GO
Select * 
into TrafficImsiHour
From vwTrafficImsiHour
GO
--------------------------------------
CREATE VIEW [dbo].[vwTrafficPercentMultipleByImsiLocationDayHour]
AS
SELECT     dbo.TrafficCountByImsiLacCiDayHour.ConnTraffic / dbo.TrafficLocationDayHour.AvgConnTrafficPerUser AS TrafficLocationMutiple, 
                      dbo.TrafficCountByImsiLacCiDayHour.ConnTraffic / dbo.TrafficImsiDayHour.ConnTraffic AS TrafficUserPercent, 
                      dbo.TrafficCountByImsiLacCiDayHour.Imsi, dbo.TrafficCountByImsiLacCiDayHour.Location, dbo.TrafficCountByImsiLacCiDayHour.ConnectHour, 
                      dbo.TrafficCountByImsiLacCiDayHour.ConnectDay, dbo.TrafficCountByImsiLacCiDayHour.ConnTraffic
FROM         dbo.TrafficCountByImsiLacCiDayHour INNER JOIN
                      dbo.TrafficLocationDayHour ON dbo.TrafficCountByImsiLacCiDayHour.Location = dbo.TrafficLocationDayHour.Location AND 
                      dbo.TrafficCountByImsiLacCiDayHour.ConnectDay = dbo.TrafficLocationDayHour.ConnectDay AND 
                      dbo.TrafficCountByImsiLacCiDayHour.ConnectHour = dbo.TrafficLocationDayHour.ConnectHour INNER JOIN
                      dbo.TrafficImsiDayHour ON dbo.TrafficCountByImsiLacCiDayHour.Imsi = dbo.TrafficImsiDayHour.Imsi AND 
                      dbo.TrafficCountByImsiLacCiDayHour.ConnectHour = dbo.TrafficImsiDayHour.ConnectHour AND 
                      dbo.TrafficCountByImsiLacCiDayHour.ConnectDay = dbo.TrafficImsiDayHour.ConnectDay
WHERE     (dbo.TrafficCountByImsiLacCiDayHour.ConnTraffic > 0)
GO
Select * 
into TrafficPercentMultipleByImsiLocationDayHour
from vwTrafficPercentMultipleByImsiLocationDayHour
-----------------------------------------------------
CREATE VIEW [dbo].[vwTrafficDaysCountByImsiLocationHour]
AS
SELECT     SUM(CASE WHEN TrafficLocationMutiple > 2 THEN 1 ELSE 0 END) AS TrafficHighDays, SUM(CASE WHEN TrafficUserPercent > 0.5 THEN 1 ELSE 0 END) 
                      AS TrafficStableDays, COUNT(ConnectDay) AS TotalDays, Imsi, Location, ConnectHour, AVG(ConnTraffic) AS ConnTraffic
FROM         dbo.TrafficPercentMultipleByImsiLocationDayHour
GROUP BY Location, Imsi, ConnectHour
GO
Select *
into TrafficDaysCountByImsiLocationHour
from vwTrafficDaysCountByImsiLocationHour
------------------------------------------------
CREATE VIEW [dbo].[vwTrafficFinalTable]
AS
SELECT     TrafficHighDays, TrafficStableDays, TotalDays, Imsi, Location, ConnectHour, ConnTraffic
FROM         dbo.TrafficDaysCountByImsiLocationHour
WHERE     (TotalDays >= 4) AND (TrafficStableDays >= TotalDays * 0.5) AND (TrafficHighDays >= TotalDays * 0.5)
GO
Select *
into TrafficFinalTable
from vwTrafficFinalTable
--------------------------------------------

CREATE VIEW [dbo].[vwTrafficFinalTableWithData]
AS
SELECT     dbo.TrafficFinalTable.*, dbo.TrafficLocationHour.AvgConnTrafficPerUser AS LocationAvgConnTrafficPerUser, 
                      dbo.TrafficImsiHour.AvgConnTraffic AS UserAvgConnTraffic
FROM         dbo.TrafficFinalTable INNER JOIN
                      dbo.TrafficImsiHour ON dbo.TrafficFinalTable.Imsi = dbo.TrafficImsiHour.Imsi AND dbo.TrafficFinalTable.ConnectHour = dbo.TrafficImsiHour.ConnectHour INNER JOIN
                      dbo.TrafficLocationHour ON dbo.TrafficFinalTable.Location = dbo.TrafficLocationHour.Location AND dbo.TrafficFinalTable.ConnectHour = dbo.TrafficLocationHour.ConnectHour

GO

----------
CREATE View vwTrafficFinalTableAll
as
SELECT     dbo.vwTrafficFinalTableWithData.Location / 100000 AS Lac, dbo.vwTrafficFinalTableWithData.Location % 100000 AS Ci, 
                      dbo.vwTrafficFinalTableWithData.ConnectHour, dbo.vwTrafficFinalTableWithData.Imsi, dbo.vwTrafficFinalTableWithData.LongTimeDays
                      AS HighTrafficDays, 
                      dbo.vwTrafficFinalTableWithData.StableDays AS TrafficStableDays, dbo.vwTrafficFinalTableWithData.TotalDays AS TrafficTotalDays, 
                      dbo.vwTrafficFinalTableWithData.ConnTraffic, dbo.vwTrafficFinalTableWithData.LocationAvgConnTrafficPerUser, 
                      dbo.vwTrafficFinalTableWithData.UserAvgConnTraffic, dbo.temp_imei2.IMEI, dbo.temp_uri2.URIs
FROM         dbo.vwTrafficFinalTableWithData LEFT OUTER JOIN
                      dbo.temp_imei2 ON dbo.vwTrafficFinalTableWithData.Imsi = dbo.temp_imei2.Imsi LEFT OUTER JOIN
                      dbo.temp_uri2 ON dbo.vwTrafficFinalTableWithData.Imsi = dbo.temp_uri2.Imsi



