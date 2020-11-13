from flask import Flask, request
import pandas as pd
from datetime import datetime
app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'hello world'
#处理单个app
@app.route('/send', methods=['POST'])
def handle_send():
    list=getTop5(request.form['appname'])
    str=','.join(list)
    return str
#根据当前应用，给出5个推荐应用
#你需要重写此方法！！！
def getTop5(currentApp):
    list=[]
    list.append(currentApp)
    list.append("菜鸟")
    list.append("微信")
    list.append("手机淘宝")
    list.append("拼多多")
    return list
#处理用户上传的app列表
@app.route('/sends', methods=['POST'])
def handle_sends():
    appnames=request.form['appnames'].split(',')
    timestamps=request.form['timestamps'].split(',')
    time_str=[]
    for t in timestamps:
        t=int(t)/1000
        s=datetime.fromtimestamp(t).strftime('%Y-%m-%d %H:%M:%S')
        time_str.append(s)
    dataframe = pd.DataFrame({'appname':appnames,'timestamp':time_str})
    dataframe.to_csv("test.csv",index=False,sep=',')
    return 'done'

if __name__ == '__main__':
    app.run(host='0.0.0.0',
    port=5000,
    debug=True
    )