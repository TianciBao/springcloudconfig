#!/usr/bin/python
# -*- coding:utf-8 -*-

import sys
from PIL import Image

#图片统一规格 256x256的分辨率,RGB 模式
def make_regalur_image(img, size = (256, 256)):
    return img.resize(size).convert('RGB')

# 每块的分辨率为 64x64    
def split_image(img, part_size = (64, 64)):
    w, h = img.size
    pw, ph = part_size
    
    assert w % pw == h % ph == 0
#crop() : 从图像中提取出某个矩形大小的图像。它接收一个四元素的元组作为参数，各元素为（left, upper, right, lower），坐标系统的原点（0, 0）是左上角    
    return [img.crop((i, j, i+pw, j+ph)).copy() \
                for i in xrange(0, w, pw) \
                for j in xrange(0, h, ph)]

def hist_similar(lh, rh):
    assert len(lh) == len(rh)
    return sum(1 - (0 if l == r else float(abs(l - r))/max(l, r)) for l, r in zip(lh, rh))/len(lh)

#调用 img.histogram() 方法获得直方图数据,PIL 为 RGB 模式的图像计算的 histogram 样点数为 768,把规则图像分为 4x4 块，
def calc_similar(li, ri):
#    return hist_similar(li.histogram(), ri.histogram())
#zip() 函数用于将可迭代的对象作为参数，将对象中对应的元素打包成一个个元组，然后返回由这些元组组成的列表
    return sum(hist_similar(l.histogram(), r.histogram()) for l, r in zip(split_image(li), split_image(ri))) / 16.0
            

def calc_similar_by_path(lf, rf):
    li, ri = make_regalur_image(Image.open(lf)), make_regalur_image(Image.open(rf))
    return calc_similar(li, ri)
    
if __name__ == '__main__':
    for i in range(1, len(sys.argv)):
        pictureId = sys.argv[i]
    for i in xrange(1, 2):
        print('%.3f%%' 
        %(calc_similar_by_path('C:/soft/pictureDefiny/testpic/TEST/%s.jpg'%(pictureId+"1"), 'C:/soft/pictureDefiny/testpic/TEST/%s.jpg'%(pictureId+"2")) *100 ))


