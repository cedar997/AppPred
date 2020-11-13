import requests

user_info = {'name': 'letian', 'password': '123'}
r = requests.post("http://192.168.199.225:5000/send", data=user_info)

print (r.text)