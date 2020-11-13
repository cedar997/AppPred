import csv 
import pandas as pd
from datetime import datetime
from app_convert import *
headers=[
    'appId',"wifi","net4g","battery","charging"
    ,"last1","last2"
]

def fill_1():
    rows=[]
    conn=sqlite3.connect("dbs\\11_4.db")
    cur=conn.cursor()
    res = cur.execute("select appname,net,battery,charging from apptime;")
    last1=0
    i=-1
    for line in res:
        i+=1
        if i==0:
            last2=getAppNameNumber(line[0])
            continue
        if i==1:
            last1=getAppNameNumber(line[0])
            continue
        appId=-1
        appId=getAppNameNumber(line[0])
        wifi,net4g=getWifiAnd4G(line[1])
        battery=line[2]
        charging=line[3]
        rows.append([appId,wifi,net4g,battery,charging,last1,last2])
        last2=last1
        last1=appId
        
    with open('test.csv','w')as f:
        f_csv = csv.writer(f)
        f_csv.writerow(headers)
        f_csv.writerows(rows)
def fill_2():
    
    conn=sqlite3.connect("1113.db")
    cur=conn.cursor()
    res = cur.execute("select eventType,appName,timeStamp from Event;")
    eventType=[]
    appName=[]
    timeStamp=[]
    for line in res:
        
        eventType.append(line[0])
        appName.append(line[1])
        print(line[2])
        ts=line[2]/1000
        s=datetime.fromtimestamp(ts).strftime('%Y-%m-%d %H:%M:%S')

        
        timeStamp.append(s)
        
    dataframe = pd.DataFrame({'event':eventType,'appname':appName,'timestamp':timeStamp})
    dataframe.to_csv("11130.csv",index=False,sep=',')
    

if __name__ == "__main__":
    fill_2()