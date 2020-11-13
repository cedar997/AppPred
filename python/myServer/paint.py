import matplotlib.pyplot as plt
import numpy as np
# plt.scatter() 展示变量之间的关系
def update_image():
    x = np.linspace(0.05,10,100)
    y = np.random.rand(100)
    plt.figure(figsize=(10,8),dpi=100) #设置画布大小，像素
    plt.scatter(x,y,label='scatter figure') #画散点图并指定图片标签
    plt.legend() #显示图片中的标签
    plt.savefig('static/1.png')#保存图片
    #plt.show()#展示图片