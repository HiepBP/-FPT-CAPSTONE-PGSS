import requests
import errno

from model.Item import Item

resp = requests.get('http://capstoneapi.azurewebsites.net//api/CarParks/GetCoordinateNearestCarPark/10.8045389/106.6980829/10')
if resp.status_code != 200:
    print('Error')
else:
    result = resp.json()['result']
    print(result)
    # print('{}{}'.format(todo_item['result'], todo_item['success']))

