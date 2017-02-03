class PubnubMessage:

    _message = "Target: {}, Command: {}, Data: {}"
    
    def __init__(self, command, target, data):
        self.command = command
        self.target = target
        self.data = data
    def __str__(self):
        return self._message.format(self.target, self.command, self.data)


