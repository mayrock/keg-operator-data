SELECT     *
INTO new2_IMSI_Filtered_3
FROM         dbo.new2_IMSI_All_2
WHERE     (uricount >= 10) AND (Locationcount > 5) AND (minutecount > 35)
-----------------------------
SELECT     URI, COUNT(*) AS TotalCount, COUNT(DISTINCT Imsi) AS UserCount, 
       COUNT(DISTINCT SiteId) AS LocationCount
       INTO new2_URI_All_2
FROM         dbo.new2_GN_All_1
GROUP BY URI
-----------------------------
SELECT TOP 1000 *
INTO new2_URI_Filtered_3
FROM new2_URI_All_2
ORDER BY UserCount DESC
-----------------------------
SELECT g.*
INTO new2_GN_Filtered_4
FROM new2_GN_All_1 AS g 
inner join new2_IMSI_Filtered_3 as i on g.imsi = i.imsi
inner join new2_URI_Filtered_3 as u on u.uri = g.uri
----------------------------
SELECT [SiteId]
      ,Longitude
      ,Latitude
      ,count(distinct [Imsi]) as UserCount
      ,count(distinct convert(smalldatetime,[ConnectTime])) as MinuteCount
      ,count(distinct [URI]) as URICount
      ,COUNT(*) as TotalCount
  into new2_temptemptemp
  FROM [ZhuData].[dbo].[new2_GN_All_1]
  group by SiteId, Longitude, Latitude
  -----------------------------
SELECT  URI, COUNT(*) AS TotalCount, COUNT(DISTINCT Imsi) AS UserCount, 
       COUNT(DISTINCT SiteId) AS LocationCount
       INTO new2_URI_Filtered_5
FROM         dbo.new2_GN_Filtered_4
GROUP BY URI
-------------------------------
SELECT     GN.Imsi, GN.Lac, GN.Ci, gn.SiteId,gn.Longitude, gn.Latitude, GN.ConnectTime, 
                      dbo.new2_URI_Mapping.ProcessedUri AS URI, GN.GetOrPost
INTO new2_GN_All_1
FROM         dbo.new_GN_All_1 as GN INNER JOIN
           dbo.new_URI_Mapping ON GN.URI = dbo.new_URI_Mapping.OriginalUri 
------------------------------------------------
SELECT     IMSI, COUNT(DISTINCT URI) AS URICount, COUNT(DISTINCT CONVERT(smalldatetime, ConnectTime)) AS MinuteCount, COUNT(DISTINCT siteId) AS LocationCount
, COUNT(*) AS TotalCount
into new2_IMSI_All_2
FROM         dbo.new2_GN_All_1
GROUP BY IMSI
------------------------------------------------
SELECT     *
INTO new2_IMSI_Filtered_3
FROM         dbo.new2_IMSI_All_2
WHERE     (uricount >= 10) AND (Locationcount >= 5) AND (minutecount >= 35)
-----------------------------
SELECT     URI, COUNT(*) AS TotalCount, COUNT(DISTINCT Imsi) AS UserCount, 
       COUNT(DISTINCT SiteId) AS LocationCount
       INTO new2_URI_All_2
FROM         dbo.new2_GN_All_1
GROUP BY URI
-----------------------------
SELECT TOP 1000 *
INTO new2_URI_Filtered_3
FROM new2_URI_All_2
ORDER BY UserCount DESC
-----------------------------
SELECT g.*
INTO new2_GN_Filtered_4
FROM new2_GN_All_1 AS g 
inner join new2_IMSI_Filtered_3 as i on g.imsi = i.imsi
inner join new2_URI_Filtered_3 as u on u.uri = g.uri
----------------------------
SELECT [SiteId]
      ,Longitude
      ,Latitude
      ,count(distinct [Imsi]) as UserCount
      ,count(distinct convert(smalldatetime,[ConnectTime])) as MinuteCount
      ,count(distinct [URI]) as URICount
      ,COUNT(*) as TotalCount
  into new2_SiteInfo5
  FROM [ZhuData].[dbo].[new2_GN_Filtered_4]
  group by SiteId, Longitude, Latitude
  -----------------------------
SELECT  URI, COUNT(*) AS TotalCount, COUNT(DISTINCT Imsi) AS UserCount, 
       COUNT(DISTINCT SiteId) AS LocationCount
       INTO new2_URI_Filtered_5
FROM         dbo.new2_GN_Filtered_4
GROUP BY URI