import requests
import json
import errno

####### Update the number of empty slot of selected area #######
def UpdateEmptyNumber(areaId, emptyNumber):
    url = 'http://capstoneapi.azurewebsites.net/api/Areas/UpdateNumberOfEmptySlot'
    data = {'AreaId':areaId,'EmptyNumber':emptyNumber}
    headers = {'content-type':'application/json'}

    resp = requests.post(url, json = data, headers = headers)
    if resp.status_code != 200:
        print('Error')
    else:
        result = resp.json()
        print(result)