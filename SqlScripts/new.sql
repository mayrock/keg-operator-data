SELECT     *
INTO new_IMSI_Filtered_3
FROM         dbo.new_IMSI_All_2
WHERE     (uricount >= 10) AND (Locationcount > 5) AND (minutecount > 35)
-----------------------------
SELECT     URI, COUNT(*) AS TotalCount, COUNT(DISTINCT Imsi) AS UserCount, 
       COUNT(DISTINCT SiteId) AS LocationCount
       INTO new_URI_All_2
FROM         dbo.new_GN_All_1
GROUP BY URI
-----------------------------
SELECT TOP 1000 *
INTO new_URI_Filtered_3
FROM new_URI_All_2
ORDER BY UserCount DESC
-----------------------------
SELECT g.*
INTO new_GN_Filtered_4
FROM new_GN_All_1 AS g 
inner join new_IMSI_Filtered_3 as i on g.imsi = i.imsi
inner join new_URI_Filtered_3 as u on u.uri = g.uri
----------------------------
SELECT [SiteId]
      ,Longitude
      ,Latitude
      ,count(distinct [Imsi]) as UserCount
      ,count(distinct convert(smalldatetime,[ConnectTime])) as MinuteCount
      ,count(distinct [URI]) as URICount
      ,COUNT(*) as TotalCount
  into new_temptemptemp
  FROM [ZhuData].[dbo].[new_GN_All_1]
  group by SiteId, Longitude, Latitude
  -----------------------------
SELECT  URI, COUNT(*) AS TotalCount, COUNT(DISTINCT Imsi) AS UserCount, 
       COUNT(DISTINCT SiteId) AS LocationCount
       INTO new_URI_Filtered_5
FROM         dbo.new_GN_Filtered_4
GROUP BY URI