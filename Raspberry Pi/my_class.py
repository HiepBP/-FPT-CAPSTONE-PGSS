class BiDict(dict):
    def __init__(self, *args, **kwargs):
        super(BiDict, self).__init__(*args, **kwargs)
        self.inverse = {}
        for key, value in list(self.items()):
            self.__setitem__(value, key)

    def __setitem__(self, key, value):
        if key in self:
            t = self[key]
            if t in self:
                del self[t]
            del self[key]
        if value in self:
            t = self[value]
            if t in self:
                del self[t]
            del self[value]
        dict.__setitem__(self, key, value)
        dict.__setitem__(self, value, key)

    def __delitem__(self, key):
        if self[key] in self:
            dict.__delitem__(self, self[key])
        if key in self:
            dict.__delitem__(self, key)

    def __len__(self):
        return dict.__len__(self) // 2

class PubnubMessage:

    _message = "Target: {}, Command: {}, Data: {}"
    
    def __init__(self, command, target, data):
        self.command = command
        self.target = target
        self.data = data
    def __str__(self):
        return self._message.format(self.target, self.command, self.data)

class ParkingLot:

    def __init__(self, sensor_name, indicator_name, barrier_name, information_name):
        self.sensor_name = sensor_name
        self.indicator_name = indicator_name
        self.barrier_name = barrier_name
        self.information_name = information_name
        self.available = True

    def set_available(self, available):
        self.available = available



