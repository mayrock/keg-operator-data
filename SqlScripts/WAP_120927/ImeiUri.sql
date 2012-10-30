CREATE View vwTemp_IMEI
AS
Select * From
(
	Select Imsi, IMEI, COUNT(*) as TotalCount,
	RANK() over (partition by Imsi order by count(*) desc) as ImeiRank
	from GN
	where Imei is not null and Imsi in (Select Imsi from FinalTable)
	group by Imsi,Imei 
) as t
where t.ImeiRank = 1

Select *
into Temp_IMEI
from vwTemp_IMEI
-------------------------------------
Select Imsi, MAX(imei) as IMEI
into Temp_IMEI2
from temp_IMEI
group by Imsi
----------------------------------------
CREATE View vwTemp_URI
AS
Select * From
(
	Select Imsi, URI, COUNT(*) as TotalCount,
	RANK() over (partition by Imsi order by count(*) desc) as URIRank
	from GN
	where URI is not null and Imsi in (Select Imsi from FinalTable)
	group by Imsi,URI 
) as t
where t.URIRank <= 5
Select *
into Temp_URI
from vwTemp_URI
-----------------------------------------
SET CONCAT_NULL_YIELDS_NULL OFF;
GO
CREATE VIEW vwTemp_URI2
AS
select Imsi, ([1]+';'+[2]+';'+[3]+';'+[4]+';'+[5]) AS URIs  from 
(select imsi, uri, urirank from temp_uri) p
pivot
(max(uri) 
for URIRank in 
([1],[2],[3],[4],[5])
) as pvt

select * 
into Temp_URI2
from vwTemp_URI2
-----------------------------------
SELECT distinct Imsi
      into StableUsers
  FROM [ZhuData].[dbo].[vwDaysCountByImsiLocationHour]
  where StableDays >= 0.5 * TotalDays