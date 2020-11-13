import sqlite3
#每个应用名对应一个数字
appname_table={
        
        "菜鸟":1,
        "设置":2,
        "QQ":3,
        "知乎":4,
        "Chrome":5,
        "手机淘宝":6,
        "相册":7,
        "哔哩哔哩":8,
        "唯品会":9,
        "微信":10,
        "电话":11,
        "应用商店":12,
        "OneNote":13,
        "WPS Office":14,
        "QQ音乐":15,
        "通讯录与拨号":16,
        "小爱同学":17,
        "录音机":18,
        "电量和性能":19,
        "QQ邮箱":20,
        "简书":21,
        "支付宝":22,
        "时钟":23,
        "文件管理":24,
        "日历":25,
        "短信":26,
        "苏e行":27,
        "ES文件浏览器":28,
        "MyQQ":29,
        "VNC Viewer":30,
        "中国大学MOOC":31,
        "中国银行":32,
        "云闪付":33,
        "京东":34,
        "喵喵折":35,
        "天气":36,
        "小米社区":37,
        "屏幕录制":38,
        "工银融e联":39,
        "应用包管理组件":40,
        "应用收藏":41,
        "拼多多":42,
        "掌阅":43,
        "搜狗输入法小米版":44,
        "搜索":45,
        "智能助理":46,
        "智能服务":47,
        "有道云笔记":48,
        "权限管理服务":49,
        "永安行":50,
        "牛客":51,
        "百度":52,
        "百度地图":53,
        "百度贴吧":54,
        "相机":55,
        "米家":56,
        "老虎证券Tiger Trade":57,
}

#建立appname_table表
def getTableString():
    conn=sqlite3.connect("dbs\\11_10.db")
    cur=conn.cursor()
    res = cur.execute("select appName from App order by useCount desc;")
    print_it(res)
def getTableString2():
    conn=sqlite3.connect("dbs\\11_4.db")
    cur=conn.cursor()
    res = cur.execute("select appname from apptime group by appname;")
    print_it(res)
def print_it(res):
    i=len(appname_table)
    for line in res:
        if line[0] not in appname_table.keys():
            print("\t\"{}\":{},".format(line[0],i))
            i+=1

def getAppNameNumber(appname):
    return appname_table.get(appname)
def getWifiAnd4G(netNumber):
    #1代表移动网络
    if netNumber==1:
        return 0,1
    if netNumber==2:
        return 1,0
    else :
        return 0,0

if __name__ == "__main__":
    #getTableString2()
    getAppNameNumber("kjf")

