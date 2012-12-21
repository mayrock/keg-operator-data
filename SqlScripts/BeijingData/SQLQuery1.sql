
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
      ,MAX(TowerName) as SiteName
      ,MAX(RegionTypeId) as RegionTypeId
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