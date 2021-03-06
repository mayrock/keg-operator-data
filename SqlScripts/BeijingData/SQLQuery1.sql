
/****** Script for SelectTopNRows command from SSMS  ******/
SELECT [LAC]
      ,[CI]
      ,[TowerId]
      ,[TowerName]
      ,[Annt]
      ,[Longitude]
      ,[Latitude]
      ,TypeId as [RegionTypeId]
      into LocationInfo
  FROM [BeijingData].[dbo].[RawLocationInfo] l
  left outer join RegionType t on l.RegionType = t.TypeName
  where Longitude is not null and Latitude is not null
  -------------------------
SELECT [SiteId]
      ,s.Longitude
      ,s.Latitude
      ,MIN(TowerName) as SiteName
      ,MIN(i.RegionTypeId) as RegionTypeId
      into SiteInfo
  FROM [BeijingData].[dbo].[SiteInfo2] as s
  inner join LocationInfo as i on s.longitude = i.longitude and s.latitude = i.latitude
  group by [SiteId],s.longitude,i.longitude,s.latitude,i.latitude
  
  
  -----------
  SELECT distinct
      URI
      into temp_URI
  FROM [BeijingData].[dbo].RawData
  
      select uri into temp_URI2 from temp_uri
  where uri not like '/%'
  
  SELECT [OriginalUri]
      ,[ProcessedUri]
      into FilteredURI_Mapping
  FROM [BeijingData].[dbo].[URI_Mapping]
  where len(processeduri) < 30
-----------------------------------------------
  SELECT [SiteId]
      ,[ContentType]
      ,[Imei]
      ,[APN]
      ,[URI]
      ,[ProcessedUri]
      ,[GetOrPost]
      ,[ConnectTime]
      ,[SERVER_ID]
      ,[Imsi]
      ,[Latitude]
      ,[Longitude]
      ,[Domain3]
  into FilteredByCT_Data
  FROM [BeijingData].[dbo].[Data]
  where contenttype not like 'image/%' and contenttype not like '%javascript%'
	and contenttype <> 'text/css'
  -----------------------
  SELECT  [SiteId]
      ,ContentType
      ,[Imei]
      ,[APN]
      ,[URI]
      ,[ProcessedUri]
      ,[GetOrPost]
      ,[ConnectTime]
      ,[SERVER_ID]
      ,[Imsi]
      ,[Latitude]
      ,[Longitude]
      ,[Domain3]
      ,(case 
      when processeduri like '%soso.com' 
        or processeduri like '%qq.com' or processeduri like '%baidu.com' or processeduri like '%sogou.com'
      then domain3 else processedURI end) as Domain
      into FilteredByCT_Data2
  FROM [BeijingData].[dbo].[FilteredByCT_Data]
  -------------------
  
SELECT [SiteId]
      ,[ContentType]
      ,Datepart(dayofyear,ConnectTime) as ConnDay
      ,DATEPART(hour,ConnectTime) as ConnHour
      ,[Imsi]
      ,[Domain]
      ,COUNT(*) as TotalCount
      into FilteredbyCT_DataAggr
  FROM [BeijingData].[dbo].[FilteredByCT_Data2]
  group by ContentType, Domain, Imsi, SiteId, ConnectTime
  ----------------------
  SELECT COUNT(Distinct [SiteId]) as LocCount
      ,COUNT(Distinct [ConnDay]) as DayCount
      ,COUNT(DISTINCT ConnDay*100+[ConnHour]) As HourCount
      ,[Imsi]
      ,COUNT(Distinct [Domain]) as DomainCount
      ,Sum([TotalCount]) as TotalCount
      into FilteredByCT_Imsi
  FROM [BeijingData].[dbo].[FilteredbyCT_DataAggr]
  group by Imsi
----------------------
  SELECT [SiteId]
      ,COUNT(Distinct [ConnDay]) as DayCount
      ,COUNT(DISTINCT ConnDay*100+[ConnHour]) As HourCount
      ,COUNT(DISTINCT [Imsi]) as UserCount
      ,COUNT(Distinct [Domain]) as DomainCount
      ,Sum([TotalCount]) as TotalCount
      into FilteredByCT_SiteId
  FROM [BeijingData].[dbo].[FilteredbyCT_DataAggr]
  group by SiteId
----------------------
  SELECT [ConnDay]
      ,[ConnHour]
      ,SiteId
      ,COUNT(DISTINCT [Imsi]) as UserCount
      ,COUNT(Distinct [Domain]) as DomainCount
      ,Sum([TotalCount]) as TotalCount
      into FilteredByCT_SiteIdDayHour
  FROM [BeijingData].[dbo].[FilteredbyCT_DataAggr]
  group by SiteId,ConnDay,ConnHour
----------------------
  SELECT Domain
      ,COUNT(Distinct [ConnDay]) as DayCount
      ,COUNT(DISTINCT ConnDay*100+[ConnHour]) As HourCount
      ,COUNT(Distinct SiteId) as LocCount
      ,COUNT(DISTINCT [Imsi]) as UserCount
      ,Sum([TotalCount]) as TotalCount
      ,(Sum([TotalCount]) + 0) / COUNT(DISTINCT [Imsi])
      into FilteredByCT_Domain
  FROM [BeijingData].[dbo].[FilteredbyCT_DataAggr]
  group by Domain
------------------------------------------
SELECT [TotalCount]
      ,Count([Imsi]) as ImsiCount
      ,MAX([LocCount]) as MaxLocCount
      ,MAX([DayCount]) as MaxDayCount
      ,MAX([HourCount]) as MaxHourCount
      ,Max([DomainCount]) as MaxDomainCount
  into FilteredByCT_Imsi_Count
  FROM [BeijingData].[dbo].[FilteredByCT_Imsi]
  group by totalcount
-----------------------------------
-----------------------------------
SELECT TOP 1000 [Domain]
      ,[DayCount]
      ,[HourCount]
      ,[LocCount]
      ,[UserCount]
      ,[TotalCount]
      ,[Ratio]
      into new_Domain2
  FROM [BeijingData].[dbo].[FilteredByCT_Domain]
--------------------------------------
SELECT     dbo.FilteredByCT_Data2.*, dbo.Host990_Group924.GroupNum
into group_data
FROM         dbo.FilteredByCT_Data2 INNER JOIN
       dbo.Host990_Group924 ON dbo.FilteredByCT_Data2.Domain
        = dbo.Host990_Group924.Host
--------------------------------------- 
  SELECT COUNT(Distinct [SiteId]) as LocCount
      ,COUNT(Distinct CONVERT(smalldatetime, connecttime)) as MinuteCount
      ,[Imsi]
      ,COUNT(Distinct WebsiteNum) as DomainCount
      ,COUNT(*) as TotalCount
      into group_Imsi
  FROM [BeijingData].[dbo].[group_data]
  group by Imsi
---------------------------------------                     
SELECT *
	into group_imsi_filtered2
  FROM [BeijingData].[dbo].[group_Imsi]
  where domaincount >= 10 and minutecount >= 20 and totalcount >= 50
---------------------------------------
SELECT d.* 
into group_data_filtered3
from group_data d inner join group_imsi_filtered2 i on d.imsi = i.imsi
----------------------------------
SELECT  t.[SiteId]
      ,t.[ConnHour]
      ,e.[DayCount] as WeekendDayCount
      ,e.[TotalCount] as WeekendTotalCount
      ,d.[DayCount] as WeekdayDayCount
      ,d.[TotalCount] as WeekdayTotalCount
      into SiteId_ConnHour_dayend
  FROM ttttttttt t left outer join
   [BeijingData].[dbo].[SiteId_ConnHour_weekday] d on t.siteid = d.siteid
   and t.connhour = d.connhour
  left outer join [SiteId_ConnHour_weekend] e on e.siteid = t.siteid and 
  e.connhour = t.connhour
  
