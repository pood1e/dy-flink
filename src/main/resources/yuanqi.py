import json

import requests
from bs4 import BeautifulSoup
import os
import hashlib


def download():
    page_index = 1
    while True:
        print(f"page: {page_index}")
        ids = []
        soup = BeautifulSoup(requests.get(f"https://bizhi.cheetahfun.com/4/index_{page_index}.shtml").text,
                             'html.parser')
        objs = soup.find_all("a", class_="wallpaper-item-mask page-behaviour")
        if len(objs) == 0:
            break
        for i in objs:
            ids.append(int(i['data-image-id']))
        body = json.dumps({
            "wids": ids
        })
        data = requests.post("https://pcwallpaper.zhhainiao.com/wallpaper/static/list/by/wids", headers={
            "Content-Type": "application/json"
        }, data=body).json()
        for i in data['data']:
            try_times = 1
            while (not exist(i['id'], i['md5'])) and try_times < 5:
                try_times += 1
                save(i['id'], i['jpg_url'])
            if exist(i['id'], i['md5']):
                print("+", end="", flush=True)
            else:
                print(f"error: {i['id']}")
        print()
        page_index += 1


def exist(name, md5):
    if os.path.exists(f"game/{name}.jpg"):
        with open(f"game/{name}.jpg", 'rb') as fp:
            data = fp.read()
        file_md5 = hashlib.md5(data).hexdigest()
        return file_md5 == md5
    return False


def save(name, url):
    data = requests.get(url).content
    with open(f"game/{name}.jpg", "wb") as fp:
        fp.write(data)


if __name__ == '__main__':
    download()
