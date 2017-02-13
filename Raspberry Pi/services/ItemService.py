import requests
import errno

from model.Item import Item

resp = requests.get('202.78.227.93/api/')
if resp.status_code != 200:
    print('Error')
for todo_item in resp.json():
    print('{}{}'.format(todo_item['id'], todo_item['name']))

def GetItemFromServer():
