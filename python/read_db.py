import sqlite3
from appname_convert import *
conn=sqlite3.connect("dbs\\11_10.db")
cur=conn.cursor()
res = cur.execute("select appName from App order by useCount desc;")
for line in res:
    print(getAppNameNumber(line[0]))
    
