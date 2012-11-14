BULK INSERT locationinfo
from 'D:\用户迁移项目基础信息汇总\location.txt'
with
(
ROWTERMINATOR = '\n',
FIELDTERMINATOR = '\t'
)